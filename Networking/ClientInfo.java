package Networking;

import java.net.InetAddress;
import java.util.Date;

/**
 * Client information of a single client
 * @author Carson Rohan, Lucas Steffens
 * @version 3-16-2021
 */

public class ClientInfo
{
    // set time to live to 30000ms or 30s for all objects
    private static final int TTL = 30000;

    private InetAddress IP;
    private int port;
    private Date lastSeen;


    public ClientInfo(InetAddress IP, int port)
    {
        this.IP = IP;
        this.port = port;
        this.lastSeen = new Date(System.currentTimeMillis());
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
     * Determine if the client is dead by checking if it has been greater than
     * 30 seconds since we've last seen this client
     * @return true if client is dead, false if otherwise
     */
    public boolean isDead()
    {
        Date currentTime = new Date(System.currentTimeMillis());
        return currentTime.getTime() - lastSeen.getTime() > TTL;
    }

}
