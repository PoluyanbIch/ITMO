package utility;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class StandardConsole implements Console {
    private final DatagramSocket socket;
    private final InetAddress clientAddress;
    private static final int TIMEOUT_MS = 5000;
    private final int clientPort;
    private static final String PROMPT = "> ";
    private static final int MAX_PACKET_SIZE = 65507 - 100; // Запас для заголовков
    private static final String END_MARKER = "<END>";

    public StandardConsole(DatagramSocket socket, InetAddress clientAddress, int clientPort) {
        this.socket = socket;
        this.clientAddress = clientAddress;
        this.clientPort = clientPort;
    }

    @Override
    public void print(Object obj) {
        sendToClient(obj.toString());
    }

    @Override
    public void println(Object obj) {
        sendToClient(obj.toString() + "\n");
    }

    @Override
    public String readln() {
        try {
            // Ожидание ответа
            byte[] buffer = new byte[65507];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.setSoTimeout(TIMEOUT_MS);
            socket.receive(packet);

            return new String(
                    packet.getData(),
                    0,
                    packet.getLength(),
                    StandardCharsets.UTF_8
            );
        } catch (SocketTimeoutException e) {
            return "Ошибка: время ожидания ответа истекло";
        } catch (IOException e) {
            return "Ошибка получения ответа: " + e.getMessage();
        }
    }

    @Override
    public boolean hasNextLine() {
        return false;
    }

    @Override
    public void printErr(Object obj) {
        sendToClient("ERROR: " + obj.toString());
    }

    @Override
    public void printTable(Object obj1, Object obj2) {
        sendToClient(String.format("%-30s%-30s", obj1, obj2));
    }

    @Override
    public void prompt() {
        // На сервере не выводим приглашение
    }

    @Override
    public String getPrompt() {
        return PROMPT;
    }

    @Override
    public void selectFileScanner(Scanner scanner) {
        // Не используется на сервере
    }

    @Override
    public void selectConsoleScanner() {
        // Не используется на сервере
    }

    private void sendToClient(String message) {
        try {
            byte[] sendData = message.getBytes("UTF-8");
            DatagramPacket packet = new DatagramPacket(
                    sendData,
                    sendData.length,
                    clientAddress,
                    clientPort
            );
            socket.send(packet);
        } catch (Exception e) {
            System.err.println("Ошибка отправки сообщения клиенту: " + e.getMessage());
        }
    }
}