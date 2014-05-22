package name.pju.alhambra;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class WallFinder {
	private List<WallSequence> paths;
	private int maxLen = -1;
	public static class WallLocation {
		int x , y ;
		Direction w;
		private boolean atRelativeLoc(WallLocation other, int dx, int dy) {
			return other.x == x+dx && other.y == y+dy; 
		}
		public WallLocation(int xin, int yin, Direction win) {
			x = xin;
			y = yin;
			w = win;
		}
		public boolean connects(WallLocation other) {
			switch (w) {
			case east:
				if (other.w == Direction.north && atRelativeLoc(other, 0, 0)) return true;
				if (other.w == Direction.east && atRelativeLoc(other, 0, -1)) return true;
				if (other.w == Direction.south && atRelativeLoc(other, 1, -1)) return true;
				break;
			case north:
				if (other.w == Direction.west && atRelativeLoc(other, 0, 0)) return true;
				if (other.w == Direction.north && atRelativeLoc(other, -1, 0)) return true;
				if (other.w == Direction.east && atRelativeLoc(other, -1, -1)) return true;
				break;
			case south:
				if (other.w == Direction.east && atRelativeLoc(other, 0, 0)) return true;
				if (other.w == Direction.south && atRelativeLoc(other, 1, 0)) return true;
				if (other.w == Direction.west&& atRelativeLoc(other, 1, 1)) return true;
				break;
			case west:
				if (other.w == Direction.south && atRelativeLoc(other, 0, 0)) return true;
				if (other.w == Direction.west && atRelativeLoc(other, 0, 1)) return true;
				if (other.w == Direction.north && atRelativeLoc(other, -1, 1)) return true;
				break;
			}
			return false;
		}
	}
	public static class WallSequence {
		private ArrayDeque<WallLocation> adjacentWalls;
		private void clear() {
			adjacentWalls.clear();
		}
		public WallSequence(WallLocation origin) {
			adjacentWalls = new ArrayDeque<>();
			adjacentWalls.add(origin);
		}
		public boolean connects(WallSequence left) {
			return adjacentWalls.getLast().connects(left.adjacentWalls.getFirst()); 
		}
		public int getLength() { return adjacentWalls.size(); }
		public WallSequence connect(WallSequence left) {
			adjacentWalls.addAll(left.adjacentWalls);
			left.clear();
			return this; 
		}
		public boolean isClear() { return adjacentWalls.size() == 0; }
	}
	
	public WallFinder(List<WallLocation> startWalls) {
		paths = new ArrayList<WallSequence>();
		for (WallLocation w : startWalls) {
			paths.add(new WallSequence(w));
		}
	}
	
	public int getMaxWall() {
		if (maxLen < 0) {
			maxLen = 0;
			resolveWallSequences();
			for (WallSequence segment : paths) {
				if (segment.getLength() > maxLen){
					maxLen = segment.getLength();
				}
			}
		}
		return maxLen;
	}

	private void resolveWallSequences() {
		boolean progress;
		do {
			progress = false;
			for (WallSequence segment : paths) {
				if (segment.isClear()) continue;
				for (WallSequence candidate : paths) {
					if (segment.connects(candidate)) {
						segment.connect(candidate);
						progress = true;
					}
				}
			}
		} while (progress);
	}

}
