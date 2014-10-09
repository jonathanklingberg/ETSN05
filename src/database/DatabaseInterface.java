package database;

import java.sql.Connection;

import data.Role;

/** 
 * A simple interface with two methods which all non-components 
 * classes will implement, thus all classes interacting with 
 * the database in any way should implement this interface
 * 
 * @author SG
 * @version 0.2
 *
 */
public abstract class DatabaseInterface {
	protected Connection conn;
	/**
	 * Creates an HTML representation of the object depending on
	 * what object and which user that asks for it
	 * 
	 * @param requestingUserRole The user who wants to print the object in HTML.
	 * @return  A HTML representation of the object.
	 */
	// It is suspected that this is not an awesome solution...
	public abstract String toHTML(Role requestingUserRole);
	
	/**
	 * Removes the object completely from the database. What actually
	 * will be removed is different for different objects and is 
	 * elaborated on in the classes which implement this interface.
	 * 
	 * @return True if the object is able to remove itself,
	 * otherwise false
	 */
	public abstract boolean removeMe();
}
