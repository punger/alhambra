package name.pju.alhambra;

import java.io.Serializable;
import java.util.EnumMap;


/**
 * Represents the hand 
 * @author paulu
 *
 */
public class Hand extends CardSet implements Serializable {
	private static final long serialVersionUID = 1L;

	public Hand() {
		super();
	}
	public Hand(CardSet cs) {
		cards = cs.cards;
	}
	public int valueOfColor(MarketColor m) {
		int v = 0;
		for (Card c : cards) {
			if (c.getColor().equals(m)) {
				v += c.value();
			}
		}
		return v;
	}
	public EnumMap<MarketColor, Integer> values() {
		EnumMap<MarketColor, Integer> vals = new EnumMap<MarketColor, Integer>(MarketColor.class);
		for (Card c : cards) {
			int iv = c.value();
			MarketColor m = c.getColor();
			if (vals.containsKey(m)) {
				Integer v = vals.get(m);
				iv += v.intValue();
			}
			vals.put(m, new Integer(iv));
		}
		return vals;
	}
	public CardSet cardsOfColor(MarketColor spot) {
		CardSet cs = new CardSet();
		for (Card c : cards) {
			if (c.getColor().equals(spot))
				cs.addCard(c);
		}
		return cs;
	}
	public boolean discardFrom(CardSet cs) {
		boolean allPresent = true;
		for (Card c : cs.cards) {
			if (cards.contains(c)) {
				cards.remove(c);
			} else allPresent = false;
		}
		return allPresent;
	}
	public boolean wasSelectedFrom(CardSet cs) {
		return cards.containsAll(cs.cards);
	}
}
