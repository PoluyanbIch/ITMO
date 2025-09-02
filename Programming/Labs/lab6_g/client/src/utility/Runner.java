package utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.nio.file.*;

import managers.CommandManager;
import managers.UDPManager;  // Изменено с TCPManager на UDPManager
import utility.Console;
import commands.DefaultCommand;

public class Runner {

    public enum ExitCode {
        OK,
        ERROR,
        EXIT
    }

    private StandardConsole console;
    private final CommandManager commandManager;
    private final UDPManager udpManager;  // Изменено с TCPManager на UDPManager
    private final List<String> scriptStack = new ArrayList<>();
    private int lengthRecursion = -1;

    public Runner(StandardConsole console, CommandManager commandManager, UDPManager udpManager) {  // Изменен тип параметра
        this.console = console;
        this.commandManager = commandManager;
        this.udpManager = udpManager;
    }

    /**
     * Интерактивный режим
     */
    public void interactiveMode() {
        try {
            ExitCode commandStatus;
            String[] userCommand = {"", ""};

            do {
                console.prompt();
                userCommand = (console.readln().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();

                commandManager.addToHistory(userCommand[0]);
                commandStatus = launchCommand(userCommand);
            } while (commandStatus != ExitCode.EXIT);

        } catch (NoSuchElementException exception) {
            console.printErr("Пользовательский ввод не обнаружен!");
        } catch (IllegalStateException exception) {
            console.printErr("Непредвиденная ошибка!");
        }
    }

    /**
     * Режим для запуска скрипта.
     * @param argument Аргумент скрипта
     * @return Код завершения.
     */
    public ExitCode scriptMode(String argument) {
        String[] userCommand = {"", ""};
        ExitCode commandStatus;
        scriptStack.add(argument);

        if (!new File(argument).exists()) {
            console.printErr("Файл не существует!");
            return ExitCode.ERROR;
        }
        if (!Files.isReadable(Paths.get(argument))) {
            console.printErr("Нет прав для чтения!");
            return ExitCode.ERROR;
        }

        try (Scanner scriptScanner = new Scanner(new File(argument))) {
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();
            console.selectFileScanner(scriptScanner);

            do {
                userCommand = (console.readln().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();

                while (console.isCanReadln() && userCommand[0].isEmpty()) {
                    userCommand = (console.readln().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                }

                console.println(console.getPrompt() + String.join(" ", userCommand));
                var needLaunch = true;

                if (userCommand[0].equals("execute_script")) {
                    var recStart = -1;
                    var i = 0;
                    for (String script : scriptStack) {
                        i++;
                        if (userCommand[1].equals(script)) {
                            if (recStart < 0) recStart = i;
                            if (lengthRecursion < 0) {
                                console.selectConsoleScanner();
                                console.println("Обнаружена рекурсия! Введите максимальную глубину рекурсии (0..500)");
                                while (lengthRecursion < 0 || lengthRecursion > 500) {
                                    try {
                                        console.print("> ");
                                        lengthRecursion = Integer.parseInt(console.readln().trim());
                                    } catch (NumberFormatException e) {
                                        console.println("Некорректное значение длины");
                                    }
                                }
                                console.selectFileScanner(scriptScanner);
                            }
                            if (i > recStart + lengthRecursion || i > 500)
                                needLaunch = false;
                        }
                    }
                }
                commandStatus = needLaunch ? launchCommand(userCommand) : ExitCode.OK;
            } while (commandStatus == ExitCode.OK && console.isCanReadln());

            console.selectConsoleScanner();
            if (commandStatus == ExitCode.ERROR && !(userCommand[0].equals("execute_script") && !userCommand[1].isEmpty())) {
                console.println("Проверьте скрипт на корректность введенных данных!");
            }

            return commandStatus;
        } catch (FileNotFoundException exception) {
            console.printErr("Файл со скриптом не найден!");
        } catch (NoSuchElementException exception) {
            console.printErr("Файл со скриптом пуст!");
        } catch (IllegalStateException exception) {
            console.printErr("Непредвиденная ошибка!");
            System.exit(0);
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
        return ExitCode.ERROR;
    }

    /**
     * Запускает команду
     * @param userCommand Команда для запуска
     * @return Код завершения
     */
    private ExitCode launchCommand(String[] userCommand) {
        commandManager.clear();

        // Получаем команды через UDP
        var response = udpManager.send("get_commands");
        if (response == null) {
            console.printErr("Не удалось получить команды с сервера");
            return ExitCode.ERROR;
        }

        // Регистрируем команды
        for (var e : (ArrayList<String[]>)udpManager.send("get_commands").getResponseObj()) {
            commandManager.registerCommand("$"+e[0], new DefaultCommand(e, console, udpManager));  // Передаем UDPManager
        }

        if (userCommand[0].equals("")) return ExitCode.OK;

        var command = commandManager.getCommands().get(userCommand[0]);
        if (command == null) command = commandManager.getCommands().get('$'+userCommand[0]);

        if (command == null) {
            console.printErr("Команда '" + userCommand[0] + "' не найдена. Наберите 'help' для справки");
            return ExitCode.ERROR;
        }

        switch (userCommand[0]) {
            case "exit" -> {
                if (!command.apply(userCommand)) return ExitCode.ERROR;
                else return ExitCode.EXIT;
            }
            case "execute_script" -> {
                if (!command.apply(userCommand)) return ExitCode.ERROR;
                else return scriptMode(userCommand[1]);
            }
            default -> {
                if (!command.apply(userCommand)) return ExitCode.ERROR;
            }
        };

        return ExitCode.OK;
    }
}