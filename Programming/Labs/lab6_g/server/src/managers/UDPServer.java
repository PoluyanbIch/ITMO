package managers;

import utility.ClientConsole;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class UDPServer {

    public interface UDPExecute {
        Object execute(String command, Object data);
    }
    private final ClientConsole console = new ClientConsole();
    private final int port;
    private final UDPExecute executor;
    private DatagramChannel channel;
    private Selector selector;

    public UDPServer(int port, UDPExecute executor) {
        this.port = port;
        this.executor = executor;
    }

    public void start() {
        try {
            selector = Selector.open();
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(port));
            channel.register(selector, SelectionKey.OP_READ);

            ByteBuffer buffer = ByteBuffer.allocate(65507); // Максимальный размер UDP пакета

            while (true) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (!key.isValid()) continue;

                    if (key.isReadable()) {
                        buffer.clear();
                        SocketAddress clientAddress = channel.receive(buffer);

                        if (clientAddress != null) {
                            buffer.flip();
                            byte[] receivedData = new byte[buffer.remaining()];
                            buffer.get(receivedData);

                            try (ByteArrayInputStream bais = new ByteArrayInputStream(receivedData);
                                 ObjectInputStream ois = new ObjectInputStream(bais)) {

                                String command = ois.readUTF();
                                Object data = ois.readObject();
                                Object result = executor.execute(command, data);

                                try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                     ObjectOutputStream oos = new ObjectOutputStream(baos)) {

                                    oos.writeObject(result);
                                    byte[] responseData = baos.toByteArray();

                                    buffer.clear();
                                    buffer.put(responseData);
                                    buffer.flip();
                                    channel.send(buffer, clientAddress);
                                }
                            } catch (Exception e) {
                                sendErrorResponse(clientAddress, "503");
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            console.printErr("Address already in use");
        }
    }

    private void sendErrorResponse(SocketAddress clientAddress, String errorCode) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(errorCode.getBytes());
            channel.send(buffer, clientAddress);
        } catch (IOException e) {
            console.printErr("Failed to send error response: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (channel != null) channel.close();
            if (selector != null) selector.close();
        } catch (IOException e) {
            console.printErr("Error closing server: " + e.getMessage());
        }
    }
}