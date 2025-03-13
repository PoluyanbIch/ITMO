package models;

import utility.Validatable;

public class Coordinates implements Validatable{
    private Integer x; //Максимальное значение поля: 443, Поле не может быть null
    private float y;

    public Coordinates(Integer x, float y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates(String s) {
        String[] arr = s.split(" ");
        try {
            this.x = Integer.parseInt(arr[0]);
            this.y = Float.parseFloat(arr[1]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {}
    }

    @Override 
    public boolean validate() {
        return x != null && x <= 443;
    }

    @Override
    public String toString() {
        return x + " " + y;
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
        Coordinates other = (Coordinates) obj;
        if (x == null) {
            if (other.x != null) {
                return false;
            }
        } else if (!x.equals(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return x.hashCode() + Float.hashCode(y);
    }
}
