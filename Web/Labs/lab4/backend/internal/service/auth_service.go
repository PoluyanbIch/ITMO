package service

import (
	"context"
	"errors"
	"regexp"
	"strconv"
	"strings"
	"time"
	"unicode"

	"github.com/PoluyanbIch/ITMO/Web/Labs/lab4/backend/internal/domain"
	"github.com/golang-jwt/jwt/v5"
	"golang.org/x/crypto/bcrypt"
)

type AuthService struct {
	userRepo      domain.UserRepository
	jwtSecret     string
	refreshSecret string
}

type AuthTokens struct {
	AccessToken  string
	RefreshToken string
}

type CustomClaims struct {
	Login string `json:"login"`
	jwt.RegisteredClaims
}

const (
	MinLoginLength    = 4
	MaxLoginLength    = 32
	MinPasswordLength = 8
	MaxPasswordLength = 64
)

var loginRegex = regexp.MustCompile(`^[a-zA-Z0-9_]+$`)

func NewAuthService(userRepo domain.UserRepository, jwtSecret string, refreshSecret string) *AuthService {
	return &AuthService{
		userRepo:      userRepo,
		jwtSecret:     jwtSecret,
		refreshSecret: refreshSecret,
	}
}

func (s *AuthService) Register(ctx context.Context, login string, password string) (AuthTokens, error) {
	authTokens := AuthTokens{}

	if err := validateLogin(login); err != nil {
		return authTokens, err
	}

	if err := validatePassword(password); err != nil {
		return authTokens, err
	}

	_, err := s.userRepo.GetUserByLogin(ctx, login)
	if err == nil {
		return authTokens, ErrLoginExists
	}
	if !errors.Is(err, domain.ErrUserNotFound) {
		return authTokens, err
	}
	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	if err != nil {
		return authTokens, err
	}
	user := domain.User{
		Login:        login,
		PasswordHash: string(hashedPassword),
	}
	if err := s.userRepo.Add(ctx, user); err != nil {
		return authTokens, err
	}
	user, err = s.userRepo.GetUserByLogin(ctx, login)
	if err != nil {
		return authTokens, err
	}
	userID := user.ID
	accessToken, err := s.generateAccessToken(userID, login)
	if err != nil {
		return authTokens, err
	}
	refreshToken, err := s.generateRefreshToken(userID, login)
	if err != nil {
		return authTokens, err
	}
	authTokens.AccessToken = accessToken
	authTokens.RefreshToken = refreshToken
	return authTokens, nil
}

func (s *AuthService) Login(ctx context.Context, login string, password string) (AuthTokens, error) {
	authTokens := AuthTokens{}

	login = strings.TrimSpace(login)
	if len(login) == 0 || len(login) > MaxLoginLength {
		return authTokens, ErrInvalidCredentials
	}

	password = strings.TrimSpace(password)
	if len(password) == 0 || len(password) > MaxPasswordLength {
		return authTokens, ErrInvalidCredentials
	}

	user, err := s.userRepo.GetUserByLogin(ctx, login)
	if err != nil {
		if errors.Is(err, domain.ErrUserNotFound) {
			return authTokens, ErrInvalidCredentials
		}
		return authTokens, err
	}
	err = bcrypt.CompareHashAndPassword([]byte(user.PasswordHash), []byte(password))
	if err != nil {
		if errors.Is(err, bcrypt.ErrMismatchedHashAndPassword) {
			return authTokens, ErrInvalidCredentials
		}
		return authTokens, err
	}
	accessToken, err := s.generateAccessToken(user.ID, user.Login)
	if err != nil {
		return authTokens, err
	}
	refreshToken, err := s.generateRefreshToken(user.ID, user.Login)
	if err != nil {
		return authTokens, err
	}
	authTokens.AccessToken = accessToken
	authTokens.RefreshToken = refreshToken
	return authTokens, nil
}

func (s *AuthService) Refresh(ctx context.Context, refreshToken string) (AuthTokens, error) {
	userId, login, err := s.VerifyRefreshToken(refreshToken)
	if err != nil {
		return AuthTokens{}, err
	}
	access, err := s.generateAccessToken(userId, login)
	if err != nil {
		return AuthTokens{}, err
	}
	refresh, err := s.generateRefreshToken(userId, login)
	if err != nil {
		return AuthTokens{}, err
	}
	return AuthTokens{AccessToken: access, RefreshToken: refresh}, nil
}

func (s *AuthService) generateAccessToken(userID int, login string) (string, error) {
	claims := CustomClaims{
		Login: login,
		RegisteredClaims: jwt.RegisteredClaims{
			Subject:   strconv.Itoa(userID),
			ExpiresAt: jwt.NewNumericDate(time.Now().Add(15 * time.Minute)),
			IssuedAt:  jwt.NewNumericDate(time.Now()),
		},
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString([]byte(s.jwtSecret))
}

func (s *AuthService) generateRefreshToken(userID int, login string) (string, error) {
	claims := CustomClaims{
		Login: login,
		RegisteredClaims: jwt.RegisteredClaims{
			Subject:   strconv.Itoa(userID),
			ExpiresAt: jwt.NewNumericDate(time.Now().Add(30 * 24 * time.Hour)),
			IssuedAt:  jwt.NewNumericDate(time.Now()),
		},
	}
	token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
	return token.SignedString([]byte(s.refreshSecret))
}

func (s *AuthService) VerifyAccessToken(tokenString string) (int, string, error) {
	claims := &CustomClaims{}

	token, err := jwt.ParseWithClaims(tokenString, claims, func(token *jwt.Token) (interface{}, error) {
		return []byte(s.jwtSecret), nil
	})

	if err != nil || !token.Valid {
		return 0, "", ErrInvalidToken
	}

	userID, err := strconv.Atoi(claims.Subject)
	if err != nil {
		return 0, "", ErrInvalidToken
	}

	return userID, claims.Login, nil
}

func (s *AuthService) VerifyRefreshToken(tokenString string) (int, string, error) {
	claims := &CustomClaims{}

	token, err := jwt.ParseWithClaims(tokenString, claims, func(token *jwt.Token) (interface{}, error) {
		return []byte(s.refreshSecret), nil
	})

	if err != nil || !token.Valid {
		return 0, "", ErrInvalidToken
	}

	userID, err := strconv.Atoi(claims.Subject)
	if err != nil {
		return 0, "", ErrInvalidToken
	}

	return userID, claims.Login, nil
}

func validateLogin(login string) error {
	login = strings.TrimSpace(login)

	if len(login) < MinLoginLength {
		return ErrLoginTooShort
	}

	if len(login) > MaxLoginLength {
		return ErrLoginTooLong
	}

	// Проверка на допустимые символы
	if !loginRegex.MatchString(login) {
		return ErrLoginInvalidChars
	}

	// Проверка, что логин не начинается с цифры
	if len(login) > 0 && unicode.IsDigit(rune(login[0])) {
		return ErrLoginStartsWithNumber
	}

	return nil
}

// validatePassword проверяет пароль на соответствие требованиям
func validatePassword(password string) error {
	password = strings.TrimSpace(password)

	if len(password) < MinPasswordLength {
		return ErrPasswordTooShort
	}

	if len(password) > MaxPasswordLength {
		return ErrPasswordTooLong
	}

	// Проверка на пробелы
	if strings.Contains(password, " ") {
		return ErrPasswordContainsSpaces
	}

	var (
		hasUpper   = false
		hasLower   = false
		hasDigit   = false
		hasSpecial = false
	)

	// Специальные символы
	specialChars := "!@#$%^&*()-_=+{}[]|;:,.<>?"

	for _, char := range password {
		switch {
		case unicode.IsUpper(char):
			hasUpper = true
		case unicode.IsLower(char):
			hasLower = true
		case unicode.IsDigit(char):
			hasDigit = true
		case strings.ContainsRune(specialChars, char):
			hasSpecial = true
		}
	}

	if !hasUpper {
		return ErrPasswordNoUpperCase
	}

	if !hasLower {
		return ErrPasswordNoLowerCase
	}

	if !hasDigit {
		return ErrPasswordNoDigit
	}

	if !hasSpecial {
		return ErrPasswordNoSpecialChar
	}

	return nil
}
