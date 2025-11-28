package service

import (
	"context"
	"errors"
	"strconv"
	"time"

	"github.com/PoluyanbIch/ITMO/Web/Labs/lab4/backend/internal/domain"
	"github.com/golang-jwt/jwt/v5"
	"golang.org/x/crypto/bcrypt"
)

type AuthService struct {
	userRepo  domain.UserRepository
	jwtSecret string
}

func NewAuthService(userRepo domain.UserRepository, jwtSecret string) *AuthService {
	return &AuthService{
		userRepo:  userRepo,
		jwtSecret: jwtSecret,
	}
}

func (s *AuthService) Register(ctx context.Context, login string, password string) (string, error) {
	_, err := s.userRepo.GetUserByLogin(ctx, login)
	if err == nil {
		return "", ErrLoginExists
	}
	if !errors.Is(err, domain.ErrUserNotFound) {
		return "", err
	}
	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	if err != nil {
		return "", err
	}
	user := domain.User{
		Login:        login,
		PasswordHash: string(hashedPassword),
	}
	if err := s.userRepo.Add(ctx, user); err != nil {
		return "", err
	}
	user, err = s.userRepo.GetUserByLogin(ctx, login)
	if err != nil {
		return "", err
	}
	userID := user.ID
	token, err := s.generateJWT(userID)
	if err != nil {
		return "", err
	}
	return token, nil
}

func (s *AuthService) Login(ctx context.Context, login string, password string) (string, error) {
	user, err := s.userRepo.GetUserByLogin(ctx, login)
	if err != nil {
		if errors.Is(err, domain.ErrUserNotFound) {
			return "", ErrInvalidCredentials
		}
		return "", err
	}
	err = bcrypt.CompareHashAndPassword([]byte(user.PasswordHash), []byte(password))
	if err != nil {
		return "", err
	}
	token, err := s.generateJWT(user.ID)
	if err != nil {
		return "", err
	}
	return token, nil
}

func (s *AuthService) generateJWT(userID int) (string, error) {
	claims := jwt.RegisteredClaims{
		Subject:   strconv.FormatInt(int64(userID), 10),
		ExpiresAt: jwt.NewNumericDate(time.Now().Add(24 * time.Hour)),
		IssuedAt:  jwt.NewNumericDate(time.Now()),
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString([]byte(s.jwtSecret))
}

func (s *AuthService) VerifyToken(tokenString string) (int, error) {
	claims := &jwt.RegisteredClaims{}

	token, err := jwt.ParseWithClaims(tokenString, claims, func(token *jwt.Token) (interface{}, error) {
		return []byte(s.jwtSecret), nil
	})

	if err != nil || !token.Valid {
		return 0, ErrInvalidToken
	}

	userID, err := strconv.Atoi(claims.Subject)
	if err != nil {
		return 0, ErrInvalidToken
	}

	return userID, nil
}
