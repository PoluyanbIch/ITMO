package postgres

import (
	"context"

	"github.com/PoluyanbIch/ITMO/Web/Labs/lab4/backend/internal/domain"
)

type UserRepository struct {
	db *DB
}

func NewUserRepository(db *DB) *UserRepository {
	return &UserRepository{db: db}
}

func (ur *UserRepository) Add(ctx context.Context, user domain.User) error {
	query := `
				INSERT INTO users (login, password)
				VALUES ($1, $2)
	`
	_, err := ur.db.conn.ExecContext(ctx, query, user.Login, user.PasswordHash)
	if err != nil {
		ur.db.log.Error("add users db error", "error", err)
		return err
	}
	return nil
}

func (ur *UserRepository) GetUserByLogin(ctx context.Context, login string) (domain.User, error) {
	query := `
				SELECT * FROM users 
				WHERE login = $1
	`
	user := domain.User{}
	if err := ur.db.conn.SelectContext(ctx, &user, query, login); err != nil {
		ur.db.log.Error("get user by login db error", "error", err)
		return domain.User{}, err
	}
	return user, nil
}
