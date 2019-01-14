import java.awt.*;

/**
 * This is what the grid is made up of and where the pieces are placed
 * 
 * @author Devin Wright
 */
public class Square
{

	/**
	 * row is the row the square exsists at
	 */
	public int			row;
	/**
	 * col is the column that the square exsists at
	 */
	public int			col;
	/**
	 * the top left hand corner of the square
	 */
	public int			x;
	/**
	 * top left hand corner of the square
	 */
	public int			y;
	/**
	 * the piece which exsists at that square (null if no piece exsists)
	 */
	public Piece		piece;
	/**
	 * represents the clicking area of the square
	 */
	public Rectangle	rect;

	/**
	 * @param row
	 *            row is the row where the square is positioned
	 * @param col
	 *            is the column where the square is positioned
	 * @param piece
	 *            piece is the piece that exsists on the square
	 */
	public Square(int row, int col, Piece piece)
	{

		this.row = row;
		this.col = col;
		this.piece = piece;
		y = row * 80;
		x = col * 80;
		rect = new Rectangle(x, y, 80, 80);
	}

	/**
	 * This returns the x coordinate of the top left hand corner of the square
	 * 
	 * @return x is the x pixel of the top left hand corner of the square
	 */
	public int getRow()
	{

		return x;
	}

	/**
	 * This returns the y coordinate of the top left hand corner of the square
	 * 
	 * @return y is the y pixel of the top left hand corner of the square
	 */
	public int getCol()
	{

		return y;
	}

	/**
	 * This returns the x coordinate of the top left hand corner of the square
	 * 
	 * @return returns the piece type or returns null if piece doesn't exsists
	 */
	public Piece getPiece()
	{

		return piece;
	}

}