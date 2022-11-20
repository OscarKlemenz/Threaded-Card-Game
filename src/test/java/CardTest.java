import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/** Small check to make sure card is functioning correctly
 */
public class CardTest {

    private Card cardToTest;

    @Before
    public void setUp() throws Exception {
        cardToTest = new Card(1);
    }

    @Test
    public void getCardValue() {
        assertEquals(1, cardToTest.getCardValue());
    }
}