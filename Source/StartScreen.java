//imports for the frame and buttons
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;

//this is used for the start/welcome screen and uses buttons as an action listener  
@SuppressWarnings("serial")
public class StartScreen extends JFrame implements ActionListener
{

	// instance variables to be used everywhere
	JButton		start, help, Ai;
	JPanel		buttons;
	JLabel		label;
	ImageIcon	goBackground;

	public StartScreen()
	{

		super("Go");	// renames the top of the frame

		goBackground = new ImageIcon("gopic.png");	// sets the board I made as an image icon

		JPanel topPane = new JPanel();	// new jpanel

		topPane.setPreferredSize(new Dimension(500, 565));	// sets the size of the panel

		add(topPane, BorderLayout.NORTH);	// adds it to the jframe

		buttons = new JPanel(new GridLayout(1, 3));	// creates the space for 2 buttons
		buttons.setPreferredSize(new Dimension(500, 50));	// sets the space

		start = new JButton("VS Computer");	// names the buttons
		Ai = new JButton("VS Person");
		help = new JButton("View Tutorial");

		start.setPreferredSize(new Dimension(300, 100));// sets the button sizes
		help.setPreferredSize(new Dimension(300, 100));
		Ai.setPreferredSize(new Dimension(300, 100));

		buttons.add(start);	// adds the buttons to the jframe
		buttons.add(Ai);
		buttons.add(help);
		add(buttons, BorderLayout.SOUTH);// and to the bottom

		start.addActionListener(this);	// creates action listeners to the buttons
		Ai.addActionListener(this);
		help.addActionListener(this);

		setSize(900, 600);	// sets jframe size
		pack();	// packs the jframe
		setVisible(true);	// shows the jframe

		setResizable(false);	// makes the screen not resizable

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// exits the application on exit

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();	// gets the computer screen
																		// size

		setLocation((screen.width - 500) / 2, (screen.height - 600) / 2);	// puts the window in the
																			// middle of the screen

	}

	// puts background onto the Start menu
	public void paint(Graphics g)
	{

		super.paint(g);
		g.drawImage(goBackground.getImage(), 0, 0, 515, 585, this);
	}

	// used for the mouse clicks
	public void actionPerformed(ActionEvent e)
	{

		// start button is pushed and then the start screen disappears and the board screen apears
		if (e.getSource() == start)

		{

			final GoAiGame start = new GoAiGame();
			Timer t = new Timer(25, new ActionListener()
			{

				public void actionPerformed(ActionEvent e)
				{

					start.render();

				}

			});
			t.start();
			setVisible(false);
		}

		if (e.getSource() == help)
		{
			// still need to edit the tutorial class to make it so that when a button is pushed it
			// goes to the correct site
			// and also removes the frame
			try
			{
				Desktop.getDesktop().browse(URI.create("http://www.youtube.com/watch?v=4cWlXy8frII"));
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}

		}
		// start button is pushed and then the start screen disappears and the board screen apears
		if (e.getSource() == Ai)

		{

			final GoGame start = new GoGame();
			Timer t = new Timer(25, new ActionListener()
			{

				public void actionPerformed(ActionEvent e)
				{

					start.render();

				}

			});
			t.start();
			setVisible(false);
		}

	}

	// this method is used to draw the actual board and the cursor without flickering
	public void render()
	{

		Image buffer = createImage(500, 600);
		Graphics b = buffer.getGraphics();
		super.paint(b);
		b.drawImage(null, 0, 0, 500, 550, this);

		// any other drawing goes here
		getGraphics().drawImage(buffer, 0, 0, 500, 600, this);

	}

}
