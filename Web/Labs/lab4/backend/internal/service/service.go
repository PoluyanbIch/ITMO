package service

import (
	"context"

	"github.com/PoluyanbIch/ITMO/Web/Labs/lab4/backend/internal/domain"
)

type Service struct {
	PointRepo domain.PointRepository
}

func (s *Service) AddPoint(ctx context.Context, point domain.Point) error {
	if err := s.PointRepo.Add(ctx, point); err != nil {
		return err
	}
	return nil
}
