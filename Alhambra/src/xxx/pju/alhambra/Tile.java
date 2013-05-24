package xxx.pju.alhambra;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Implements a single tile.
 * 
 * A tile has a color which indicates what family it is from.  The
 * garden is a separate family from all other families.
 * @author paulu
 *
 */
public class Tile implements Serializable {
	private static final long serialVersionUID = 1L;

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
		super();
		this.color = color;
		this.cost = cost;
		walls.addAll(w);
	}

	boolean canAdjoin(Tile t, Direction d) {
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
		return '['+color.toString() +": "+cost+']';
	}

}
