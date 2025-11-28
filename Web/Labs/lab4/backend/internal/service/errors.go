package service

import "errors"

var (
	ErrLoginExists        = errors.New("user with this login already exist")
	ErrInvalidCredentials = errors.New("invalid credentials")
	ErrInvalidToken       = errors.New("invalid JWT")
)
