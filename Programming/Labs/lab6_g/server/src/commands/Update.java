package commands;

import utility.Console;
import managers.CollectionManager;
import models.Ask;
import models.Worker;

public class Update extends Command {
    private final CollectionManager collectionManager;
    private final Console console;

    public Update(Console console, CollectionManager collectionManager) {
        super("update", "обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = collectionManager;
        this.console = console;
    }

    @Override
    public boolean apply(String[] args) {
        try {
            if (args.length == 1) {
                console.printErr("Неправильное число аргументов");
                console.println("Использование update id");
                return false;
            }

            int id = -1;
            try {
                id = Integer.parseInt(args[1]);
            }
            catch (NumberFormatException e) {
                console.printErr("Неправильный формат аргумента");
                console.println("Использование update id");
                return false;
            }

            if (collectionManager.getById(id) == null) {
                console.printErr("Элемента с таким id нет в коллекции");
                return false;
            }

            Worker w = Ask.askWorker(console);
            if (w != null && w.validate()) {
                collectionManager.update(w);
                return true;
            }
            else {
                console.printErr("Элемент не прошел валидацию");
                return false;
            }
        }
        catch (Ask.AskBreakException exception) {
            console.printErr("Cancel...");
            return false;
        }
    }
}
