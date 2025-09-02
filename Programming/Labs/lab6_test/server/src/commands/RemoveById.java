package commands;

import managers.CollectionManager;
import utility.Console;
import models.Worker;

public class RemoveById extends Command{
    private final Console console;
    private final CollectionManager collectionManager;

    public RemoveById(Console console, CollectionManager collectionManager) {
        super("remove_by_id", "удаляет элемент по id");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) {
        if (args[1].isEmpty()) {
            console.println("Укажите id");
            return false;
        }
        try {
            int id = Integer.parseInt(args[1]);
            Worker worker = collectionManager.getById(id);
            if (worker == null) {
                console.println("Работника с таким id нет");
                return false;
            }
            collectionManager.remove(id);
            console.println("Работник удален");
        } catch (NumberFormatException e) {
            console.println("Некорректный id");
        }
        return true;
    }
}
