
package securityproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
* A simple socket server
* @author faheem
*
*/
public class Server {
    
    private ServerSocket serverSocket;
    private int port;
    
    public Server(int port) {
        this.port = port;
    }
    
    public String start() throws IOException {
        System.out.println("Entered Start");
        String connectionInfo = ("----------------Server----------------\n" + "Starting the socket server at port: " + port);
        
        serverSocket = new ServerSocket(port);
        System.out.println("ServerSocket is started" + port);
        
        //Listen for clients. Block till one connects
        connectionInfo = connectionInfo + ("\nWaiting for clients...\n");
        
        Socket client = serverSocket.accept();
        System.out.println("Accepted client");
        
        //A client has connected to this server. Send welcome message
        receive(client);
        return connectionInfo;
    }
    
    public void sendWelcomeMessage(Socket client) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        writer.write("Hello. You are connected to a Simple Socket Server. What is your name?");
        writer.flush();
        writer.close();
    }
    
    public void receive(Socket client) throws IOException{
        InputStream input = client.getInputStream();
        InputStreamReader instr = new InputStreamReader(input);
        BufferedReader br = new BufferedReader(instr);
        String message = br.readLine();
        System.out.println("Message received from client is "+ message);
    }
    
    public static boolean serverOnlineCheck(){ 
        try (Socket s = new Socket("localhost", 8080)) {
            return true;
        } catch (IOException ex) {
            /* ignore */
        }
        return false;
}
    
    /**
    * Creates a SocketServer object and starts the server.
    *
    * @param args
    */
    public static void main(String[] args) {
        // Setting a default port number.
        int portNumber = 8080;
        
        try {
            // initializing the Socket Server
            Server server = new Server(portNumber);
            server.start();
            
            } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
