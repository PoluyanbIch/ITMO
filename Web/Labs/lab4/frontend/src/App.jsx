import React, { useEffect, useState, useCallback, createContext, useContext } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate, useNavigate } from "react-router-dom";
import Graph from "./Graph";
import "./App.css";


const AuthContext = createContext(null);

function useAuth() {
  return useContext(AuthContext);
}

function decodeJwtPayload(token) {
  try {
    const parts = token.split('.');
    if (parts.length !== 3) return null;
    const payload = parts[1];
    const json = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
    return JSON.parse(decodeURIComponent(escape(json)));
  } catch (e) { return null; }
}

function createApi(getToken, setToken) {
  const API = {};
  API.request = async (path, opts = {}) => {
    const headers = opts.headers || {};
    const token = getToken();
    if (token) headers['Authorization'] = `Bearer ${token}`;
    const res = await fetch(`${process.env.REACT_APP_API_URL}`+path, {
      credentials: 'include',
      headers: { 'Content-Type': 'application/json', ...headers },
      ...opts,
    });

    if (res.status === 401) {
      const refreshed = await fetch(`${process.env.REACT_APP_API_URL}/auth/refresh`, { method: 'POST', credentials: 'include' });
      if (refreshed.ok) {
        const data = await refreshed.json();
        if (data.access_token) {
          setToken(data.access_token);
          const retryHeaders = { ...headers, Authorization: `Bearer ${data.access_token}` };
          const retry = await fetch(`${process.env.REACT_APP_API_URL}`+path, { credentials: 'include', headers: { 'Content-Type': 'application/json', ...retryHeaders }, ...opts });
          return retry;
        }
      }
    }

    return res;
  };

  API.post = async (path, body) => API.request(path, { method: 'POST', body: JSON.stringify(body) });
  API.get = async (path) => API.request(path, { method: 'GET' });
  return API;
}

export default function App() {
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);

  const api = createApi(() => token, setToken);

  useEffect(() => {
    (async () => {
      try {
        const res = await fetch(`${process.env.REACT_APP_API_URL}/auth/refresh`, { method: 'POST', credentials: 'include' });
        if (res.ok) {
          const data = await res.json();
          if (data.access_token) setToken(data.access_token);
        }
      } catch (e) {
      } finally {
        setLoading(false);
      }
    })();
  }, []);

  const logout = useCallback(async () => {
    await fetch(`${process.env.REACT_APP_API_URL}/auth/logout`, { method: 'POST', credentials: 'include' });
    setToken(null);
  }, []);

  const value = { token, setToken, api, logout };

  if (loading) return <div className="app">Loading...</div>;

  return (
    <AuthContext.Provider value={value}>
      <Router>
        <div className="app page">
          <div className="header">
            <div className="brand">ФИО: Полуянов Игорь Андреевич — Группа: P3210 — Вариант: 91</div>
            <div className="top-right">
              {token ? <div className="small-muted">{decodeJwtPayload(token)?.login ?? '—'}</div> : null}
              {token ? <button className="btn ghost" onClick={logout}>Выйти</button> : null}
            </div>
          </div>

          <Routes>
            <Route path="/" element={<AuthPage />} />
            <Route path="/points" element={token ? <PointsPage /> : <Navigate to="/" replace />} />
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </div>
      </Router>
    </AuthContext.Provider>
  );
}

// AuthPage
function AuthPage() {
  const { setToken, api } = useAuth();
  const [mode, setMode] = useState('login');
  const [login, setLogin] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const submit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const path = mode === 'login' ? '/auth/login' : '/auth/register';
      const res = await fetch(`${process.env.REACT_APP_API_URL}`+path, {
        method: 'POST',
        credentials: 'include',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ login, password }),
      });
      if (!res.ok) {
        const text = await res.text();
        setError(text || 'Ошибка');
        return;
      }
      const data = await res.json();
      const access = data.access_token || data.token || data.accessToken || data.token;
      if (!access) {
        setError('no access token from server');
        return;
      }
      setToken(access);
      navigate('/points');
    } catch (e) {
      setError('network error');
    }
  };

  return (
    <div className="auth-box card">
      <h2>{mode === 'login' ? 'Вход' : 'Регистрация'}</h2>
      <form onSubmit={submit}>
        <label>Логин</label>
        <input className="input" value={login} onChange={(e)=>setLogin(e.target.value)} />
        <label>Пароль</label>
        <input className="input" type="password" value={password} onChange={(e)=>setPassword(e.target.value)} />
        {error ? <div className="error">{error}</div> : null}
        <div style={{display:'flex',gap:8,marginTop:8}}>
          <button className="btn" type="submit">{mode === 'login' ? 'Войти' : 'Зарегистрироваться'}</button>
          <button type="button" className="btn ghost" onClick={()=>setMode(mode === 'login' ? 'register' : 'login')}>{mode === 'login' ? 'Регистрация' : 'Вход'}</button>
        </div>
      </form>
    </div>
  );
}

// PointsPage
function PointsPage() {
  const { token, api } = useAuth();
  const [points, setPoints] = useState([]);
  const [xChoice, setXChoice] = useState(null);
  const [yVal, setYVal] = useState('0');
  const [rChoice, setRChoice] = useState(null);
  const [error, setError] = useState('');

  const fetchPoints = useCallback(async () => {
    const res = await api.get('/points');
    if (!res.ok) return;
    const data = await res.json();
    const sortedData = [...data].sort((a, b) => b.id - a.id);
    setPoints(sortedData);
  }, [api]);

  useEffect(() => { fetchPoints(); }, [fetchPoints]);

  const submitPoint = async (e) => {
    e && e.preventDefault();
    setError('');
    if (xChoice === null) { setError('Выберите X'); return; }
    const y = parseFloat(yVal.replace(',', '.'));
    if (isNaN(y) || y < -5 || y > 3) { setError('Y должен быть числом от -5 до 3'); return; }
    if (rChoice === null) { setError('Выберите R'); return; }

    const body = { x: String(xChoice), y: String(y), r: String(rChoice) };
    const res = await api.post('/points/add', body);
    if (!res.ok) {
      const txt = await res.text(); setError(txt || 'Ошибка'); return;
    }
    await fetchPoints();
  };

  const canvasClick = async (ev) => {
    const el = ev.currentTarget;
    const rect = el.getBoundingClientRect();
    const sx = ev.clientX - rect.left;
    const sy = ev.clientY - rect.top;
    const lx = (sx / rect.width) * 10 - 5;
    const ly = -((sy / rect.height) * 10 - 5);
    const r = rChoice ?? 1;
    const res = await api.post('/points/add', { x: String(lx.toFixed(2)), y: String(ly.toFixed(2)), r: String(r) });
    if (res.ok) fetchPoints();
  };

  return (
    <div className="container">
      <div className="layout">
        <div className="left">
          <div className="card">
            <h3>График (кликните чтобы добавить точку)</h3>
            <div className="canvas-wrap">
              <Graph
                R={rChoice}
                points={points}
                currentX={xChoice}
                currentY={parseFloat(yVal)}
                onAddPoint={(x,y,r) => {
                  api.post('/points/add', { x:String(x), y:String(y), r:String(r) })
                    .then(() => fetchPoints());
                }}
              />
            </div>

            <h3 style={{marginTop:12}}>Форма</h3>
            <form onSubmit={submitPoint}>
              <div>
                <div className="small-muted">X (выберите)</div>
                <div style={{display:'flex',gap:8,flexWrap:'wrap',marginTop:8}}>
                  {['-3','-2','-1','0','1','2','3','4','5'].map(v=> (
                    <label key={v} style={{display:'inline-flex',alignItems:'center',gap:6}}>
                      <input type="radio" name="x" checked={String(xChoice)===v} onChange={()=>setXChoice(Number(v))} /> {v}
                    </label>
                  ))}
                </div>
              </div>

              <div style={{marginTop:12}}>
                <div className="small-muted">Y (число от -5 до 3)</div>
                <input className="input" value={yVal} onChange={e=>setYVal(e.target.value)} />
              </div>

              <div style={{marginTop:12}}>
                <div className="small-muted">R (выберите)</div>
                <div style={{display:'flex',gap:8,flexWrap:'wrap',marginTop:8}}>
                  {['0','1','2','3','4','5'].map(v=> (
                    <label key={v} style={{display:'inline-flex',alignItems:'center',gap:6}}>
                      <input type="radio" name="r" checked={String(rChoice)===v} onChange={()=>setRChoice(Number(v))} /> {v}
                    </label>
                  ))}
                </div>
              </div>

              {error ? <div className="error" style={{marginTop:8}}>{error}</div> : null}

              <div style={{display:'flex',gap:8,marginTop:12}}>
                <button className="btn" type="submit">Отправить</button>
                <button type="button" className="btn ghost" onClick={()=>{ setXChoice(null); setYVal('0'); setRChoice(null); }}>Сброс</button>
              </div>
            </form>

          </div>
        </div>

        <div className="right">
          <div className="card">
            <h3>Результаты</h3>
            <div style={{ 
              maxHeight: '400px', 
              overflowY: 'auto',
              border: '1px solid #eef2f7',
              borderRadius: '4px'
            }}>
            <table className="table">
              <thead>
                <tr><th>X</th><th>Y</th><th>R</th><th>Попал</th></tr>
              </thead>
              <tbody>
                {points.map((p,i)=>(
                  <tr key={i}>
                    <td>{p.x}</td>
                    <td>{p.y}</td>
                    <td>{p.r}</td>
                    <td>{p.isHit ? 'YES' : 'NO'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
