package name.pju.alhambra;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author paulu
 *
 */
public class Player {
	private Hand hand = new Hand();
	private Alhambra alh = null;
	private List<Tile> reserveBoard = new ArrayList<Tile>();
	private int score;
	private List<Tile> unattached = new ArrayList<Tile>();
	private Game g;
	
	private int actions = 0;
	private PlayerColor meeple;
	
	public Hand getHand() {
		return hand;
	}
	public Alhambra getAlh() {
		return alh;
	}
	public List<Tile> getReserveBoard() {
		return reserveBoard;
	}
	public int getScore() {
		return score;
	}
	public List<Tile> getUnattached() {
		return unattached;
	}

	public PlayerColor getMeeple() {
		return meeple;
	}

	private static class PowerSetResolver {
		private ArrayList<Integer> vs;
		public PowerSetResolver(ArrayList<Integer> values) {
			vs = values;
		}
		private int total() {
			int cum = 0;
			for (int val : vs)
				cum += val;
			return cum;
		}
		int numChosen(int key) {
			int count = 0;
			while (key > 0) {
				if (key % 2 != 0) count++;
				key = key /2;
			}
			return count;
		}
		int bestFit(int target) {
			if (vs.size() >= 32) return -1;
			int bestTotal = total();
			if (bestTotal < target) return -1;
			int pSetSize = 2^(vs.size());
			int currentBest = pSetSize - 1;
			for (int i = 1; i < pSetSize; i++) {
				int thisVal = valOf(i);
				if (thisVal >= target && thisVal <= bestTotal) {
					if (numChosen(i) < numChosen(currentBest)) {
						currentBest = i;
						bestTotal = thisVal;
					}
				}
			}
			return currentBest;
		}
		private int valOf(int key) {
			int cum = 0;
			for (int index = 0; key > 0; index++) {
				if (key % 2 != 0) cum += vs.get(index).intValue();
				key /= 2;
			}
			return cum;
		}
	}


	public Player(Game g, PlayerColor meeple) {
		this.g = g;
		this.meeple = meeple;
		alh = new Alhambra(g.getGarden());
	}
	
	public void addCard(Card c) {
		hand.addCard(c);
	}
	
	public void addFromExchange(CardSet cs) {
		if (cs == null || cs.isEmpty())
			return;
		actions--;
		for (Card c : cs.getCards())
			addCard(c);
	}
	
	public int numberOfColor(Tile.Family color) {
		return 0;
	}
	
	public boolean canBuy(MarketColor mc) {
		Market m = g.getMarket();
		Tile t = m.whatsOnOffer(mc);
		int howMuchIHave = hand.valueOfColor(mc);
		return howMuchIHave >= t.getCost();
	}
	
	public Tile buy(MarketColor mc) {
		Payment pmt = getPossiblePayment(mc);
		return buy(mc, pmt);
	}
	
	public Tile buy(MarketColor mc, CardSet offer) {
		if (!canBuy(mc))
			return null;
		if (!hasActions()) return null;
		Market m = g.getMarket();
		Payment pmt = new Payment(offer);
		Tile t = m.buy(mc, pmt);
		if (t == null)
			return null;
		unattached.add(t);
		if (offer.totalWorth() > t.getCost())
			actions--;
		hand.discardFrom(offer);
		g.discardTo(offer);
		return t;
	}
	
	public boolean canBuy(MarketColor mc, CardSet offer) {
		if (!hand.wasSelectedFrom(offer))
			return false;
		Hand selectedCards = new Hand(offer);
		Market m = g.getMarket();
		Tile t = m.whatsOnOffer(mc);
		int howMuchIHave = selectedCards.valueOfColor(mc);
		return howMuchIHave >= t.getCost();
	}
	
	
	public Payment getPossiblePayment(MarketColor spot) {
		Tile t = g.getMarket().whatsOnOffer(spot);
		if (hand.valueOfColor(spot) < t.getCost())
			return null;
		ArrayList<Integer> vals = new ArrayList<Integer>();
		CardSet potentialPmt = hand.cardsOfColor(spot);
		for (Card c : potentialPmt.getCards()) {
			vals.add(new Integer(c.value()));
		}
		PowerSetResolver res = new PowerSetResolver(vals);
		int key = res.bestFit(t.getCost());
		if (key < 0) return null;
		Payment p = new Payment();
		for (int i = 0; key > 0; key /= 2, i++) {
			if (key % 2 != 0) 
				p.addCard(potentialPmt.getCards().get(i));
		}
		return p;
	}
	
	public boolean hasActions() {
		return actions > 0;
	}
	public void startTurn() {
		actions = 100;
	}
	
	public void endTurn() {
		actions = 0;
		reserveBoard.addAll(unattached);
		unattached.clear();
	}
	public void addToScore(int scoreForRound) {
		score += scoreForRound;
	}
	@Override
	public String toString() {
		return new ToStringBuilder(this).
				append("color", meeple.name()).
				append("hand", hand).
				append("reserve", reserveBoard.toArray(new Tile[reserveBoard.size()])).
				toString();
	}


}
