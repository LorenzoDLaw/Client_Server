
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

                                        ObjectOutputStream objOutput = new ObjectOutputStream(Client_Soket.getOutputStream());
                                        ObjectInputStream objInput = new ObjectInputStream(Client_Soket.getInputStream());
                                        
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

        private static boolean handShake(ObjectInputStream in, ObjectOutputStream out) 
        throws IOException, ClassNotFoundException{
                System.out.println("SERVER: waiting for client");
                //First step of the handshake 
                if (!CLIENT_HELLO.equals(in.readObject())){
                   System.out.println("SERVER: Error");
                   return false;     
                }
                //Second step of hamdshake

                /**
                 * Flushes the stream. This will write any buffered output bytes and flush
                 * through to the underlying stream.
                 */
                out.writeObject(SERVER_HELLO);
                out.flush(); 
                System.out.println("SERVER: HELLO sent");

                //Third step of handshake
                if(!ACK_CLIENT.equals(in.readObject())){
                        System.out.println("SERVER: Server ERROR");
                        return false;
                }
                System.out.println("SERVER: ACK client arrived");
                //Forth step of handshake ACK server
                out.writeObject(ACK_SERVER);
                out.flush();
                //the handshake was successful
                return true;
        }

        
        private static void runCalculator(ObjectInputStream in, ObjectOutputStream out)
            throws IOException, ClassNotFoundException {

                // Welcome banner
                out.writeObject("=== WELCOME TO THE CALCULATOR ===");
                //out.writeObject("=== WRITE EXIT FOR END THE SERVICE ===");
                out.flush();

                // Ask for value1 
                double val1 = promptDouble(in, out, "Enter first value:");
                if (Double.isNaN(val1)) return;          // error already sent to client

                // Ask for operator 
                out.writeObject("Enter operator (+, -, *, /):");
                out.flush();
                String operatorInput = (String) in.readObject();
                System.out.println("[Server] Operator received: " + operatorInput);

                if (operatorInput == null || operatorInput.trim().isEmpty()) {
                out.writeObject(MathResponseDTO.error("No operator provided."));
                out.flush();
                return;
                }
                char op = operatorInput.trim().charAt(0);

                // Ask for value2
                double val2 = promptDouble(in, out, "Enter second value:");
                if (Double.isNaN(val2)) return;

                // Build DTO, calculate and return result
                MathRequestDTO  request  = new MathRequestDTO(val1, op, val2);
                MathResponseDTO response = new MathHendler(request).calc(); 
                System.out.println("[Server] Sending response: " + response);
                out.writeObject(response);
                out.flush();

                out.writeObject(END);
        }

        //Method for handle the calc comunication with the client 
        //because we have to answere more question and we need to wait the response
        private static double promptDouble(ObjectInputStream in, ObjectOutputStream out, String prompt)
            throws IOException, ClassNotFoundException {

                //First i send the string 
                out.writeObject(prompt);
                out.flush();

                //Second i wait until the client responde
                String rawRespons = (String) in.readObject();
                System.out.println("[Server] Received: " + rawRespons);

                try {
                return Double.parseDouble(rawRespons.trim());
                } catch (NumberFormatException e) {
                out.writeObject(MathResponseDTO.error("'" + rawRespons + "' is not a valid number."));
                out.flush();
                return Double.NaN; //Not-a-Number (NaN) value of type 
                }
    }

    
}

