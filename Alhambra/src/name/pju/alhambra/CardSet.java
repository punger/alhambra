package name.pju.alhambra;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Basic class that implements a collection of cards
 * @author paulu
 *
 */
public class CardSet  {
	/**
	 * Card storage collection
	 */
	protected ArrayList<Card> cards = new ArrayList<Card>();
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
	 * @param s
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
	

}
