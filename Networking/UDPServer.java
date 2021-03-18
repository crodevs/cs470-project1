package Networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Server side implementation that listens for packets
 * @author Carson Rohan, Lucas Steffens
 * @version 3-16-2021
 */

public class UDPServer 
{
    DatagramSocket socket = null;

    public UDPServer() { }

    /**
     * This method listens on port 25565 for incoming packets, then
     * sends an acknowledgement when it receives a packet
     */
    public void createAndListenSocket() 
    {
        try 
        {
            socket = new DatagramSocket(25565);
            byte[] incomingData = new byte[1024];

            // initialize array of clients
            ArrayList<Node> clients = new ArrayList<Node>();

            while (true) 
            {
                System.out.println("Waiting for packets...\n");

                // set packet length, receive packet, get data from packet and IP/port
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                String message = new String(incomingPacket.getData());
                InetAddress IPAddress = incomingPacket.getAddress();
                int port = incomingPacket.getPort();

                // set client information, add client to client list
                Node client = new Node(IPAddress, port);

                /**
                 * TODO fix adding client that already exists into clients list
                 */
                clients.add(client);

                // output for server user
                System.out.println("Received message from client: \n" + message);
                System.out.println("Client IP: "+IPAddress.getHostAddress());
                System.out.println("Client port: "+port);

                String deadClient = "Dead clients: \n";
                String liveClient = "Live clients: \n";

                // check for dead and alive clients
                for(int i = 0; i < clients.size(); i++)
                {
                    if(clients.get(i).isDead())
                    {
                        deadClient += clients.get(i).toString();
                    } else {
                        liveClient += clients.get(i).toString();
                    }
                }

                // acknowledgment
                String reply = "Thank you for the message\n\n" + liveClient + deadClient;
                byte[] data = reply.getBytes();

                // create packet for acknowledgement
                DatagramPacket replyPacket = new DatagramPacket(data, data.length, IPAddress, port);

                // send acknowledgement, close socket
                socket.send(replyPacket);
                Thread.sleep(2000);
                //socket.close();
            }
        }

        catch (SocketException e) 
        {
            e.printStackTrace();
        }

        catch (IOException i) 
        {
            i.printStackTrace();
        }

        catch (InterruptedException e) 
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) 
    {
        UDPServer server = new UDPServer();
        server.createAndListenSocket();
    }
}

