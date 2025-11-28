package domain

import "context"

type UserRepository interface {
	Add(context.Context, User) error
	GetUserByLogin(context.Context, string) (User, error)
}

type PointRepository interface {
	Add(context.Context, Point) error
	GetPointsByUserID(context.Context, int) ([]Point, error)
}
