package com.lab6.common.models;

import com.lab6.common.utility.Element;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс, представляющий
 */
public class Worker extends Element implements Serializable {
    @Serial
    private static final long serialVersionUID = 20L;

    private Long id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private LocalDateTime creationDate; // Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long salary; // Поле может быть null, Значение поля должно быть больше 0
    private Long workersCount; // Поле может быть null, Значение поля должно быть больше 0
    private String description; // Поле не может быть null
    private Position position; // Поле может быть null
    private Organization organization; //Поле не может быть null

    /**
     * Конструктор для создания объекта Worker.
     *
     * @param id                   идентификатор группы, не может быть null
     * @param name                 название группы, не может быть null
     * @param creationDate         дата создания группы, не может быть null
     * @param salary               зарплата, может быть null
     * @param description          описание организации, не может быть null
     * @param coordinates          координаты группы, не может быть null
     * @param workersCount          количество альбомов, может быть null
     * @param position                жанр музыки, может быть null
     * @param organization               студия, не может быть null
     */
    public Worker(Long id, String name, LocalDateTime creationDate, Long salary, String description, Coordinates coordinates, Long workersCount, Position position, Organization organization) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.salary = salary;
        this.description = description;
        this.coordinates = coordinates;
        this.workersCount = workersCount;
        this.position = position;
        this.organization = organization;
    }

    public void updateId(Long id) {
       this.id = id;
    }

    /**
     * Возвращает идентификатор .
     *
     * @return идентификатор
     */
    public Long getId() {
        return id;
    }

    /**
     * Возвращает название .
     *
     * @return название
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает координаты .
     *
     * @return координаты
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Возвращает дату создания .
     *
     * @return дата создания
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Возвращает количество работников
     *
     * @return количество работников
     */
    public Long getSalary() {
        return salary;
    }

    /**
     * Возвращает количество работников.
     *
     * @return количество работников
     */
    public Long getWorkersCount() {
        return workersCount;
    }

    /**
     * Возвращает описание .
     *
     * @return описание
     */
    public String getDescription() {
        return description;
    }

    /**
     * Возвращает позицию.
     *
     * @return позиция
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Возвращает организацию
     *
     * @return организацию
     */
    public Organization getOrganization() {
        return organization;
    }

    /**
     * Проверяет равенство текущего объекта с другим объектом.
     *
     * @param object объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Worker worker = (Worker) object;
        return Objects.equals(id, worker.id);
    }

    /**
     * Возвращает хэш-код объекта.
     *
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, salary, workersCount, description, position, organization);
    }

    /**
     * Возвращает строковое представление объекта.
     *
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return
                "id=" + id +
                ", \nname='" + name + '\'' +
                ", \ncoordinates=" + coordinates +
                ", \ncreationDate=" + creationDate +
                ", \nsalary=" + salary +
                ", \nworkersCount=" + workersCount +
                ", \ndescription='" + description + '\'' +
                ", \nposition=" + position +
                ", \norganization=" + organization +
                '\n';
    }

    /**
     * Проверяет валидность объекта.
     */
    public boolean validate() {
        if (id == null || id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if (coordinates == null || !coordinates.validate()) return false;
        if (creationDate == null) return false;
        if (salary <= 0) return false;
        if (workersCount <= 0) return false;
        if (description == null) return false;
        if (organization == null || !organization.validate()) return false;
        return true;
    }

    /**
     * Возвращает идентификатор элемента.
     *
     * @return идентификатор элемента
     */
    @Override
    public Long getID() {
        return id;
    }

    /**
     * Сравнивает текущий объект с другим объектом.
     *
     * @param o объект для сравнения
     * @return результат сравнения
     */
    @Override
    public int compareTo(Element o) {
        return (int) (this.id - o.getID());
    }

    /**
     * Создает объект Worker из массива строк.
     *
     * @param array массив строк
     * @return объект Worker
     */
    public static Worker fromArray(String[] array) {
        Long id;
        String name;
        Coordinates coordinates;
        LocalDateTime creationDate;
        Long numberOfParticipants;
        Long albumsCount;
        String description;
        Position genre;
        Organization organization;
        try {
            id = Long.parseLong(array[0]);
            name = array[1];
            coordinates = new Coordinates(Double.parseDouble(array[2]), Integer.parseInt(array[3]));
            creationDate = LocalDateTime.parse(array[4]);
            numberOfParticipants = Long.parseLong(array[5]);
            albumsCount = Long.parseLong(array[6]);
            description = array[7];
            genre = Position.valueOf(array[8]);
            organization = new Organization(array[9], array[10]);
        } catch (Exception e) {
            return null;
        }
        return new Worker(id, name, creationDate, numberOfParticipants, description, coordinates, albumsCount, genre, organization);
    }

    /**
     * Преобразует объект Worker в массив строк.
     *
     * @param worker объект Worker
     * @return массив строк
     */
    public static String[] toArray(Worker worker) {
        return new String[]{
                worker.getId().toString(),
                worker.getName(),
                Double.toString(worker.getCoordinates().getX()),
                worker.getCoordinates().getY().toString(),
                worker.getCreationDate().toString(),
                worker.getSalary().toString(),
                worker.getWorkersCount().toString(),
                worker.getDescription(),
                worker.getPosition().toString(),
                worker.getOrganization().getName(),
                worker.getOrganization().getAddress()
        };
    }
}
