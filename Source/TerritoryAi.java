import java.util.HashSet;
import java.util.Set;
/**
 *This class checks to see if an intersection or group of intersections is surrounded by a certain color, if so it counts toward that colors points. 
 * @author John
 *
 */

public class TerritoryAi
{

	public Set<Square>	squares;
	public GoAiGame		game;

	public TerritoryAi(GoAiGame game1)
	{

		game = game1;
		squares = new HashSet<Square>();
	}
	
	/**
	 * This method checks each square and (including edge pieces) to verify they are "Captured"
	 * @return
	 */
	public int team()
	{
		
		Set<Piece> stones = new HashSet<Piece>();
		//for each square in squares
		for (Square s : squares)
		{
			//Get the row and column of the square
			int row = s.row;
			int col = s.col;
			//Check to see if the stone is in bounds and in the same column/row is set to null
			if (row > 0 && game.squares[row - 1][col].piece != null)
			{
				stones.add(game.squares[row - 1][col].piece);
			}
			if (col > 0 && game.squares[row][col - 1].piece != null)
			{
				stones.add(game.squares[row][col - 1].piece);
			}
			if (row < 8 && game.squares[row + 1][col].piece != null)
			{
				stones.add(game.squares[row + 1][col].piece);
			}
			if (col < 8 && game.squares[row][col + 1].piece != null)
			{
				stones.add(game.squares[row][col + 1].piece);
			}
		}
		//This 
		int black = 0;
		int white = 0;
		//For each piece in stones
		for (Piece p : stones)
		{
			//if team is true add to black
			if (p.team)
				black++;
			//else add to white
			else
				white++;
		}
		//make sure that anything that isn't explicitly in a group of territories isn't counted
		if (stones.size() == 0)
			return 0;
		if (black > 0 && white > 0)
			return 0;
		else if (white == 0)
		{
			return 1;
		}
		else
			return 2;
	}
}
