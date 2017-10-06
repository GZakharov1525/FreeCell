import java.awt.Point;

/**
 * Keeps track of a Freecell, inherits GHand's methods. Freecells are only one
 * Card But code is simplified if we assume a Freecell is a GHand. This class is
 * very simple and contains only the constructor and 2 other methods: a method
 * to check if the single card can be picked up, and a method to pick up that
 * card.
 * @author Ridout and George Zakharov
 * @version December 9, 2014
 */
public class Freecell extends GHand
{

	/**
	 * Creates a freecell using x,y positions.
	 * @param x X position of the freecell.
	 * @param y Y position of the freecell.
	 */
	public Freecell(int x, int y)
	{
		super(x, y, 0);
	}

	/**
	 * Checks to see if you can pick up the card in this freecell.
	 * @return True if there is a card to pick up, false if the freecell is
	 *         empty.
	 */
	public boolean canPickUp(Point point)
	{
		if (this.contains(point) && this.cardsLeft() == 1)
			return true;
		// Empty cell
		return false;
	}

	/**
	 * Removes the GCard from the freecell returning it as a movable object.
	 * @return A single GCard object.
	 */
	public Movable pickUp(Point point)
	{
		return this.removeTopCard();
	}
}
