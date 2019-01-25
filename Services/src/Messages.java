/**
 * Created by silvia on 25/02/18.
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Messages {
    public static int PORT = 2345;
    public static String IP = "224.0.0.100";

    /*  INFORMATION ABOUT MESSAGE IDENTIFIERS
    *       - 1 : server sends this message to share bingo numbers.
    *       - 2 : client says bingo and sends his bingo card.
    *       - 3 : server indicates that one client is the winner and shares winner's bingo card.
    * */

    public void sendMessage(MulticastSocket socket, byte[] message) {
        try {
            InetAddress group = InetAddress.getByName(IP);
            DatagramPacket messageOut = new DatagramPacket(message, message.length, group, PORT);
            socket.send(messageOut);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] receiveMessage(MulticastSocket socket) {
        byte[] message = new byte[0];
        try {
            byte[] buffer = new byte[1000];
            DatagramPacket messageIn = new DatagramPacket(buffer, buffer.length);
            socket.receive(messageIn);
            message = messageIn.getData();
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
        return message;
    }

    public byte[] makeBingoMessage(int identifier, ArrayList bingoCard) {
        byte[] bingoNumbers = {(byte) ((int) identifier), 0, 0, 0, 0, 0, 0};
        for (int i = 0; i < bingoCard.size(); i++) {
            bingoNumbers[i + 1] = (byte) ((int) bingoCard.get(i));
        }
        return bingoNumbers;
    }
}