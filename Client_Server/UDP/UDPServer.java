import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    public static void main(String[] args) {
        int port = 12345;

        try (DatagramSocket socket = new DatagramSocket(port)) {
            System.out.println("Server UDP in ascolto sulla porta " + port);

            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Messaggio ricevuto dal client: " + message);

                // Risposta al client
                String response = "Messaggio ricevuto: " + message;
                byte[] responseData = response.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(
                        responseData, responseData.length, packet.getAddress(), packet.getPort());
                socket.send(responsePacket);
                break;
            }
        } catch (Exception e) {
            System.err.println("Errore del server UDP: " + e.getMessage());
        }
    }
}
