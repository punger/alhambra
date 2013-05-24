package xxx.pju.alhambra;

import java.io.Serializable;

/**
 * Represents the Exchange, where cards are on offer.
 */
public class Exchange implements Serializable {
	private static final long serialVersionUID = 1L;
	/** Maximum value of cards when more than one is being taken */
	private static final int MULTICARDSUM = 5;
	/** Available places for cards */
	private Card slots[] = new Card[4];
	/** Ponter to the overarching game this exchange participates in */
	private Game g;
	public Exchange(Game g) {
		this.g = g;
	}
	/**
	 * Fill any open slots with cards from the deck
	 * @param deck deck to draw from
	 */
	public void replenish(Deck deck) {
		for (int i = 0; i < slots.length; i++) {
			if (slots[i] == null) {
				int round = deck.isScoringRound(); 
				if (round != 0)
					g.triggerScoringRound(round);
				slots[i] = deck.deal();
				
			}
		}
	}
	/**
	 * Claim a card from a named slot and add it to the target CardSet.
	 * 
	 * If the CardSet provided is null, allocate a fresh one.  Only add to 
	 * the set if the set is empty to start with or the total of the carsd
	 * in the set and the new card claimed is not more than the card claim
	 * maximum.  If the card was added, clear the slot the card was in so that
	 * it can be refilled at the end of this user's turn.
	 * @param cardFromSlot slot to claim the card from; must be between 0 and 3
	 * @param cs CardSet to add the card to; allocate if null
	 * @return the modified CardSet with new cards if added
	 */
	public CardSet claim(int cardFromSlot, CardSet cs) {
		if (cardFromSlot >= 0 && cardFromSlot < slots.length) {
			Card c = slots[cardFromSlot];
			if (cs == null)
				cs = new CardSet();
			int curWorth = cs.totalWorth();
			if (c != null) {
				if (curWorth == 0 || c.value() + curWorth <= MULTICARDSUM) {
					cs.addCard(c);
					slots[cardFromSlot] = null;
				}
			}
		}
		return cs;
	}
	/**
	 * Claim a single card from the exchange and return it as a CardSet
	 * @param cardFromSlot slot number to take the card from in the range 0 to 3
	 * @return a CardSet containing the single card from the named slot
	 */
	public CardSet claim(int cardFromSlot) {
		return claim(cardFromSlot, null);
	}
	/**
	 * The user chickened out and is returning the cards to their slots without
	 * actually using them.
	 * @param cs
	 * @return
	 */
	public boolean restoreClaimedCards(CardSet cs) {
		for (int i = 0; i < slots.length && !cs.isEmpty(); i++) {
			if (slots[i] == null) {
				slots[i] = cs.cards.get(0);
				cs.cards.remove(0);
			}
		}
		return cs.isEmpty();
	}
	
	

}
