
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

        private ServerSocket serverSocket;
        private static int port = 1234;
        public Server(ServerSocket serverSocket){
                this.serverSocket = serverSocket;
        }

        public void start() {
                //Here we accept the user who want to connect to the chat and we create a new thread
                try {
                        while (!serverSocket.isClosed()) {
                                System.err.println("Server waiting for Clients on port " + port + ".");
                                Socket socket = serverSocket.accept();
                                
                                // 1. Create the handler
                                ClientHandler handler = new ClientHandler(socket);
                                
                                System.out.println("A NEW CLIENT IS CONNECTED" + handler.getUsername());
                                // 2. Pass the handler (which must implement Runnable) to the Thread
                                Thread thread = new Thread(handler);
                                thread.start();
                        }
                } catch (Exception e) {
                        close();
                }
        }

        public void close(){
                try{
                        if(serverSocket != null){
                                serverSocket.close();
                        }  
                }catch(IOException e){
                        e.printStackTrace();
                }
        }

        public static void main(String[] args) throws IOException {
                ServerSocket serverSocket = new ServerSocket(port);
                Server server = new Server(serverSocket);
                server.start();
        }

}