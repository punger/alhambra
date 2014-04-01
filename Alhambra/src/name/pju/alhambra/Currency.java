package name.pju.alhambra;

import java.io.Serializable;

/**
 * Currency are the standard card type. They are distinct from abstract cards
 * because cards, particularly in the expansions not implemented here, may have
 * other qualities than an explicit color or value
 * 
 * @author paulu
 * 
 */
public class Currency extends Card implements Serializable {
	private static final long serialVersionUID = 1L;
	private CurrencyColor type;
	/** Value of the currency */
	private int denomination;
	public CurrencyColor getType() {
		return type;
	}
	public int getDenomination() {
		return denomination;
	}
	public Currency(CurrencyColor type, int denomination) {
		super();
		this.type = type;
		this.denomination = denomination;
	}
	@Override
	public
	MarketColor getColor() {
		return getType().canBuy();
	}
	@Override
	public
	int value() {
		return denomination;
	}

}
