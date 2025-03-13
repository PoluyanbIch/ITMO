package commands;

import utility.Console;
import managers.CollectionManager;
import models.Worker;

import java.time.ZonedDateTime;
import java.util.stream.Collectors;
import java.util.List;

public class FilterByEndDate extends Command {
    private final Console console;
    private final CollectionManager collectionManager;

    public FilterByEndDate(Console console, CollectionManager collectionManager) {
        super("filter_by_end_date", "вывести элементы, значение поля endDate которых равно заданного");
        this.console = console;
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean apply(String[] args) {
        try {
            if (args[1].isEmpty()) {
                console.println("Укажите дату");
                return false;
            }

            ZonedDateTime time = ZonedDateTime.parse(args[1]);
            List<Worker> workers = collectionManager.getCollection().stream()
                    .filter(worker -> worker.getEndDate().equals(time))
                    .collect(Collectors.toList());
            if (workers.isEmpty()) {
                console.println("Работников с такой датой окончания работы нет");
            } else {
                workers.forEach(console::println);
            }
            return true;
        }
        catch (Exception e) {
            console.println("Некорректная дата");
            return false;
        }
    }
}
