import java.util.Comparator;

/**
 * The Card class contains data which keeps track of a card object's suit, rank,
 * and whether it is face up or not. This class includes two constructors, a
 * default constructor which is given a rank and suit, or a constructor which
 * creates a card based on a string, a method which flips a card, a method which
 * checks to see whether a card is face up or not, a method to check if a card
 * is an ace, two methods that check whether a card can be placed on a cascade
 * and a foundation respectively, a helper method which checks if two cards have
 * the same colour, a compareTo method, a suit order comparator, and a toString
 * method which displays a card as a string.
 * @author George Zakharov
 * @version December 9, 2014
 */
public class Card implements Comparable<Card>
{
	// 1 - Ace, 2 to 9 , 10 - Ten, 11 - Jack, 12 - Queen, 13 - King
	private int rank;
	// 1 - Clubs, 2 - Diamonds, 3 - Hearts, 4 - Spades
	private int suit;
	// Face up(true), face down(false)
	protected boolean isFaceUp;

	// Two static lists that are used to look up and change the suit/rank of
	// each card from an integer to a character.
	private static final String SUITS = " CDHS";
	private static final String RANKS = " A23456789TJQK";
	// The suit order comparator which changes the compareTo criterion from rank
	// first to suit first.
	public static final Comparator<Card> SUIT_ORDER = new SuitOrder();

	/**
	 * Default constructor which takes a rank and suit value and sets a card
	 * object's suit and rank as that.
	 * @param rank The integer value representing the rank.
	 * @param suit The integer value representing the suit.
	 */
	public Card(int rank, int suit)
	{
		this.rank = rank;
		this.suit = suit;
		// Set the face up value of the card based on the upper/lower case of
		// the suit since the suit is the only value guaranteed to be a letter
		// once looked up in the static lists at the top of the class.
		if (Character.isUpperCase(suit))
			this.isFaceUp = true;

		this.isFaceUp = false;
	}

	/**
	 * Constructor which creates a card object based on two characters passed in
	 * as a string to the constructor.
	 * @param givenCard The string which contains the rank and suit of the card.
	 */
	public Card(String givenCard)
	{
		// CharAt(1) always guaranteed to be a letter (suit)
		if (Character.isUpperCase(givenCard.charAt(1)))
		{
			this.isFaceUp = true;
		}
		this.isFaceUp = false;
		// Change string to uppercase in order to be able to look up the
		// Characters in the static lists at the top of the class
		givenCard = givenCard.toUpperCase();
		this.rank = RANKS.indexOf(givenCard.charAt(0));
		this.suit = SUITS.indexOf(givenCard.charAt(1));
	}

	/**
	 * Flips a card face up.
	 */
	public void flip()
	{
		this.isFaceUp = true;
	}

	/**
	 * Checks to see if a card is face up or face down.
	 * @return A true if the card is face up or false if it is face down.
	 */
	public boolean isFaceUp()
	{
		return this.isFaceUp;
	}

	/**
	 * Checks to see if the card is an ace.
	 * @return A true if it is an ace, false if it is not.
	 */
	public boolean isAce()
	{
		if (this.rank == 1)
			return true;
		else
			return false;
	}

	/**
	 * Checks to see if the card can be place on the top card of a cascade using
	 * a helper method.
	 * @param card The card upon which you are trying to place this card object.
	 * @return A true if it can be done, false if not.
	 */
	public boolean canPlaceOnCascade(Card card)
	{
		// Card is the card you are trying to place on.
		// Check for the same (colour/type) of suit using helper method and
		// check for this object to be 1 less than the card.
		return (!this.isSameColour(card) && this.rank == (card.rank - 1));
	}

	/**
	 * Helper method used by canPlaceOnCascade to determine if two cards have
	 * the same suit or the same colour of suit.
	 * @param other The card to compare to this card object.
	 * @return A true if their suits or colours of suits are the same, false
	 *         otherwise.F
	 */
	public boolean isSameColour(Card other)
	{
		return (this.suit == other.suit || (this.suit + other.suit) == 5);
	}

	/**
	 * Checks to see if a card can be placed on a foundation.
	 * @param card The card on which to place this card object.
	 * @return True if it can be done, false if the card cannot be placed on the
	 *         foundation.
	 */
	public boolean canPlaceOnFoundation(Card card)
	{
		// Card is the card you are trying to place on
		return (this.suit == card.suit && this.rank == (card.rank + 1));
	}

	/**
	 * A method which compares two cards based on the natural order of rank
	 * first then suit.
	 */
	public int compareTo(Card otherCard)
	{
		if (this.rank != otherCard.rank)
			return this.rank - otherCard.rank;
		// If ranks are equal sort by suits
		return otherCard.suit - this.suit;
	}

	/**
	 * Comparator used to modify the natural order (rank first) criterion to
	 * suit first.
	 * @author George Zakharov
	 * @version December 1, 2014
	 */
	public static class SuitOrder implements Comparator<Card>
	{
		/**
		 * Compares the two card objects to determine their order using suits
		 * first then ranks.
		 * @return a value of <0, if the card in this card reference comes first
		 *         by suit before the card in the other card reference a value
		 *         of > 0, if this card comes after the other card by suit and a
		 *         value of 0, if the cards in the two card references are the
		 *         exact same.
		 */
		public int compare(Card first, Card second)
		{
			if (first.suit != second.suit)
				return second.suit - first.suit;
			// If suits are equal sort by ranks
			return second.rank - first.rank;
		}
	}

	/**
	 * Displays the rank then suit of a card object using formatting.
	 */
	public String toString()
	{
		return String.format("%c%c", RANKS.charAt(rank), SUITS.charAt(suit));
	}
}
