package postgres

import (
	"context"

	"github.com/PoluyanbIch/ITMO/Web/Labs/lab4/backend/internal/domain"
)

type PointRepository struct {
	db *DB
}

func NewPointRepository(db *DB) *PointRepository {
	return &PointRepository{db: db}
}

func (pr *PointRepository) Add(ctx context.Context, p domain.Point) error {
	query := `
				INSERT INTO points (x, y, r, isHit, userID)
				VALUES ($1, $2, $3, $4, $5)
	`
	_, err := pr.db.conn.ExecContext(ctx, query, p.X, p.Y, p.R, p.IsHit, p.UserID)
	if err != nil {
		pr.db.log.Error("point add db error", "error", err)
		return err
	}
	return nil
}
