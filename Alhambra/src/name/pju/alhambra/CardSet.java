package name.pju.alhambra;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Basic class that implements a collection of cards
 * @author paulu
 *
 */
public class CardSet  {
	/**
	 * Card storage collection
	 */
	private ArrayList<Card> cards = new ArrayList<Card>();
	public ArrayList<Card> getCards() {
		return cards;
	}
	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}
	public void addCard(Card c) {
		cards.add(c);
	}
	public CardSet() {}
	/**
	 * Initialize a set of cards from another set of cards.
	 * This is effectively a copy constructor
	 * @param s CardSet to copy
	 */
	public CardSet(CardSet s) {
		this(s.cards);
	}
	/**
	 * Initialize a set of cards from any kind of collection of cards
	 * @param s a collection of cards
	 */
	public CardSet(Collection<Card> s) {
		cards = new ArrayList<Card>(s);
	}
	public boolean isEmpty() { return cards.isEmpty(); }
	/**
	 * Sum up the value of all cards.
	 * This is useful to put a simple upper bound on the buying power of a 
	 * CardSet
	 * @return the sum of all values on the cards in the set irrespective of 
	 * currency color
	 */
	public int totalWorth() {
		int val = 0;
		for (Card c : cards) {
			val += c.value();
		}
		return val;
	}
	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("cards", cards.toArray(new Card[cards.size()])).
				toString();

	}	

}
