package name.pju.alhambra;

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
		return '('+getColor().toString()+value()+')';
	}
}
