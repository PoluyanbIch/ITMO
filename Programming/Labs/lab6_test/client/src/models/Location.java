package models;

import utility.Validatable;

public class Location implements Validatable{
    private Float x; //Поле не может быть null
    private Double y; //Поле не может быть null
    private Double z; //Поле не может быть null

    public Location(Float x, Double y, Double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(String s) {
        String[] arr = s.split(" ");
        try {
            this.x = Float.parseFloat(arr[0]);
            this.y = Double.parseDouble(arr[1]);
            this.z = Double.parseDouble(arr[2]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {}
    }

    @Override
    public boolean validate() {
        return x != null && y != null && z != null;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Location other = (Location) obj;
        if (x == null) {
            if (other.x != null) {
                return false;
            }
        } else if (!x.equals(other.x)) {
            return false;
        }
        if (y == null) {
            if (other.y != null) {
                return false;
            }
        } else if (!y.equals(other.y)) {
            return false;
        }
        if (z == null) {
            if (other.z != null) {
                return false;
            }
        } else if (!z.equals(other.z)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return x.hashCode() + y.hashCode() + z.hashCode();
    }
}
