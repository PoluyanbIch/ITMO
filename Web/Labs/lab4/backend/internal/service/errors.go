package service

import "errors"

var (
	ErrLoginExists        = errors.New("user with this login already exist")
	ErrInvalidCredentials = errors.New("invalid credentials")
	ErrInvalidToken       = errors.New("invalid JWT")

	ErrInvalidX      = errors.New("invalid X, X must be: -5 <= X <= 5")
	ErrInvalidY      = errors.New("invalid Y, Y must be: -5 <= Y <= 3")
	ErrInvalidRadius = errors.New("invalid R, R must be: -3 <= R <= 5")

	ErrLoginTooShort          = errors.New("логин должен содержать минимум 4 символа")
	ErrLoginTooLong           = errors.New("логин должен содержать максимум 32 символа")
	ErrLoginInvalidChars      = errors.New("логин может содержать только буквы, цифры и подчеркивание")
	ErrLoginStartsWithNumber  = errors.New("логин не может начинаться с цифры")
	ErrPasswordTooShort       = errors.New("пароль должен содержать минимум 8 символов")
	ErrPasswordTooLong        = errors.New("пароль должен содержать максимум 64 символа")
	ErrPasswordNoUpperCase    = errors.New("пароль должен содержать хотя бы одну заглавную букву")
	ErrPasswordNoLowerCase    = errors.New("пароль должен содержать хотя бы одну строчную букву")
	ErrPasswordNoDigit        = errors.New("пароль должен содержать хотя бы одну цифру")
	ErrPasswordNoSpecialChar  = errors.New("пароль должен содержать хотя бы один специальный символ (!@#$%^&*()-_=+{}[]|;:,.<>?)")
	ErrPasswordContainsSpaces = errors.New("пароль не должен содержать пробелы")
)
