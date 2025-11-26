package domain

import "context"

type UserRepository interface {
	Add(context.Context, User) error
	Check(context.Context, User) (bool, error)
}

type PointRepository interface {
	Add(context.Context, Point) error
	GetPointsByUserID(context.Context, int) ([]Point, error)
}
