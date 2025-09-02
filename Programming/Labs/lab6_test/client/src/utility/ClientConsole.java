package utility;

import utility.Console;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClientConsole implements Console {
    private static Scanner scanner = new Scanner(System.in);
    private final DatagramSocket socket;
    private final InetAddress serverAddress;
    private final int serverPort;
    private static final int BUFFER_SIZE = 65507;
    private static final int MAX_ATTEMPTS = 3;
    private static final int TIMEOUT_MS = 2000;

    public ClientConsole(DatagramSocket socket, String host, int port) throws UnknownHostException {
        this.socket = socket;
        this.serverAddress = InetAddress.getByName(host);
        this.serverPort = port;
    }

    @Override
    public void print(Object obj) {
        System.out.print(obj);
    }

    @Override
    public void println(Object obj) {
        System.out.println(obj);
    }

    @Override
    public String readln() {
        return scanner.nextLine();
    }

    @Override
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    @Override
    public void printErr(Object obj) {
        System.err.print(obj);
    }

    @Override
    public void printTable(Object obj1, Object obj2) {
        System.out.printf("%-30s%-30s%n\n", obj1, obj2);
    }

    @Override
    public void prompt() {
        System.out.print("> ");
    }

    @Override
    public String getPrompt() {
        return "> ";
    }

    @Override
    public void selectFileScanner(Scanner scanner) {
        ClientConsole.scanner = scanner;
    }

    @Override
    public void selectConsoleScanner() {
        ClientConsole.scanner = new Scanner(System.in);
    }

    public InetAddress getServerAddress() {
        return this.serverAddress;
    }

    public void receiveServerOutput() {
        try {
            byte[] buffer = new byte[65507];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            String message = new String(packet.getData(), 0, packet.getLength(), "UTF-8");
            System.out.print(message);
        } catch (Exception e) {
            System.err.println("Ошибка получения данных от сервера: " + e.getMessage());
        }
    }

    public String receiveMultiPacket() throws IOException {
        StringBuilder response = new StringBuilder();
        byte[] buffer = new byte[BUFFER_SIZE];
        int attempts = 0;

        while (attempts < MAX_ATTEMPTS) {
            try {
                socket.setSoTimeout(TIMEOUT_MS);
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String chunk = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);

                // Проверяем маркер конца сообщения
                if (chunk.endsWith("<END>")) {
                    response.append(chunk.substring(0, chunk.length() - 5));
                    break;
                } else {
                    response.append(chunk);
                }

            } catch (SocketTimeoutException e) {
                attempts++;
                if (attempts == MAX_ATTEMPTS) {
                    throw new IOException("Превышено время ожидания ответа");
                }
            }
        }

        return response.toString();
    }
}