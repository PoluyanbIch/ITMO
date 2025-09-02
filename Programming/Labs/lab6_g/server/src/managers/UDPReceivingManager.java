package managers;

import java.io.IOException;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

public class UDPReceivingManager {
    public byte[] receive(DatagramChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(65507);
        SocketAddress clientAddress = channel.receive(buffer);

        if (clientAddress != null) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            return data;
        }
        return null;
    }
}