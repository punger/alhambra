package xxx.pju.alhambra;

import java.io.Serializable;
import java.util.EnumMap;

public class Market implements Serializable {
	private static final long serialVersionUID = 1L;
	private EnumMap<MarketColor, Tile> availableTiles = new EnumMap<>(MarketColor.class);
	
	public Tile buy(MarketColor spot, Payment offer) {
		MarketColor target = offer.totalColor();
		if (!spot.equals(target)) return null;
		Tile item = availableTiles.get(spot);
		if (item.getCost() > offer.value()) return null;
		availableTiles.remove(spot);
		return item;
	}
	
	public boolean refill(BagOfTiles bag) {
		for (MarketColor m : MarketColor.values()) {
			if (availableTiles.get(m) == null) {
				if (bag.empty()) return false;
				Tile t = bag.getNext();
				availableTiles.put(m, t);
			}
		}
		return true;
	}
	public Tile whatsOnOffer(MarketColor spot) {
		return availableTiles.get(spot);
	}

}
