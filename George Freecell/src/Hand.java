import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * The Hand class contains the data for the list of cards in each hand. This
 * class also contains two constructors, one for an empty hand and one for a
 * given hand, as well as the following methods to manipulate the hand: adding a
 * card to the hand, sorting the hand by rank or by suit, emptying a hand,
 * determining the number of cards left in the hand and the toString method to
 * display the hand for debugging purposes.
 * @author George Zakharov
 * @version December 9, 2014
 */
public class Hand
{
	// Array list to keep track of a hand
	protected ArrayList<Card> hand;

	/**
	 * Default constructor which creates an empty hand.
	 */
	public Hand()
	{
		// Creates empty hand
		hand = new ArrayList<Card>();
	}

	/**
	 * Constructor which creates a hand based on the a string.
	 * @param str String which contains all of the cards of the hand.
	 */
	public Hand(String str)
	{
		// Initialize scanner
		Scanner handStr = new Scanner(str);
		// Initialize array list
		hand = new ArrayList<Card>(str.length() / 3 + 1);
		// Add all cards in the string to the hand
		while (handStr.hasNext())
		{
			hand.add(new Card(handStr.next()));
		}
		// Close scanner to prevent memory leak
		handStr.close();
	}

	/**
	 * Adds a given card to the hand.
	 * @param card Card to be added to hand.
	 */
	public void addCard(Card card)
	{
		hand.add(card);
	}

	/**
	 * Sorts hand by natural order of rank first.
	 */
	public void sortByRank()
	{
		Collections.sort(hand);
	}

	/**
	 * Sorts hand using a specific comparator which changes the natural order
	 * from rank first, to suits first.
	 */
	public void sortBySuit()
	{
		Collections.sort(hand, Card.SUIT_ORDER);
	}

	/**
	 * Clears all cards out of the hand.
	 */
	public void clear()
	{
		// Empty the hand
		hand.clear();
	}

	/**
	 * Displays the number of cards in a hand.
	 * @return An integer value representing the number cards in the hand.
	 */
	public int cardsLeft()
	{
		return hand.size();
	}

	/**
	 * Overrides the object's toString method to display the hand as a string.
	 */
	public String toString()
	{
		return hand.toString();
	}
}
