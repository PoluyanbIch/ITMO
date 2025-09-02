import managers.UDPServer;

import commands.*;
import managers.CollectionManager;
import managers.CommandManager;
import managers.DumpManager;
import utility.StandardConsole;
import utility.Runner;

public class Main {
    private static int PORT;

    public static void main(String[] args) {
        var console = new StandardConsole();
        var dumpManager = new DumpManager("test.csv", console);
        var collectionManager = new CollectionManager(dumpManager);

        PORT = dumpManager.getProperty("PORT", 1234);
        if (PORT < 0) {
            System.out.println("Property PORT undefined");
            System.exit(1);
        }

        var commandManager = new CommandManager() {{
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

        var runner = new Runner(console, commandManager);

        // Создаем и запускаем UDP сервер вместо TCP
        var udpServer = new UDPServer(PORT, runner::executeCommand);
        udpServer.start();

    }
}