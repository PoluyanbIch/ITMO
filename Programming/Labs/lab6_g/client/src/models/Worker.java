package models;

import java.time.ZonedDateTime;
import java.util.Date;

import utility.Validatable;
import utility.Element;

public class Worker extends Element implements Validatable{
    private Integer id;
    private String name;
    private Coordinates coordinates;
    private ZonedDateTime creationData;
    private long salary;
    private Date startDate; //Поле не может быть null
    private ZonedDateTime endDate; //Поле может быть null
    private Position position;
    private Organization organization;

    public Worker(Integer id, String name, Coordinates coordinates, ZonedDateTime creationData, long salary, Date startDate, ZonedDateTime endDate, Position position, Organization organization) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationData = creationData;
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.organization = organization;
    }

    public Worker(Integer id, String name, Coordinates coordinates, long salary, Date startDate, ZonedDateTime endDate, Position position, Organization organization) {
        this(id, name, coordinates, ZonedDateTime.now(), salary, startDate, endDate, position, organization);
    }

    public long getSalary() {
        return salary;
    }

    public Position getPosition() {
        return position;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    @Override
    public boolean validate() {
        if (id == null || id <= 0) {
            return false;
        }
        if (name == null || name.isEmpty()) {
            return false;
        }
        if (coordinates == null) {
            return false;
        }
        if (creationData == null) {
            return false;
        }
        if (salary <= 0) {
            return false;
        }
        if (startDate == null) {
            return false;
        }
        if (position == null) {
            return false;
        }
        if(organization == null){
            return false;
        }
        return true;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Element o) {
        return Integer.compare(id, o.getId());
    }

    @Override
    public String toString() {
        return "Name: " + this.name +
                "\nCoordinates: " + this.coordinates.toString() +
                "\nSalary: " + this.salary +
                "\nPosition: " + getPosition();
    }


}