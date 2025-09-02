package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.nio.file.*;

import managers.CommandManager;
import utility.Console;

public class Runner {
    private Console console;
    private final CommandManager commandManager;
    private final List<String> scriptStack = new ArrayList<>();
    private int lengthRecursion = -1;

    public Runner(Console console, CommandManager commandManager) {
        this.console = console;
        this.commandManager = commandManager;
    }

    /**
     * Запускает команду
     * @param userCommand Массив команды и аргументов
     * @param obj Дополнительный объект для команды
     * @return Ответ выполнения команды
     */
    private Response launchCommand(String[] userCommand, Object obj) {
        if (userCommand[0].isEmpty()) {
            return new Response("OK");
        }

        var command = commandManager.getCommands().get(userCommand[0]);

        if (command == null || userCommand[0].equals("save") || userCommand[0].equals("load")) {
            return new Response(400, "Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");
        }

        try {
            var resp = command.apply(userCommand);
            if (!resp) {
                return new Response(503, "Ошибка выполнения команды");
            }
            else {
                return new Response(200, "Команда выпонена успешно");
            }
        } catch (Exception e) {
            console.printErr("Ошибка при выполнении команды: " + e.getMessage());
            return new Response(500, "Внутренняя ошибка сервера");
        }
    }

    /**
     * Выполняет команду, полученную от клиента
     * @param s Строка команды
     * @param obj Дополнительный объект
     * @return Результат выполнения команды
     */
    public Object executeCommand(String s, Object obj) {
        try {
            // Автоматическая загрузка коллекции перед выполнением команды
            commandManager.getCommands().get("load").apply(new String[]{"load", ""});

            String[] userCommand = {"", ""};
            // Нормализация строки команды
            userCommand = (s.replace('\n',' ').replace('\r',' ') + " ").split(" ", 2);
            userCommand[1] = userCommand[1].trim();

            console.println("Выполнение команды: " + userCommand[0]);

            // Добавление в историю
            if (!userCommand[0].isEmpty()) {
                commandManager.addToHistory(userCommand[0]);
            }

            return launchCommand(userCommand, obj);
        } catch (Exception e) {
            console.printErr("Ошибка обработки команды: " + e.getMessage());
            return new Response(500, "Ошибка обработки команды");
        } finally {
            // Автоматическое сохранение коллекции после выполнения команды
            commandManager.getCommands().get("save").apply(new String[]{"save", ""});
        }
    }
}