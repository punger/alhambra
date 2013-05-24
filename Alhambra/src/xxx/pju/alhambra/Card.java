package xxx.pju.alhambra;

/**
 * Abstract base class for all cards
 * @author paulu
 *
 */
public abstract class Card {
	public Card() {}
	abstract MarketColor getColor();
	abstract int value();
	public String toString() {
		return '('+getColor().toString()+value()+')';
	}
}
