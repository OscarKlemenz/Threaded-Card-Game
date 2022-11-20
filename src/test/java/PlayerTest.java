import org.junit.Test;
import static org.junit.Assert.*;

/** Tests on the player class
 *
 */
public class PlayerTest {

    CardGame testCardGame = new CardGame();

    @Test
    /** Checks player is correctly formatting their
     *  hand to a string
     */
    public void correctHandOutputTest() {
        // Creates mock player
        Player testPlayer = new Player(1, testCardGame);
        // Sets all card to the same num
        for (int i = 0; i < 4; i++) {
            testPlayer.addCard(new Card(1));
        }

        String playersTrueHand = " 1 1 1 1";
        assertEquals(playersTrueHand, testPlayer.handString());
    }

    @Test
    /** Checks if when a player has a winning hand they win
     */
    public void winningHandTest(){

        // Creates mock player
        Player testPlayer = new Player(1, testCardGame);
        // Sets all card to the same num
        for (int i = 0; i < 4; i++) {
            testPlayer.addCard(new Card(1));
        }

        assertEquals(true, testPlayer.checkIfWin());
    }

    @Test
    /** Checks a player can win even without their preferred number
     */
    public void wrongNumberWinningHandTest(){
        Player testPlayer2 = new Player(2, testCardGame);
        // Sets all card to the same num
        for (int i = 0; i < 4; i++) {
            testPlayer2.addCard(new Card(6));
        }
        assertEquals(true, testPlayer2.checkIfWin());
    }

    @Test
    /** Checks a player can draw a card
     */
    public void drawCardTest(){
        Card testCard = new Card(1);
        Deck testDeck = new Deck(1);
        testDeck.addCard(testCard);

        Player testPlayer3 = new Player(3, testCardGame);
        // Draw deck set
        testPlayer3.setDeckIn(testDeck);
        Card drawnCard = testPlayer3.drawCard();

        assertEquals(testCard, drawnCard);
    }

    @Test
    /** Checks a player can discard a card
     */
    public void discardCardTest(){
        Card testCard = new Card(1);
        Deck testDeck = new Deck(1);

        Player testPlayer3 = new Player(3, testCardGame);
        testPlayer3.addCard(testCard);
        // Discard deck set
        testPlayer3.setDeckOut(testDeck);
        testPlayer3.removeCard();

        Card discardedCard = testDeck.removeCard();

        assertEquals(testCard, discardedCard);

    }

    @Test
    /** Checks a player removes non-preferred cards
     */
    public void removeNonPreferredCardTest(){

        Player testPlayer4 = new Player(4, testCardGame);

        //Checks the correct card is removed in a deck
        // Gives player 3 preferred cards
        for (int i=0; i<3;i++){
            Card newCard = new Card(4);
            testPlayer4.addCard(newCard);
        }
        // And one card that is not favoured
        Card badCard = new Card(6);
        testPlayer4.addCard(badCard);
        // Players discard deck is set
        Deck testDeckOut = new Deck(1);
        testPlayer4.setDeckOut(testDeckOut);
        // Player chooses which card to remoe
        testPlayer4.removeCard();
        // Checks if the card the player placed in the out deck is the non-preferred one
        assertEquals(6, testDeckOut.removeCard().getCardValue());
    }

    @Test
    /** Checks a player can properly take a turn
     */
    public void nonWinningTurnTest(){

        // Creates player whose turn will be checked
        Player testPlayer5 = new Player(5, testCardGame);

        // Creates players in and out decks
        Deck inDeck = new Deck(1);
        Deck outDeck = new Deck(2);

        // Gives player and decks cards
        for(int i=1;i<5;i++){
            testPlayer5.addCard(new Card(i));
        }
        inDeck.addCard(new Card(5));

        // Decks are assigned to player
        testPlayer5.setDeckIn(inDeck);
        testPlayer5.setDeckOut(outDeck);
        // Player takes a turn
        testPlayer5.turn();
        // This is then validated by checking deckOuts contents
        assertEquals(1, outDeck.removeCard().getCardValue());
    }

    @Test
    /** Checks that a player can take a turn and recognise they've won
     */
    public void winningTurnTest() {
        // Creates player whose turn will be checked
        Player testPlayer6 = new Player(6, testCardGame);

        // Creates players in and out decks
        Deck inDeck = new Deck(1);
        Deck outDeck = new Deck(2);

        // Gives player and decks cards
        // Player is one 6 off of winning the game
        for(int i=0;i<3;i++){
            testPlayer6.addCard(new Card(6));
        }
        testPlayer6.addCard(new Card(5));
        // Deck contains the last 6 the player needs
        inDeck.addCard(new Card(6));

        // Decks are assigned to player
        testPlayer6.setDeckIn(inDeck);
        testPlayer6.setDeckOut(outDeck);
        // Player takes a turn
        testPlayer6.turn();

        // Checks player has notified the system of the win
        assertEquals(true, testCardGame.getWin());
        assertEquals(6, testCardGame.getWinner());
        assertEquals(" 6 6 6 6", testPlayer6.handString());
    }

    @Test
    /** Checks that the order of the players cards doesn't stagnate
     */
    public void cardsShuffledTest(){
        // Creates player whose turn will be checked
        Player testPlayer7 = new Player(7, testCardGame);

        // Creates players in and out decks
        Deck inDeck = new Deck(1);
        Deck outDeck = new Deck(2);

        // Gives player and decks cards
        // Players hand is 0 1 2 3
        for(int i=0;i<4;i++){
            testPlayer7.addCard(new Card(i));
        }
        // Upon the first turn the player will remove the first non equal
        // Card before shuffling
        inDeck.addCard(new Card(0));

        // Decks are assigned to player
        testPlayer7.setDeckIn(inDeck);
        testPlayer7.setDeckOut(outDeck);
        // Player takes a turn
        testPlayer7.turn();

        assertNotEquals(" 0 1 2 3", testPlayer7.handString());
    }

}