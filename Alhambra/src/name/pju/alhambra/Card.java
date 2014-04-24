package name.pju.alhambra;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Abstract base class for all cards
 * @author paulu
 *
 */
public abstract class Card {
	public Card() {}
	public abstract MarketColor getColor();
	public abstract int value();
	public String toString() {
		return new ToStringBuilder(this).
				append("color", getColor()).
				append("val", value()).
				toString();
	}
}
