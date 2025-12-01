package main

import (
	"context"
	"errors"
	"flag"
	"log/slog"
	"net/http"
	"os"
	"os/signal"

	"github.com/PoluyanbIch/ITMO/Web/Labs/lab4/backend/internal/config"
	myHttp "github.com/PoluyanbIch/ITMO/Web/Labs/lab4/backend/internal/http"
	"github.com/PoluyanbIch/ITMO/Web/Labs/lab4/backend/internal/repository/postgres"
	"github.com/PoluyanbIch/ITMO/Web/Labs/lab4/backend/internal/service"
)

func main() {
	var configPath string
	flag.StringVar(&configPath, "config", "config.yaml", "server configureation file")
	flag.Parse()
	cfg := config.MustLoad(configPath)

	log := mustMakeLogger(cfg.LogLevel)
	if err := run(cfg, log); err != nil {
		log.Error("server exit", "error", err)
		os.Exit(1)
	}
}

func run(cfg config.Config, log *slog.Logger) error {
	log.Info("Starting server")
	db, err := postgres.NewDB(log, cfg.DBAddress)
	if err != nil {
		log.Error("db create error", "error", err)
		return err
	}
	if err := db.Migrate(); err != nil {
		log.Error("failed to migrate db", "error", err)
		return err
	}

	userRepo := postgres.NewUserRepository(db)
	pointRepo := postgres.NewPointRepository(db)
	authService := service.NewAuthService(userRepo, cfg.JWTSecret, cfg.RefreshSecret)
	srv := service.NewService(pointRepo)
	handler := myHttp.NewHandler(log, srv, authService)

	mux := http.NewServeMux()

	mux.HandleFunc("POST /auth/login", handler.LoginHandler)
	mux.HandleFunc("POST /auth/register", handler.RegisterHandler)
	mux.HandleFunc("POST /auth/refresh", handler.RefreshHandler)
	mux.HandleFunc("POST /auth/logout", handler.LogoutHandler)

	mux.Handle("GET /points", handler.AuthMiddleware(http.HandlerFunc(handler.GetPointsHandler)))
	mux.Handle("POST /points/add", handler.AuthMiddleware(http.HandlerFunc(handler.AddPointHandler)))

	h := myHttp.CorsMiddleware(mux, cfg.CorsOrigins)
	server := http.Server{
		Addr:    cfg.Address,
		Handler: h,
	}

	ctx, stop := signal.NotifyContext(context.Background(), os.Interrupt)
	defer stop()

	go func() {
		<-ctx.Done()
		log.Debug("shutting down server")
		if err := server.Shutdown(context.Background()); err != nil {
			log.Error("erroneous shutdown", "error", err)
		}
	}()

	log.Info("Running HTTP server", "address", cfg.Address)
	if err := server.ListenAndServe(); err != nil {
		if !errors.Is(err, http.ErrServerClosed) {
			log.Error("server closed unexpectedly", "error", err)
			return err
		}
	}
	return nil
}

func mustMakeLogger(logLevel string) *slog.Logger {
	var level slog.Level
	switch logLevel {
	case "DEBUG":
		level = slog.LevelDebug
	case "INFO":
		level = slog.LevelInfo
	case "ERROR":
		level = slog.LevelError
	default:
		panic("unknown log level: " + logLevel)
	}
	handler := slog.NewTextHandler(os.Stderr, &slog.HandlerOptions{Level: level})
	return slog.New(handler)
}
