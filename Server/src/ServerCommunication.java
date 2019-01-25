/**
 * Created by silvia on 25/02/18.
 */
public class ServerCommunication extends Thread {
    private String type;
    private Server server;

    public ServerCommunication(String type, Server server){
        this.type = type;
        this.server = server;
    }

    public ServerCommunication(){
        this.type = new String();
        this.server = new Server();
    }

    @Override
    public void run() {
        if (this.type == "main") {
            this.server.startGame();
        } else if (this.type == "listenner") {
            this.server.listenClients();
        } else {
            System.out.println("This type of server doesn't exist.");
        }
    }
}