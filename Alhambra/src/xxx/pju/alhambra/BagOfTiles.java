package xxx.pju.alhambra;

import java.io.Serializable;
import java.util.Collections;
import java.util.Stack;

import xxx.pju.alhambra.resource.TileList;

/**
 * Represent the bag of tiles yet to be brought into play
 * @author paulu
 *
 */
public class BagOfTiles implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 *  data structure holding the tiles
	 */
	private Stack<Tile> bag = new Stack<Tile>();
	/**
	 * Constructor retrieves the tile list from resources and 
	 * shuffles them.
	 */
	public BagOfTiles() {
		bag.addAll(TileList.getTiles());
		Collections.shuffle(bag);
	}
	/**
	 * Retrieve the top tile in the bag stack.  Remove the returned tile from 
	 * the bag.
	 * @return the next available tile in the bag 
	 */
	public Tile getNext() {
		if (bag.empty()) return null;
		return bag.pop();
	}
	
	/**
	 * Whether there are any more tiles in the bag.
	 * @return true if there are no more tiles in the bag
	 */
	public boolean empty() { return bag.empty(); }

}
