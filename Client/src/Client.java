/**
 * Created by silvia on 20/02/18.
 */
import java.io.IOException;
import java.net.MulticastSocket;
import java.util.ArrayList;

public class Client {
    private ArrayList bingoCard;
    private Boolean winner;
    private Connect connection;
    private ArrayList usedNumbers;
    private MulticastSocket clientSocket;
    public static boolean finish = false;

    //Builders
    public Client() {
        this.bingoCard = new ArrayList();
        this.setUsedNumbers(new ArrayList());
        this.setWinner(false);
        this.setConnection(new Connect());
        this.clientSocket = null;

        this.generateRandomBingoCard();
    }

    //Getters & Setters
    public ArrayList getBingoCard() {
        return bingoCard;
    }

    public void setBingoCard(ArrayList bingoCard) {
        if (this.bingoCard != null)
            this.bingoCard = bingoCard;
        else
            this.bingoCard = new ArrayList();
    }

    public ArrayList getUsedNumbers() {
        return usedNumbers;
    }

    public void setUsedNumbers(ArrayList usedNumbers) {
        this.usedNumbers = usedNumbers;
    }

    public Boolean getWinner() {
        return winner;
    }

    public void setWinner(Boolean winner) {
        this.winner = winner;
    }

    public Connect getConnection() {
        return connection;
    }

    public void setConnection(Connect connection) {
        this.connection = connection;
    }

    public MulticastSocket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(MulticastSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    //Methods
    public void generateRandomBingoCard() {
        Randomizer randomizer = new Randomizer();
        for (int i = 0; i < 6; i++) {
            randomizer.generateRandomNumber(this.getBingoCard());
        }
        this.setUsedNumbers((ArrayList) this.getBingoCard().clone());
    }

    public void checkNumbers(Integer number) {
        for (int i = 0; i < this.getBingoCard().size(); i++) {
            if (number.equals(this.getBingoCard().get(i))) {
                this.getUsedNumbers().set(i, 0); // 0 indicates a marked number
                System.out.println("Marked number " + number);
                System.out.println("Bingo card status: " + this.getUsedNumbers());
                System.out.println();
                isWinner(getUsedNumbers());
                break;
            }
        }
    }

    public Boolean isWinner(ArrayList usedNumbers) {
        Integer mark = 0;
        for (int i = 0; i < usedNumbers.size(); i++) {
            if (usedNumbers.get(i) != mark) {
                return false;
            }
        }
        return true;
    }

    public Boolean checkWinner(byte[] bingoMessage) {
        System.out.println("Checking winner's bingo card...");
        for (int i = 1; i < 7; i++) {
            if (!this.getBingoCard().contains((int) bingoMessage[i])) {
                return false;
            }
        }
        return true;
    }

    public void playBingo() {
        try {
            this.setClientSocket(this.getConnection().makeConnection()); //making connection
            Messages message = new Messages();
            System.out.println("My bingo card: " + this.getBingoCard()); //printing bingo card
            System.out.println("I'm waiting for the server...\n");
            while (!finish) { // while the game doesn't ends
                byte[] buffer = message.receiveMessage(this.getClientSocket()); //receiving server's message
                if (buffer[0] == 1) { //if identifier is 1... (look identifier's mean in Messages.java)
                    this.checkNumbers((int) buffer[1]); //checking if bingo number is in our bingo card
                    if (this.isWinner(this.getUsedNumbers())) { //checking if this client is the winner
                        System.out.println("BINGO!"); //if client wins he say bingo and change his attribute "winner"
                        this.setWinner(true);
                        byte[] buf = message.makeBingoMessage(2, this.getBingoCard()); //making message (identifier + bingo card)
                        message.sendMessage(this.getClientSocket(), buf); //sending bingo card to server
                        System.out.println("Waiting for results...\n");
                    }
                }
                else if(buffer[0] == 3){
                    System.out.print("Bingo finished. ");
                    this.setWinner(checkWinner(buffer)); // client checks if his bingo card is equal than winner bingo card
                    if (this.getWinner() && this.isWinner(this.getUsedNumbers())) {
                        System.out.println("I'm the winner!");
                    }else{
                        System.out.println("I lost... I should try again.");
                    }
                    finish = true; // game ends
                    this.connection.closeConnection(this.getClientSocket());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
    }
}