package service

import "errors"

var (
	ErrLoginExists        = errors.New("user with this login already exist")
	ErrInvalidCredentials = errors.New("invalid credentials")
	ErrInvalidToken       = errors.New("invalid JWT")

	ErrInvalidX      = errors.New("invalid X, X must be: -3 <= X <= 5")
	ErrInvalidY      = errors.New("invalid Y, Y must be: -5 <= Y <= 3")
	ErrInvalidRadius = errors.New("invalid R, R must be: -3 <= R <= 5")
)
