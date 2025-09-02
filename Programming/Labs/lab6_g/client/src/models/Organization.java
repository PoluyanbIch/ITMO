package models;

import utility.Validatable;

public class Organization implements Validatable{
    private Double annualTurnover; //Поле не может быть null, Значение поля должно быть больше 0
    private Adress postalAdress; //Поле не может быть null

    public Organization(Double annualTurnover, Adress postalAdress) {
        this.annualTurnover = annualTurnover;
        this.postalAdress = postalAdress;
    }

    @Override
    public boolean validate() {
        return annualTurnover != null && annualTurnover > 0 && postalAdress != null;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "annualTurnover=" + annualTurnover +
                ", postalAdress=" + postalAdress +
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
        Organization other = (Organization) obj;
        if (annualTurnover == null) {
            if (other.annualTurnover != null) {
                return false;
            }
        } else if (!annualTurnover.equals(other.annualTurnover)) {
            return false;
        }
        if (postalAdress == null) {
            if (other.postalAdress != null) {
                return false;
            }
        } else if (!postalAdress.equals(other.postalAdress)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return annualTurnover.hashCode() + postalAdress.hashCode();
    }
}
