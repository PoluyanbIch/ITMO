package utility;

import managers.CommandManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import commands.Command;

import java.io.File;

public class Runner {
    
    public enum ExitCode {
        SUCCESS,
        ERROR,
        EXIT
    }

    private Console console;
    private final CommandManager commandManager;
    private final List<String> scriptStack = new ArrayList<>();
    private int lengthRecursion = -1;

    public Runner(Console console, CommandManager commandManager) {
        this.console = console;
        this.commandManager = commandManager;
    }

    public void interactiveMode() {
        try {
            ExitCode exitCode;
            String[] userCommand;

            do {
                console.prompt();
                userCommand = (console.readln().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();

                commandManager.addToHistory(userCommand[0]);
                exitCode = launchCommand(userCommand);
            } while (exitCode != ExitCode.EXIT);
        }
        catch (Exception e) {
            console.printErr("Произошла ошибка");
            console.println(e.getMessage());
        }
    }

    public ExitCode scriptMode(String argument) {
        String[] userCommand = {"", ""};
        ExitCode commandStatus;
        scriptStack.add(argument);
        
        try {
            Scanner scriptScanner = new Scanner(new File(argument));
            if (!scriptScanner.hasNext()) throw new Exception("Файл пуст");
            console.selectFileScanner(scriptScanner);

            do {
                userCommand = (console.readln().trim() + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                while (console.hasNextLine() && userCommand[0].isEmpty()) {
                    userCommand = (console.readln().trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                }
                console.println(console.getPrompt() + String.join(" ", userCommand));
                boolean isNeedLaunch = true;
                if (userCommand[0].equals("exexute_script")) {
                    int recursionStart = -1;
                    int i = 0;
                    for (String script : scriptStack) {
                        i++;
                        if (userCommand[1].equals(script)) {
                            if (recursionStart < 0) recursionStart = i;
                            if (lengthRecursion < 0) {
                                console.selectConsoleScanner();
                                console.println("Рекурсия, введите макс. длину (<500)");
                                while (lengthRecursion < 0 || lengthRecursion > 500) {
                                    try {
                                        console.prompt();
                                        lengthRecursion = Integer.parseInt(console.readln().trim());
                                    }
                                    catch (NumberFormatException exception) {
                                        console.printErr("Неверный формат входных данных");
                                    }
                                }
                                console.selectFileScanner(scriptScanner);
                            }
                            if (i > recursionStart + lengthRecursion || i > 500) {
                                console.println("Превышена максимальная глубина рекурсии");
                                isNeedLaunch = false;
                                break;
                            }
                        }
                    }
                }
                if (isNeedLaunch) {
                    commandStatus = launchCommand(userCommand);
                }
                else {
                    commandStatus = ExitCode.ERROR;
                }
            } while (commandStatus == ExitCode.SUCCESS && console.hasNextLine());
            console.selectConsoleScanner();
            if (commandStatus == ExitCode.ERROR) {
                console.println("Ошибка во время выполнения скрипта " + argument);
            }
            else {
                console.println("Скрипт " + argument + " выполнен");
            }
            return commandStatus;
        } catch (Exception e) {
            console.println("Ошибка во время выполнения скрипта " + argument);
            return ExitCode.ERROR;
        } finally {
            scriptStack.remove(scriptStack.size() - 1);
        }
    }

    private ExitCode launchCommand(String[] userCommand) {
        if (userCommand[0].equals("")) {
            return ExitCode.SUCCESS;
        }
        Command command = commandManager.getCommands().get(userCommand[0]);
        if (command == null) {
            console.printErr("Команда не найдена. Введите 'help' для справки.");
            return ExitCode.ERROR;
        }

        switch (userCommand[0]) {
            case "exit":
                return ExitCode.EXIT;
            case "execute_script":
                if (userCommand.length == 1) {
                    console.printErr("Отсутствует аргумент");
                    return ExitCode.ERROR;
                }
                return scriptMode(userCommand[1]);
            default:
                if (!command.apply(userCommand)) return ExitCode.ERROR;
        }
        return ExitCode.SUCCESS;
    }
}
