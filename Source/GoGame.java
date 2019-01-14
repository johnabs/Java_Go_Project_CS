//this class is going to be used as the actual playing board of the game

//import stuff
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("serial")
public class GoGame extends JFrame implements MouseListener, MouseMotionListener, WindowListener

{

	// the instance variables
	ArrayList<int[][]>		ko;
	ImageIcon				board;
	int						x		= -100000;
	int						y		= -100000;
	Square[][]				squares;
	boolean					current	= true;
	boolean					whitePass, blackPass;
	Image					buffer;
	BufferedImage			image;
	String					name	= "";
	ArrayList<Territory>	territories;
	// true is black, false is white
	ArrayList<Group>		groups;
	double					whiteScore;
	double					blackScore;

	// this is the board
	public GoGame()
	{

		super("Go");	// name of the top of the board
		groups = new ArrayList<Group>();
		board = new ImageIcon("go.png");// puts the board as an image icon
		whitePass = false;// boolean which checks to see if white has passed
		blackPass = false;// Checks to see if black has passed
		JPanel panel = new JPanel();// new jpanel and setting the size and putting it to the jframe
		panel.setPreferredSize(new Dimension(720, 720));
		add(panel, BorderLayout.NORTH);
		image = new BufferedImage(720, 720, BufferedImage.TYPE_INT_RGB);
		addMouseListener(this);// sets a mouselistener to the jframe
		addWindowListener(this);
		setSize(720, 720);	// sets the size of jframe
		setVisible(true);// shows frame
		setResizable(false);// makes it not resizable
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// exits the program when the x is clicked
		setIgnoreRepaint(true);     // doesnt repaint the board
		addMouseMotionListener(this);	// adds mouse motion listener to the screen
		territories = new ArrayList<Territory>();
		ko = new ArrayList<int[][]>();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();	// gets screen size and put
		// the frame in the middle
		setLocation((screen.width - 720) / 2, (screen.height - 720) / 2);

		squares = new Square[9][9];	// double array of a 9 by 9 squares
		blackScore = 0;// Score of black
		whiteScore = 6.5;// Score of white, white gets s 6.5 handicap because black goes first

		// goes through and actually places the squares on the board and sets them to null
		for (int i = 0; i < squares.length; i++)
		{
			for (int j = 0; j < squares[i].length; j++)
			{

				squares[i][j] = new Square(i, j, null);

			}

		}

	}

	public void mouseClicked(MouseEvent e)
	{

	}

	// the next two methods must be implemented to satisfy the MouseListener interface
	// the implementation here says 'do nothing'
	public void mouseExited(MouseEvent e)
	{

	}

	// this is used to place a piece on the correct space where the mouse is
	// ALmost the entire calculation aspect occurs in this step. The program goes through the
	// methods to calculate pieces, board position and also runs through the methods which
	// allow for capturing of territories and pieces. This is performed everytime a user plays, so
	// it made
	// sense to place it where it would be triggered by a mouse press.
	public void mousePressed(MouseEvent e)
	{

		if (e.getButton() == 1)
		{
			for (Square[] a : squares)
			{
				for (Square s : a)
				{
					if (s.rect.contains(e.getPoint()) && s.piece == null)	// makes sure that a piece
																			// is
																			// not already there
					{
						// This creates a backup board which is a copy of the original board and if
						// a user can't play in a specific position, this wont allow them to, and
						// the boards position will
						// be reset to the backup board
						Piece[][] backup = new Piece[9][9];
						for (int i = 0; i < 9; i++)
							for (int j = 0; j < 9; j++)
								backup[i][j] = squares[i][j].piece;
						processGroups();
						int count = 0;
						for (Group h : groups)
						{
							if (h.pieces.size() > 0 && h.pieces.toArray(new Piece[0])[0].team == current)
							{
								count++;
							}
						}
						s.piece = new Piece(current, s);
						processGroups();
						int count2 = 0;
						for (Group h : groups)
						{
							if (h.pieces.size() > 0 && h.pieces.toArray(new Piece[0])[0].team == current)
							{
								count2++;
							}
						}
						capture();
						int count3 = 0;
						for (Group h : groups)
						{
							if (h.pieces.size() > 0 && h.pieces.toArray(new Piece[0])[0].team == current)
							{
								count3++;
							}
						}
						// checks to see if a group will be captured if being played here
						boolean atari = false;
						boolean koRule = false;
						for (int[][] i : ko)
						{
							int[][] copy = new int[9][9];
							for (int k = 0; k < squares.length; k++)
								for (int j = 0; j < squares[0].length; j++)
								{
									if (squares[k][j].piece == null)
									{
										copy[k][j] = -1;

									}
									else if (squares[k][j].piece.team)
									{
										copy[k][j] = 1;

									}
									else
										copy[k][j] = 0;
								}
							boolean equal = true;
							for (int j = 0; j < squares.length; j++)
							{
								for (int k = 0; k < squares[0].length; k++)
								{
									if (copy[j][k] != i[j][k])
										equal = false;
								}
							}
							if (equal)
							{
								koRule = true;
								break;
							}
						}
						if (count >= count2)
						{
							if (count2 != count3)
							{
								atari = true;
							}
						}
						else
						{
							if (count == count3)
							{
								atari = true;
							}
						}
						// changes color to other player
						if (!atari && !koRule)
						{
							int[][] copy = new int[9][9];
							for (int i = 0; i < squares.length; i++)
								for (int j = 0; j < squares[0].length; j++)
								{
									if (squares[i][j].piece == null)
									{
										copy[i][j] = -1;

									}
									else if (squares[i][j].piece.team)
									{
										copy[i][j] = 1;

									}
									else
										copy[i][j] = 0;
								}
							ko.add(copy);

							if (current)
							{
								blackPass = false;
							}
							else
							{
								whitePass = false;
							}
							current = !current;
						}
						// If the group was in atari and a user played to which the group would be
						// killed(their own stones, not their opponents)
						// The board gets reset to its original position and the current player,
						// must play at a different position.
						else if (atari)
						{
							for (int i = 0; i < 9; i++)
							{
								for (int j = 0; j < 9; j++)
								{
									squares[i][j].piece = backup[i][j];
								}

							}
							JOptionPane.showMessageDialog(this, "You cant play here (see Atari Rule):\n You can't play anywhere which would allow you to kill a group of your own stones");
						}
						else if (koRule)
						{
							for (int i = 0; i < 9; i++)
							{
								for (int j = 0; j < 9; j++)
								{
									squares[i][j].piece = backup[i][j];
								}

							}
							JOptionPane.showMessageDialog(this, "You cant play here (see Ko Rule):\n You can't play anywhere which would result in a previous position of the board");
						}
					}
				}
			}
		}
		//if someone right clicks then the color is changed and so is the boolean corresponding to the player who passed
		if (e.getButton() == 3)
		{
			if (current)
			{
				blackPass = true;
			}
			else
			{
				whitePass = true;
			}
			current = !current;
		}
	}


	public void mouseReleased(MouseEvent e)
	{

	}

	public void mouseEntered(MouseEvent e)	// not used
	{

	}

	public void mouseDragged(MouseEvent e)	// not used
	{

	}

	public void mouseMoved(MouseEvent e)	
	// used to show where the piece will be placed
	{

		for (Square[] a : squares)
		{
			for (Square s : a)
			{
				if (s.rect.contains(e.getPoint()))
				{
					x = s.x + 40;
					y = s.y + 40;

				}
			}
		}
	}

	// this is used to make the board and the cursor and to make the cursor follow the mouse
	public void render()
	{

		try
		{
			buffer = createImage(720, 720);
			Graphics g = buffer.getGraphics();
			Graphics i = image.getGraphics();
			g.drawImage(board.getImage(), 0, 0, 720, 720, this);
			i.drawImage(board.getImage(), 0, 0, 720, 720, this);
			g.setColor(Color.BLACK);
			g.drawRect(x - 25, y - 25, 50, 50);

			for (Square[] a : squares)
			{
				for (Square s : a)
				{

					if (s.piece != null)	// makes sure the space is open, if so it draws the piece
					{
						s.piece.draw(g);
						s.piece.draw(i);
					}
				}

			}
			// If both black and white pass in the same turn the game epds
			if ((whitePass && blackPass) || numOfEmptySquares() == 0)
			{
				dispose();
			}
			getGraphics().drawImage(buffer, 0, 0, 720, 720, this);	// so there is no updating
																	// flashes
		}
		catch (Exception e)
		{

		}
	}

	// This method processes groups which involves adding stones to groups if they are touching
	public void processGroups()
	{

		groups = new ArrayList<Group>();
		for (Square[] c : squares)
		{
			for (Square s : c)
			{
				if (s.piece != null)
				{
					boolean inGroup = false;
					for (Group g : groups)
					{
						if (g.pieces.contains(s.piece))
						{
							inGroup = true;
							break;
						}
					}
					if (!inGroup)
					{
						Group g = new Group(this);
						groups.add(g);
						addToGroup(g, s.piece);
					}
				}
			}
		}
	}

	// Calculates the total number of "null" squares, on the board.
	public int numOfEmptySquares()
	{

		int count = 0;
		for (Square[] a : squares)
		{
			for (Square s : a)
			{
				if (s.piece == null)
				{
					count++;
				}
			}
		}
		return count;
	}

	// This method adds a stone to a group if it is adjacent to said group, this allows for
	// connections between groups by placing stones(usually to extend liberties
	public void addToGroup(Group g, Piece p)
	{

		if (p == null || g.pieces.contains(p) || (g.pieces.size() > 0 && g.pieces.toArray(new Piece[0])[0].team != p.team))
		{
			return;
		}
		int row = p.where.row;
		int col = p.where.col;
		g.pieces.add(p);
		if (col > 0)
		{

			addToGroup(g, squares[row][col - 1].piece);
		}
		if (row > 0)
		{
			addToGroup(g, squares[row - 1][col].piece);
		}
		if (row < 8)
		{
			addToGroup(g, squares[row + 1][col].piece);
		}
		if (col < 8)
		{
			addToGroup(g, squares[row][col + 1].piece);
		}
	}

	// This checks to see if a groups liberties are exhausted and if so the group is captured and
	// the player receives the amount of stones * .5 to their score
	public void capture()
	{

		processGroups();
		for (int i = groups.size() - 1; i > -1; i--)
		{
			Group h = groups.get(i);
			if ((h.pieces.size() > 0 && h.pieces.toArray(new Piece[0])[0].team != current) && h.numOfLiberties() == 0)
			{
				for (Piece p : h.pieces)
				{
					p.where.piece = null;
				}
				if (current)
				{
					blackScore += h.pieces.size() * .5;
				}
				else
					whiteScore += h.pieces.size() * .5;
				// add h.pieces.size()*.5 to other side
			}
		}
		// This checks to see if any like colored stones are touching and if so, it adds them to a
		// group
		processGroups();
		for (int i = groups.size() - 1; i > -1; i--)
		{
			Group h = groups.get(i);
			if ((h.pieces.size() > 0 && h.pieces.toArray(new Piece[0])[0].team == current) && h.numOfLiberties() == 0)
			{
				for (Piece p : h.pieces)
				{
					p.where.piece = null;
				}
				if (!current)
				{
					blackScore += h.pieces.size() * .5;
				}
				else
					whiteScore += h.pieces.size() * .5;
				// add h.pieces.size()*.5 to other side
			}
		}
		processGroups();
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{

	}

	@Override
	public void windowClosed(WindowEvent arg0)
	{

		// This checks territories and based on that, it calculates the score.
		processTerritories();
		for (Territory t : territories)
		{
			switch (t.team())
			{
				case 0:
					continue;
				case 1:
					blackScore += t.squares.size();
					break;
				case 2:
					whiteScore += t.squares.size();

			}
		}
		// This takes the score of black and the score of white, compares them and displays the
		// appropriate message
		if (whiteScore > blackScore)
			JOptionPane.showMessageDialog(null, "WHITE WINS!!! \nWhite scored " + whiteScore + " points, and Black scored " + blackScore + " points");
		else if (blackScore > whiteScore)
			JOptionPane.showMessageDialog(null, "BLACK WINS!!! \nWhite scored " + whiteScore + " points, and Black scored " + blackScore + " points");
		else
			JOptionPane.showMessageDialog(null, "ITS A TIE!!! \nWhite scored " + whiteScore + " points, and Black scored " + blackScore + " points");

		// This saves the game(if the user wants to) to a file named saves as a png, then exits the
		// program.
		name = JOptionPane.showInputDialog("What is the name of this game?");
		if (name != null)
			try
			{
				new File("Saves").mkdir();
				ImageIO.write(image, "png", new File("Saves/" + name + ".png"));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		System.exit(0);
	}

	@Override
	public void windowClosing(WindowEvent arg0)
	{

	}

	@Override
	public void windowDeactivated(WindowEvent arg0)
	{

	}

	@Override
	public void windowDeiconified(WindowEvent arg0)
	{

	}

	@Override
	public void windowIconified(WindowEvent arg0)
	{

	}

	@Override
	public void windowOpened(WindowEvent arg0)
	{

	}

	// This method checks to see if a territory is within a group then calls the addToTerritory
	// method to add the territories to a certain team
	public void processTerritories()
	{

		territories = new ArrayList<Territory>();
		for (Square[] c : squares)
		{
			for (Square s : c)
			{
				if (s.piece == null)
				{
					boolean inGroup = false;
					for (Territory t : territories)
					{
						if (t.squares.contains(s))
						{
							inGroup = true;
							break;
						}
					}
					if (!inGroup)
					{
						Territory t = new Territory(this);
						territories.add(t);
						addToTerritory(t, s);
					}
				}
			}
		}
	}

	// This adds a square to a territory if it is contained within a group of like colored stones
	public void addToTerritory(Territory t, Square s)
	{

		if (s == null || t.squares.contains(s) || s.piece != null)
		{
			return;
		}
		int row = s.row;
		int col = s.col;
		t.squares.add(s);
		if (col > 0)
		{

			addToTerritory(t, squares[row][col - 1]);
		}
		if (row > 0)
		{
			addToTerritory(t, squares[row - 1][col]);
		}
		if (row < 8)
		{
			addToTerritory(t, squares[row + 1][col]);
		}
		if (col < 8)
		{
			addToTerritory(t, squares[row][col + 1]);
		}
	}

}