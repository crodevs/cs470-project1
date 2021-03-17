package Networking;

import java.io.IOException;
import java.net.*;

/**
 * Client side implementation that sends packets
 * @author Carson Rohan, Lucas Steffens
 * @version 3-16-2021
 */

public class UDPClient 
{
    DatagramSocket Socket;

    public UDPClient() { }

    public void createAndListenSocket() 
    {
        try 
        {
            Socket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("24.117.219.41");
            byte[] incomingData = new byte[1024];
            String sentence = "Viehmann";
            byte[] data = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 25565);

            Socket.send(sendPacket);
            System.out.println("Message sent from client");

            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            Socket.receive(incomingPacket);

            String response = new String(incomingPacket.getData());
            System.out.println("Response from server:" + response);

            Socket.close();
        }

        catch (UnknownHostException e) 
        {
            e.printStackTrace();
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

    public static void main(String[] args) 
    {
        UDPClient client = new UDPClient();
        client.createAndListenSocket();
    }
}
