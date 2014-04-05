package name.pju.alhambra;


/**
 * Represents the Exchange, where cards are on offer.
 */
public class Exchange extends CardSet {
	/** Maximum value of cards when more than one is being taken */
	private static final int MULTICARDSUM = 5;
	/** Number of slots in the exchange */
	private static final int NUMSLOTS = 4;
	
	/**
	 * empty constructor
	 */
	public Exchange() {}

	/**
	 * Fill any open slots with cards from the deck
	 * 
	 * @param deck
	 *            deck to draw from
	 * @return zero normally or a positive number to indicate the round has
	 *         ended
	 */
	public int replenish(Deck deck) {
		int round = 0;
		while(getCards().size() < NUMSLOTS) {
			if (round == 0) round = deck.isScoringRound(); 
			addCard(deck.deal());
		}
		return round;
	}
	/**
	 * Claim a card from a named slot and add it to the target CardSet.
	 * 
	 * If the CardSet provided is null, allocate a fresh one.  Only add to 
	 * the set if the set is empty to start with or the total of the cards
	 * in the set and the new card claimed is not more than the card claim
	 * maximum.  If the card was added, clear the slot the card was in so that
	 * it can be refilled at the end of this user's turn.
	 * @param cardFromSlot slot to claim the card from; must be between 0 and 3
	 * @param cs CardSet to add the card to; allocate if null
	 * @return the modified CardSet with new cards if added
	 */
	public CardSet claim(int cardFromSlot, CardSet cs) {
		if (cardFromSlot >= 0 && cardFromSlot < NUMSLOTS) {
			Card c = getCards().get(cardFromSlot);
			if (cs == null)
				cs = new CardSet();
			int curWorth = cs.totalWorth();
			if (c != null) {
				if (curWorth == 0 || c.value() + curWorth <= MULTICARDSUM) {
					cs.addCard(c);
					getCards().remove(cardFromSlot);
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
	 * Claim a card by identity
	 * @param cc target card to take
	 * @param cs set to add it to if not null
	 * @return a CardSet that contains the card if it could be claimed
	 */
	public CardSet claim(Card cc, CardSet cs) {
		if (getCards().contains(cc)) {
			int slot = getCards().indexOf(cc);
			return claim(slot, cs);
		}
		return cs;
	}
	/**
	 * The user chickened out and is returning the cards to their slots without
	 * actually using them.
	 * @param cs source cardset
	 * @return true if all the cards in the provided set could be put back
	 * in the exchange
	 */
	public boolean restoreClaimedCards(CardSet cs) {
		while(getCards().size() < NUMSLOTS) {
			if (cs.isEmpty()) return false;
			Card c = cs.getCards().get(0);
			addCard(c);;
			cs.getCards().remove(0);
		}
		return cs.isEmpty();
	}

}
