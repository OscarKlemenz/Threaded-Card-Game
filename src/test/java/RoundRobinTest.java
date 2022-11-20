import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/** Deals cards to players and decks
 *  checks cards have been dealt in a round-robin
 */
public class RoundRobinTest {

    private CardGame cardGameTest;

    @Before
    public void setUp() throws Exception {
        cardGameTest = new CardGame();
        // Sets a two player game
        cardGameTest.setPlayerNum(2);
        // Sets the deck of cards
        for (int i=0;i<16;i++){
            Card card = new Card(i);
            cardGameTest.addCard(card);
        }
        // Initialises the players
        cardGameTest.initialisePlayersAndDecks(cardGameTest);
    }

    @Test
    public void dealCardTest(){
        // Deals the cards to player
        cardGameTest.dealCards();
        // Checks they've been dealt in a round-robin fashion
        ArrayList<Player> players = cardGameTest.getPlayers();
        // Checks that both players were dealt card in a round-robin fashion
        assertEquals(" 0 2 4 6", players.get(0).handString());
        assertEquals(" 1 3 5 7", players.get(1).handString());
    }

}