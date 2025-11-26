package domain

type User struct {
	ID           int    `db:"id"`
	Login        string `db:"login"`
	PasswordHash string `db:"password"`
}
