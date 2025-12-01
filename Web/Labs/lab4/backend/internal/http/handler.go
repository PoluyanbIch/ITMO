package http

import (
	"context"
	"encoding/json"
	"errors"
	"log/slog"
	"net/http"
	"slices"
	"strconv"
	"strings"

	"github.com/PoluyanbIch/ITMO/Web/Labs/lab4/backend/internal/domain"
	"github.com/PoluyanbIch/ITMO/Web/Labs/lab4/backend/internal/service"
)

type Handler struct {
	log         *slog.Logger
	service     *service.Service
	authService *service.AuthService
}

func NewHandler(log *slog.Logger, service *service.Service, authService *service.AuthService) *Handler {
	return &Handler{
		log:         log,
		service:     service,
		authService: authService,
	}
}

func CorsMiddleware(next http.Handler, corsOrigins string) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		origins := strings.Split(corsOrigins, ",")
		origin := r.Header.Get("Origin")
		if slices.Contains(origins, origin) {
			w.Header().Set("Access-Control-Allow-Origin", origin)
		}
		w.Header().Set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
		w.Header().Set("Access-Control-Allow-Headers", "Content-Type, Authorization")
		w.Header().Set("Access-Control-Allow-Credentials", "true")

		if r.Method == "OPTIONS" {
			w.WriteHeader(http.StatusOK)
			return
		}

		next.ServeHTTP(w, r)
	})
}

func (h *Handler) LoginHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithCancel(r.Context())
	defer cancel()
	login := r.PostFormValue("login")
	password := r.PostFormValue("password")
	tokens, err := h.authService.Login(ctx, login, password)
	if err != nil {
		if errors.Is(err, service.ErrInvalidCredentials) {
			http.Error(w, "invalid credentials", http.StatusUnauthorized)
			return
		}
		h.log.Error("authService login error", "error", err)
		http.Error(w, "interanl error", http.StatusInternalServerError)
		return
	}

	http.SetCookie(w, &http.Cookie{
		Name:     "refresh",
		Value:    tokens.RefreshToken,
		HttpOnly: true,
		Secure:   true,
		SameSite: http.SameSiteLaxMode,
		Path:     "/auth/refresh",
	})

	response := map[string]string{
		"access_token": tokens.AccessToken,
	}
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(response)
}

func (h *Handler) RegisterHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithCancel(r.Context())
	defer cancel()
	login := r.PostFormValue("login")
	password := r.PostFormValue("password")
	tokens, err := h.authService.Register(ctx, login, password)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		h.log.Error("authService register error", "error", err)
		return
	}

	http.SetCookie(w, &http.Cookie{
		Name:     "refresh",
		Value:    tokens.RefreshToken,
		HttpOnly: true,
		Secure:   true,
		SameSite: http.SameSiteLaxMode,
		Path:     "/auth/refresh",
	})

	response := map[string]string{
		"access_token": tokens.AccessToken,
	}
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusCreated)
	json.NewEncoder(w).Encode(response)
}

func (h *Handler) RefreshHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithCancel(r.Context())
	defer cancel()

	cookie, err := r.Cookie("refresh")
	if err != nil {
		http.Error(w, "missing refresh token", http.StatusUnauthorized)
		return
	}
	refreshToken := cookie.Value

	tokens, err := h.authService.Refresh(ctx, refreshToken)
	if err != nil {
		http.Error(w, "invalid refresh token", http.StatusUnauthorized)
		return
	}

	http.SetCookie(w, &http.Cookie{
		Name:     "refresh",
		Value:    tokens.RefreshToken,
		HttpOnly: true,
		Secure:   true,
		SameSite: http.SameSiteLaxMode,
		Path:     "/auth/refresh",
	})

	response := map[string]string{
		"access_token": tokens.AccessToken,
	}
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(response)
}

func (h *Handler) LogoutHandler(w http.ResponseWriter, r *http.Request) {
	http.SetCookie(w, &http.Cookie{
		Name:     "refresh",
		Value:    "",
		Path:     "/auth/refresh",
		HttpOnly: true,
		Secure:   true,
		SameSite: http.SameSiteLaxMode,
		MaxAge:   -1,
	})
	w.WriteHeader(http.StatusOK)
}

func (h *Handler) AuthMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		token := extractToken(r)
		if token == "" {
			http.Error(w, "Unauthorised", http.StatusUnauthorized)
			return
		}
		userID, login, err := h.authService.VerifyAccessToken(token)
		if err != nil {
			http.Error(w, "Unauthorised", http.StatusUnauthorized)
			return
		}
		ctx := context.WithValue(r.Context(), "userID", userID)
		ctx = context.WithValue(ctx, "login", login)

		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

func extractToken(r *http.Request) string {
	authHeader := r.Header.Get("Authorization")
	if authHeader == "" {
		return ""
	}
	parts := strings.SplitN(authHeader, " ", 2)
	if len(parts) != 2 || !strings.EqualFold(parts[0], "Bearer") {
		return ""
	}
	return parts[1]
}

func (h *Handler) AddPointHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithCancel(r.Context())
	defer cancel()

	userID, ok := ctx.Value("userID").(int)
	if !ok {
		http.Error(w, "user not found in context", http.StatusUnauthorized)
		return
	}
	var body struct {
		X string `json:"x"`
		Y string `json:"y"`
		R string `json:"r"`
	}
	if err := json.NewDecoder(r.Body).Decode(&body); err != nil {
		http.Error(w, "invalid request body", http.StatusBadRequest)
		return
	}
	x, err := strconv.ParseFloat(body.X, 64)
	if err != nil {
		http.Error(w, "invalid x", http.StatusBadRequest)
		return
	}
	y, err := strconv.ParseFloat(body.Y, 64)
	if err != nil {
		http.Error(w, "invalid y", http.StatusBadRequest)
		return
	}
	radius, err := strconv.ParseFloat(body.R, 64)
	if err != nil {
		http.Error(w, "invalid radius", http.StatusBadRequest)
		return
	}

	p := domain.Point{X: x, Y: y, R: radius, UserID: userID}
	if err := h.service.AddPoint(ctx, p); err != nil {
		if errors.Is(err, service.ErrInvalidX) || errors.Is(err, service.ErrInvalidY) || errors.Is(err, service.ErrInvalidRadius) {
			http.Error(w, err.Error(), http.StatusBadRequest)
			return
		}
		http.Error(w, "internal error", http.StatusBadRequest)
		return
	}
	w.WriteHeader(http.StatusCreated)
}

func (h *Handler) GetPointsHandler(w http.ResponseWriter, r *http.Request) {
	ctx, cancel := context.WithCancel(r.Context())
	defer cancel()

	userID, ok := ctx.Value("userID").(int)
	if !ok {
		http.Error(w, "user not found in context", http.StatusUnauthorized)
		return
	}

	points, err := h.service.GetPoints(ctx, userID)
	if err != nil {
		http.Error(w, "failed to get points", http.StatusInternalServerError)
		return
	}
	w.WriteHeader(http.StatusOK)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(points)
}
