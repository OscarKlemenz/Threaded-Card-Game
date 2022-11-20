import java.io.*;
import java.util.concurrent.LinkedBlockingQueue;

/** Deck class holds cards that players can take and discard from
 */
public class Deck {

    // Holds cards that players take and add to, in a queue structure so that the head and tail
    // are easily identified.
    LinkedBlockingQueue<Card> queue = new LinkedBlockingQueue<>();
    // Unique identifier for each deck
    private final int deckNum;

    /** Constructor
     *
     * @param deckNum   The unique identifier for each deck
     */
    public Deck(int deckNum){
        this.deckNum = deckNum;
    }

    /** Getter
     *
     * @return deckNum  Returns unique identifier of deck
     */
    public int getDeckNum() { return deckNum; }

    /** Adds a card to the back of the deck
     *
     * @param newCard   Card to be added to the deck
     */
    public void addCard(Card newCard){ queue.add(newCard); }

    /** Removes the top card from the deck
     *
     * @return topCard  The card from the top of the deck, which the player takes
     */
    public Card removeCard(){
        try {
            return queue.take();
        } catch (InterruptedException e) {
            // This occurs if the player is waiting and an interruption is thrown
            // meaning a player has won the game, in this instance nothing needs
            // to be done
        }
        return null;
    }

    /** Once the game is over, each deck will write its contents to their output file
     */
    public void writeFinalDeck() {
        // Initialises the string to be output and the name of the file to write to
        StringBuilder outputString = new StringBuilder("deck" + deckNum + " contents");
        String filename =  "deck" + deckNum + "_output.txt";
        // Loops through the queue and adds it to the output string
        for (Card card : queue) {
            outputString.append(" ").append(card.getCardValue());
        }

        // Output string is then written to the file
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
}
