import managers.UDPServer;
import managers.UDPClient;
import managers.DumpManager;
import java.util.ArrayList;
import utility.StandardConsole;
import utility.Response;

public class Main {
    private static int PORT;

    private static final StandardConsole console = new StandardConsole();
    private static ArrayList<Integer> serversPorts = new ArrayList<>();
    private static ArrayList<String> serversAddresses = new ArrayList<>();
    private static int selectedServer = 0;

    public static void main(String[] args) {
        initProperty();

        var udpServer = new UDPServer(PORT, (String command, Object data) -> {
            console.println("Получен запрос");
            Object response = null;
            int attempts = 0;

            while (attempts < serversAddresses.size() * 2) {
                String address = serversAddresses.get(selectedServer);
                int port = serversPorts.get(selectedServer);

                console.println("Попытка подключения к серверу");

                try {
                    UDPClient udpClient = new UDPClient(address, port);
                    console.println("Отправка запроса...");
                    response = udpClient.send(command, data);

                    if (response != null) {
                        console.println("Успешный ответ от сервера");
                        return response;
                    }
                } catch (Exception e) {
                    console.printErr("Ошибка подключения");
                    console.println(e.getMessage());
                }

                console.printErr("Не удалось получить ответ, пробуем следующий сервер");
                selectedServer = (selectedServer + 1) % serversPorts.size();
                attempts++;

                if (selectedServer == 0) {
                    console.println("Обновляем список серверов");
                    initProperty();
                }
            }

            console.printErr("Все серверы недоступны");
            return new Response(503, "Все серверы недоступны");
        });

        udpServer.start();
    }

    public static void initProperty() {
        var console = new StandardConsole();
        serversPorts.clear();
        serversAddresses.clear();

        var dumpManager = new DumpManager("repeater.ini", console);

        PORT = dumpManager.getProperty("PORT", 1235);
        if (PORT < 0) {
            console.println("Property PORT undefined");
            System.exit(1);
        }

        var i = 1;
        while (true) {
        var serverPort = dumpManager.getProperty("SERVER_PORT_" + i, 1234);
            var serverAddress = dumpManager.getProperty("SERVER_ADDRESS_" + i, "127.0.0.1");

            if (serverPort < 0 && i == 1) {
                console.println("Property SERVER_PORT_1 undefined");
                System.exit(1);
            } else if (serverPort < 0) {
                break;
            } else {
                serversPorts.add(serverPort);
                serversAddresses.add(serverAddress);
            }
            i++;
        }

        if (serversPorts.isEmpty()) {
            console.println("Не найдено ни одного сервера в конфигурации");
            System.exit(1);
        }
    }
}