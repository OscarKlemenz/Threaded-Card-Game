import org.junit.Test;
import static org.junit.Assert.*;

public class CardGameTest {

    @Test
    /** Uses a pre-made valid text file to check
     *  no errors in setting up file
     */
    public void validPackFileTest(){
        CardGame cardGameTest = new CardGame();

        cardGameTest.setPlayerNum(2);

        cardGameTest.setTextFile("testValidPackFile.txt");
        // If filename has been set this will be true
        assertEquals(true, cardGameTest.getFilenameSet());
    }

    @Test
    /** Pack contains a negative number, test used to check
     *  cardGame notices
     */
    public void nonNegativePackFileTest(){
        CardGame cardGameTest = new CardGame();

        cardGameTest.setPlayerNum(2);

        cardGameTest.setTextFile("nonNegativePackFileTest.txt");

        // If filename has not been set this will be false
        assertEquals(false, cardGameTest.getFilenameSet());

    }

    @Test
    /** Pack contains an empty space, test used to check
     *  cardGame notices
     */
    public void emptySpacePackFileTest(){
        CardGame cardGameTest = new CardGame();

        cardGameTest.setPlayerNum(2);

        cardGameTest.setTextFile("emptySpacePackFileTest.txt");

        // If filename has not been set this will be false
        assertEquals(false, cardGameTest.getFilenameSet());

    }

    @Test
    /** Too many player for the card game to work,
     *  checks that cardGame notices
     *
     */
    public void wrongPlayerNumberTest(){
        CardGame cardGameTest = new CardGame();

        cardGameTest.setPlayerNum(3);

        cardGameTest.setTextFile("testValidPackFile.txt");

        // If filename has not been set this will be false
        assertEquals(false, cardGameTest.getFilenameSet());
    }

    @Test
    /** Checks that the initial win checker is functioning,
     *  this test pack has it guaranteed that player one will
     *  win
     */
    public void initialWinTest() {

        CardGame cardGameTest = new CardGame();
        cardGameTest.setPlayerNum(2);
        cardGameTest.setTextFile("initialWinTest.txt");

        // Creates players and decks
        cardGameTest.initialisePlayersAndDecks(cardGameTest);
        // Deals the cards to players and decks
        cardGameTest.dealCards();
        // Assigns players draw and discard deck
        cardGameTest.assignDecks();
        // check initial win
        cardGameTest.checkInitialWin();

        // Validates that player 1 has won and the card game knows it
        assertEquals(1, cardGameTest.getWinner());
        assertEquals(true, cardGameTest.getWin());

    }

    @Test
    /** Starts game and then waits for threads to finish game,
     *  validates that the threads are functioning
     */
    public void threadedWinTest() {
        CardGame cardGameTest = new CardGame();
        cardGameTest.setPlayerNum(2);
        cardGameTest.setTextFile("testValidPackFile.txt");

        // Creates players and decks
        cardGameTest.initialisePlayersAndDecks(cardGameTest);
        // Deals the cards to players and decks
        cardGameTest.dealCards();
        // Assigns players draw and discard deck
        cardGameTest.assignDecks();
        // check initial win
        cardGameTest.checkInitialWin();
        cardGameTest.playGame();
        // Waits until threading is finished
        while(!cardGameTest.getWin()){
            // Do nothing
        }
        // Validates that player 2 has won and the card game knows it
        assertEquals(2, cardGameTest.getWinner());
        assertEquals(true, cardGameTest.getWin());
    }
}