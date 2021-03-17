package Networking;

import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class UDPClient 
{
    DatagramSocket Socket;

    public UDPClient() 
    {

    }

    public void createAndListenSocket() 
    {
        try 
        {
            Socket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");
            int port = 25565;
            ping(Socket, IPAddress, port);
        }
        catch (UnknownHostException e) 
        {
            e.printStackTrace();
        } 
        catch (SocketException e) 
        {
            e.printStackTrace();
        }
    }

    void ping (DatagramSocket socket, InetAddress ip, int port)
    {
        Random rand = new Random();
        String ping = "ping";
        byte[] data = ping.getBytes();
        byte[] incomingData = new byte[1024];
        while (true)
        {
            try
            {
                int randInt = rand.nextInt(31);
                TimeUnit.SECONDS.sleep(randInt);
                DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
                Socket.send(packet);
                System.out.println("Packet sent");
                boolean flag = false;
                while (!flag)
                {
                    DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
                    Socket.receive(incomingPacket);
                    String response = new String(incomingPacket.getData());
                    System.out.println("Available clients: " + response);
                    flag = true;
                }
            }

            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

        }
    }


    public static void main(String[] args) 
    {
        UDPClient client = new UDPClient();
        client.createAndListenSocket();
    }
}
