package xxx.pju.alhambra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;

public class Game implements Serializable {
	private static final long serialVersionUID = 1L;

	private static class PlayerOrder implements Serializable {
		private static final long serialVersionUID = 1L;
		List<PlayerColor> roster;
		int curPlayer = 0;
		PlayerOrder (List<PlayerColor> roster) {
			this.roster = roster;
		}
		void setStart(PlayerColor p) {
			curPlayer = roster.indexOf(p);
		}
		PlayerColor next() {
			PlayerColor p = roster.get(curPlayer++);
			if (curPlayer >= roster.size()) 
				curPlayer = 0;
			return p;
		}
		PlayerColor cur() {
			return roster.get(curPlayer);
		}
	}
	private boolean endOfGame = false;
	private Market mkt = new Market();
	private EnumMap<PlayerColor, Player> participants = 
			new EnumMap<>(PlayerColor.class);
	private List<Player> players = new ArrayList<Player>();
	private Deck deck = new Deck();
	private Exchange xchg = new Exchange(this);
	private BagOfTiles tiles = new BagOfTiles();
	private PlayerOrder turnOrder;
	private Scorer sc;
	
	private void setupPlayers(EnumSet<PlayerColor> geeks) {
		// instantiate players
		PlayerColor startPlayer = null;
		List<PlayerColor> meepleList = new ArrayList<PlayerColor>();
		int leastMoney = 100;
		for (PlayerColor meeple : geeks) {
			Player p = new Player(this);
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
	
	public Game (EnumSet<PlayerColor> geeks) {
		setupPlayers(geeks);
		populateMarket();
		fillExchange();
		deck.assignScoringTimes();
		sc = new Scorer(participants);
	}
	
	private void fillExchange() {
		xchg.replenish(deck);
	}

	private void populateMarket() {
		mkt.refill(tiles);
	}

	public boolean replenish() {
		xchg.replenish(deck);
		endOfGame = mkt.refill(tiles);
		return endOfGame;
	}

	public void triggerScoringRound(int round) {
		EnumMap<PlayerColor, Integer> scoreForRound = sc.getScores(round);
		for (PlayerColor meeple : participants.keySet()) {
			Player geek = participants.get(meeple);
			geek.addToScore(scoreForRound.get(meeple).intValue());
		}
		
	}

	public Market getMarket() {
		return mkt;
	}

	public void discardTo(CardSet cs) {
		deck.discard(cs.cards);
	}

}
