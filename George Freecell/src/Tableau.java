import java.awt.Point;

/**
 * Keeps track of a moving Cascade know as a Tableau, inherits Cascade's
 * methods. Contains a variable which keeps track of the cascade from which this
 * tableau was taken from. The following methods are included in this class:
 * constructor, method used to move each card in the tableau, a method to check
 * if a tableau intersects with a cascade, a method that determines if it can be
 * placed on a freecell/foundation/cascade, and a method which places this
 * tableau onto a cascade.
 * @author Ridout and George Zakharov
 * @version December 9, 2014
 */
public class Tableau extends Cascade implements Movable
{
	@SuppressWarnings("unused")
	private GHand sourceHand;

	/**
	 * Constructor for the tableau class, created at indicated starting position
	 * using cards from the originating cascade.
	 * @param x Starting X position of this object.
	 * @param y Starting Y position of this object.
	 * @param sourceHand The cascade which originally contained the cards in
	 *            this tableau.
	 */
	public Tableau(int x, int y, Cascade sourceHand)
	{
		super(x, y);
		this.sourceHand = sourceHand;
	}

	/**
	 * Moves each card in the tableau from intial position to the final position
	 * by adding the difference between the two positions.
	 */
	public void move(Point initialPos, Point finalPos)
	{
		position.x += finalPos.x - initialPos.x;
		position.y += finalPos.y - initialPos.y;
		for (Card next : hand)
		{
			((GCard) next).move(initialPos, finalPos);
		}
	}

	/**
	 * Checks to see if this tableau object intersects with a cascade object by
	 * comparing their rectangles.
	 * @return True if the two intersects, false if they do not.
	 */
	public boolean intersects(GHand otherHand)
	{
		return (otherHand.getRectangle().intersects(this.getRectangle()));
	}

	/**
	 * Checks to see if this tableau can be placed on a cascade, foundation and
	 * freecell automatically return false as they only accept a single card at
	 * a time.
	 * @return True if you can place this tableau object onto the cascade, false
	 *         if you cannot.
	 */
	public boolean canPlaceOn(GHand otherHand)
	{
		// otherHand is the cascade on which you are trying to place this
		// tableau object onto
		if (otherHand instanceof Foundation || otherHand instanceof Freecell)
			return false;

		if (otherHand.cardsLeft() == 0)
			return true;

		// Store the top card of the cascade which you want to place this
		// tableau on
		Card topCard = otherHand.getTopCard();
		// Store the bottom card of the tableau
		Card cardToPlace = this.hand.get(0);
		// Return a boolean statement of the comparison
		return (cardToPlace.canPlaceOnCascade(topCard));
	}

	/**
	 * Adds all cards in this tableau to the valid cascade using a for loop.
	 */
	public void placeOn(GHand otherHand)
	{
		for (Card next : hand)
		{
			otherHand.addCard((GCard) next);
		}
	}
}
