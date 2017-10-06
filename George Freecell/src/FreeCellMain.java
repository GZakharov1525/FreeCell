import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Main Frame for a Simple Free cell Game Sets up the menus and places a
 * CardPanel in the Frame also contains a method which allows the undo option to
 * be enabled/disabled.
 * 
 * @author Ridout and George Zakharov
 * @version December 9, 2014
 */
@SuppressWarnings("serial")
public class FreeCellMain extends JFrame implements ActionListener
{
	private CardPanel cardArea;
	private JMenuItem newMenuItem, statisticsOption, quitMenuItem;
	private JMenuItem undoOption, aboutMenuItem;

	/**
	 * Creates a FreeCellMain from object.
	 */
	public FreeCellMain()
	{
		super("Freecell");
		setResizable(false);

		// Add in an Icon - Ace of Spades
		setIconImage(new ImageIcon("images\\ace.png").getImage());

		// Add in a Simple Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		gameMenu.setMnemonic('G');
		newMenuItem = new JMenuItem("New Game");
		newMenuItem.addActionListener(this);
		newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				InputEvent.CTRL_MASK));

		statisticsOption = new JMenuItem("Statistics");
		statisticsOption.addActionListener(this);

		quitMenuItem = new JMenuItem("Exit");
		quitMenuItem.addActionListener(this);

		undoOption = new JMenuItem("Undo Move");
		undoOption.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				InputEvent.CTRL_MASK));
		undoOption.addActionListener(this);
		undoOption.setEnabled(false);

		gameMenu.add(newMenuItem);
		gameMenu.add(statisticsOption);
		gameMenu.add(undoOption);
		gameMenu.addSeparator();
		gameMenu.add(quitMenuItem);
		menuBar.add(gameMenu);

		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		aboutMenuItem = new JMenuItem("About...");
		aboutMenuItem.addActionListener(this);
		helpMenu.add(aboutMenuItem);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);

		// Set up the layout and add in a CardPanel for the card area
		setLayout(new BorderLayout());
		cardArea = new CardPanel(this);
		add(cardArea, BorderLayout.CENTER);

		// Center the frame in the middle (almost) of the screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setVisible(true);
		setLocation((screen.width - CardPanel.WIDTH) / 2 - this.getWidth(),
				(screen.height - CardPanel.HEIGHT) / 2 - this.getHeight());
	}

	/**
	 * Method that deals with the menu options.
	 * 
	 * @param event the event that triggered this method.
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == newMenuItem)
		{
			cardArea.newGame();
		}
		else if (event.getSource() == statisticsOption)
		{
			Statistics myStats = Statistics.readFromFile("stats.dat");
			JOptionPane.showMessageDialog(cardArea,
					"Number of Wins: " + myStats.numberOfWins
							+ "\nNumber of Games Played: "
							+ myStats.numberOfGames + "\nCurrent Win Streak: "
							+ myStats.currentStreak + "\nLongest Win Streak: "
							+ myStats.longestStreak + "\nWin Percentage: "
							+ myStats.winPercent() + "%",
					"Freecell Statistics", JOptionPane.INFORMATION_MESSAGE);

		}
		else if (event.getSource() == quitMenuItem)
		{
			// Checks if the user quit early, therefore not adding a win.
			cardArea.quitEarly();
			System.exit(0);
		}
		else if (event.getSource() == undoOption)
		{
			cardArea.undo();
			if (!cardArea.canUndo())
				setUndoOption(false);
		}

		else if (event.getSource() == aboutMenuItem)
		{
			JOptionPane.showMessageDialog(cardArea,
					"Freecell by Ridout\nand George Zakharov\n\u00a9 2014",
					"About Freecell", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * This class contains a method that checks for the user closing the game
	 * via the window and then calls a method to make sure a win is not added
	 * and statistics are appropriately updated.
	 * 
	 * @author Ridout and George Zakharov
	 *
	 */
	@SuppressWarnings("unused")
	private class CloseWindow extends WindowAdapter
	{
		/**
		 *@param event The event that triggers this method.
		 */
		public void windowClosing(WindowEvent event)
		{
			cardArea.quitEarly();	
			System.exit(0);
		}
	}

	/**
	 * Sets the Undo object in the Menu.
	 * @param canUndo if you can undo or not.
	 */
	public void setUndoOption(boolean canUndo)
	{
		this.undoOption.setEnabled(canUndo);
	}

	public static void main(String[] args)
	{
		FreeCellMain frame = new FreeCellMain();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
