package com.lab6.common.models;

import com.lab6.common.utility.Validatable;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * Класс, представляющий организацию.
 */
public class Organization implements Validatable, Serializable {
    @Serial
    private static final long serialVersionUID = 22L;

    private String name; // Поле не может быть null
    private String address; // Поле может быть null

    /**
     * Конструктор для создания объекта Организация.
     * @param name название организации, не может быть null
     * @param address адрес организации, может быть null
     */
    public Organization(String name, String address) {
        this.name = name;
        this.address = address;
    }

    /**
     * Возвращает название организации.
     * @return название организации
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает адрес студии.
     * @return адрес студии
     */
    public String getAddress() {
        return address;
    }

    /**
     * Проверяет равенство текущего объекта с другим объектом.
     * @param object объект для сравнения
     * @return true, если объекты равны, иначе false
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Organization organization = (Organization) object;
        return Objects.equals(name, organization.name) && Objects.equals(address, organization.address);
    }

    /**
     * Возвращает хэш-код объекта.
     * @return хэш-код объекта
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    /**
     * Возвращает строковое представление объекта.
     * @return строковое представление объекта
     */
    @Override
    public String toString() {
        return '{' +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    /**
     * Проверяет валидность объекта.
     * @return true, если объект валиден, иначе false
     */
    @Override
    public boolean validate() {
        return name != null;
    }
}
