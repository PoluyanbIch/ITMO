package managers;

import utility.Response;

public class UDPManager {
    private final UDPClient udpClient;

    public UDPManager(UDPClient udpClient) {
        this.udpClient = udpClient;
    }

    public Response send(String s, Object obj) {
        var r = ((Response) udpClient.send(s, obj));
        if (r == null) {
            try {
                Thread.sleep(3000);
                r = send(s, obj); // Рекурсивный повтор
            } catch (Exception e) {
                System.out.print(e);
                System.exit(1);
            }
        }
        return r;
    }

    public Response send(String s) {
        return send(s, null);
    }

    public String sendAndGetMessage(String s, Object obj) {
        return send(s, obj).getMessage();
    }

    public String sendAndGetMessage(String s) {
        return send(s).getMessage();
    }
}