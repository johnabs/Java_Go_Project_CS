//Piece class that tells where the piece is and its team

import java.awt.*;

/**
 * This is the piece class which draws a piece on the board and keeps track of which color is to
 * play next.
 * 
 * @author John Biechele-Speziale, Devin Wright
 */
public class Piece
{

	/**
	 * team represents the color of the piece true is black and false is white
	 */
	boolean	team;
	/**
	 * where is the location of the piece
	 */
	Square	where;

	/**
	 * Piece creates a piece of the proper color at location square.
	 * 
	 * @param team
	 *            team represents the color of the piece
	 * @param where
	 *            where is the location of the piece
	 */
	public Piece(boolean team, Square where)
	{

		this.team = team;
		this.where = where;
	}

	/**
	 * This returns the team of the piece
	 * 
	 * @return returns the team
	 */
	public boolean getTeam()
	{

		return this.team;
	}

	/**
	 * setTeam sets the team of the piece about to be played
	 * 
	 * @param setting
	 *            sets the team to true or false
	 */
	public void setTeam(boolean setting)
	{

		this.team = setting;
	}

	/**
	 * This draws the piece at position square
	 * 
	 * @param g
	 *            is the graphics of the picture which the piece is being drawn on
	 */
	public void draw(Graphics g)
	{

		if (team)
		{
			g.setColor(Color.black);
		}
		else
			g.setColor(Color.white);
		//draws oval at position needed
		g.fillOval(where.x + 16, where.y + 16, 48, 48);
	}

}