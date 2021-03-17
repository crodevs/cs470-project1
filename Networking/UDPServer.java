package Networking;

import java.io.IOException;
import java.net.*;
import java.util.Date;
import java.util.LinkedList;

public class UDPServer 
{
    DatagramSocket socket = null;

    public UDPServer() 
    {

    }
    public void createAndListenSocket() 
    {
        try 
        {
            socket = new DatagramSocket(25565);
            byte[] incomingData = new byte[1024];
            LinkedList<String> nodes = new LinkedList<String>();
            long start = System.currentTimeMillis();
            long elapsed = 0L;

            while (true) 
            {
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                String message = new String(incomingPacket.getData());
                InetAddress IPAddress = incomingPacket.getAddress();
                int port = incomingPacket.getPort();
                System.out.println("Received message from client: " + message);
                System.out.println("Client IP: " + IPAddress.getHostAddress());
                System.out.println("Client port: " + port);
                nodes.add(IPAddress.getHostAddress() + ":" + port);

                elapsed = (new Date()).getTime() - start;
                if (elapsed >= 30000)
                {
                    response(nodes, socket);
                    System.out.println(nodes);
                    start = System.currentTimeMillis();
                }
            }
        } 
        catch (SocketException e) 
        {
            e.printStackTrace();
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void response( LinkedList<String> nodes, DatagramSocket socket)
    {
        for (int i = 0; i < nodes.size(); i++)
        {
            try
            {
                String node = nodes.get(i);
                String[] splitNode = node.split(":", 2);
                String dataString = nodes.toString();
                byte[] data = dataString.getBytes();
                DatagramPacket replyPacket = new DatagramPacket(data, data.length, InetAddress.getByName(splitNode[0]),
                        Integer.parseInt(splitNode[1]));
                socket.send(replyPacket);
            }
            catch (UnknownHostException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        nodes.clear();
    }

    public static void main(String[] args) 
    {

        UDPServer server = new UDPServer();
        server.createAndListenSocket();
    }
}

