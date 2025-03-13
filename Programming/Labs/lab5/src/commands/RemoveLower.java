package commands;

import utility.Console;
import managers.CollectionManager;
import models.Ask;
import models.Worker;


/**
 * удалить из коллекции все элементы, зарплаты которых меньше, чем у заданного
 */
public class RemoveLower extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    
    public RemoveLower(Console console, CollectionManager collectionManager) {
        super("remove_lower", "удаляет из коллекции все элементы, меньшие, чем заданный");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) {
        if (args[1].isEmpty()) {
            console.println("Укажите элемент");
            return false;
        }
        try {
            Worker w = Ask.askWorker(console);
            for (Worker worker : collectionManager.getCollection()) {
                if (worker.getSalary() < w.getSalary()) {
                    collectionManager.remove(worker.getId());
                }
            }
            console.println("Работники удалены");
        } catch (Ask.AskBreakException e) {
            console.println("Некорректный работник");
        }
        return true;
    }
}
