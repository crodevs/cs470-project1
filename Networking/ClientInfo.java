package Networking;

import java.net.InetAddress;

public class ClientInfo
{
    private InetAddress IP;
    private int port;

    public ClientInfo(InetAddress IP, int port)
    {
        this.IP = IP;
        this.port = port;
    }

    public void setIP(InetAddress IP)
    {
        this.IP = IP;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public InetAddress getIP()
    {
        return this.IP;
    }

    public int getPort()
    {
        return this.port;
    }

    /**
     * Determine if the client is dead
     * @return true if client is dead, false if otherwise
     */
    public boolean isDead()
    {
        return false;
    }

}
