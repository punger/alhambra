package xxx.pju.alhambra;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

/**
 * Provides an object model for the game as a whole.
 * 
 * It manages associated players, holds the other pieces of the game board like
 * the market and the exchange, manages the deck and generates the initial 
 * setup.
 * @author paulu
 *
 */
public class Game {

	/**
	 * Provides management for the order of play. 
	 * 
	 * It persistently holds the players in the order established at setup
	 * time.  It supports discovering the current player and moving on to the
	 * next player at the end of his turn.
	 */
	private static class PlayerOrder {
		/**
		 *  Represents the players in order
		 */
		List<PlayerColor> roster;
		
		int curPlayer = 0;
		PlayerOrder (List<PlayerColor> roster) {
			this.roster = roster;
		}
		void setStart(PlayerColor p) {
			curPlayer = roster.indexOf(p);
		}
		/**
		 * Get the next player and update the current player pointer
		 * @return the color of the next player
		 */
		public PlayerColor next() {
			PlayerColor p = roster.get(curPlayer++);
			if (curPlayer >= roster.size()) 
				curPlayer = 0;
			return p;
		}
		/**
		 * @return the color of the player whose turn it currently is
		 */
		public PlayerColor cur() {
			return roster.get(curPlayer);
		}
	}
	private boolean endOfGame = false;
	private Market mkt;
	private EnumMap<PlayerColor, Player> participants = 
			new EnumMap<PlayerColor, Player>(PlayerColor.class);
	private List<Player> players = new ArrayList<Player>();
	private Deck deck;
	private Exchange xchg = new Exchange(this);
	private BagOfTiles tiles;
	private PlayerOrder turnOrder;
	private Scorer sc;
	
	private void setupPlayers(EnumSet<PlayerColor> geeks) {
		// instantiate players
		PlayerColor startPlayer = null;
		List<PlayerColor> meepleList = new ArrayList<PlayerColor>();
		int leastMoney = 100;
		for (PlayerColor meeple : geeks) {
			Player p = new Player(this, meeple);
			participants.put(meeple, p);
			int initialHandValue = 0;
			while (initialHandValue < 20) {
				Card c = deck.deal();
				initialHandValue += c.value();
				p.addCard(c);
			}
			if (initialHandValue < leastMoney) {
				leastMoney = initialHandValue;
				startPlayer = meeple;
			}
			players.add(p);
			meepleList.add(meeple);
		}
		turnOrder = new PlayerOrder(meepleList);
		turnOrder.setStart(startPlayer);
		
	}
	
	
	/**
	 * Initialize the game with the players
	 * @param geeks set of players by color
	 */
	public Game (EnumSet<PlayerColor> geeks, CardSet cs, BagOfTiles bag) {
		this.deck = new Deck(cs);
		setupPlayers(geeks);
		mkt = new Market(bag);
		populateMarket();
		fillExchange();
		deck.assignScoringTimes();
		sc = new Scorer(participants);
	}
	
	private void fillExchange() {
		xchg.replenish(deck);
	}

	private void populateMarket() {
		mkt.refill();
	}

	/**
	 * Fixes up the game setup after every turn.
	 * 
	 * Determines if the game has ended because the market cannot be 
	 * refilled.
	 * @return true when the game has ended and final scoring must be done
	 */
	public boolean replenish() {
		xchg.replenish(deck);
		endOfGame = mkt.refill();
		return endOfGame;
	}

	/**
	 * Generate scores for the round
	 * @param round the round number (0, 1, or 2)
	 */
	public void triggerScoringRound(int round) {
		EnumMap<PlayerColor, Integer> scoreForRound = sc.getScores(round);
		for (PlayerColor meeple : participants.keySet()) {
			Player geek = participants.get(meeple);
			geek.addToScore(scoreForRound.get(meeple).intValue());
		}
		
	}

	/**
	 * @return market
	 */
	public Market getMarket() {
		return mkt;
	}

	/**
	 * Discards cards to the discard pile
	 * @param cs set of cards to discard
	 */
	public void discardTo(CardSet cs) {
		deck.discard(cs.cards);
	}
	
	/**
	 * @return the currently active player
	 */
	public Player getCurPlayer() {
		return participants.get(turnOrder.cur());
	}

	/**
	 * @return the unique garden tile
	 */
	public Tile getGarden() {
		return tiles.getGarden();
	}

}
