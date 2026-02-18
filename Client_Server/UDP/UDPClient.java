import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 12345;

        try (DatagramSocket socket = new DatagramSocket()) {
            String message = "Ciao, server UDP!";
            byte[] data = message.getBytes();
            InetAddress serverInetAddress = InetAddress.getByName(serverAddress);

            DatagramPacket packet = new DatagramPacket(data, data.length, serverInetAddress, port);
            socket.send(packet);
            System.out.println("Messaggio inviato al server: " + message);

            byte[] buffer = new byte[1024];
            DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(responsePacket);

            String response = new String(responsePacket.getData(), 0, responsePacket.getLength());
            System.out.println("Risposta dal server: " + response);
        } catch (Exception e) {
            System.err.println("Errore del client UDP: " + e.getMessage());
        }
    }
}
