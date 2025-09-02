package managers;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class ClientReceivingManager {
    private final int MAX_PACKET_SIZE = 65507; // Максимальный размер UDP пакета
    private final UDPClient udpClient;

    public ClientReceivingManager(UDPClient udpClient) {
        this.udpClient = udpClient;
    }

    public byte[] receive() {
        try {
            DatagramSocket socket = udpClient.getSocket();
            byte[] buffer = new byte[MAX_PACKET_SIZE];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            socket.receive(packet);

            // Проверяем последний байт на признак конца передачи
            int length = packet.getLength();
            if (buffer[length - 1] == 1) {
                return Arrays.copyOf(buffer, length - 1);
            }

            return Arrays.copyOf(buffer, length);
        } catch (Exception e) {
            System.out.println("Ошибка при получении данных: " + e.getMessage());
            return null;
        }
    }
}