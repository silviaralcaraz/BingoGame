/**
 * Created by silvia on 25/02/18.
 */
import java.io.*;
import java.net.*;

public class Connect {
    public static int PORT = 2345;
    public static String IP = "224.0.0.100";

    /* Method to init multicast communication */
    public MulticastSocket makeConnection() throws IOException {
        InetAddress group = InetAddress.getByName(IP);
        MulticastSocket socket = new MulticastSocket(PORT);
        socket.joinGroup(group);
        return socket;
    }

    /* Method to end multicast communication */
    public void closeConnection(MulticastSocket socket) throws IOException{
        InetAddress group = InetAddress.getByName(IP);
        socket.leaveGroup(group);
        if(socket!=null) {
            socket.close();
        }
    }
}