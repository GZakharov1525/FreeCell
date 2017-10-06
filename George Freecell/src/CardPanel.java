import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Looks after most of the Freecell Game.
 * 
 * Contains variables that keep track of: The dimensions (Width, Height) of the
 * panel, the locations and spacing of the Cascade/FreeCell/Foundation fields, a
 * list of all moves made, a list of all cascades, a reference to the parent
 * frame, a reference to the Graphical Deck from which the cards are dealt, the
 * card(s) currently picked up, the hand from which the selected card(s) were
 * taken from, a boolean variable to check if it is an ongoing game or has just
 * been started, a point variable to track where the user has pressed down the
 * mouse button and a boolean variable to turn the animation of the deck being
 * dealt on/off.
 * 
 * This class includes the following methods: A method that creates the panel, a
 * method that starts a new game by reseting, shuffling and dealing the cards
 * from the deck to the cascades, a method that checks if the user has quit
 * early or just started the game and updates stats accordingly, an undo method
 * which reverses a move made by the player, a method that checks if the undo
 * move feature is enabled, a method that takes care of all graphical related
 * tasks (such as drawing), a method which automatically moves cards to a
 * foundation providing they cannot be used to stack other cards on top of, a
 * method which checks if the user has won by completely sorting all cards into
 * their four respective foundations, a method that moves the selected card(s)
 * during their animation, a delay method, and all abstract methods from the
 * mouseListener class, some of which are not used.
 * 
 * @author Ridout and George Zakharov
 * @version December 9, 2014
 */
@SuppressWarnings("serial")
public class CardPanel extends JPanel implements MouseListener,
		MouseMotionListener
{
	// Constants for the table layout
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private final Color TABLE_COLOUR = new Color(140, 225, 140);
	private final int ANIMATION_FRAMES = 6;

	// Constants for layout of card area
	private final int NO_OF_CASCADES = 8;
	private final int NO_OF_FREECELLS = 4;
	private final int NO_OF_FOUNDATIONS = 4;
	private final int CASCADE_X = 30;
	private final int CASCADE_Y = 150;
	private final int CASCADE_SPACING = 95;
	private final int FREECELL_X = 30;
	private final int FREECELL_Y = 30;
	private final int TOP_SPACING = 90;
	private final int FOUNDATION_X = 425;
	private final int FOUNDATION_Y = 30;

	// Variables for the Freecell Game
	private FreeCellMain parentFrame;
	private LinkedList<Move> moves;

	private GDeck myDeck;
	private ArrayList<GHand> allHands;
	private Movable selectedItem;
	private GHand sourceHand;
	private Point lastPoint;
	private GCard movingCard;
	private boolean animate = true;
	private boolean onGoingGame;
	Statistics myStats = Statistics.readFromFile("stats.dat");

	/**
	 * Constructs a CardPanel by setting up the Panel and the Deck and all of
	 * required Hands to keep track of the free cells, foundations and cascades.
	 * Also sets up listeners for mouse events and a move list.
	 * @param parentFrame the main Frame that holds this panel.
	 */
	public CardPanel(FreeCellMain parentFrame)
	{
		// Set up the size and background colour
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(TABLE_COLOUR);
		this.parentFrame = parentFrame;

		// Add mouse listeners to the card panel
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		// Set up the deck, cascades, foundations and free cells
		myDeck = new GDeck(400 - GCard.WIDTH / 2, 470);
		allHands = new ArrayList<GHand>();

		// Create Cascades
		int xCascade = CASCADE_X;
		int yCascade = CASCADE_Y;
		for (int i = 0; i < NO_OF_CASCADES; i++)
		{
			allHands.add(new Cascade(xCascade, yCascade));
			xCascade += CASCADE_SPACING;
		}

		// Create Free cells
		int xFreecell = this.FREECELL_X;
		int yFreecell = this.FREECELL_Y;
		for (int i = 0; i < this.NO_OF_FREECELLS; i++)
		{
			allHands.add(new Freecell(xFreecell, yFreecell));
			xFreecell += TOP_SPACING;
		}

		// Create Foundations
		int xFoundation = FOUNDATION_X;
		int yFoundation = FOUNDATION_Y;
		for (int i = 0; i < this.NO_OF_FOUNDATIONS; i++)
		{
			allHands.add(new Foundation(xFoundation, yFoundation));
			xFoundation += TOP_SPACING;
		}
		// Sets the variable that is used to check for a premature exit to
		// false
		onGoingGame = false;
		movingCard = null;
		moves = new LinkedList<Move>();
	}

	/**
	 * Starts up a new game by clearing all of the Hands, shuffling the Deck and
	 * dealing new Cards to the Cascades. Also resets the move list.
	 */
	public void newGame()
	{
		// Checks for a premature exit after the first ever use of newGame
		// (premature exit during a started game to avoid loss)
		quitEarly();
		// Sets the variable to true because premature exits are now possible
		onGoingGame = true;
		// Add 1 to the number of games played by user.
		myStats.numberOfGames++;
		myStats.writeToFile("stats.dat");
		// Clear out all of the Hands
		for (Hand next : allHands)
			next.clear();

		myDeck.shuffle();

		// Deal the Cards to the Cascades (first 8 Hands)
		int cascasdeIndex = 0;
		while (myDeck.cardsLeft() > 0)
		{
			GCard dealtCard = myDeck.dealCard();
			Point pos = new Point(dealtCard.getPosition());
			allHands.get(cascasdeIndex).addCard(dealtCard);
			Point finalPos = new Point(dealtCard.getPosition());
			if (animate)
				moveACard(dealtCard, pos, finalPos);
			if (!dealtCard.isFaceUp())
				dealtCard.flip();
			cascasdeIndex++;
			if (cascasdeIndex == NO_OF_CASCADES)
				cascasdeIndex = 0;

		}
		moves.clear();
		// Cannot undo at start of the game as no moves have been made yet
		parentFrame.setUndoOption(false);
		repaint();
	}

	/**
	 * This method checks for the premature exit of a game, if a game has not
	 * been started before it will not count as a premature exit.
	 */
	public void quitEarly()
	{
		if (onGoingGame == true)
		{
			// Player quit prematurely thus counting as a loss.
			myStats.numberOfWins--;
			// If player loses, it breaks their current win streak.
			myStats.currentStreak = 0;
			// Update stats.
			myStats.writeToFile("stats.dat");

		}
	}

	/**
	 * Undoes the last move made.
	 */
	public void undo()
	{
		if (canUndo())
		{
			Move lastMove = moves.removeLast();
			lastMove.undo();
			repaint();
		}
	}

	/**
	 * Checks if there are any moves in the moves list so that we can see if it
	 * is ok to undo a move.
	 * @return true if we can undo, false if not.
	 */
	public boolean canUndo()
	{
		return !moves.isEmpty();
	}

	/**
	 * Draws the information in this CardPanel. Draws the Deck, all of the
	 * hands, the moving Card in an animation and the selected Card or GHand.
	 * @param g the Graphics context to do the drawing.
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		// Layer drawing the way you want them to cover over other cards.
		// Draw the deck if there are cards left
		if (myDeck.cardsLeft() > 0)
			myDeck.draw(g);

		// Draw all of the Hands
		for (GHand next : allHands)
			next.draw(g);

		// For animation to draw the moving Card
		if (movingCard != null)
			movingCard.draw(g);

		// Draw selected GHand or Card on top
		if (selectedItem != null)
			selectedItem.draw(g);
	}

	/**
	 * Automatically moves any Cards up to the Foundations when possible.
	 */
	private void autoComplete()
	{
		// Start with an ace, update everytime you can place a different rank of
		// card up into foundations.
		// int lowestInPlay = 1;

		// dont move black 4s if there are still red 3s in play!!!

		// (loop) while moves are being made

		// for loop to check allHands, inner for loop to check each top card and
		// freecell for valid auto completes into foundation

		// .add(removecard from cascade)
	}

	/**
	 * Checks to see if the player has won by completing all of the foundations.
	 * @return true if they have won, false if not.
	 */
	private boolean checkForWinner()
	{
		for (GHand nextFoundation : allHands.subList(NO_OF_CASCADES
				+ NO_OF_FREECELLS, allHands.size()))
		{
			if (nextFoundation.cardsLeft() < 13)
				return false;
		}
		Statistics myStats = Statistics.readFromFile("stats.dat");
		myStats.numberOfWins++;
		myStats.currentStreak++;
		myStats.writeToFile("stats.dat");
		return true;
	}

	/**
	 * Moves a Card during the animation.
	 * @param cardToMove Card that you want to move.
	 * @param fromPos initial position of the Card.
	 * @param toPos final position of the Card.
	 */
	public void moveACard(final GCard cardToMove, Point fromPos, Point toPos)
	{
		int dx = (toPos.x - fromPos.x) / ANIMATION_FRAMES;
		int dy = (toPos.y - fromPos.y) / ANIMATION_FRAMES;

		for (int times = 1; times <= ANIMATION_FRAMES; times++)
		{
			fromPos.x += dx;
			fromPos.y += dy;
			cardToMove.setPosition(fromPos);

			// Update the drawing area
			paintImmediately(0, 0, getWidth(), getHeight());
			delay(30);

		}
		cardToMove.setPosition(toPos);
	}

	/**
	 * Delays the given number of milliseconds.
	 * 
	 * @param milliSec number of milliseconds to delay.
	 */
	private void delay(int milliSec)
	{
		try
		{
			Thread.sleep(milliSec);
		}
		catch (Exception e)
		{
		}
	}

	/**
	 * Handles the mouse pressed events to pick a Card or a Tableau.
	 * @param event event information for mouse pressed.
	 */
	public void mousePressed(MouseEvent event)
	{
		if (selectedItem == null)
		{
			Point selectedPoint = event.getPoint();

			// Pick up one of cards from a Hand (Freecell or Cascade)
			// Could also pick up from a Foundation if you want.no.
			for (GHand nextHand : allHands)
				if (nextHand.contains(selectedPoint)
						&& nextHand.canPickUp(selectedPoint))
				{
					// Split off a section of the Cascade or pick up a Card
					selectedItem = nextHand.pickUp(selectedPoint);

					// In case our move is not valid, we want to return the
					// Card(s) to where they initially came from
					sourceHand = nextHand;
					lastPoint = selectedPoint;
					repaint();
					return;
				}
		}
	}

	/**
	 * Handles the mouse released events to drop a Card or a Tableau.
	 * @param event Event information for mouse released.
	 */
	public void mouseReleased(MouseEvent event)
	{
		if (selectedItem != null)
		{
			// Check to see if we can add this to another cascade
			// foundation or free cell
			for (GHand nextHand : allHands)
				if (selectedItem.intersects(nextHand)
						&& selectedItem.canPlaceOn(nextHand))
				{
					selectedItem.placeOn(nextHand);

					// Count this move if you didn't place it on the same spot
					if (nextHand != sourceHand)
					{
						moves.addLast(new Move(sourceHand, nextHand,
								selectedItem));
						parentFrame.setUndoOption(true);
					}
					selectedItem = null;
					repaint();

					// Check these things after a Card is dropped
					autoComplete();
					if (checkForWinner())
					{
						parentFrame.setUndoOption(false);
						JOptionPane.showMessageDialog(parentFrame,
								"Congratulations", "You win",
								JOptionPane.INFORMATION_MESSAGE);
						onGoingGame = false;
						// update stats with true parameter

					}
					return;
				}

			// Return to original spot if not a valid move
			selectedItem.placeOn(sourceHand);
			selectedItem = null;
			repaint();
		}
	}

	/**
	 * Handles the mouse dragged events to drag the moving card(s).
	 * @param event Event information for mouse dragged.
	 */
	public void mouseDragged(MouseEvent event)
	{
		Point currentPoint = event.getPoint();

		if (selectedItem != null)
		{
			// We use the difference between the lastPoint and the
			// currentPoint to move the Cascade or Card so that the position of
			// the mouse on the Cascade/Card doesn't matter.
			// i.e. we can drag the card from any point on the card image
			selectedItem.move(lastPoint, currentPoint);
			lastPoint = currentPoint;
			repaint();
		}

	}

	/**
	 * Handles the mouse moved events to show which Cards can be picked up.
	 * @param event event information for mouse moved.
	 */
	public void mouseMoved(MouseEvent event)
	{
		// Set the cursor to the hand if we are on a card that we can pick up
		Point currentPoint = event.getPoint();
		for (GHand nextHand : allHands)
			if (nextHand.contains(currentPoint)
					&& nextHand.canPickUp(currentPoint))
			{
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				return;
			}

		// Otherwise we just use the default cursor
		setCursor(Cursor.getDefaultCursor());

	}

	// Extra methods needed since we implemented MouseListener
	// Not implemented in this class

	@Override
	public void mouseClicked(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}
}
