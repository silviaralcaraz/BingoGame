/**
 * Created by silvia on 20/02/18.
 */
import java.io.*;

public class mainServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = new Server();
        ServerCommunication game = new ServerCommunication("main", server);
        ServerCommunication listenner = new ServerCommunication("listenner", server);
        listenner.start();
        game.start();
    }
}