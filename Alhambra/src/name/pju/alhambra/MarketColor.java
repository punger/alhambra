package name.pju.alhambra;

import java.io.Serializable;

public enum MarketColor implements Serializable {
	blue, yellow, orange, green;

	@Override
	public String toString() {
		return name();
	}
	
}
