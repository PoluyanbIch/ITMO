
import commands.*;
import managers.CommandManager;
import utility.ClientConsole;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Main {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 12345;
    private static final int TIMEOUT_MS = 5000;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(TIMEOUT_MS);
            InetAddress serverAddress = InetAddress.getByName(SERVER_HOST);
            Scanner scanner = new Scanner(System.in);

            System.out.println("UDP Client started. Type 'exit' to quit.");

            ClientConsole console = new ClientConsole(socket, SERVER_HOST, SERVER_PORT);


            while (true) {
                System.out.print("> ");
                String command = scanner.nextLine();
                if (command.equals("add")) {

                }

                if ("exit".equalsIgnoreCase(command)) {
                    break;
                }

                try {
                    // Отправка команды
                    byte[] sendData = command.getBytes("UTF-8");
                    DatagramPacket sendPacket = new DatagramPacket(
                            sendData,
                            sendData.length,
                            serverAddress,
                            SERVER_PORT
                    );
                    socket.send(sendPacket);

                    // Получение ответа
                    byte[] receiveData = new byte[65507];
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    socket.receive(receivePacket);

                    String response = new String(
                            receivePacket.getData(),
                            0,
                            receivePacket.getLength(),
                            "UTF-8"
                    );

                    // Обработка ответа
                    if (response.startsWith("DATA: ")) {
                        System.out.println(response.substring(6));
                    } else if (response.startsWith("ERROR: ")) {
                        System.err.println(response.substring(7));
                    } else {
                        System.out.println(response);
                    }

                } catch (SocketTimeoutException e) {
                    System.err.println("Error: Server response timeout");
                } catch (IOException e) {
                    System.err.println("Network error: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Client initialization failed: " + e.getMessage());
        }
    }
}