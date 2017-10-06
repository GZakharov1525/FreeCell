import java.awt.Point;

/**
 * Keeps track of a Cascade's position, inherits GHand's methods. This class's
 * methods include the constructor, a method that determines if a movable object
 * can be picked up from this cascade and a method which picks up that object.
 * @author Ridout and George Zakharov
 * @version December 9, 2014
 */
public class Cascade extends GHand
{
	// Keeps track of free cascades, use for checking valid moves
	// FORMULA:
	// (freecells + 1) * 2^number of free cascades(not including cascade to be
	// move to)
	protected static int freeCascades = 8;

	/**
	 * Constructor for the cascade class.
	 * @param x X position of the cascade.
	 * @param y Y position of the cascade.
	 */
	public Cascade(int x, int y)
	{
		super(x, y, 20);
	}

	/**
	 * Determines if a movable object (GCard or Tableau) can be taken from a
	 * cascade.
	 * @return True if a movable object can be retrieved, false if no objects
	 *         can be picked up.
	 */
	public boolean canPickUp(Point point)
	{
		// Empty cascade, nothing to pick up.
		if (hand.size() == 0)
			return false;
		// If the top card is the card selected, it can be picked up for sure.
		if (this.getTopCard().contains(point))
			return true;
		// Start with the upper card of the cascade.
		int upperCardIndex = cardsLeft() - 1;
		// Now look through the cascade upwards (at the cards before the upper
		// card)
		// Checking if cards no longer contain the point (gone past the selected
		// card) and if they can be stacked and picked up as a tableau.
		while (!((GCard) hand.get(upperCardIndex)).contains(point))
		{
			// Check if card can be place on the card behind (before) it.
			if (!this.hand.get(upperCardIndex).canPlaceOnCascade(
					this.hand.get(upperCardIndex - 1)))
			{
				return false;
			}
			else
			{
				// Go back to the last card that could be put into a tableau.
				upperCardIndex--;
			}
		}
		return true;
	}

	/**
	 * Checks to see which what objects can be picked up from the cascade either
	 * a single card (GCard) or 2 or more cards (Tableau).
	 * @return A movable object, GCard or Tableau.
	 */
	public Movable pickUp(Point point)
	{
		// If only the top card can be picked up, return a GCard
		if (this.getTopCard().contains(point))
			return this.removeTopCard();
		// Find the index of the card selected by the user.
		int selectedCardIndex = 0;
		// Cycle through hand(cascade) checking for the first card that contains
		// the point as it will be the first card of the tableau.
		for (int cycle = hand.size() - 1; cycle >= 0; cycle--)
		{
			if (((GCard) hand.get(cycle)).contains(point))
			{
				// Selected card found
				selectedCardIndex = cycle;
				// Quit check
				cycle = -1;
			}
		}
		// Get the GCard that was clicked on.
		GCard selectedCard = (GCard) hand.get(selectedCardIndex);
		// Use the position of the selected card as a starting point from where
		// the tableau object will be created.
		Point cardReference = selectedCard.getPosition();
		// Create emtpy tableau object
		Tableau toMove = new Tableau(cardReference.x,
				cardReference.y,
				this);
		// Add cards to tableau starting with the selected card and going down
		// to the top card of the cascade.
		while (selectedCardIndex < hand.size())
		{
			// Remove card from cascade and add to tableau
			toMove.addCard(this.removeCard(selectedCardIndex));
		}
		return toMove;
	}
}
