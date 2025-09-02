import utility.Response;
import managers.UDPClient;
import managers.UDPManager;
import managers.DumpManager;
import managers.CommandManager;
import java.util.Scanner;
import java.util.ArrayList;

import models.Worker;
import commands.*;
import utility.StandardConsole;
import utility.Runner;

public class Main {
    private static int PORT;
    private static String SERVER_ADDRESS;

    public static void main(String[] args) {
        var console = new StandardConsole();
        var dumpManager = new DumpManager("client.ini", console);

        PORT = dumpManager.getProperty("PORT", 1234);
        if (PORT < 0) { console.println("Property PORT undefined"); System.exit(1); }

        SERVER_ADDRESS = dumpManager.getProperty("SERVER_ADDRESS", "127.0.0.1");
        var udpClient = new UDPClient(SERVER_ADDRESS, PORT);
        var udpManager = new UDPManager(udpClient);

        var commandManager = new CommandManager() {{
            registerCommand("help", new Help(console, this));
            registerCommand("execute_script", new ExecuteScript(console));
            registerCommand("exit", new Exit(console));
            registerCommand("show", new Show(console, udpManager));
        }};

        new Runner(console, commandManager, udpManager).interactiveMode();
    }
}