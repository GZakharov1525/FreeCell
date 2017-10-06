import java.awt.Point;

/**
 * Keeps track of a Foundation, inherits GHand's methods. This class is very
 * simple, it contains a constructor, a method to check if a card can be picked
 * up (always false), and a method to pick up a card (also false, or null in
 * this case).
 * @author Ridout and George Zakharov
 * @version December 9, 2014
 */
public class Foundation extends GHand
{
	/**
	 * Creates a foundation using given x,y co-ordinates.
	 * @param x X position of the foundation.
	 * @param y Y position of the foundation.
	 */
	public Foundation(int x, int y)
	{
		super(x, y, 0);
	}

	/**
	 * Checks to see if a card can be picked up from the foundation, which is
	 * always false in freecell.
	 * @return false.
	 */
	public boolean canPickUp(Point point)
	{
		return false;
	}

	/**
	 * Does not pick up any cards, since you cannot pick up any cards off the
	 * foundation.
	 * @return Null
	 */
	public Movable pickUp(Point point)
	{
		return null;
	}
}
