package commands;

import utility.Console;

public class Exit extends Command {
    private final Console console;

    public Exit(Console console) {
        super("exit", "завершает программу (без сохранения в файл)");
        this.console = console;
    }

    @Override
    public boolean apply(String[] args) {
        if (!args[1].isEmpty()) {
            console.println("Команда не принимает аргументы");
            return false;
        }
        console.println("Завершение программы");
        System.exit(0);
        return true;
    }
}
