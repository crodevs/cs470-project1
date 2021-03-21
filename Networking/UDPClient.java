package Networking;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

/**
 * Client side implementation that sends packets
 * @author Carson Rohan, Lucas Steffens
 * @version 3-20-2021
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
            InetAddress IPAddress = InetAddress.getByName("localhost");
            byte[] incomingData = new byte[1024];
            String sentence = "Hello from " + IPAddress.toString() + "!";
            byte[] data = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 25565);

            String choice = "ping";
            Scanner scanner = new Scanner(System.in);
            while (choice.equals("ping"))
            {
                Socket.send(sendPacket);
                System.out.println("Message sent from client\n");
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                Socket.receive(incomingPacket);

                String response = new String(incomingPacket.getData());
                System.out.println("Response from server:\n" + response);

                System.out.println("Waiting for input:");
                choice = scanner.nextLine();
            }
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
