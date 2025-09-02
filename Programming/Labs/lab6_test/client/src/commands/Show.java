package commands;

import utility.Console;
import managers.CollectionManager;


public class Show extends Command{
    private final CollectionManager collectionManager;
    private final Console console;

    public Show(Console console, CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public boolean apply(String[] args) {
        if (!args[1].isEmpty()) {
            console.printErr("Неправильное число аргументов");
            return false;
        }
        if (collectionManager.getCollection().isEmpty()) {
            console.println("Коллекция пустая");
        }
        else {
            console.println(collectionManager.toString());
        }

        return true;
    }
}
