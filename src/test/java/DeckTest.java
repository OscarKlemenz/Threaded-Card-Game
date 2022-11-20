import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/** Checks that cards are added and removed in a queue fashion
 */
public class DeckTest {

    Deck deckToTest;


    @Before
    public void setUp() throws Exception {
        deckToTest = new Deck(1);

        deckToTest.addCard(new Card (1));
        deckToTest.addCard(new Card (2));
        deckToTest.addCard(new Card (3));
        deckToTest.addCard(new Card (4));
    }


    @Test
    public void cardOrderTest() {
        assertEquals(1, deckToTest.removeCard().getCardValue());
        assertEquals(2, deckToTest.removeCard().getCardValue());
        assertEquals(3, deckToTest.removeCard().getCardValue());
        assertEquals(4, deckToTest.removeCard().getCardValue());
    }

}