/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package securityproject;
import com.chilkatsoft.*;
/**
 *
 * @author Josh
 */
public class SendSocket {

    static {
        try {
            System.loadLibrary("chilkat");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load.\n" + e);
            System.exit(1);
        }
    }

    public void bind(String message, int port){
        CkSocket listenSocket = new CkSocket();

        boolean success;
        success = listenSocket.UnlockComponent("Anything for 30-day trial");
        if (success != true) {
            System.out.println(listenSocket.lastErrorText());
        }

        //  Bind to a port and listen for incoming connections:
        //  This example will listen at port 5555 and allows for a backlog
        //  of 25 pending connection requests.
        success = listenSocket.BindAndListen(port,25);
        if (success != true) {
            System.out.println("send error 1: \n" + listenSocket.lastErrorText());
        }

        //  Get the next incoming connection
        //  Wait a maximum of 20 seconds (20000 millisec)
        CkSocket connectedSocket;
        connectedSocket = listenSocket.AcceptNextConnection(20000);
        if (connectedSocket == null ) {
            System.out.println("send error 2 \n" + listenSocket.lastErrorText());
        }

        //  Set maximum timeouts for reading an writing (in millisec)
        connectedSocket.put_MaxReadIdleMs(10000);
        connectedSocket.put_MaxSendIdleMs(10000);

        //  Send a "Hello World!" message to the client:
        System.out.println("Message to send is: " + message);
        success = connectedSocket.SendString(message);
        if (success != true) {
            System.out.println("send error 3 \n" + connectedSocket.lastErrorText());
        }

        //  Close the connection with the client.
        //  Wait a max of 20 seconds (20000 millsec)
        connectedSocket.Close(20000);

        System.out.println("success!");

    }
}

