/** Card represents each unique card a player and deck can have
 */
public class Card {

    // Value of the card, once card value is set it does not change
    private final int cardValue;

    /** Constructor
     *
     * @param value     Chosen value of the card
     */
    public Card(int value){
        cardValue = value;
    }

    /** Getter
     *
     * @return cardValue    Returns the value of the card
     */
    public int getCardValue(){
        return cardValue;
    }

    /** toString
     *
     * @return  Unique description of card
     */
    @Override
    public String toString() {
        return "Card{" + "cardValue=" + cardValue + '}'; }
}
