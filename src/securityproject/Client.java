
package securityproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A Simple Socket client that connects to our socket server
 * @author faheem
 *
 */
public class Client {

    private String hostname;
    private int port;
    static Socket socketClient;

    public Client(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    public String connect() throws UnknownHostException, IOException{
        String connectionInfo = ("Attempting to connect to "+hostname+":"+port);
       
        socketClient = new Socket(hostname,port);
        
        connectionInfo = connectionInfo + "\nConnection Established";
        return connectionInfo;
    }

    public void readResponse() throws IOException{
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        System.out.println("Response from server:");
    }

    public void send(Socket socket, String message) throws IOException{

        // Send the message to the server
        OutputStream os = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(os);
        BufferedWriter bw = new BufferedWriter(osw);

        String sendMessage = message + "\n";
        bw.write(sendMessage);
        bw.flush();
        System.out.println("Message sent to the server : " + sendMessage);
    }
    
    public static void main(String arg[]){
        //Creating a SocketClient object
        Client client = new Client ("localhost",8080);
        try {
            //trying to establish connection to the server
            client.connect();
            //if successful, read response from server
            client.readResponse();

        } catch (UnknownHostException e) {
            System.err.println("Host unknown. Cannot establish connection");
        } catch (IOException e) {
            System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
        }
    }
}