package commands;

import utility.Console;

import java.util.Comparator;

import managers.CollectionManager;
import models.Worker;

public class PrintDescending extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public PrintDescending(Console console, CollectionManager collectionManager) {
        super("print_descending", "вывести элементы коллекции в порядке убывания");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) {
        if (!args[1].isEmpty()) {
            console.printErr("Неправильное число аргументов");
            console.println("Использование print_descending");
            return false;
        }
        
        collectionManager.getCollection().stream()
                .sorted(Comparator.comparingLong(Worker::getSalary).reversed())
                .forEach(worker -> console.println(worker));
        return true;
    }
}
