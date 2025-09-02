package com.lab6.client.managers;

import com.lab6.client.utility.FileConsole;
import com.lab6.common.models.Organization;
import com.lab6.common.models.Worker;
import com.lab6.common.models.Position;
import com.lab6.common.models.Coordinates;
import com.lab6.client.utility.Console;

import java.time.LocalDateTime;

/**
 * Класс, запрашивающий у пользователя данные для создания объектов.
 */
public class Asker {
    /**
     * Исключение, выбрасываемое для прерывания ввода.
     */
    public static class Breaker extends Exception { }

    /**
     * Исключение, выбрасываемое при некорректном вводе.
     */
    public static class IllegalInputException extends IllegalArgumentException {
        /**
         * Конструктор исключения с сообщением.
         * @param message Сообщение об ошибке.
         */
        public IllegalInputException(String message) {
            super(message);
        }
    }

    /**
     * Запрашивает у пользователя данные для создания объекта.
     * @param console Консоль для ввода/вывода.
     * @param id Идентификатор .
     * @return Объект MusicBand с введенными данными.
     * @throws Breaker Исключение, выбрасываемое при вводе команды "exit".
     * @throws IllegalInputException Исключение, выбрасываемое при некорректном вводе.
     */
    public static Worker askBand(Console console, Long id) throws Breaker, IllegalInputException {
        String name;
        do {
            console.println("Введите значение поля name:");
            name = console.readln();
            if (name.equals("exit")) {
                throw new Breaker();
            }
        } while (name.isEmpty());

        Coordinates coordinates = askCoordinates(console);

        Long salary;
        do {
            console.println("Введите значение поля salary:");
            try {
                String input = console.readln();
                if (input.equals("exit")) {
                    throw new Breaker();
                } else {
                    salary = Long.valueOf(input);
                }
            } catch (NumberFormatException e) {
                salary = -1L;
            }
            if (salary <= 0) {
                if (console instanceof FileConsole){
                    throw new IllegalInputException("Некорректное значение поля salary!\nЗначение поля должно быть больше 0");
                }
                console.printError("Некорректное значение поля salary!\nЗначение поля должно быть больше 0");
            }
        } while (salary <= 0);

        Long workersCount;
        do {
            console.println("Введите значение поля workersCount:");
            try {
                String input = console.readln();
                if (input.equals("exit")) {
                    throw new Breaker();
                } else {
                    workersCount = Long.valueOf(input);
                }
            } catch (NumberFormatException e) {
                workersCount = -1L;
            }
            if (workersCount <= 0) {
                if (console instanceof FileConsole){
                    throw new IllegalInputException("Некорректное значение поля workersCount!\nЗначение поля должно быть больше 0");
                }
                console.printError("Некорректное значение поля workersCount!\nЗначение поля должно быть больше 0");
            }
        } while (workersCount <= 0);

        String description;
        do {
            console.println("Введите значение поля description:");
            description = console.readln();
            if (description.equals("exit")) {
                throw new Breaker();
            }
        } while (description.isEmpty());

        Position position = null;
        do {
            console.println("Введите значение поля position:");
            console.println("Список возможных значений: " + Position.list());
            String input = console.readln();
            if (input.equals("exit")) {
                throw new Breaker();
            } else {
                try {
                    position = Position.valueOf(input);
                } catch (IllegalArgumentException e) {
                    if (console instanceof FileConsole){
                        throw new IllegalArgumentException("Некорректное значение поля position!");
                    }
                    console.printError("Некорректное значение поля position!");
                }
            }
        } while (position == null);

        Organization organization = askOrganization(console);

        return new Worker(id, name, LocalDateTime.now(), salary, description, coordinates, workersCount, position, organization);
    }

    /**
     * Запрашивает у пользователя данные для создания объекта Coordinates.
     * @param console Консоль для ввода/вывода.
     * @return Объект Coordinates с введенными данными.
     * @throws Breaker Исключение, выбрасываемое при вводе команды "exit".
     * @throws IllegalInputException Исключение, выбрасываемое при некорректном вводе.
     */
    private static Coordinates askCoordinates(Console console) throws Breaker, IllegalInputException {
        console.println("Ввод значений поля Coordinates...");
        double x;
        do {
            console.println("Введите значение поля x:");
            try {
                String input = console.readln();
                if (input.equals("exit")) {
                    throw new Breaker();
                } else {
                    x = Double.parseDouble(input);
                }
            } catch (NumberFormatException e) {
                x = -981;
            }
            if (x <= -980) {
                if (console instanceof FileConsole){
                    throw new IllegalInputException("Некорректное значение поля x!\nЗначение поля должно быть больше -980");
                }
                console.printError("Некорректное значение поля x!\nЗначение поля должно быть больше -980");
            }
        } while (x <= -980);

        Integer y;
        do {
            console.println("Введите значение поля y:");
            try {
                String input = console.readln();
                if (input.equals("exit")) {
                    throw new Breaker();
                } else {
                    y = Integer.valueOf(input);
                }
            } catch (NumberFormatException e) {
                y = 296;
            }
            if (y > 295) {
                if (console instanceof FileConsole){
                    throw new IllegalInputException("Некорректное значение поля y!\nМаксимальное значение поля: 295");
                }
                console.printError("Некорректное значение поля y!\nМаксимальное значение поля: 295");
            }
        } while (y > 295);

        console.println("Значения поля Coordinates записаны...");
        return new Coordinates(x, y);
    }

    /**
     * Запрашивает у пользователя данные для создания объекта Organization.
     * @param console Консоль для ввода/вывода.
     * @return Объект Organization с введенными данными.
     * @throws Breaker Исключение, выбрасываемое при вводе команды "exit".
     */
    private static Organization askOrganization(Console console) throws Breaker {
        console.println("Ввод значений поля Organization...");
        String name;
        do {
            console.println("Введите значение поля name:");
            name = console.readln();
            if (name.equals("exit")) {
                throw new Breaker();
            }
        } while (name.isEmpty());

        String address;
        do {
            console.println("Введите значение поля address:");
            address = console.readln();
            if (address.equals("exit")) {
                throw new Breaker();
            }
        } while (address.isEmpty());

        console.println("Значения поля Organization записаны...");
        return new Organization(name, address);
    }
}
