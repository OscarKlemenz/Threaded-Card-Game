import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/** Threaded player class, this class represents each player in the game.
 */
public class Player extends Thread {

    // Attributes
    // Holds the players current cards
    private List<Card> cards = new ArrayList<Card>();
    // Current game is used to notify the game when a player wins
    private final CardGame currentGame;
    // Name of the file players will write outputs to
    private final String filename;
    // Player num identifies each player
    private final int playerNum;
    // The deck a player takes cards from
    private Deck deckIn;
    // The deck a player discards cards to
    private Deck deckOut;
    // Has won is used so that a unique output can be specified if a player wins
    private Boolean hasWon = false;

    /** Constructor
     *
     * @param playerNum     Players assigned number, also their preferred number
     */
    public Player(int playerNum, CardGame currentGame){
        this.playerNum = playerNum;
        filename = "player" + playerNum + "_output.txt";
        this.currentGame = currentGame;
    }


    /** Gets the number of the player
     * @return playerNum
     */
    public int getPlayerNum() { return playerNum; }

    /** Sets the deck that the player will take cards from
     * @param playersInDeck     Draw deck
     */
    public void setDeckIn(Deck playersInDeck){deckIn = playersInDeck;}

    /** Sets the deck that the player will discard cards to
     * @param playersOutDeck    Discard deck
     */
    public void setDeckOut(Deck playersOutDeck){deckOut = playersOutDeck; }

    /** Sets the boolean describing if the player has won
     * @param hasWon    If the player has won or not
     */
    public void setHasWon(Boolean hasWon) {this.hasWon = hasWon;}

    /** Draws a card from the deck that the player draws cards from, this
     *  is then written to the output text file.
     * @return newCard  Card that the player has just drawn
     * @throws NullPointerException Threads that are still playing as the game comes to an
     * end may try and access cards that don't exist, throwable prevents console errors
     */
    public Card drawCard() throws NullPointerException {

        Card newCard = deckIn.removeCard();
        writeDraw(newCard.getCardValue());
        return newCard;

    }

    /** Adds a card to the players hand.
     * @param newCard   Card to add to the hand.
     */
    public void addCard(Card newCard) {
        cards.add(newCard);
    }

    /** Removes a card from the players hand, only removes cards that aren't
     *  their preferred card. After cards are randomised so that the card in
     *  the same index isn't always removed
     */
    public void removeCard(){

        Card cardToRemove = null;

        for(int i = 0; i < 4; i++){
            if( cards.get(i).getCardValue() != playerNum) {

                cardToRemove = cards.get(i); //to come back to when have logic
                cards.remove(i);
                deckOut.addCard(cardToRemove);

                break;
            }
        }
        Collections.shuffle(cards);
        // Writes to file when removed
        writeDiscard(cardToRemove.getCardValue());
    }

    /** Checks if the player has won the game
     *
     * @return Boolean  True if the player has won, false if not.
     */
    public Boolean checkIfWin(){
        List<Integer> valuesToCheck = new ArrayList<>();
        for(int i=0; i<4;i++){
            valuesToCheck.add(cards.get(i).getCardValue());
        }

        int cardToCompare = valuesToCheck.get(0);
        boolean allSame = true;

        for(int i=1; i<4; i++){
            if (cardToCompare != valuesToCheck.get(i)) {
                allSame = false;
                break;
            }
        }

        return allSame;
    }

    /** Creates a text string containing the numbers currently in the
     *  players hand.
     * @return String   Values in the players hand.
     */
    public String handString(){
        StringBuilder outputString = new StringBuilder();
        for(Card card : cards) {
            outputString.append(" ").append(card.getCardValue());
        }
        return outputString.toString();
    }

    /** When the game starts the cards the player is initially holding are written
     *  to the output file.
     */
    public void writeInitialHand(){

        String outputString = "player " + playerNum + " initial hand";
        outputString += handString();

        try {
            PrintWriter writer = new PrintWriter(filename, "UTF-8");
            writer.println(outputString);
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not find file!");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e);
        }
    }

    /** When a player draws a card this information is written to the output file.
     * @param cardNum   The number of the card the player has just drawn.
     */
    public void writeDraw(int cardNum) {

        String outputString = "player " + playerNum + " draws a " + cardNum + " from deck " + deckIn.getDeckNum();
        try(FileWriter fileWriter = new FileWriter(filename, true);
            PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(outputString);
        } catch (IOException e){
            System.out.println(e);
        }
    }

    /** When a player discards a card this information is written to the output file.
     * @param cardNum   The number of the card the player has discarded.
     */
    public void writeDiscard(int cardNum) {

        String outputString = "player " + playerNum + " discards a " + cardNum + " to deck " + deckOut.getDeckNum();
        try(FileWriter fileWriter = new FileWriter(filename, true);
            PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(outputString);
        } catch (IOException e){
            System.out.println(e);
        }
    }

    /** Outputs the players current hand to the output file
     */
    public void writeCurrentHand() {

        String outputString = "player " + playerNum + " current hand";
        outputString += handString();

        try(FileWriter fileWriter = new FileWriter(filename, true);
            PrintWriter printWriter = new PrintWriter(fileWriter);) {
            printWriter.println(outputString);
        } catch (IOException e){
            System.out.println(e);
        }
    }

    /** Once one player has one each player writes their exit lines to the output file.
     *  If the player is the winner they have a different text output to other players.
     */
    public void writeExit(){
        // If player has won do specific thing
        String winStatus;
        if (hasWon) {
            winStatus = "player " + playerNum + " wins";
        } else {
            // If player has not one the informs line is created
            int winner = currentGame.getWinner();
            winStatus = "player " + winner + " has informed player " + playerNum + " that player " + winner +
                    " has won";
        }

        try(FileWriter fileWriter = new FileWriter(filename, true);
            PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(winStatus);

            // Exit line is written
            printWriter.println("player " + playerNum + " exits");

            // Hand line is written
            String outputString = "player " + playerNum + " hand:";
            outputString += handString();
            printWriter.println(outputString);

        } catch (IOException e){
            System.out.println(e);
        }
    }

    /** Threaded method, this is where players carry out their turns. This involves
     *  drawing a card, removing one and checking if they have one. If so they will
     *  notify the rest of the threads.
     */
    public synchronized void turn(){

        try {
            // Draws card and writes draw to output
            Card drawnCard = drawCard();
            // Doesn't check win until removes card, mark new card as new
            // Removes card and writes to file
            removeCard();
            // Add card to hand
            addCard(drawnCard);
            // Write hand to file
            writeCurrentHand();
            // Check if win
            if (checkIfWin()) {
                hasWon = true;
                // Notifies other players that there is a winner.
                currentGame.setWin();
                // Tells card game who the winner is.
                currentGame.setWinner(playerNum);
                // Ends the game.
                currentGame.endGame();
            }
        } catch (NullPointerException e) {
            // Do nothing
        }
    }

    /** Threaded method for players taking turns, while the game is not won
     *  players will continuously take turns.
     */
    @Override
    public void run() {
        while(!currentGame.getWin()) {
            turn();
        }
    }
}
