import commands.*;
import managers.CollectionManager;
import managers.CommandManager;
import managers.DumpManager;
import utility.Runner;
import utility.StandardConsole;

import java.io.*;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws IOException {

        

        StandardConsole console = new StandardConsole();

        String filePath = System.getenv().get("VAR");


        DumpManager dumpManager = new DumpManager(filePath, console);
        CollectionManager collectionManager = new CollectionManager(dumpManager);

        if (!collectionManager.init()) {
            System.exit(1);
        }

        
        CommandManager commandManager = new CommandManager() {{
            registerCommand("help", new Help(console, this));
            registerCommand("info", new Info(console, collectionManager));
            registerCommand("show", new Show(console, collectionManager));
            registerCommand("add", new Add(console, collectionManager));
            registerCommand("update", new Update(console, collectionManager));
            registerCommand("remove_by_id", new RemoveById(console, collectionManager));
            registerCommand("clear", new Clear(console, collectionManager));
            registerCommand("save", new Save(console, collectionManager));
            registerCommand("execute_script", new ExecuteScript(console));
            registerCommand("exit", new Exit(console));
            registerCommand("remove_greater", new RemoveGreater(console, collectionManager));
            registerCommand("remove_lower", new RemoveLower(console, collectionManager));
            registerCommand("history", new History(console, this));
            registerCommand("max_by_position", new MaxByPosition(console, collectionManager));
            registerCommand("filter_by_end_date", new FilterByEndDate(console, collectionManager));
            registerCommand("print_descending", new PrintDescending(console, collectionManager));
        }};

        new Runner(console, commandManager).interactiveMode();
    }
}
