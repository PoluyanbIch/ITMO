package domain

type Point struct {
	ID     int     `db:"id"`
	X      float64 `db:"x"`
	Y      float64 `db:"y"`
	R      float64 `db:"r"`
	IsHit  bool    `db:"isHit"`
	UserID int     `db:"userID"`
}
