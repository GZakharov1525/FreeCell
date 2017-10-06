/**
 * The deck class contains data which keeps track of the array of the deck that
 * contains all of the cards, and the top card index. This class contains the
 * following methods: Two constructors, one for a single deck and one which has
 * an integer parameter which constructs a deck with 1 or more full decks (52
 * cards), a method which deals a card from the deck, a method which identifies
 * how many cards the deck has left, a shuffle method to randomize the cards in
 * the deck and lastly a toString to display the deck for debugging purposes.
 * @author George Zakharov
 * @version December 9, 2014
 */
public class Deck
{
	protected Card[] deck;
	protected int topCard;

	/**
	 * Class constructor which creates all 13 cards of each of the 4 suits.
	 * @param noOfDecks The number of decks to use in the main deck.
	 */
	public Deck(int noOfDecks)
	{
		topCard = 0;
		deck = new Card[52 * noOfDecks];
		for (int decks = 0; decks < noOfDecks; decks++)
		{
			for (int suit = 1; suit < 5; suit++)
			{
				for (int rank = 1; rank < 14; rank++)
				{
					deck[topCard] = (new Card(rank, suit));
					topCard++;
				}
			}
		}
	}

	/**
	 * Default class constructor which calls the other deck constructor using
	 * only one full deck (52 cards).
	 */
	public Deck()
	{
		// calls other deck constructor but with only one deck
		this(1);
	}

	/**
	 * Deals a card from the deck, adjusting the index of the top card as
	 * necessary.
	 * @return The card object which was dealt.
	 */
	public Card dealCard()
	{
		// No cards left in deck
		if (topCard == 0)
			return null;

		// Move top card index down one as a card has been taken out of the
		// deck.
		topCard--;
		return deck[topCard];
	}

	/**
	 * Returns the amount of cards left in the deck.
	 * @return An integer value representing the number of cards remaining in
	 *         the deck.
	 */
	public int cardsLeft()
	{
		return topCard;
	}

	/**
	 * Randomizes the deck of cards using a random number generator.
	 */
	public void shuffle()
	{
		// Zero based arrays
		for (int i = 0; i < deck.length - 1; i++)
		{
			// Generate random number
			int random = (int) (Math.random() * (deck.length - 1));
			// Temporarily store card
			Card temp = deck[i];
			// Replace temporary card's location with the random card's
			deck[i] = deck[random];
			// Put the temporary card into the random card's location
			deck[random] = temp;
		}
		// check to make sure all cards are face down after shuffle
		for (int cards = 0; cards < deck.length - 1; cards++)
		{
			if (deck[cards].isFaceUp() == true)
				deck[cards].flip();
		}
		// re-set topCard to deck length
		topCard = deck.length;
	}

	/**
	 * Overrides an object's toString method to display the contents of the deck
	 * using a string builder.
	 */
	public String toString()
	{
		// show only non-dealt cards
		StringBuilder deckDisplay = new StringBuilder((deck.length - 1) * 2);
		for (int index = 0; index < topCard; index++)
		{
			deckDisplay.append(deck[index].toString());
			deckDisplay.append(" ");
		}
		return deckDisplay.toString();
	}
}
