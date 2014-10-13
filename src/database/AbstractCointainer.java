package database;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import data.Role;

/** 
 * An abstract class which acts as the super class for
 * classes symbolizing different tables in the database.
 * 
 * @author SG
 * @version 0.3
 *
 */
public abstract class AbstractCointainer {
	protected Connection conn;
	protected static Map<String, HttpSession> usersSessions = new HashMap<String, HttpSession>();
	
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
