import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 12345;

        try (Socket socket = new Socket(serverAddress, port);
             
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader cmdInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connesso al server!");
             // Invio di un messaggio al server
            String message = "Hi, server!";
            output.println(message);
            System.out.println("MSG sent to the server: " + message);
            String msg = input.readLine(); // Read welcome message
            System.out.println("MSG recived by the server: " + msg);
            String serverMessage;
            
            // Loop per leggere i messaggi dal server
            while ((serverMessage = input.readLine()) != null) {
                
                System.out.println("For end the program write END");
                // If server sent a "END" message -> close the connection
                if (serverMessage.equals("END")) {
                    System.out.println("Connection ended");
                    break;
                }
                
                // Mostra il messaggio del server
                System.out.println(serverMessage);
                
                // Se il messaggio contiene ":" significa che il server vuole una risposta
                if (serverMessage.contains(":")) {
                    System.out.print("> ");
                    String userInput = cmdInput.readLine();
                    output.println(userInput);
                }
            }

        } catch (IOException e) {
            System.err.println("Errore del client: " + e.getMessage());
        }
    }
}