import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

/**
 * Keeps track of a Graphical Card (GCard). Inherits data and methods from Card.
 * Keeps track of a position and an Image for each GCard. Also keep track of
 * some static variables for the background image and the width and height of
 * each Card. Includes methods to construct a new Card, look at and change a
 * Card's position and draw this Card. Contains methods used to get/set the
 * position of a GCard, the constructor, a method to draw each GCard, a method
 * to move each GCard, a method to identify whether a GCard contains a point, a
 * method to check whether two GCards intersect, a method to check if one GCard
 * can be placed on another, and a method to place this GCard on another.
 * 
 * @author Ridout and George Zakharov
 * @version December 7, 2014
 *
 */
public class GCard extends Card implements Movable
{
	public final static Image BACK_IMAGE = new ImageIcon("images\\blueback.png")
			.getImage();
	public final static int WIDTH = BACK_IMAGE.getWidth(null);
	public final static int HEIGHT = BACK_IMAGE.getHeight(null);

	private Point position;
	private Image image;

	/**
	 * Constructs a graphical Card.
	 * @param rank the rank of the Card.
	 * @param suit the suit of the Card.
	 * @param position the initial position of the Card.
	 */
	public GCard(int rank, int suit, Point position)
	{
		super(rank, suit);
		this.position = position;
		// Load up the appropriate image file for this card
		String imageFileName = "" + " cdhs".charAt(suit) + rank + ".png";
		imageFileName = "images\\" + imageFileName;
		image = new ImageIcon(imageFileName).getImage();

	}

	/**
	 * Sets the current position of this GCard.
	 * @param position the Card's current position.
	 */
	public void setPosition(Point position)
	{
		this.position = position;
	}

	/**
	 * Gets the current position of this GCard.
	 * @return the Card's current position.
	 */
	public Point getPosition()
	{
		return position;
	}

	/**
	 * Draws a card in a Graphics context.
	 * @param g Graphics to draw the card in.
	 */
	public void draw(Graphics g)
	{
		if (isFaceUp)
			g.drawImage(image, position.x, position.y, null);
		else
			g.drawImage(BACK_IMAGE, position.x, position.y, null);
	}

	/**
	 * Moves a Card by the amount between the initial and final position.
	 * @param initialPos the initial position to start dragging this Card.
	 * @param initialPos the final position to keep dragging this Card.
	 */
	public void move(Point initialPos, Point finalPos)
	{
		position.x += finalPos.x - initialPos.x;
		position.y += finalPos.y - initialPos.y;
	}

	/**
	 * Checks to see whether a point (x,y position) is within a GCard's
	 * rectangle (outline).
	 * @return True if this GCard contains the point, false if it does not.
	 */
	public boolean contains(Point point)
	{
		return (new Rectangle(position.x, position.y, GCard.WIDTH, GCard.HEIGHT)
				.contains(point));
	}

	/**
	 * Checks to see if two GCards cross over top of each other.
	 * @return True if the two GCards overlap, false if they do not.
	 */
	public boolean intersects(GHand otherHand)
	{
		return (otherHand.getRectangle().intersects(new Rectangle(position.x,
				position.y, GCard.WIDTH,
				GCard.HEIGHT)));
	}

	/**
	 * Checks to see if this GCard can be placed on a
	 * Cascade/Freecell/Foundation.
	 * @return True if you can place this GCard on a
	 *         Cascade/Freecell/Foundation, false if it cannot.
	 */
	public boolean canPlaceOn(GHand otherHand)
	{
		// Store the size of the cascade
		int handSize = otherHand.cardsLeft();
		if (otherHand instanceof Foundation)
		{
			// If hand is empty and placing an ace
			if (handSize == 0 && this.isAce())
				return true;
			// if hand is empty but not placing an ace (invalid move)
			if (handSize == 0 && !this.isAce())
				return false;
			// Return a boolean based on if this card can be place on the
			// top card of the cascade
			return (this.canPlaceOnFoundation(otherHand.getTopCard()));
		}
		if (otherHand instanceof Freecell)
		{
			// If there is no card currently occupying the free cell
			if (otherHand.cardsLeft() == 0)
				return true;

			return false; // Freecell is occupied
		}

		// Always place on empty hand
		if (otherHand.cardsLeft() == 0)
			return true;
		// Since otherHand is a cascade, compare the top card of the cascade to
		// this GCard
		return (this.canPlaceOnCascade(otherHand.getTopCard()));
	}

	/**
	 * Places this GCard onto the cascade.
	 */
	public void placeOn(GHand otherHand)
	{
		otherHand.addCard(this);
	}
}
