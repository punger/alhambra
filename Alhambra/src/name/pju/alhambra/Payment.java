package name.pju.alhambra;


public class Payment extends CardSet {

	public Payment() {
		super();
	}
	public Payment(CardSet cs) {
		setCards(cs.getCards());
	}
	boolean couldAddCard(Card c) {
		if (getCards().isEmpty()) return true;
		MarketColor m = totalColor();
		if (m == null) return false;
		return m.equals(c.getColor());
	}
	MarketColor totalColor() {
		if (getCards().isEmpty()) return null;
		MarketColor m = null;
		for (Card c : getCards()) {
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
		if (getCards().size() <= 1) return true;
		return totalColor() != null;
	}
	int value() {
		if (!oneColor()) return -1;
		int val = 0;
		for (Card c : getCards()) {
			val += c.value();
		}
		return val;
	}
	

}
