import java.util.HashSet;
import java.util.Set;

/**
 * This class adds stones which are of the same color and adjacent to another stone and it adds them
 * to a group
 * 
 * @author John
 */
public class GroupAi
{

	public Set<Piece>	pieces;
	public Set<Square>	liberties;
	public GoAiGame		game;

	public GroupAi(GoAiGame game1)
	{

		game = game1;
		pieces = new HashSet<Piece>();
		liberties = new HashSet<Square>();
	}
	/**
	 * This method checks the number of liberties of a group and adds pieces to a group.
	 */
	public int numOfLiberties()
	{

		// For each piece in pieces
		for (Piece p : pieces)
		{
			// GEt the location
			int row = p.where.row;
			int col = p.where.col;
			// check to see wether the pieces are in certain positions(edges of the board) and
			// otherwise if there isnt anything next to that particular stone add however many
			// necessary pieces to the group
			if (row > 0 && game.squares[row - 1][col].piece == null)
			{
				liberties.add(game.squares[row - 1][col]);
			}
			if (col > 0 && game.squares[row][col - 1].piece == null)
			{
				liberties.add(game.squares[row][col - 1]);
			}
			if (row < 8 && game.squares[row + 1][col].piece == null)
			{
				liberties.add(game.squares[row + 1][col]);
			}
			if (col < 8 && game.squares[row][col + 1].piece == null)
			{
				liberties.add(game.squares[row][col + 1]);
			}
		}
		//returns the size of the liberties 
		return liberties.size();
	}
}