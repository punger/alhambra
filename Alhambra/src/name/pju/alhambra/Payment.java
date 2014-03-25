package name.pju.alhambra;

import java.io.Serializable;

public class Payment extends CardSet implements Serializable {

	private static final long serialVersionUID = 1L;
	public Payment() {
		super();
	}
	public Payment(CardSet cs) {
		cards = cs.cards;
	}
	boolean couldAddCard(Card c) {
		if (cards.isEmpty()) return true;
		MarketColor m = totalColor();
		if (m == null) return false;
		return m.equals(c.getColor());
	}
	MarketColor totalColor() {
		if (cards.isEmpty()) return null;
		MarketColor m = null;
		for (Card c : cards) {
			if (m == null) m = c.getColor();
			else if (m != c.getColor()) return null;
		}
		return m;
	}
	/**
	 * If the cards are all of a compatible market color
	 * @return true if the cards are all of one color; false otherwise
	 */
	boolean oneColor() {
		if (cards.size() <= 1) return true;
		return totalColor() != null;
	}
	int value() {
		if (!oneColor()) return -1;
		int val = 0;
		for (Card c : cards) {
			val += c.value();
		}
		return val;
	}
	

}
