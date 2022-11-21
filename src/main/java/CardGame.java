import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/** Executable class, used to run the card game
 */
public class CardGame {

    // The number of players playing the game
    private int playerNum;
    // Checks the file name has been set correctly
    private Boolean filenameSet = false;
    // List of cards being played in the game
    private ArrayList<Card> cards = new ArrayList<Card>();
    // List of players in the game
    private ArrayList<Player> players = new ArrayList<Player>();
    // List of decks in the game
    private ArrayList<Deck> decks = new ArrayList<Deck>();
    // Win is true when the game has been won
    private Boolean win = false;
    // Exit started tells the game that a player has won, so no other player
    // can call the exit methods
    private Boolean exitStarted = false;
    // Player number of the winning player
    private int winner;

    /** Asks user for the number of players, and performs checks on the input
     */
    public int askPlayerNum(){
        // Will continuously ask the user for a player number until the input is a non-negative integer
        int checkPlayerNum = -1;
        while (checkPlayerNum <= 1) {
            // Scans the terminal
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object
            System.out.println("Please enter the number of players:");
            String str = myObj.nextLine();  // Read user input
            try {
                // Turns string into an int
                checkPlayerNum = Integer.parseInt(str);
                // Checks the player number is non-negative
                if(checkPlayerNum <= 0){
                    throw new InvalidPlayersException("Player number must be a non-negative integer!");
                }
                if(checkPlayerNum == 1){
                    throw new InvalidPlayersException("Number of players must be more than one!");
                }
            } catch (InvalidPlayersException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Number of players must be a number!");
            }
        }
        return checkPlayerNum;
    }

    /** Adds additional card to the center pile
     *
     * @param card  Card to add to the center pil
     */
    public void addCard(Card card) {cards.add(card);}

    /** Sets the number of players in the game
     */
    public void setPlayerNum(int playerNum){this.playerNum=playerNum;}

    /** Gets the user's console input for the filename
     */
    public String getFilename(){

        // Gets the filename for the game
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Please enter the location of the pack to load:");

        return myObj.nextLine();
    }

    /** Getters
     * @return filenameSet  Whether the filename has been set
     */
    public Boolean getFilenameSet() { return filenameSet;}

    /** Checks the filename is correct and
     *  the number of values is 8n the number of players.
     */
    public void setTextFile(String filename) {

        try {
            File fileToRead = new File(filename);Scanner myReader = new Scanner(fileToRead);
            while (myReader.hasNextLine()) {
                //With the data, append it to a new list of integers
                // Checks each row is a non-negative integer
                // Throws an exception if not
                int intValue = Integer.parseInt(myReader.nextLine());
                // Checks number is non-negative
                if(intValue <= 0) {
                    throw new NumberFormatException();
                } else {
                    // Creates a card class
                    Card newCard = new Card(intValue);
                    // Adds the newly created to the center deck
                    cards.add(newCard);
                }
            }
            // Check there is the right amount of card numbers
            if (8 * playerNum == cards.size()) {
                filenameSet = true;
            } else {
                throw new FileLengthException("File length is not 8n of players n!");
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File does not exist!");
        } catch (NumberFormatException e) {
            System.out.println("File contains a line with a value that is not a non-negative integer!");
        } catch (FileLengthException e) {
            System.out.println(e);
        }
    }


    /** Setter, boolean says if the game has been won or not
     */
    public void setWin() {
        win = true;
    }

    /** Setter
     *
     * @param winner    The player number of the player that has won
     */
    public void setWinner(int winner) {
        this.winner = winner;
    }

    /** Getter
     *
     * @return win  Boolean, if a player has won or not
     */
    public Boolean getWin(){
        return win;
    }

    /** Getter
     *
     * @return winner   The player number of the player that has won
     */
    public int getWinner() {
        return winner;
    }

    /** Initialises players and decks, for the number of players entered
     *
     * @param currentGame   Passed in to players, so they can notify card game when
     *                      they have won
     */
    public void initialisePlayersAndDecks(CardGame currentGame){
        for (int i = 1; i <= playerNum ; i++) {

            Player newPlayer = new Player(i, currentGame);
            players.add(newPlayer);

            Deck newDeck = new Deck(i);
            decks.add(newDeck);
        }
    }

    /** Getter
     *
     * @return players  List of players playing the game
     */
    public ArrayList<Player> getPlayers() {return players;}

    /** Getter
     *
     * @return decks    List of decks in the game
     */
    public ArrayList<Deck> getDecks() {return decks;}

    /** Deals cards to each individual player and deck
     */
    public void dealCards() {
        // Deals to players first
        // Loops through all players 4 times, dealing them all cards
        for(int i=0 ; i<4; i++){
            for (Player player : players) {
                // Deals a card to the player
                Card cardToAdd = cards.get(0);
                cards.remove(0);
                player.addCard(cardToAdd);
            }
        }
        // Then deals to decks
        // Loops through decks dealing the rest of the cards
        for(int i=0 ; i<4; i++){
            for (Deck deck : decks) {
                // Deals a card to the deck
                Card cardToAdd = cards.get(0);
                cards.remove(0);
                deck.addCard(cardToAdd);
            }
        }
    }

    /** Assigns the decks that the players draw and discard cards from,
     *  and then writes their initial hand to their output file
     */
    public void assignDecks(){
        // Assigns the deck that the player takes from
        for(int i=0; i<playerNum; i++) {
            players.get(i).setDeckIn(decks.get(i));
            if (i < (playerNum - 1)) {
                players.get(i).setDeckOut(decks.get(i+1));
            } else {
                players.get(i).setDeckOut(decks.get(0));
            }
            // Output file is written to
            players.get(i).writeInitialHand();
        }
    }

    /** When the game starts all players are sequentially checked to
     *  see if any player has been dealt a winning hand.
     *  If two players get winning hands initially, the winning player,
     *  will always be the one with the lowest playerNum
     *
     * @return initialWin   True if a player has won, false if not
     *                      notifies the main method if it should start threading
     */
    public boolean checkInitialWin(){

        boolean initialWin = false;
        // Loops through all players
        for (Player player : players) {
            if(player.checkIfWin()) {
                // If a player has won, write their values
                win = true;
                winner = player.getPlayerNum();
                player.setHasWon(true);
                writeExits();
                return true;
            }
        }
        return initialWin;
    }


    /** Starts all the player threads who then start playing the game
     */
    public void playGame(){
        // Start all the player threads
        for(Player player : players) {
            player.start();
        }
    }

    /** endGame occurs once, it interrupts all player threads and calls
     *  the method to write the exit outputs
     */
    public void endGame(){
        // Prevents endGame occurring twice
        if(!exitStarted) {
            exitStarted = true;
            // Once a player has one all threads are stopped
            for (Player player : players) {
                player.interrupt();
            }
            writeExits();
        }
    }

    /** Generates all the final file outputs before outputting
     *  the winner to console
     */
    public void writeExits(){

        // Writes the exit outputs for cards and players
        for (int i=0; i < playerNum ; i++) {
            players.get(i).writeExit();
            decks.get(i).writeFinalDeck();
        }
        // Writes the winner to the terminal
        System.out.println("player " + winner + " wins");
    }

    /** Contains game initialisation and starting the card game
     * @param args
     */
    public static void main(String[] args) {

        CardGame cardGame = new CardGame();
        // Gets the player number and filename
        cardGame.setPlayerNum(cardGame.askPlayerNum());
        while(!cardGame.getFilenameSet()) {
            String filename = cardGame.getFilename();
            cardGame.setTextFile(filename);
        }
        // Creates players and decks
        cardGame.initialisePlayersAndDecks(cardGame);
        // Deals the cards to players and decks
        cardGame.dealCards();
        // Assigns players draw and discard deck
        cardGame.assignDecks();
        // check initial win
        if(!cardGame.checkInitialWin()) {
            // Start card game
            cardGame.playGame();
        }
    }

    public static class InvalidPlayersException extends Throwable {
        /**
         * Thrown when attempting to assign player number to a non-negative integer
         */
        public InvalidPlayersException(String message) {super(message);}
    }

    public static class FileLengthException extends Throwable {
        /**
         * Thrown when attempting to use a file with a length that is not 8n
         * of the number of players n.
         */
        public FileLengthException(String message) {super(message);}}
}
