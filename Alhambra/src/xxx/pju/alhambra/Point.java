package xxx.pju.alhambra;

import java.io.Serializable;

/**
 * Utility class that encapsulates x and y coordinates
 * @author paulu
 *
 */
public class Point implements Serializable {
	private static final long serialVersionUID = 1L;
	private int x = 0;
	private int y = 0;
	/**
	 * @return the x value
	 */
	public int getX() {
		return x;
	}
	/**
	 * @return the y value
	 */
	public int getY() {
		return y;
	}
	/**
	 * Initialize with given coordinate values
	 * @param x left-right
	 * @param y up-down
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Initialize to the origin 0,0
	 */
	public Point() {
		this(0, 0);
	}
	/**
	 * Move this point by the deltas in o
	 * @param o pair of deltas to displace by
	 */
	public void displace(Point o) {
		if (o == null) return;
		x += o.x;
		y += o.y;
	}
	
	/**
	 * Add o to this and return this
	 * @param o deltas
	 * @return this
	 */
	public Point add(Point o) {
		displace(o);
		return this;
	}
}

