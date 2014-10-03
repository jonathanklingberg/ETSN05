package database;

/** 
 * A simple interface with two methods which all non-components 
 * classes will implement, thus all classes interacting with 
 * the database in any way should implement this interface
 * 
 * @author SG
 * @version 0.1
 *
 */
public interface DatabaseInterface {
	
	/**
	 * Creates an HTML representation of the object depending on
	 * what object and which user that asks for it
	 * 
	 * @param user The user who wants to print the object in HTML.
	 * @return  A HTML representation of the object.
	 */
	public String toHTML(User user);
	
	/**
	 * Removes the object completely from the database. What actually
	 * will be removed is different for different objects and is 
	 * elaborated on in the classes which implement this interface.
	 * 
	 * @return True if the object is able to remove itself,
	 * otherwise false
	 */
	public boolean removeMe();
}
