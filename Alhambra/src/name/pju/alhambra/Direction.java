package name.pju.alhambra;

import java.util.Collection;

/**
 * Directions that make sense in the context of relative placement in the
 * Alhambra
 */
public enum Direction {
	north, east, south, west;

	private static char abbr(Direction d) {
		return d.name().charAt(0);
	}

	public static String dirs2String(Collection<Direction> walls) {
		String cum = "";
		for (Direction w : walls)
			cum += abbr(w);
		return cum;
	}
}