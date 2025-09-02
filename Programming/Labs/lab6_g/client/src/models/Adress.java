package models;

import utility.Validatable;

public class Adress implements Validatable{
    private String zipCode; //Длина строки должна быть не меньше 9, Поле может быть null
    private Location town; //Поле не может быть null

    public Adress(String zipCode, Location town) {
        this.zipCode = zipCode;
        this.town = town;
    }

    @Override
    public boolean validate() {
        return zipCode != null && zipCode.length() >= 9 && town != null;
    }

    @Override
    public String toString() {
        return "Adress{" +
                "zipCode='" + zipCode + '\'' +
                ", town=" + town +
                '}';
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
        Adress other = (Adress) obj;
        if (zipCode == null) {
            if (other.zipCode != null) {
                return false;
            }
        } else if (!zipCode.equals(other.zipCode)) {
            return false;
        }
        if (town == null) {
            if (other.town != null) {
                return false;
            }
        } else if (!town.equals(other.town)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return zipCode.hashCode() + town.hashCode();
    }
}
