package commands;

import managers.CollectionManager;
import utility.Console;

public class Clear extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Clear(Console console, CollectionManager collectionManager) {
        super("clear", "очищает коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) {
        collectionManager.clear();
        console.println("Коллекция очищена");
        return true;
    }
}
