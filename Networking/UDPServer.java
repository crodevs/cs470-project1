package Networking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

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

            while (true) 
            {
                // set packet length, receive packet, get data from packet and IP/port
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                socket.receive(incomingPacket);
                String message = new String(incomingPacket.getData());
                InetAddress IPAddress = incomingPacket.getAddress();
                int port = incomingPacket.getPort();

                // output for server user
                System.out.println("Received message from client: " + message);
                System.out.println("Client IP:"+IPAddress.getHostAddress());
                System.out.println("Client port:"+port);

                // acknowledgment
                String reply = "Thank you for the message";
                byte[] data = reply.getBytes();

                // create packet for acknowledgement
                DatagramPacket replyPacket = new DatagramPacket(data, data.length, IPAddress, port);

                // send acknowledgement, close socket
                socket.send(replyPacket);
                Thread.sleep(2000);
                socket.close();
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

