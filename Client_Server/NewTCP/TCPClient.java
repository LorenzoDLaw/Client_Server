import java.io.*;
import java.net.Socket;


public class TCPClient{
        private static final String serverAddress = "localhost";
        final static int PORT = 8080;
        final static String SERVER_HELLO = "HELLO_SERVER";
        final static String CLIENT_HELLO = "HELLO_CLIENT";
        final static String ACK_CLIENT = "ACK_CLIENT";
        final static String ACK_SERVER = "ACK_SERVER";
        final static String END = "END";
        
        
        public static void main(String[] args) {
                
                try (Socket socket = new Socket(serverAddress, PORT)) {
                        //Creation of BufferedReader for read the stream
                        //BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        //Creation of PrintWriter for send to the stream
                        //PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

                        ObjectOutputStream output   = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream  input    = new ObjectInputStream(socket.getInputStream());

                        //Buffer reader for read user input for the calculator
                        BufferedReader cmdInput = new BufferedReader(new InputStreamReader(System.in));

                        if(!Handshake(input, output)){
                                System.out.println("ERROR");

                        }else{
                                //Here the server service start
                                // Keep reading lines from the server until we get the END message
                                while (true) {
                                        Object received = input.readObject();

                                        if (received instanceof MathResponseDTO) {
                                                // Obtain the final result otherwise we get an error from the MathHandler
                                                MathResponseDTO response = (MathResponseDTO) received;
                                                System.out.println(response); 
                                                break;

                                        } else if (received instanceof String) {
                                                String serverLine = (String) received;
                                                System.out.println(serverLine);

                                                if (serverLine.equals(END)) {
                                                        System.out.println("Session ended.");
                                                        break;
                                                }
                                                // If it's a prompt read user input and send it back
                                                if (serverLine.contains(":")) {
                                                        System.out.print("> ");
                                                        String userInput = cmdInput.readLine();
                                                        output.writeObject(userInput);
                                                        output.flush();
                                                }
                                        }
                                        }
                                
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }        
        }


        private static boolean Handshake(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException{
                //Handshake
                //First step of the handshake 
                out.writeObject(CLIENT_HELLO);
                out.flush();
                System.out.println("CLIENT: HELLO sent");
                
                //Second step of hamdshake
                if (!SERVER_HELLO.equals(in.readObject())){
                   System.out.println("CLIENT: Error");
                   return false;     
                }

                //Third step of handshake ACK client
                out.writeObject(ACK_CLIENT);
                out.flush();
                System.out.println("CLIENT: ACK sent");

                //Forth step of handshake 
                if(!ACK_SERVER.equals(in.readObject())){
                        System.out.println("CLIENT: Server ERROR");
                        return false;
                }
                System.out.println("CLIENT: ACK Server arrived");
                return true;

        }
}

