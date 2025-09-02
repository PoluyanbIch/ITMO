package commands;

import managers.CommandManager;
import utility.Console;

public class History extends Command {
    private final Console console;
    private final CommandManager commandManager;

    public History(Console console, CommandManager commandManager) {
        super("history", "выводит последние 13 команд (без их аргументов)");
        this.console = console;
        this.commandManager = commandManager;
    }

    @Override
    public boolean apply(String[] args) {
        if (!args[1].isEmpty()) {
            console.println("Команда не принимает аргументы");
            return false;
        }

        console.println("Последние 13 команд:");
        commandManager.getCommandHistory().forEach(command -> console.println(" " + command));
        return true;
    }
}
