import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) {
        int port = 12345; // Porta su cui il server ascolta
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server in ascolto sulla porta " + port);

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {

                    System.out.println("Connessione accettata da " + clientSocket.getInetAddress());
                    // Creazione di flussi per la comunicazione
                    try (BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true)) {
                        
                        String message = input.readLine();
                        System.out.println("Messaggio ricevuto dal client: " + message);
                        
                        /*if (ToupperCase(message.equals("END")) ){
                            break;
                        }*/
                        
                        // Inizialize the variables for calculator
                        char operator;
                        double val1;
                        double val2;
                        
                        //I use the in bufferedreader for get the valuse and the operator from the client
                        output.println("Welcome to caluclator:");
                        
                        //Reqest for the first value into val1
                        output.println("Chose your first value: ");
                        val1 = Double.parseDouble(input.readLine());
                        System.out.println(val1);
                        
                        //Reqest for the operator
                        output.println("Chose an operator (+, -, *, /) for your math calculation: "); //chose the operator
                        String strOperator = input.readLine();
                        operator = strOperator.charAt(0);
                        System.out.println(operator);
                        
                        //Reqest for the second value into val2
                        output.println("Chose your second value: "); //Insert the second value into val2
                        val2 = Double.parseDouble(input.readLine());
                        System.out.println(val2);
                        
                        Calculator calculator = new Calculator();
                        Double result = calculator.calc(operator, val1,val2);
                        // Risposta al client
                        output.println("The result for the operation is: " + result);
                        output.println("END");
                    }
                }
				//serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Errore del server: " + e.getMessage());
        }
    }

    private static boolean ToupperCase(boolean equals) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
