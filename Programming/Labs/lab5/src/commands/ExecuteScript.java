package commands;

import utility.Console;

public class ExecuteScript extends Command {
    private final Console console;

    public ExecuteScript(Console console) {
        super("execute_script", "считывает и исполняет скрипт из указанного файла");
        this.console = console;
    }

    @Override
    public boolean apply(String[] args) {
        if (args[1].isEmpty()) {
            console.println("Укажите файл");
            return false;
        }
        console.println("выполнение скрипта " + args[1]);
        return true;
    }
}
