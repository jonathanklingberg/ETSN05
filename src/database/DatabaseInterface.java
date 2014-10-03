package database;

/**
 * 
 * An interface that defines methods that should be callable on all the data from the database.
 * @author SG
 * @version 0.1
 *
 */
public interface DatabaseInterface {
	
	/**
	 * 
	 * @param user The user who wants to print the object in HTML.
	 * @return  A HTML representation of the object.
	 */
	public String toHTML(User user);
	
	/**
	 * 
	 * @return True if it succeeds, false otherwise.
	 */
	public boolean removeMe();
}
