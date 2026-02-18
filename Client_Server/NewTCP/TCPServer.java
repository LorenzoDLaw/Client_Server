
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
                                        
                                        //Creation of bufferedReader for read the stream
                                        BufferedReader input = new BufferedReader(new InputStreamReader(Client_Soket.getInputStream()));
                                        //Creation of PrintWriter for send to the stream
                                        PrintWriter output = new PrintWriter(Client_Soket.getOutputStream(),true);

                                        //Handshake with client
                                        if (!handShake(input, output)) {
                                                System.out.println("[Server] Handshake failed. Dropping client.");
                                                continue;// return to listen the stream waiting for request
                                        }

                                        //Start the Calculator
                                        runCalculator(input, output);
     
                               }
                        }                    
                } catch (Exception e) {
                        e.printStackTrace();
                }

        }

        private static boolean handShake(BufferedReader in, PrintWriter out) throws IOException{
                System.out.println("SERVER: waiting for client");
                //First step of the handshake 
                if (!in.readLine().equals(CLIENT_HELLO)){
                   System.out.println("SERVER: Error");
                   return false;     
                }
                //Second step of hamdshake
                out.println(SERVER_HELLO);
                System.out.println("SERVER: HELLO sent");

                //Third step of handshake
                if(!in.readLine().equals(ACK_CLIENT)){
                        System.out.println("SERVER: Server ERROR");
                        return false;
                }
                System.out.println("SERVER: ACK client arrived");
                //Forth step of handshake ACK server
                out.println(ACK_SERVER);

                return true;
        }

        
        private static void runCalculator(BufferedReader in, PrintWriter out)throws IOException{
                //Start the logic for comunication with the client

                out.println("WELCOME TO CALCULATOR");
                out.println("CHOSE YOUT FIRST VALUE:");
                String value1 = in.readLine();
                Double val1=0.0;
                try {
                        val1 = Double.parseDouble(value1);
                } catch (NumberFormatException e) {
                        out.println("ERROR -> '" + value1 + "' is not a valid value.");
                        // END the comunication 
                        out.println(END);
                        return;
                }


                out.println("CHOSE YOUT OPERATOR:");
                String operator = in.readLine();
                char op=operator.charAt(0);

                out.println("CHOSE YOUT SECOND VALUE:");
                String value2 = in.readLine();
                Double val2=0.0;
                try {
                        val2 = Double.parseDouble(value2);
                } catch (NumberFormatException e) {
                        out.println("ERROR -> '" + value2 + "' is not a valid.");
                        // END the comunication 
                        out.println(END);
                        return;
                }
                MathHendler calc = new MathHendler(op,val1,val2);
                out.println(calc.calc());
                out.println(END);
        }

}

