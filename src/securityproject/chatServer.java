/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package securityproject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author Josh
 */
public class chatServer {

    static Vector ClientSockets;
    static Vector LoginNames;
    
    chatServer() throws Exception
    {
        ServerSocket soc=new ServerSocket(5217);
        ClientSockets=new Vector();
        LoginNames=new Vector();

        while(true)
        {    
            Socket CSoc=soc.accept();        
            AcceptClient obClient=new AcceptClient(CSoc);
        }
    }
    public static void main(String args[]) throws Exception
    {
        chatServer ob=new chatServer();     
    }
    
    
class AcceptClient extends Thread
{
    Socket ClientSocket;
    DataInputStream din;
    DataOutputStream dout;
    AcceptClient (Socket CSoc) throws Exception
    {
        ClientSocket=CSoc;

        din=new DataInputStream(ClientSocket.getInputStream());
        dout=new DataOutputStream(ClientSocket.getOutputStream());
        
        String LoginName=din.readUTF();

        System.out.println("User Logged In :" + LoginName);
        LoginNames.add(LoginName);
        ClientSockets.add(ClientSocket);    
        start();
    }

    public void run()
    {
        while(true)
        {
            try
            {
                String msgFromClient = din.readUTF();
                System.out.println(msgFromClient);
                StringTokenizer st = new StringTokenizer(msgFromClient);
                String Sendto = st.nextToken();                
                String MsgType = st.nextToken();
                int iCount = 0;
    
                if(MsgType.equals("LOGOUT"))
                {
                    for(iCount=0;iCount<LoginNames.size();iCount++)
                    {
                        if(LoginNames.elementAt(iCount).equals(Sendto))
                        {
                            LoginNames.removeElementAt(iCount);
                            ClientSockets.removeElementAt(iCount);
                            System.out.println("User " + Sendto +" Logged Out ...");
                            break;
                        }
                    }
    
                }
                else
                {
                    String msg="";
                    while(st.hasMoreTokens())
                    {
                        msg = msg + " " + st.nextToken();
                    }
                    for(iCount=0;iCount<LoginNames.size();iCount++)
                    {
                        if(LoginNames.elementAt(iCount).equals(Sendto))
                        {    
                            System.out.println(msg);
                            Socket tSoc =(Socket)ClientSockets.elementAt(iCount);                            
                            DataOutputStream tdout = new DataOutputStream(tSoc.getOutputStream());
                            System.out.println("tSoc is: " + tSoc + "\n tdout is: "  + tdout);
                            tdout.writeUTF(msg);                            
                            break;
                        }
                    }
                    if(iCount==LoginNames.size())
                    {
                        dout.writeUTF("I am offline");
                    }
                    else
                    {
                        
                    }
                }
                if(MsgType.equals("LOGOUT"))
                {
                    break;
                }

            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }        
    }
}
}
