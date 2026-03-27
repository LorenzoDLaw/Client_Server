
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

        //The arrayList contain all the users that are connected to the chat
        public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); 
        private Socket socket;

        //private BufferedReader reader;
        //private BufferedWriter writer;

        private ObjectOutputStream outputStream;
        private ObjectInputStream inputStream;
        

        private String username;

        public String getUsername() {
                return username;
        }
        // COSTRUCTOR
        public ClientHandler(Socket socket) {
                try {
                    this.socket = socket;
                    this.inputStream = new ObjectInputStream(socket.getInputStream());
                    this.outputStream = new ObjectOutputStream(socket.getOutputStream());
                    this.username = (String) inputStream.readObject();  //the user will write down his username and after he click enter will get his username 

                    clientHandlers.add(this); //Here I add the new username in the array
                    //Every user in the chat will receive this message every time a new user enter
                    broadcastMessage("SERVER: " + username + " has entered the chat");

                } catch (Exception e) {
                }
        }

        public void run() {
                String message;
                while (socket.isConnected() && !socket.isClosed()) {
                        try {
                        Object received = inputStream.readObject();
                        if (received == null) break;
                        
                        message = received.toString().trim();
                        
                        if (message.equalsIgnoreCase("EXIT")) {
                                closeEverything(socket, inputStream, outputStream);
                                break; // Break the thread loop
                        }
                        
                        broadcastMessage(message);
                        } catch (Exception e) {
                                closeEverything(socket, inputStream, outputStream);
                                break;
                        }
                }
        }

        public void broadcastMessage(String sendMessage){
                //This method is used to send the message to all the users in the chat, exept for the one who send the message
                for(ClientHandler client : clientHandlers){ //We use a for each, the reason is that we want to send the message for each user in the array
                        try {
                            if (!client.username.equals(username)) {
                                client.outputStream.writeObject(sendMessage);
                                client.outputStream.flush();
                            }
                        } catch (Exception e) {
                                closeEverything(socket, inputStream, outputStream);
                        }
                }
        }

        //method to remove a client from the array when he leave the chat
        public void removeClientHandler(){
                //here I remove the user who left the chat
                clientHandlers.remove(this);
                broadcastMessage("SERVER: " + username + " has left the chat");
        }

        //Thanks to this method I close all the connection of the user after he left the chat
        private void closeEverything(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
                removeClientHandler();
                try{
                        if(inputStream != null){
                                inputStream.close();
                        }
                        if(outputStream != null){
                                outputStream.close();
                        }
                        if(socket != null){
                                socket.close();
                        }
                } catch (Exception e) {
                        e.printStackTrace();

                }
        }
}