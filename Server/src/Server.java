/**
 * Created by silvia on 20/02/18.
 */
import java.io.IOException;
import java.net.MulticastSocket;
import java.util.ArrayList;

public class Server {
    private ArrayList generatedNumbers;
    private Boolean findWinner;
    private Connect connection;
    private ArrayList winnerNumbers;

    //Builders
    public Server() {
        this.setWinner(false);
        this.generatedNumbers = new ArrayList();
        this.connection = new Connect();
        this.setWinnerNumbers(new ArrayList());
    }

    //Getters & Setters
    public ArrayList getGeneratedNumbers() {
        return generatedNumbers;
    }

    public void setGeneratedNumbers(ArrayList generatedNumbers) {
        this.generatedNumbers = generatedNumbers;
    }

    public Boolean getWinner() {
        return findWinner;
    }

    public void setWinner(Boolean winner) {
        this.findWinner = winner;
    }

    public Connect getConnection() {
        return connection;
    }

    public void setConnection(Connect connection) {
        this.connection = connection;
    }

    public ArrayList getWinnerNumbers() {
        return winnerNumbers;
    }

    public void setWinnerNumbers(ArrayList winnerNumbers) {
        this.winnerNumbers = winnerNumbers;
    }

    //Methods
    public void startGame() {
        try {
            System.out.println("BINGO GAME STARTS\n");
            MulticastSocket socket = this.connection.makeConnection(); //making connection
            Messages message = new Messages();
            Randomizer randomizer = new Randomizer();
            int count = 0;
            while (!findWinner && count < 49) {
                randomizer.generateRandomNumber(this.getGeneratedNumbers()); // generating random number
                System.out.println("Generated number[" + count + "]: " + this.getGeneratedNumbers().get(count));
                byte[] bingoNumber = {(byte) ((int) 1), (byte) ((int) this.getGeneratedNumbers().get(count))}; // making message (identifier + random number)
                message.sendMessage(socket, bingoNumber); //sending random number
                count++;
                Thread.sleep(1000); //waiting 1 second
            }
            this.connection.closeConnection(socket);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void listenClients() {
        try {
            MulticastSocket socket = this.connection.makeConnection(); //making connection
            while (!findWinner) {
                Messages message = new Messages();
                byte[] buffer = message.receiveMessage(socket); //listening clients' messages
                if (buffer[0] == 2) { // someone said bingo...
                    System.out.println("\nSomeone said bingo!");
                    if (this.checkWinner(buffer)) { //checking if bingo is real
                        findWinner = true;
                        System.out.println("Winner bingo card is: " + this.getWinnerNumbers());
                        byte[] buf = message.makeBingoMessage(3, this.getWinnerNumbers());
                        message.sendMessage(socket, buf); //sending winner's bingo card
                    }else{
                        findWinner = false;
                        System.out.println("Someones cheated.");
                    }
                }
            }
            this.connection.closeConnection(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean checkWinner(byte[] bingoMessage) {
        System.out.println("Checking winner's bingo card...");
        for (int i = 1; i < 7; i++) {
            if (!this.getGeneratedNumbers().contains((int)bingoMessage[i])) {
                return false;
            } else {
                this.getWinnerNumbers().add((int)bingoMessage[i]);
            }
        }
        return true;
    }
}