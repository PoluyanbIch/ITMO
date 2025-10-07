package ru.poluyanblch.web.models;

import java.util.Objects;

public class Point {
    private final double x;
    private final double y;
    private final int r;

    private final boolean isInArea;

    public Point(double x, double y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.isInArea = isInside(x, y, r);
    }

    private boolean isInside(double x, double y, double r) {
        if (x >= 0 && y >= 0) {
            return (x <= r) && (y <= r / 2);
        }
        if (x < 0 && y >= 0) {
            return (y <= x + r);
        }
        if (x <= 0 && y < 0) {
            return (x * x + y * y) <= (r * r);
        }

        return false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getR() {
        return r;
    }

    public boolean isInArea() {
        return isInArea;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && Double.compare(y, point.y) == 0 && r == point.r;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, r);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", isInArea=" + isInArea +
                '}';
    }
}