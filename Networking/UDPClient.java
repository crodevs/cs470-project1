package Networking;

import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

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
            System.out.println("Please enter the desired server IP: ");
            Scanner scanner = new Scanner(System.in);
            String ip = scanner.nextLine();

            Socket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName(ip);
            byte[] incomingData = new byte[1024];
            String sentence = "Hello from " + IPAddress.toString() + "!";
            byte[] data = sentence.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, 25565);

            Random random = new Random();
            while (true)
            {
                Socket.send(sendPacket);
                System.out.println("Message sent from client\n");
                DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                Socket.setSoTimeout(2000);
                Socket.receive(incomingPacket);

                String response = new String(incomingPacket.getData());
                System.out.println("Response from server:\n" + response);

                System.out.println("Waiting for next ping...");
                int randInt = random.nextInt(31);
                TimeUnit.SECONDS.sleep(randInt);
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
            System.out.println("Server has failed, creating new server...");
            Socket.close();
        }

        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        finally
        {
            UDPServer server = new UDPServer();
            server.createAndListenSocket();
        }
    }

    public static void main(String[] args) 
    {
        UDPClient client = new UDPClient();
        client.createAndListenSocket();
    }
}
