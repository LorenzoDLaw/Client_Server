
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;



public class TCPServer{
        final static int PORT = 8080;
        final static String SERVER_HELLO = "HELLO_SERVER";
        final static String CLIENT_HELLO = "HELLO_CLIENT";
        final static String ACK_CLIENT = "ACK_CLIENT";
        final static String ACK_SERVER = "ACK_SERVER";
        final static String END = "END";
        public static void main(String[] args) {
                


                try (ServerSocket server_Soket = new ServerSocket(PORT)){
                        // The server is active
                        System.out.println("[Server] Listening on port " + PORT);

                        while(true){
                               try (Socket Client_Soket = server_Soket.accept()){
                                        
                                        //Creation of bufferedReader for read the console
                                        //BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                                        //Creation of PrintWriter for send to the stream
                                        //PrintWriter output = new PrintWriter(Client_Soket.getOutputStream(),true);

                                        ObjectInputStream objInput = new ObjectInputStream(Client_Soket.getInputStream());
                                        ObjectOutputStream objOutput = new ObjectOutputStream(Client_Soket.getOutputStream());

                                        //Handshake with client
                                        if (!handShake(objInput, objOutput)) {
                                                System.out.println("[Server] Handshake failed. Dropping client.");
                                                continue;// return to listen the stream waiting for request
                                        }

                                        //Start the Calculator
                                        runCalculator(objInput, objOutput);
     
                               }
                        }                    
                } catch (Exception e) {
                        e.printStackTrace();
                }

        }

        private static boolean handShake(ObjectInputStream in, ObjectOutputStream out) throws IOException{
                System.out.println("SERVER: waiting for client");
                //First step of the handshake 
                if (!in.readLine().equals(CLIENT_HELLO)){
                   System.out.println("SERVER: Error");
                   return false;     
                }
                //Second step of hamdshake
                out.writeObject(SERVER_HELLO);
                System.out.println("SERVER: HELLO sent");

                //Third step of handshake
                if(!in.readLine().equals(ACK_CLIENT)){
                        System.out.println("SERVER: Server ERROR");
                        return false;
                }
                System.out.println("SERVER: ACK client arrived");
                //Forth step of handshake ACK server
                out.writeObject(ACK_SERVER);

                

                return true;
        }

        
        private static void runCalculator(ObjectInputStream in, ObjectOutputStream out)
            throws IOException, ClassNotFoundException {

                // Welcome banner
                out.writeObject("=== WELCOME TO THE CALCULATOR ===");
                out.flush();

                // --- Ask for value1 ---
                double val1 = promptDouble(in, out, "Enter first value:");
                if (Double.isNaN(val1)) return;          // error already sent to client

                // --- Ask for operator ---
                out.writeObject("Enter operator (+, -, *, /):");
                out.flush();
                String opInput = (String) in.readObject();
                System.out.println("[Server] Operator received: " + opInput);

                if (opInput == null || opInput.trim().isEmpty()) {
                out.writeObject(MathResponseDTO.error("No operator provided."));
                out.flush();
                return;
                }
                char op = opInput.trim().charAt(0);

                // --- Ask for value2 ---
                double val2 = promptDouble(in, out, "Enter second value:");
                if (Double.isNaN(val2)) return;

                // --- Build DTO, calculate and return result ---
                MathRequestDTO  request  = new MathRequestDTO(val1, op, val2);
                String response = new MathHendler(request).calc();
                System.out.println("[Server] Sending response: " + response);

                out.writeObject(response);
                out.flush();
        }


        private static double promptDouble(ObjectInputStream in, ObjectOutputStream out,
                                       String prompt)
            throws IOException, ClassNotFoundException {

        out.writeObject(prompt);
        out.flush();

        String raw = (String) in.readObject();
        System.out.println("[Server] Received: " + raw);

        try {
            return Double.parseDouble(raw.trim());
        } catch (NumberFormatException e) {
            out.writeObject(MathResponseDTO.error("'" + raw + "' is not a valid number."));
            out.flush();
            return Double.NaN;
        }
    }

    
}

