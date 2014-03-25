package name.pju.alhambra;

import java.io.Serializable;
import java.util.EnumMap;

/**
 * Represents the marketplace where tiles can be bought.  There are four 
 * slots, one in each of the main currency colors.  The marketplace owns
 * a bag of tiles that it used to fill in the stalls when tiles are bought.
 * 
 * @author paulu
 *
 */
public class Market implements Serializable {
	private static final long serialVersionUID = 1L;
	private EnumMap<MarketColor, Tile> availableTiles = new EnumMap<MarketColor, Tile>(MarketColor.class);
	private BagOfTiles bag;
	
	/**
	 * Initialize the market passing it a source of tiles
	 * @param bag collection of tiles that can be retrieved
	 */
	public Market(BagOfTiles bag) {
		this.bag = bag;
	}

	/**
	 * Attempt to buy the tile at the given market stall with the cards in the
	 * offer
	 * @param spot color of stall the caller is attempting to buy from
	 * @param offer a set of cards that should meet or exceed the price of 
	 * the tile
	 * @return the bought tile if the offer is acceptable; null otherwise
	 */
	public Tile buy(MarketColor spot, Payment offer) {
		MarketColor target = offer.totalColor();
		if (!spot.equals(target)) return null;
		Tile item = availableTiles.get(spot);
		if (item.getCost() > offer.value()) return null;
		availableTiles.remove(spot);
		return item;
	}
	
	/**
	 * Refill the market with tiles from the bag
	 * @return true if there were enough tiles to fill all empty slots in
	 * the market 
	 */
	public boolean refill() {
		for (MarketColor m : MarketColor.values()) {
			if (availableTiles.get(m) == null) {
				if (bag.empty()) return false;
				Tile t = bag.getNext();
				availableTiles.put(m, t);
			}
		}
		return true;
	}
	/**
	 * Retrieve the tile that is availavle at the given stall
	 * @param spot stall to look at
	 * @return the tile available at the stall
	 */
	public Tile whatsOnOffer(MarketColor spot) {
		return availableTiles.get(spot);
	}

}
