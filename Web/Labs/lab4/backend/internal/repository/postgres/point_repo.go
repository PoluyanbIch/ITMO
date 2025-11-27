package postgres

type PointRepository struct {
	db *DB
}

func NewPointRepository(db *DB) *PointRepository {
	return &PointRepository{db: db}
}
