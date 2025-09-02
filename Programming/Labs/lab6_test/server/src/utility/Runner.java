package utility;

import java.io.IOException;
import java.net.*;
import java.util.*;

public class Runner {
    private final DatagramSocket socket;
    private final InetAddress serverAddress;
    private final int serverPort;

    public Runner(String host, int port) throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket();
        this.serverAddress = InetAddress.getByName(host);
        this.serverPort = port;
        socket.setSoTimeout(3000);
    }

    public String executeCommand(String command) throws IOException {
        // Отправка команды
        byte[] sendData = command.getBytes();
        socket.send(new DatagramPacket(
                sendData,
                sendData.length,
                serverAddress,
                serverPort
        ));

        // Получение ответа
        byte[] receiveData = new byte[65507];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        return new String(receivePacket.getData(), 0, receivePacket.getLength());
    }

    public void close() {
        socket.close();
    }
}