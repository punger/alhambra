package name.pju.alhambra;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Implements a single tile.
 * 
 * A tile has a color which indicates what family it is from.  The
 * garden is a separate family from all other families.
 * @author paulu
 *
 */
public abstract class Tile {
	public enum Family {blue, orange, brown, white, green, purple, garden}
	private Family color;
	private int cost;
	private Set<Direction> walls = new HashSet<Direction>();
	private static final Map<Direction, Direction> adjoinTest = 
			new HashMap<Direction, Direction>();
	static {
		adjoinTest.put(Direction.north, Direction.south);
		adjoinTest.put(Direction.south, Direction.north);
		adjoinTest.put(Direction.west, Direction.east);
		adjoinTest.put(Direction.east, Direction.west);
	}
	
	public Tile() {
		color = Family.garden;
		cost = 0;
	}
	
	public Tile(Family color, int cost, Set<Direction> w) {
		this.color = color;
		this.cost = cost;
		walls.addAll(w);
	}

	/**
	 * Test if the given tile can adjoin me in the direction indicated
	 * @param t the tile to test
	 * @param d the relative direction I want to place the tile
	 * @return true if the target tile is null (not a restriction) or if we 
	 * both have walls in the direction we touch or if we don't both have 
	 * walls at the contact edge; false otherwise
	 */
	boolean canAdjoin(Tile t, Direction d) {
		if (t == null) return true;
		if (hasWall(d))
			return t.hasWall(adjoinTest.get(d));
		return !t.hasWall(adjoinTest.get(d));
	}

	public Family getColor() {
		return color;
	}

	public int getCost() {
		return cost;
	}

	public Set<Direction> getWalls() {
		return walls;
	}
	
	public boolean hasWall(Direction d) {
		return walls.contains(d);
	}
	public String toString() {
		return new ToStringBuilder(this).
				append(color).append(cost).
				append("walls", Direction.dirs2String(walls)).
				toString();
	}

}
