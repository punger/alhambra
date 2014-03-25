package name.pju.alhambra;


/**
 * Represent the bag of tiles yet to be brought into play
 * @author paulu
 *
 */
public interface BagOfTiles {
	/**
	 * Retrieve the top tile in the bag stack.  Remove the returned tile from 
	 * the bag.
	 * @return the next available tile in the bag 
	 */
	public Tile getNext();
	/**
	 * Whether there are any more tiles in the bag.
	 * @return true if there are no more tiles in the bag
	 */
	public boolean empty();
	
	/**
	 * The specific garden tile is created in the bag but is not dealt from it
	 * @return the garden tile
	 */
	public Tile getGarden();

}
