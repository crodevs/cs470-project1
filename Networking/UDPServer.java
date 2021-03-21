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
 * @version 3-20-2021
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

            // initialize array of dead and live clients
            ArrayList<Node> liveClients = new ArrayList<Node>();
            ArrayList<Node> deadClients = new ArrayList<Node>();

            while (true) 
            {
                System.out.println("Waiting for packets...\n");

                // set packet length, receive packet, get data from packet and IP/port
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                String message = new String(incomingPacket.getData());
                InetAddress IPAddress = incomingPacket.getAddress();
                int port = incomingPacket.getPort();

                // set client information
                Node client = new Node(IPAddress, port);

                // if this is the first client, add it to the list
                if (clients.size() == 0)
                {
                    clients.add(client);
                }

                // look for the current client in the list of clients,
                // if the current client is found, update its lastSeen.
                // if the current client is not found, add it
                boolean found = false;
                for (int i = 0; i < clients.size(); i++)
                {
                    if (clients.get(i).getIP().toString().equals(client.getIP().toString()) &&
                            clients.get(i).getPort() == client.getPort())
                    {
                        clients.get(i).setLastSeen();
                        found = true;
                    }
                }
                if (!found)
                {
                    clients.add(client);
                }

                // output for server user
                System.out.println("Received message from client: \n" + message);
                System.out.println("Client IP: "+IPAddress.getHostAddress());
                System.out.println("Client port: "+port);

                liveClients.clear();
                liveClients.trimToSize();
                deadClients.clear();
                deadClients.trimToSize();

                // check for dead and alive clients
                for(int i = 0; i < clients.size(); i++)
                {
                    if(clients.get(i).isDead())
                    {
                        deadClients.add(clients.get(i));
                    } else {
                        liveClients.add(clients.get(i));
                    }
                }

                // acknowledgment
                String reply = "Thank you for the message\n\n" + "Live clients: \n";
                for (int i = 0; i < liveClients.size(); i++)
                {
                    reply += liveClients.get(i).toString();
                }
                reply += "\nDead clients: \n";
                for (int i = 0; i < deadClients.size(); i++)
                {
                    reply += deadClients.get(i).toString();
                }
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

