package domain

type Point struct {
	ID     int     `db:"id" json:"id"`
	X      float64 `db:"x" json:"x"`
	Y      float64 `db:"y" json:"y"`
	R      float64 `db:"r" json:"r"`
	IsHit  bool    `db:"ishit" json:"isHit"`
	UserID int     `db:"userid" json:"userID"`
}
