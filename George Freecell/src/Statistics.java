import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

// check for ongoing game boolean
// write update method(boolean parameter win/lost game), call update every
// win/loss/quit early (window adapter inside main)
@SuppressWarnings("serial")
/**
 * This class keeps track of a player's statistics regarding the freecell 
 * game contains methods to add to the number of games played, number of wins 
 * achieved by player, add to their win streak, and store their longest win streak, 
 * as well as methods to read and write to/from the file.
 * @author Ridout and George Zakharov
 * @version December 9, 2014
 */
public class Statistics implements Serializable
{
	// List of variables to keep track of various stats.
	public int numberOfWins;
	public int numberOfGames;
	public int currentStreak;
	public int longestStreak;

	/**
	 * Class constructor which creates an object that keeps track of the user's
	 * statistics.
	 */
	public Statistics()
	{
		numberOfWins = 0;
		numberOfGames = 0;
		currentStreak = 0;
		longestStreak = 0;
		writeToFile("stats.dat");
	}

	/**
	 * Adds to the number of games played by user.
	 */
	public void numOfGames()
	{
		numberOfGames++;
		// Update stats
		writeToFile("stats.dat");
	}

	/**
	 * Adds to the number of wins achieved by user.
	 */
	public void win()
	{
		numberOfWins++;
		currentStreak();
		// Update stats
		writeToFile("stats.dat");
	}

	/**
	 * Calculates the win percentage of the user by diving number of wins 
	 * by number of games.
	 * @return A double number which represents the user's iwn percentage.
	 */
	public double winPercent()
	{
		if (numberOfGames == 0)
			return 0;
		System.out.println(numberOfGames);
		return numberOfWins / numberOfGames;

	}

	/**
	 * Adds to user's current win streak.
	 */
	public void currentStreak()
	{
		currentStreak++;
		longestStreak();
		writeToFile("stats.dat");
	}

	/**
	 * Updates the longest win streak achieved by the user.
	 */
	public void longestStreak()
	{
		if (currentStreak > longestStreak)
			longestStreak = currentStreak;
		writeToFile("stats.dat");
	}

	/**
	 * Writes to the object storing all statistics of the user.
	 * @param fileName The object which contains the data.
	 */
	public void writeToFile(String fileName)
	{
		try
		{
			ObjectOutputStream fileOut = new ObjectOutputStream(
					new FileOutputStream(fileName));
			fileOut.writeObject(this);
			fileOut.close();
		}
		catch (IOException exp)
		{
			System.out.println("Error writing to file");
		}
	}

	/**
	 * Reads user's statistics from the object.
	 * @param fileName Object which contains the data.
	 * @return The statistics of the object contains.
	 */
	public static Statistics readFromFile(String fileName)
	{
		try
		{
			ObjectInputStream fileIn = new ObjectInputStream(
					new FileInputStream(fileName));
			Statistics stats = (Statistics) fileIn.readObject();
			fileIn.close();
			return stats;
		}
		catch (Exception exp)
		{
			return new Statistics();
		}
	}
}
