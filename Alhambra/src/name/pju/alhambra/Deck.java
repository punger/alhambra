package name.pju.alhambra;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents a deck of cards.
 * 
 * It includes both the deck to deal cards from as well as the discard pile.
 * It supports shuffling and tracks when scoring rounds will occur.
 * @author paulu
 *
 */
public class Deck extends CardSet {
	/** Card offset when the scoring rounds occur */
	private int round1, round2 = 0;
	/** Collect the discards so that they can be reshuffled if needed */
	private CardSet discards = new CardSet();
	/** Instantiate the deck from the external resource cards and shuffle them 
	 * @param cs initial list of cards 
	 */
	public Deck(CardSet cs) {
		setCards(cs.getCards());
		Collections.shuffle(getCards());
	}
	
	/**
	 * Indicates scoring rounds
	 * @return the scoring round number if that round has been reached
	 */
	public int isScoringRound() {
		if (round1 == 0) return 1;
		if (round2 == 0) return 2;
		return 0;
	}

	/**
	 * Take a card off the top of the deck if available. Shuffle the discards if
	 * any when the deck runs out.  Count down to any of the scoring rounds.
	 * @return next card on the deck if there is one
	 */
	public Card deal() {
		if (isEmpty()) {
			if (discards.isEmpty())
				return null;
			getCards().addAll(discards.getCards());
			discards.getCards().clear();
			Collections.shuffle(getCards());
		}
		
		Card next = getCards().get(0);
		getCards().remove(0);
		round1--; round2--;
		return next;
	}

	/**
	 * The deck keeps track of when the first two scoring rounds occur. These
	 * are triggered to be assigned after cards have been dealt to players and
	 * the exchange. The game will know when to make the assignment after these
	 * actions have been taken.
	 */
	public void assignScoringTimes() {
		round1 = getCards().size() / 3;
		round2 = (2 * getCards().size()) / 3; 
	}
	/**
	 * Return a card to the discard pile
	 * @param c the card to be discarded
	 */
	public void discard(Card c) {
		discards.addCard(c);
	}
	/**
	 * Discard a set of cards
	 * @param cs the set to be discarded
	 */
	public void discard(Collection<Card> cs) {
		discards.getCards().addAll(cs);
	}
	

}
