/**
 * Keeps track of a Move. This class includes two methods, the constructor and a
 * method to undo a move.
 * @author ICS4U and George Zakharov
 * @version December 7, 2014
 */
public class Move
{
	private GHand from; // Where you are moving from
	private GHand to; // Where you are moving to
	private Movable moved; // What you have moved

	/**
	 * Creates a move object which contains information regarding the move that
	 * was made.
	 * @param from What cascade you are moving an object from.
	 * @param to What cascade you are moving an object to.
	 * @param moved What object you have moved from one cascade to another.
	 */
	public Move(GHand from, GHand to, Movable moved)
	{
		this.from = from;
		this.to = to;
		this.moved = moved;
	}

	/**
	 * Allows the user to undo a move by reversing the actions taken during a
	 * move.
	 */
	public void undo()
	{
		// Check to identify what type of object was moved, then use appropriate
		// actions to reverse either case.
		if (moved instanceof GCard)
		{
			to.removeTopCard();
			moved.placeOn(from);
		}
		if (moved instanceof Tableau)
		{
			// Cycle through all cards in the tableau that was moved and move
			// them back.
			int size = ((GHand) moved).cardsLeft();
			for (int cycle = 0; cycle < size; cycle++)
			{
				GCard object = to.removeCard(cycle);
				from.addCard(object);
			}

		}
	}

}
