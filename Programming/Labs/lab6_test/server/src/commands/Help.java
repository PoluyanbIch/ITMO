package commands;

import utility.Console;
import managers.CommandManager;

public class Help extends Command{
    private final Console console;
    private final CommandManager commandManager;

    public Help(Console console, CommandManager commandManager) {
        super("help", "вывести справку по доступным командам");
        this.console = console;
        this.commandManager = commandManager;
    }

    @Override
    public boolean apply(String[] args) {
        if (!args[1].isEmpty()) {
            console.printErr("Неправильное число аргументов");
            console.println("Использование help");
            return false;
        }
        String res = "";
        for (var command : commandManager.getCommands().values()) {
            res = res + String.format("%-30s%-30s\n", command.getName(), command.getDescription());
        }
        console.println(res);
        return true;
    }
}
