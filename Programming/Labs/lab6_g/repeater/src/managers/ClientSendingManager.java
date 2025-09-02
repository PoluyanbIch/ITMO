package managers;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class ClientSendingManager {
    private final int MAX_PACKET_SIZE = 65507; // Максимальный размер UDP пакета
    private final UDPClient udpClient;

    public ClientSendingManager(UDPClient udpClient) {
        this.udpClient = udpClient;
    }

    public void send(byte[] data) {
        try {
            DatagramSocket socket = udpClient.getSocket();
            InetSocketAddress address = udpClient.getServerAddress();

            // Разбиваем данные на пакеты, если они слишком большие
            int offset = 0;
            while (offset < data.length) {
                int chunkSize = Math.min(MAX_PACKET_SIZE - 1, data.length - offset);
                byte[] chunk = new byte[chunkSize + 1];

                System.arraycopy(data, offset, chunk, 0, chunkSize);

                // Последний байт - флаг окончания (1 - последний пакет, 0 - нет)
                chunk[chunkSize] = (offset + chunkSize >= data.length) ? (byte)1 : (byte)0;

                DatagramPacket packet = new DatagramPacket(
                        chunk, chunk.length, address
                );

                socket.send(packet);
                offset += chunkSize;
            }
        } catch (Exception e) {
            System.out.println("Ошибка при отправке данных: " + e.getMessage());
        }
    }
}