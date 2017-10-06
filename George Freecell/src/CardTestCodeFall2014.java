import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Code to test the Card, Deck and Hand classes
 *
 * @author Ridout
 * @version November 2014
 */

public class CardTestCodeFall2014
{
	public static void main(String[] args) throws FileNotFoundException
	{
		// Code to test the Card class
		// Comment out any code you haven't written a method for yet

		String studentName = "Solutions";
		System.out.println("Card classes test code for: " + studentName);

		// Check default constructor
		Deck oneDeck = new Deck();
		System.out.println("\nCards left in full deck: "+ oneDeck.cardsLeft());
		// Check the Deck class and the Hand class
		// Create a new shuffled Deck
		Deck threeDecks = new Deck(3);
		threeDecks.shuffle();
		System.out.println("Cards left in 3 decks: "+ threeDecks.cardsLeft());

		// Deal a few cards and then display the deck again
		while (threeDecks.cardsLeft() > 10)
			threeDecks.dealCard();
		System.out.println("\nDisplay partial deck, after dealing some cards");
		System.out.println(threeDecks);

		// Make the first Hand from all of the Cards in the Deck
		// Flip every other card as dealt
		// Check the getValue method by looking at the total value of all Cards
		// Re-shuffle to start with the new deck
		threeDecks.shuffle();
		int totalValue = 0;
		Hand firstHand = new Hand();
		boolean flipNext = true;
		int noOfFlipped = 0;
		while (threeDecks.cardsLeft() > 0)
		{
			Card nextCard = threeDecks.dealCard();
			if (flipNext)
				nextCard.flip();
			flipNext = !flipNext;
//			totalValue += nextCard.getValue();
			if (nextCard.isFaceUp())
				noOfFlipped++;
			firstHand.addCard(nextCard);
		}

		System.out.println("\nTotal value for a deck: " + totalValue);
		System.out.println("No of Flipped Cards: " + noOfFlipped);
		System.out.println("Dealing a card from an empty deck: " + threeDecks.dealCard());

		// Re-shuffle to check flip back and isAce
		threeDecks.shuffle();
		noOfFlipped = 0;
		int aceCount = 0;
		while (threeDecks.cardsLeft() > 0)
		{
			Card nextCard = threeDecks.dealCard();
			if (nextCard.isFaceUp())
				noOfFlipped++;
			if (nextCard.isAce())
				aceCount++;
		}

		System.out.println("\nNo of Flipped Cards after shuffle: "
				+ noOfFlipped);
		System.out.println("Ace count: " + aceCount);

		// Make a second Hand from the Cards in a String
		Hand secondHand = new Hand("9H 9C 6D KH JS 5C AD 7S 8S JH 6C 8H TD " +
				"3C 3D 6H AH QD 2S 9S AC 8D TC 5D 7D QH " +
				"4D QC QS 3H 6S KD KS TS 7H 7C 4C 9D JC " +
				"AS 8C KC 2C 4S 2H TH 5S 2D 3S 5H 4H JD");

		// Display the shuffled cards and the ordered cards
		System.out.println("\nShuffled Cards");
		System.out.println(firstHand);
		System.out.println("Cards sorted by Suit then rank");
		firstHand.sortBySuit();
		
		// Divide up firstHand to check for sort
		String firstHandStr = firstHand.toString();
		int strSize = 154;
		for (int suit = 0; suit < 4; suit++)
		System.out.println(firstHandStr.substring(suit*strSize,(suit+1)*strSize));
		System.out.println("Cards sorted by Rank then suit");
		secondHand.sortByRank();
		System.out.println(secondHand);

		// Check the clear method
		secondHand.clear();
		System.out.println("\nEmpty Hand: *" + secondHand + "*");

		// Code to test the isAce method
		// This code should only display the 12 Aces
		// Flip every other ace
		System.out.println("\nDisplay only the aces (half flipped)");
		threeDecks.shuffle();
		flipNext = true;
		Hand aceHand = new Hand();
		while (threeDecks.cardsLeft() > 0)
		{
			Card nextCard = threeDecks.dealCard();
			if (flipNext)
				nextCard.flip();
			flipNext = !flipNext;
			if (nextCard.isAce())
				aceHand.addCard(nextCard);
		}
		aceHand.sortBySuit();
		System.out.println(aceHand);

		// Check the Blackjack methods with some hands in a file
		System.out.println("\n\nBlack jack hands test from hands in file");
		Scanner handFile = new Scanner(new File("hands.txt"));
		totalValue = 0;
		while (handFile.hasNextLine())
		{
			String handStr = handFile.nextLine();
			Hand nextHand = new Hand(handStr);
//			int value = nextHand.getValue();
//			totalValue += value;
		//	System.out.print(nextHand + " Value: " + value);
//			if (nextHand.isBlackJack())
//				System.out.println(" BLACK JACK");
//			else
				System.out.println();
		}
		handFile.close();

		System.out.println("Total value for all file hands: " + totalValue);
		System.out.println("\nCard classes test is finished");
	}
}
