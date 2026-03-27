
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client{
         
        Socket socket;
        private BufferedReader reader;
        //private BufferedWriter writer;
        private ObjectOutputStream output;
        private ObjectInputStream  input;

        private String username;
        private static int port = 1234;

        public String getUsername(){
                return username;
        }
        
        //MAIN
        public static void main(String[] args) throws IOException {
                Scanner scanner = new Scanner(System.in);
                scanner.reset();
                while (true) { 
                       
                        System.out.print("Enter your username: ");
                        String username = scanner.nextLine();
                
                try {
                        Socket socket = new Socket("localhost", port);
                        System.err.println("----Connecting to the server----"); 
                        System.err.println("--------------------------------"); 
                        Client client = new Client(socket, username);

                        System.err.println("----You are connected to the server----");  
                        System.err.println("------Write EXIT to leave the chat------"); 
                        
                        client.listenMessage();
                        client.sendMessage();

                        System.err.println("\n---- REBOOTING SESSION ----\n");
                        
                } catch (Exception e) {
                        System.err.println("----Connection to the server failed----");
                }
                }  
        }


        public Client(Socket socket, String username){
                try {
                        this.socket = socket;
                        this.username = username;
                        this.output = new ObjectOutputStream(socket.getOutputStream());
                        this.input = new ObjectInputStream(socket.getInputStream());
                        
                        output.writeObject(username);
                        output.flush();

                        
                } catch (IOException e) {
                        closeEverything(socket, input, output);
                }
        }

        
        public void sendMessage(){
                try {
                        Scanner scanner = new Scanner(System.in);
                        //socket.isConnected() && !socket.isClosed(): we check if the socket is still connected and not closed
                        while(socket.isConnected() && !socket.isClosed()){   //!socket.isClosed() without this the user can still write message and he will never reeboot 
                                String message = scanner.nextLine();
                                if (message.equals("EXIT")) {
                                    output.writeObject("EXIT");
                                    output.flush();
                                    closeEverything(socket, input, output);
                                    break;
                                }else {
                                        String msg = username + ": " + message;
                                        output.writeObject(msg);
                                        output.flush();
                                }
                                
                                
                        }
                } catch (Exception e) {
                        closeEverything(socket, input, output);
                }
        }

        public void listenMessage() {
                new Thread(new Runnable() {
                        public void run(){
                                Object msgFromChat;

                                while (socket.isConnected() && !socket.isClosed()) { 
                                    try {
                                        msgFromChat = input.readObject();
                                        if (msgFromChat == null) break;
                                        System.out.println("" + msgFromChat);
                                    } catch (Exception e) {
                                        break;
                                    }
                                }
                        }
                }).start();
        }

         private void closeEverything(Socket socket, ObjectInputStream input, ObjectOutputStream output) {
                try{
                        if(input != null){
                                input.close();
                        }
                        if(output != null){
                                output.close();
                        }
                        if(socket != null){
                                socket.close();
                        }
                        System.err.println("YOU HAVE LEFT THE CHAT");
                        System.err.println("PRESS ENTER TO REBOOT THE SESSION");

                } catch (Exception e) {
                        e.printStackTrace();

                }
        }
        
}