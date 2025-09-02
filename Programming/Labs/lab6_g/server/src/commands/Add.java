package commands;

import utility.Console;
import managers.CollectionManager;
import models.Ask;
import models.Worker;

public class Add extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public Add(Console console, CollectionManager collectionManager) {
        super("add", "добавить новый элемент в коллекцию");
        this.console = console;
        this.collectionManager = collectionManager;
    }
    
    @Override
    public boolean apply(String[] args) {
        try {
            if (!args[1].isEmpty())  {
                console.printErr("У команды add нет аргументов");
                console.print("Использование: add");
                return false;
            }
            console.println("Adding worker");
            Worker w = Ask.askWorker(console);
            if (w != null && w.validate()) {
                collectionManager.add(w);
                console.println("Success");
                return true;
            }
            else {
                console.printErr("Not validatable worker");
                return false;
            }
        }
        catch (Ask.AskBreakException exception) {
            console.print("Cancel...");
            return false;
        }
    }
}
