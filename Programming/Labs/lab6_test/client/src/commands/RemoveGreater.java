package commands;

import utility.Console;
import managers.CollectionManager;
import models.Ask;
import models.Worker;


/**
 * удалить из коллекции все элементы, зарплаты которых больше, чем у заданного
 */
public class RemoveGreater extends Command {
    private final Console console;
    private final CollectionManager collectionManager;
    
    public RemoveGreater(Console console, CollectionManager collectionManager) {
        super("remove_greater", "удаляет из коллекции все элементы, превышающие заданный");
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
                if (worker.getSalary() > w.getSalary()) {
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
