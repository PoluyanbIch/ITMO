package service

import (
	"context"

	"github.com/PoluyanbIch/ITMO/Web/Labs/lab4/backend/internal/domain"
)

type Service struct {
	PointRepo domain.PointRepository
}

func (s *Service) AddPoint(ctx context.Context, point domain.Point) error {
	if point.X < -3 || point.X > 5 {
		return ErrInvalidX
	}
	if point.Y < -5 || point.Y > 3 {
		return ErrInvalidY
	}
	if point.R < -3 || point.R > 5 {
		return ErrInvalidRadius
	}
	point.IsHit = checkHit(point)
	if err := s.PointRepo.Add(ctx, point); err != nil {
		return err
	}
	return nil
}

func (s *Service) GetPoints(ctx context.Context, userId int) ([]domain.Point, error) {
	points, err := s.PointRepo.GetPointsByUserID(ctx, userId)
	if err != nil {
		return nil, err
	}
	return points, nil
}

func checkHit(p domain.Point) bool {
	switch {
	case p.X >= 0 && p.Y >= 0:
		return p.Y <= -p.X+(p.R/2)
	case p.X < 0 && p.Y < 0:
		return p.Y*p.Y+p.X*p.X <= p.R*p.R
	case p.X < 0 && p.Y > 0:
		return p.Y <= (p.R/2) && p.X >= -p.R
	case p.X > 0 && p.Y < 0:
		return false
	}
	return false
}
