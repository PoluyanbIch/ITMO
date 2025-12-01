import React, { useEffect, useState, useCallback, createContext, useContext } from "react";
import { BrowserRouter as Router, Routes, Route, Navigate, useNavigate } from "react-router-dom";
import Graph from "./Graph";

// --- Simple CSS to satisfy exact breakpoints (mobile <800, tablet 800-1209, desktop >=1209) ---
const style = `
:root { --gap: 16px; }
* { box-sizing: border-box; }
body { font-family: Inter, ui-sans-serif, system-ui, -apple-system, "Segoe UI", Roboto, "Helvetica Neue", Arial; margin:0; }
.app { padding: 20px; }
.header { display:flex; justify-content:space-between; align-items:center; padding: 12px 0; }
.brand { font-weight:600; }
.auth-box { max-width:420px; margin: 20px auto; padding:20px; border-radius:8px; box-shadow:0 6px 18px rgba(0,0,0,0.06); }
.input { width:100%; padding:8px 10px; margin-top:8px; margin-bottom:12px; border-radius:6px; border:1px solid #d1d5db; }
.btn { padding:8px 12px; border-radius:8px; background:#2563eb; color:white; border:none; cursor:pointer; }
.btn.ghost { background:transparent; color:#374151; border:1px solid #e5e7eb; }
.container { display:grid; gap:var(--gap); }

/* layout based on breakpoints */
.page { max-width:1200px; margin: 0 auto; }

/* mobile: single column */
@media (max-width:799px) {
  .layout { grid-template-columns: 1fr; }
  .left { order:2 }
  .right { order:1 }
}

/* tablet: two columns (1fr 360px) */
@media (min-width:800px) and (max-width:1208px) {
  .layout { grid-template-columns: 1fr 360px; }
}

/* desktop: two columns (1fr 420px) */
@media (min-width:1209px) {
  .layout { grid-template-columns: 1fr 420px; }
}

.card { background:white; padding:16px; border-radius:8px; box-shadow:0 6px 18px rgba(0,0,0,0.04); }
.table { width:100%; border-collapse:collapse; }
.table th, .table td { padding:8px 6px; border-bottom:1px solid #eef2f7; text-align:left; }
.canvas-wrap { width:100%; aspect-ratio: 1 / 1; height:360px; display:flex; align-items:center; max-width: 600px; justify-content:center; }
.canvas { width:100%; max-width:640px; height:100%; background:linear-gradient(180deg,#fbfdff,#f7fbff); border-radius:8px; border:1px solid #e6eef8; }
.top-right { display:flex; gap:12px; align-items:center; }
.small-muted { color:#6b7280; font-size:13px; }
.error { color: #dc2626; }
@media (max-width:799px) {
  .canvas-wrap {
    max-width: 400px;
  }
}

@media (min-width:800px) and (max-width:1208px) {
  .canvas-wrap {
    max-width: 500px;
  }
}

@media (min-width:1209px) {
  .canvas-wrap {
    max-width: 520px;
  }
}
`;

// --- Auth context ---
const AuthContext = createContext(null);

function useAuth() {
  return useContext(AuthContext);
}

// simple JWT decode (no validation) to extract payload (for display only)
function decodeJwtPayload(token) {
  try {
    const parts = token.split('.');
    if (parts.length !== 3) return null;
    const payload = parts[1];
    const json = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
    return JSON.parse(decodeURIComponent(escape(json)));
  } catch (e) { return null; }
}

// --- API helper using access token in memory and credentials included for refresh cookie ---
function createApi(getToken, setToken) {
  const API = {};
  API.request = async (path, opts = {}) => {
    const headers = opts.headers || {};
    const token = getToken();
    if (token) headers['Authorization'] = `Bearer ${token}`;
    const res = await fetch(`${process.env.REACT_APP_API_URL}`+path, {
      credentials: 'include', // important for refresh-cookie
      headers: { 'Content-Type': 'application/json', ...headers },
      ...opts,
    });

    // auto-refresh on 401
    if (res.status === 401) {
      // attempt refresh
      const refreshed = await fetch(`${process.env.REACT_APP_API_URL}/auth/refresh`, { method: 'POST', credentials: 'include' });
      if (refreshed.ok) {
        const data = await refreshed.json();
        if (data.access_token) {
          setToken(data.access_token);
          // retry original
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

// --- App ---
export default function App() {
  const [token, setToken] = useState(null);
  const [loading, setLoading] = useState(true);

  const api = createApi(() => token, setToken);

  useEffect(() => {
    // silent refresh on app load
    (async () => {
      try {
        const res = await fetch(`${process.env.REACT_APP_API_URL}/auth/refresh`, { method: 'POST', credentials: 'include' });
        if (res.ok) {
          const data = await res.json();
          if (data.access_token) setToken(data.access_token);
        }
      } catch (e) {
        // ignore
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
      <style>{style}</style>
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

// --- AuthPage (login/register) ---
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
      // server returns access_token in JSON
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

// --- PointsPage ---
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
    // validation
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
    // compute coordinates relative to center based on bounding box
    const el = ev.currentTarget;
    const rect = el.getBoundingClientRect();
    const sx = ev.clientX - rect.left;
    const sy = ev.clientY - rect.top;
    // map to logical coord system: x from -5..5, y -5..5
    const lx = (sx / rect.width) * 10 - 5;
    const ly = -((sy / rect.height) * 10 - 5); // invert Y
    // use current R (if not selected pick 1)
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
