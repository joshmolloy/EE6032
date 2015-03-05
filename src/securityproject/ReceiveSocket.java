/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package securityproject;

import com.chilkatsoft.CkSocket;

/**
 *
 * @author Josh
 */
public class ReceiveSocket {
    
    static {
        try {
            System.loadLibrary("chilkat");
        } catch (UnsatisfiedLinkError e) {
                System.err.println("Native code library failed to load.\n" + e);
                System.exit(1);
        }
    }
    
    public String listen(int port){
        CkSocket socket = new CkSocket();

        boolean success;
        success = socket.UnlockComponent("Anything for 30-day trial");
        if (success != true) {
            System.out.println("receive error 1 \n" + socket.lastErrorText());
        }

        //  Connect to port 5555 of localhost.
        //  The string "localhost" is for testing on a single computer.
        //  It would typically be replaced with an IP hostname, such
        //  as "www.chilkatsoft.com".
        boolean ssl = false;
        int maxWaitMillisec = 20000;
        success = socket.Connect("localhost",port,ssl,maxWaitMillisec);
        if (success != true) {
            System.out.println("receive error 2 \n" + socket.lastErrorText());
        }

        //  Set maximum timeouts for reading an writing (in millisec)
        socket.put_MaxReadIdleMs(10000);
        socket.put_MaxSendIdleMs(10000);

        //  Pretend, for the sake of the example, that the
        //  ficticious server is going to send a "Hello World!"
        //  after accepting the connection.
        //  Note: Technically, the ReceiveString may not receive the
        //  complete string, although it's highly probable given the short
        //  length of the "Hello World!" message.
        //  See this Chilkat blog post for more information:
        //  http://www.cknotes.com/?p=302
        String receivedMsg;
        receivedMsg = socket.receiveString();
        if (receivedMsg == null ) {
            System.out.println("receive error 3 \n" + socket.lastErrorText());
        }
        //  Close the connection with the server
        //  Wait a max of 20 seconds (20000 millsec)
        socket.Close(20000);

        System.out.println("Message received: " + receivedMsg);
        return receivedMsg;
    }
}
