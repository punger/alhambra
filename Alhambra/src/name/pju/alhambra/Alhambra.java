package name.pju.alhambra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import name.pju.alhambra.Tile.Family;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * Represents the buildings as laid out.
 * @author paulu
 *
 */
public class Alhambra implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Maps each direction to the deltas required to refer to the location
	 * in that direction.
	 */
	private static final EnumMap<Direction, Point> dirDelta = 
			new EnumMap<Direction, Point>(Direction.class);
	static {
		Point left = new Point(-1, 0);
		Point up = new Point(0, 1);
		Point right = new Point(1, 0);
		Point down = new Point(0, -1);
		dirDelta.put(Direction.north, up);
		dirDelta.put(Direction.east, right);
		dirDelta.put(Direction.south, down);
		dirDelta.put(Direction.west, left);
	}
	
	/// The place where all the tiles live
	private Area mat = null;

	/**
	 * Represents the two dimensional play area where tiles are placed.
	 * @author paulu
	 *
	 */
	private static class Area implements Serializable {
		private static final long serialVersionUID = 1L;
		private Table<Integer, Integer, Tile> space = HashBasedTable.create();
		private int minX, minY, maxX, maxY = 0;

		/**
		 * Check if the given location is within the limits of possibility
		 * for adding a new tile.
		 * 
		 * Note that all existing tiles will be strictly less than max[X|Y] 
		 * and strictly more than min[X|Y].  This test works completely 
		 * independently of the arrangement of tiles on the mat.
		 * 
		 * @param x the left-right location
		 * @param y the up-down location
		 * @return true if the location is even theoretically possible as 
		 * a place to put a tile.
		 */
		public boolean inBounds(int x, int y) {
			return !(x < minX || x > maxX || y < minY || y > maxY);
		}

		/**
		 * Insert the tile at the given location.
		 * 
		 * As a side effect, change any outer bounds that may have been 
		 * affected by this insertion. So, for instance if 3 was the 
		 * farthest up that any tile had been inserted, maxY would be 4, one 
		 * more than that.  If a new tile is inserted at 4, then maxY gets
		 * incremented.  Notice that will all the bounds start at 0, when the
		 * garden is inserted, the maxes and mins all change value.
		 * @param x the left-right location
		 * @param y the up-down location
		 * @param t tile to insert
		 */
		private void put(int x, int y, Tile t) {
			space.put(new Integer(x), new Integer(y), t);
			if (x == minX)
				minX--;
			if (x == maxX)
				maxX++;
			if (y == minY)
				minY--;
			if (y == maxY)
				maxY++;
		}

		/**
		 * Look up the tile at a given location
		 * @param x the left-right location
		 * @param y the up-down location
		 * @return the tile at (x, y) or null if none present
		 */
		public Tile get(int x, int y) {
			return space.get(new Integer(x), new Integer(y));
		}

		/**
		 * Area constructor puts a garden tile at the origin
		 * @param garden the start tile
		 */
		public Area(Tile garden) {
			put(0, 0, garden);
		}
		/**
		 * The tile in the location in the given direction of 
		 * the target location
		 * @param x the left-right location
		 * @param y the up-down location
		 * @param d the direction we are looking in for a tile
		 * @return the tile in that location wrt the target location
		 * if present; null otherwise
		 */
		private Tile thisAWay(int x, int y, Direction d) {
			Point delta = dirDelta.get(d);
			return get(x + delta.getX(), y + delta.getY());
		}

		/**
		 * The neighbors of a particular location.
		 * @param x the left-right location
		 * @param y the up-down location
		 * @return an enumeraton for each direction of the valid
		 * tile neighbors of the given location; empty neighbors
		 * have null values
		 */
		private EnumMap<Direction, Tile> getNeighbors(int x, int y) {
			EnumMap<Direction, Tile> neighbors = 
					new EnumMap<Direction, Tile>(Direction.class);
			for (Direction d : Direction.values()) {
				Tile t = thisAWay(x, y, d);
				neighbors.put(d,  t);
			}
			return neighbors;
		}
		/**
		 * Test to see if a tile can legally be placed in a given 
		 * location.
		 * @param x the left-right location
		 * @param y the up-down location
		 * @param t the tile to be placed
		 * @return true if the tile was placed; false if the 
		 * location was invalid
		 */
		public boolean canPlaceTile(int x, int y, Tile t) {
			if (!inBounds(x, y)) return false;
			EnumMap<Direction, Tile> folks = getNeighbors(x, y);
			if (folks.isEmpty()) return false;
			boolean canReach = false;
			for (Direction d : folks.keySet()) {
				Tile neighbor = folks.get(d);
				if (!t.canAdjoin(neighbor, d))
					return false;
				if (!t.hasWall(d)) 
					canReach = true;
			}
			return canReach;
		}

		/**
		 * Place a tile
		 * @param x the left-right location
		 * @param y the up-down location
		 * @param t the tile to be placed
		 * @return true if the tile was placed; false if the 
		 * location was invalid
		 */
		public boolean placeTile(int x, int y, Tile t) {
			if (!canPlaceTile(x, y, t)) return false;
			put(x, y, t);
			return true;
		}
		
		/**
		 * For a given tile, the locations at which the tile can
		 * be legally put.
		 * @param candidate at tile that might be placed
		 * @return a list of points at which the tile can be placed;
		 * the list will be empty if there are no such points.
		 */
		List<Point> getValidLocations(Tile candidate) {
			ArrayList<Point> okLocs = new ArrayList<Point>(); 
			for (int x = minX; x <= maxX; x++) {
				for (int y = minY; y <= maxY; y++) {
					if (canPlaceTile(x, y, candidate)){
						Point loc = new Point(x, y);
						okLocs.add(loc);
					}
				}
			}
			return okLocs;
		}
		/**
		 * All tiles currently on the mat, in no particular order.
		 * 
		 * Note that there must always be at least one, the garden that
		 * gets instantiated at creation time.
		 * @return a list of Tiles
		 */
		public List<Tile> getAllTiles() {
			List<Tile> myTiles = new ArrayList<Tile>();
			for (int x = minX + 1; x < maxX; x++) {
				for (int y = minY + 1; y < maxY; y++) {
					Tile t = get(x, y);
					if (t != null)
						myTiles.add(t);
				}
			}
			return myTiles;
		}
		
		/**
		 * Unpack the mat into a rectangular array for external agents to 
		 * examine.
		 * @return an array containing all the tiles in correct relative 
		 * offsets
		 */
		public Tile[][] getTileArray() {
			Tile tileArray[][] = new Tile[maxX - minX - 1][maxY - minY - 1];
			for (int x = minX + 1; x < maxX; x++) {
				for (int y = minY + 1; y < maxY; y++) {
					Tile t = get(x, y);
					tileArray[x - minX - 1][y - minY - 1] = t;
				}
			}
			return tileArray;
		}
	}

	/**
	 * Construct with the garden
	 * @param garden the concrete garden tile
	 */
	public Alhambra(Tile garden) {
		mat = new Area(garden);
	}
	
	/**
	 * Maps the tile colors to a count indicating how many of that color
	 * tile this Alhambra contains 
	 * @return a map from tile color to integer indicating tile counts
	 */
	public EnumMap<Tile.Family, Integer> buildingCounts() {
		EnumMap<Tile.Family, AtomicInteger>  freqs = 
				new EnumMap<Tile.Family, AtomicInteger>(Tile.Family.class);
		for (Tile t : mat.getAllTiles()) {
			Tile.Family color = t.getColor();
			if (freqs.containsKey(color)) {
				AtomicInteger ai = freqs.get(color);
				ai.getAndIncrement();
			} else {
				freqs.put(color, new AtomicInteger());
			}
		}
		EnumMap<Tile.Family, Integer> colorCounts = 
				new EnumMap<Family, Integer>(Tile.Family.class);
		for (Tile.Family color : Tile.Family.values()) {
			colorCounts.put(color, new Integer(freqs.get(color).get()));
		}
		return colorCounts;
	}
	
	/**
	 * Length of the longest wall in the Alhambra
	 * @return number of wall segments in the longest external wall
	 */
	public int longestWall() {
		// TODO: Provide algorithm for longest wall
		return 0;
	}
	
	/**
	 * For a given tile, the locations at which the tile can legally be put.
	 * 
	 * @param candidate
	 *            at tile that might be placed
	 * @return a list of points at which the tile can be placed; the list will
	 *         be empty if there are no such points.
	 */
	List<Point> getValidLocations(Tile candidate) {
		return mat.getValidLocations(candidate);
	}

	/**
	 * Place a tile
	 * @param x the left-right location
	 * @param y the up-down location
	 * @param t the tile to be placed
	 * @return true if the tile was placed; false if the 
	 * location was invalid
	 */
	public boolean placeTile(int x, int y, Tile t) {
		return mat.placeTile(x, y, t);
	}
	
	/**
	 * Describe the mat in terms easily managed by a component that wants to
	 * display it
	 * 
	 * @return a rectangular array of the tiles on the player's mat in the
	 *         configuration that the tiles are laid out in
	 */
	public Tile[][]getTileArray() { return mat.getTileArray(); }
	
	public int getCenterX() { return -mat.minX; }
	public int getCenterY() { return -mat.minY; }

	public Point getMins() {
		return new Point(mat.minX, mat.minY);
	}

	public Point getMaxs() {
		return new Point(mat.maxX, mat.maxY);
	}
}
