package Networking;

import java.io.IOException;
import java.io.File;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Peer to peer implementation that sends and receives packets
 * @author Carson Rohan, Lucas Steffens
 * @version 3-21-2021
 */
public class UDPPeer
{
    DatagramSocket socket = null;

    public UDPPeer() { }

    public void createAndListenSocket()
    {
        try
        {
            // initialize list of IP addresses from config.txt
            ArrayList<String> IPList = new ArrayList<String>();
            File config = new File("Networking/config.txt");
            Scanner scanner = new Scanner(config);

            while (scanner.hasNextLine())
            {
                String IP = scanner.nextLine();
                IPList.add(IP);
            }

            for(int i = 0; i < IPList.size(); i++)
            {
                System.out.println(IPList.get(i));
            }

            /**
             * TODO read from file containing list of peer IP's
             * TODO check input IP against list of peer IP's
             * TODO add/delete IP's from file as they die and come back alive
             */

            InetAddress thisIP = InetAddress.getLocalHost();
            socket = new DatagramSocket(25565);
            byte[] incomingData = new byte[1024];
            String sentence = "Hello from " + thisIP + "!";
            byte[] data = sentence.getBytes();
            Random sendInterval = new Random();

            // initialize array of nodes
            ArrayList<Node> nodes = new ArrayList<Node>();

            // initialize array of dead and live nodes
            ArrayList<Node> liveNodes = new ArrayList<Node>();
            ArrayList<Node> deadNodes = new ArrayList<Node>();

            while (true)
            {
                for(int i = 0; i < nodes.size(); i++)
                {
                    if(!nodes.get(i).getIP().equals(thisIP))
                    {
                        DatagramPacket sendPacket = new DatagramPacket(data, data.length,
                                nodes.get(i).getIP(), 25565);
                        socket.send(sendPacket);
                    }
                }

                System.out.println("Waiting for packets...\n");

                // set packet length, receive packet, get data from packet and IP/port
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                String message = new String(incomingPacket.getData());
                InetAddress IPAddress = incomingPacket.getAddress();
                int port = incomingPacket.getPort();

                // set node information
                Node node = new Node(IPAddress, port);

                // if this is the first node, add it to the list
                if (nodes.size() == 0)
                {
                    nodes.add(node);
                }

                // look for the current node in the list of nodes,
                // if the current node is found, update its lastSeen.
                // if the current node is not found, add it
                boolean found = false;
                for (int i = 0; i < nodes.size(); i++)
                {
                    if (nodes.get(i).getIP().toString().equals(node.getIP().toString()) &&
                            nodes.get(i).getPort() == node.getPort())
                    {
                        nodes.get(i).setLastSeen();
                        found = true;
                    }
                }
                if (!found)
                {
                    nodes.add(node);
                }

                // output for server user
                System.out.println("Received message from node: \n" + message);
                System.out.println("node IP: "+IPAddress.getHostAddress());
                System.out.println("node port: "+port);

                liveNodes.clear();
                liveNodes.trimToSize();
                deadNodes.clear();
                deadNodes.trimToSize();

                // check for dead and alive nodes
                for(int i = 0; i < nodes.size(); i++)
                {
                    if(nodes.get(i).isDead())
                    {
                        deadNodes.add(nodes.get(i));
                    } else {
                        liveNodes.add(nodes.get(i));
                    }
                }

                // acknowledgment
                String reply = "Thank you for the message\n\n" + "Live nodes: \n";
                for (int i = 0; i < liveNodes.size(); i++)
                {
                    reply += liveNodes.get(i).toString();
                }
                reply += "\nDead nodes: \n";
                for (int i = 0; i < deadNodes.size(); i++)
                {
                    reply += deadNodes.get(i).toString();
                }
                byte[] replyData = reply.getBytes();

                for(int i = 0; i < nodes.size(); i++)
                {
                    if(!nodes.get(i).getIP().equals(thisIP))
                    {
                        // create packet for acknowledgement
                        DatagramPacket replyPacket = new DatagramPacket(replyData, replyData.length,
                                nodes.get(i).getIP(), port);
                        // send acknowledgement
                        socket.send(replyPacket);
                    }
                }
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
    }

    public static void main(String[] args)
    {
        UDPPeer peer = new UDPPeer();
        peer.createAndListenSocket();
    }
}
