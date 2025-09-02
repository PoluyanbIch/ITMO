package managers;

import utility.ClientConsole;

import java.io.IOException;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

public class UDPSendingManager {
    private final ClientConsole console = new ClientConsole();

    public void send(DatagramChannel channel, SocketAddress clientAddress, byte[] data) throws IOException {
        try {
            // Разбиваем данные на пакеты, если они превышают MTU
            int maxPacketSize = 1400; // Учитываем MTU
            int offset = 0;

            while (offset < data.length) {
                int chunkSize = Math.min(maxPacketSize, data.length - offset);
                byte[] chunk = new byte[chunkSize];
                System.arraycopy(data, offset, chunk, 0, chunkSize);

                ByteBuffer buffer = ByteBuffer.wrap(chunk);
                channel.send(buffer, clientAddress);

                offset += chunkSize;
            }

            console.println("Data sent to " + clientAddress);
        } catch (IOException e) {
            console.printErr("Failed to send data: " + e.getMessage());
            throw e;
        }
    }
}