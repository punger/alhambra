package xxx.pju.alhambra.resource;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import xxx.pju.alhambra.Card;
import xxx.pju.alhambra.CardSet;

public class CardList implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Set<Card> cards = new HashSet<Card>();
	static {
		
	}
	public static CardSet getDeck() {
		return new CardSet(cards);
	}
	

}
