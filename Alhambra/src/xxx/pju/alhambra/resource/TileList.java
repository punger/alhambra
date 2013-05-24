package xxx.pju.alhambra.resource;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import xxx.pju.alhambra.Tile;

public class TileList implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Set<Tile> allTiles = new HashSet<Tile>();
	static {
		
	}
	public static Set<Tile> getTiles() { return allTiles; }

}
