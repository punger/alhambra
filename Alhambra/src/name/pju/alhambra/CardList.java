package name.pju.alhambra;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


public class CardList implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Set<Card> cards = new HashSet<Card>();
	static {
		
	}
	public static CardSet getDeck() {
		return new CardSet(cards);
	}
	

}
