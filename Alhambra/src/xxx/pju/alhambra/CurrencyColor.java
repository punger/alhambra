package xxx.pju.alhambra;

import java.io.Serializable;
/**
 * Enumeration that lists the possible kinds of currency
 */
public enum CurrencyColor implements Serializable {
	blue, yellow, orange, green;
	/**
	 * Provide a conversion between CurrencyColor and MarketColor.
	 * While these are generally the same, cards in the expansions might be of alternate colors while the MarketColor is immutable.
	 * @param cc the currency color
	 * @return the market color this currency color applies to
	 */
	public static MarketColor canBuy(CurrencyColor cc) {
		switch (cc) {
		case blue: return MarketColor.blue;
		case yellow: return MarketColor.yellow;
		case green: return MarketColor.green;
		case orange: return MarketColor.orange;
		default:
			return null;
		}
	}
	
	public boolean canBuy(MarketColor m) {
		return m.equals(canBuy(this));
	}
	
	public MarketColor canBuy() {
		return CurrencyColor.canBuy(this);
	}
}
