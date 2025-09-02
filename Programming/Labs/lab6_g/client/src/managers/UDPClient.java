package managers;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Scanner;

public class UDPClient {
    private InetSocketAddress serverAddress;
    private DatagramSocket socket;
    private ClientReceivingManager receivingManager;
    private ClientSendingManager sendingManager;

    public UDPClient(String host, int port) {
        this.serverAddress = new InetSocketAddress(host, port);
        try {
            this.socket = new DatagramSocket();
            this.socket.setSoTimeout(5000); // Таймаут 5 секунд
            this.receivingManager = new ClientReceivingManager(this);
            this.sendingManager = new ClientSendingManager(this);
        } catch (SocketException e) {
            System.out.println("Ошибка при создании UDP сокета: " + e.getMessage());
        }
    }

    public void restart(String host, int port) {
        this.serverAddress = new InetSocketAddress(host, port);
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            this.socket = new DatagramSocket();
            this.socket.setSoTimeout(5000);
        } catch (SocketException e) {
            System.out.println("Ошибка при перезапуске UDP клиента: " + e.getMessage());
        }
    }

    public DatagramSocket getSocket() {
        return this.socket;
    }

    public InetSocketAddress getServerAddress() {
        return this.serverAddress;
    }

    public Object send(String s, Object obj) {
        try (var baos = new ByteArrayOutputStream();
             var a = new ObjectOutputStream(baos)) {

            a.writeUTF(s);
            a.writeObject(obj);
            sendingManager.send(baos.toByteArray());

            System.out.println("Отправлено. Ожидание ответа...");
            try (var command = new ObjectInputStream(
                    new ByteArrayInputStream(receivingManager.receive()))) {
//                System.out.println("Получилось");
                return command.readObject();
            }
        } catch (Exception e) {
            System.out.println("Ошибка при отправке/получении: " + e.getMessage());
            return null;
        }
    }

    public void newIP() {
        var sc = new Scanner(System.in);
        System.out.println("TimeLimit. Напишите новый хост (address:port):");
        while (true) {
            var adr = sc.nextLine();
            if (adr.contains(":")) {
                try {
                    var _port = Integer.parseInt(adr.split(":")[1]);
                    restart(adr.split(":")[0], _port);
                    break;
                } catch (Exception e1) {
                    System.out.println("Попробуйте снова");
                }
            } else {
                try {
                    restart(adr, serverAddress.getPort());
                    break;
                } catch (Exception e1) {
                    System.out.println("Попробуйте снова");
                }
            }
        }
    }
}