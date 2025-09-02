package commands;

import managers.CollectionManager;
import utility.Console;

public class Save extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Save(Console console, CollectionManager collectionManager) {
        super("save", "сохраняет коллекцию в файл");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) {
        if (!args[1].isEmpty()) {
            console.println("Команда не принимает аргументы");
            return false;
        }

        collectionManager.saveCollection();
        console.println("Коллекция сохранена");
        return true;
    }
}
