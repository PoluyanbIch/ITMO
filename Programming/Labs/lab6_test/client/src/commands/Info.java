package commands;

import utility.Console;
import managers.CollectionManager;


public class Info extends Command{
    private final Console console;
    private final CollectionManager collectionManager;

    public Info(Console console, CollectionManager collectionManager) {
        super("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) {
        if (!args[1].isEmpty()) {
			console.println("Неправильное количество аргументов");
			console.println("Использование: info");
			return false;
		}
        console.println("Сведения о коллекции:");
        console.println("Тип: " + collectionManager.getCollection().getClass().getName());
        console.println("Количество элементов: " + collectionManager.getCollection().size());
        console.println("Дата последнего сохранения: " + collectionManager.getLastSaveTime());
        console.println("Дата последнего изменения: " + collectionManager.getLastInitTime());
        return true;
    }
}
