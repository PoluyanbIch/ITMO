package commands;

import managers.UDPManager;  // Изменено с TCPManager на UDPManager
import utility.Console;
import java.util.stream.Collectors;
import java.util.LinkedList;
import models.Worker;

/**
 * Команда 'show'. Выводит все элементы коллекции.
 * @author dim0n4eg
 */
public class Show extends Command {
    private final Console console;
    private final UDPManager udpManager;  // Изменено с TCPManager на UDPManager

    public Show(Console console, UDPManager udpManager) {  // Изменен тип параметра
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.console = console;
        this.udpManager = udpManager;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public boolean apply(String[] arguments) {
        if (!arguments[1].isEmpty()) {
            console.println("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return false;
        }

        // Получаем ответ от сервера через UDPManager
        var response = udpManager.send("show");
        if (response == null) {
            console.println("Ошибка при получении данных от сервера");
            return false;
        }

        // Обрабатываем ответ
        var dragons = (LinkedList<Worker>) response.getResponseObj();
        var result = dragons.stream()
                .map(Worker::toString)
                .collect(Collectors.joining("\n"));

        if (result.isEmpty()) {
            console.println("Коллекция пуста!");
        } else {
            console.println(result);
        }
        return true;
    }
}