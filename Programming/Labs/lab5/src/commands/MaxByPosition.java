package commands;

import utility.Console;
import managers.CollectionManager;
import models.Worker;

public class MaxByPosition extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public MaxByPosition(Console console, CollectionManager collectionManager) {
        super("max_by_position", "вывести любой объект из коллекции, значение поля position которого является максимальным");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) {
        if (!args[1].isEmpty()) {
            console.printErr("Неправильное число аргументов");
            console.println("Использование max_by_position");
            return false;
        }
        Worker mymax = null;
        for (Worker w : collectionManager.getCollection()) {
            if (mymax == null || w.getPosition().compareTo(mymax.getPosition()) > 0) {
                mymax = w;
            }
        }
        if (mymax == null) {
            console.println("Коллекция пуста");
        } else {
            console.println(mymax);
        }
        return true;
    }
    
}
