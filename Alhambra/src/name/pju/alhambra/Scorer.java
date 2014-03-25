package name.pju.alhambra;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Arrays;

import com.google.common.collect.ImmutableMap;

public class Scorer implements Serializable {
	private static final long serialVersionUID = 1L;
	private EnumMap<PlayerColor, Player> players;
	private static final class ColorScore {
		private static final ImmutableMap<Tile.Family, List<Integer>> first =
			       new ImmutableMap.Builder<Tile.Family, List<Integer>>()
			           .put(Tile.Family.blue, Arrays.asList(1, 0, 0))
			           .put(Tile.Family.orange, Arrays.asList(2, 0, 0))
			           .put(Tile.Family.brown, Arrays.asList(3, 0, 0))
			           .put(Tile.Family.white, Arrays.asList(4, 0, 0))
			           .put(Tile.Family.green, Arrays.asList(5, 0, 0))
			           .put(Tile.Family.purple, Arrays.asList(6, 0, 0))
			           .put(Tile.Family.garden, Arrays.asList(0, 0, 0))
			           .build();
		private static final ImmutableMap<Tile.Family, List<Integer>> second =
			       new ImmutableMap.Builder<Tile.Family, List<Integer>>()
			           .put(Tile.Family.blue, Arrays.asList(8, 1, 0))
			           .put(Tile.Family.orange, Arrays.asList(9, 2, 0))
			           .put(Tile.Family.brown, Arrays.asList(10, 3, 0))
			           .put(Tile.Family.white, Arrays.asList(11, 4, 0))
			           .put(Tile.Family.green, Arrays.asList(12, 5, 0))
			           .put(Tile.Family.purple, Arrays.asList(13, 6, 0))
			           .put(Tile.Family.garden, Arrays.asList(0, 0, 0))
			           .build();
		private static final ImmutableMap<Tile.Family, List<Integer>> last =
			       new ImmutableMap.Builder<Tile.Family, List<Integer>>()
			           .put(Tile.Family.blue, Arrays.asList(16, 8, 1))
			           .put(Tile.Family.orange, Arrays.asList(17, 9, 2))
			           .put(Tile.Family.brown, Arrays.asList(18, 10, 3))
			           .put(Tile.Family.white, Arrays.asList(19, 11, 4))
			           .put(Tile.Family.green, Arrays.asList(20, 12, 5))
			           .put(Tile.Family.purple, Arrays.asList(21, 13, 6))
			           .put(Tile.Family.garden, Arrays.asList(0, 0, 0))
			           .build();
		public static int lookupScore(
				int round, 
				int rank, 
				int sharers, 
				Tile.Family color) 
		{
			ImmutableMap<Tile.Family, List<Integer>> roundMap;
			switch (round) {
			case 1:
				roundMap = first;
				break;
			case 2:
				roundMap = second;
				break;
			case 3:
				roundMap = last;
				break;
			default:
				return 0;
			}
			int totalVal = 0;
			for (int i = 0; i < sharers; i++) {
				List<Integer> scoreForColor = roundMap.get(color);
				int slotVal = 0;
				// This might well happen when there are more than 3 players
				if (rank + i < scoreForColor.size())
					slotVal = scoreForColor.get(rank + i);
				totalVal += slotVal;
			}
			return totalVal / sharers;
		}
		
	}
	private static class PlayerRank implements Comparable<PlayerRank> {
		private PlayerColor meeple;
		private Integer numOfColor;
		public PlayerRank(PlayerColor meeple, Integer numOfColor) {
			super();
			this.meeple = meeple;
			this.numOfColor = numOfColor;
		}
		public PlayerColor getMeeple() {
			return meeple;
		}
		public Integer getNumOfColor() {
			return numOfColor;
		}
		@Override
		public int compareTo(PlayerRank o) {
			return numOfColor.compareTo(o.numOfColor);
		}
	}

	public Scorer( EnumMap<PlayerColor, Player> everyone){
		players = everyone;
	}
	
	public EnumMap<PlayerColor, Integer> getScores(int round) {
		EnumMap<PlayerColor, Integer> scorePerPlayer = new EnumMap<PlayerColor, Integer>(
				PlayerColor.class);
		// Initialize scores to zero
		for (PlayerColor meeple : players.keySet()) {
			scorePerPlayer.put(meeple, new Integer(0));
		}
		for (Tile.Family bldgType : Tile.Family.values()) {
			ArrayList<PlayerRank> bldgAchievement = 
					new ArrayList<PlayerRank>();
			for (PlayerColor meeple : players.keySet()) {
				Player p = players.get(meeple);
				PlayerRank bldgsPerPlayer = 
						new PlayerRank(
								meeple, 
								p.getAlh().buildingCounts().get(bldgType));
				bldgAchievement.add(bldgsPerPlayer);
			}
			// Determine order
			Collections.<PlayerRank>sort(bldgAchievement);
			// Assign scores
			for (int rankForColor = 0; 
					rankForColor < bldgAchievement.size(); 
					rankForColor++) 
			{
				// Determine how many players share the score
				int sharers = 1;
				Integer numBs = bldgAchievement.get(rankForColor).getNumOfColor();
				for (int j = rankForColor + 1; 
						j <  bldgAchievement.size() &&
						bldgAchievement.get(j).getNumOfColor() == numBs;
						j++) {
					sharers++;
				}
				// Where sharers > 1, score will be reflect rank and sharers
				for (int k = 0; k < sharers; k++) {
					PlayerColor curPlayer = 
							bldgAchievement.get(k + rankForColor).getMeeple();
					Integer prevScore = scorePerPlayer.get(curPlayer);
					scorePerPlayer.put(
							curPlayer, 
							new Integer(
									prevScore.intValue() + 
									ColorScore.lookupScore
									(
											round, 
											rankForColor, 
											sharers, 
											bldgType
									)
							)
					);
				}
				rankForColor += sharers - 1;
			}
		}

		return scorePerPlayer;
	}

}
