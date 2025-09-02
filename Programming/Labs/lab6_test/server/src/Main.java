import managers.CollectionManager;
import managers.CommandManager;
import managers.DumpManager;
import utility.Console;
import utility.DumpConsole;
import utility.StandardConsole;
import commands.*;

import java.net.*;
import java.io.*;

public class Main {
    private static final int PORT = 12345;
    private static final int BUFFER_SIZE = 65507; // Максимальный размер UDP-пакета

    public static void main(String[] args) throws SocketException {
        Console dumpconsole = new DumpConsole();
        DumpManager dumpManager = new DumpManager("../data.json", dumpconsole);
        CollectionManager collectionManager = new CollectionManager(dumpManager);

        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("UDP Server started on port " + PORT);
            byte[] buffer = new byte[BUFFER_SIZE];

            while (true) {
                // Получаем запрос
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                socket.receive(request); // Блокирующий вызов

                Console console = new StandardConsole(
                        socket,
                        request.getAddress(),
                        request.getPort()
                );

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

                // Обрабатываем команду
                String command = new String(request.getData(), 0, request.getLength());
                String response = processCommand(command, commandManager);

                // Отправляем ответ
//                byte[] responseData = response.getBytes();
//                DatagramPacket reply = new DatagramPacket(
//                        responseData,
//                        responseData.length,
//                        request.getAddress(),
//                        request.getPort()
//                );
//                socket.send(reply);
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static String processCommand(String command_string, CommandManager commandManager) {
        String[] userCommand = (command_string.trim() + " ").split(" ", 2);
        Command command = commandManager.getCommands().get(userCommand[0]);
        userCommand[1] = userCommand[1].trim();
        command.apply(userCommand);
        return "Processed via UDP: " + command;
    }
}