package name.pju.alhambra;

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
	private Exchange xchg = new Exchange();
	private BagOfTiles tiles;
	private PlayerOrder turnOrder;
	private ArrayList<Scorer> sc;
    private int curRound = -1;
    	
	/**
	 * For all the current players, initialize them with a starting board and
	 * generate the starting hands.
	 * <br>
	 * Sets up the start player and sets order
	 * @param geeks players playing
	 */
	private void setupPlayers(EnumSet<PlayerColor> geeks) {
		// instantiate players
		PlayerColor startPlayer = null;
		List<PlayerColor> meepleList = new ArrayList<PlayerColor>();
		int leastMoney = 100;
		int fewestCards = 100;
		for (PlayerColor meeple : geeks) {
			Player p = new Player(this, meeple);
			participants.put(meeple, p);
			int initialHandValue = 0;
			while (initialHandValue < 20) {
				Card c = deck.deal();
				initialHandValue += c.value();
				p.addCard(c);
			}
			if (initialHandValue <= leastMoney) {
				if (initialHandValue < leastMoney
						|| p.getHand().getCards().size() < fewestCards) 
				{
					leastMoney = initialHandValue;
					fewestCards = p.getHand().getCards().size();
					startPlayer = meeple;
				}
			}
			players.add(p);
			meepleList.add(meeple);
		}
		turnOrder = new PlayerOrder(meepleList);
		turnOrder.setStart(startPlayer);
		getCurPlayer().startTurn();
		
	}
	
	/**
	 * Initialize the game with the players
	 * @param geeks set of players by color
	 * @param cs a set of cards
	 * @param bag all tiles
	 */
	public Game (EnumSet<PlayerColor> geeks, CardSet cs, BagOfTiles bag) {
		tiles = bag;
		this.deck = new Deck(cs);
		mkt = new Market(tiles);
		mkt.refill();
		setupPlayers(geeks);
		xchg.replenish(deck);
		deck.assignScoringTimes();
		sc = new ArrayList<Scorer>(3);
	}
	
	/**
	 * Fixes up the game setup after every turn.
	 * 
	 * Determines if the game has ended because the market cannot be 
	 * refilled.
	 * @return true when the game has ended and final scoring must be done
	 */
	public int replenish() {
		int round = xchg.replenish(deck);
		if (!mkt.refill())
			return 3;
		return round;
	}

	/**
	 * Generate scores for the round
	 * @param round the round number (0, 1, or 2)
	 */
	public void triggerScoringRound(int round) {
		curRound = round;
		Scorer curScorer = new Scorer(participants, round);
		EnumMap<PlayerColor, Integer> scoreForRound = curScorer.getScores();
		sc.add(round, curScorer);
		for (PlayerColor meeple : participants.keySet()) {
			Player geek = participants.get(meeple);
			geek.addToScore(scoreForRound.get(meeple).intValue());
		}
	}
	
	/**
	 * @return exchange
	 */
	public Exchange getExchange() { return xchg; }

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
		deck.discard(cs.getCards());
	}
	
	/**
	 * @return the currently active player
	 */
	public Player getCurPlayer() {
		return getPlayer(turnOrder.cur());
	}

	/**
	 * @param pc color of the player to retrieve
	 * @return the player with that color
	 */
	public Player getPlayer(PlayerColor pc) {
		return participants.get(pc);
	}

	/**
	 * @return the unique garden tile
	 */
	public Tile getGarden() {
		return tiles.getGarden();
	}
	
	/**
	 * @return all players as a list.
	 */
	public List<Player> getPlayers() {
		return players;
	}

	public void endgame() {
		// TODO End the game. TBD
		
	}

	/**
	 * End the current player's turn.
	 * @return the color of the next player
	 */
	public PlayerColor endTurn() {
		return turnOrder.cur();
//		getPlayer(turnOrder.cur()).endTurn();
//		return turnOrder.next();
	}

}
