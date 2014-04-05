package name.pju.alhambra;

/**
 * Currency are the standard card type. They are distinct from abstract cards
 * because cards, particularly in the expansions not implemented here, may have
 * other qualities than an explicit color or value
 * 
 * @author paulu
 * 
 */
public class Currency extends Card {
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + denomination;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Currency))
			return false;
		Currency other = (Currency) obj;
		if (denomination != other.denomination)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
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
