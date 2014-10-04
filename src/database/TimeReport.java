package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *	This class represents a Time report in the system, and thus
 *	contains methods which are applicable for time reports such
 *  as signing time reports or adding one the database. This
 *  is done by making queries to the MySQL database via the connection
 *  attribute.
 *  
 *  The class also implements the DatabaseInterface which will provide
 *  methods for generating the project to HTML, as well as removing the
 *  project from the database
 *  
 * @author SG
 * @version 0.1
 */
public class TimeReport implements DatabaseInterface {
	private Connection conn;
	private int id;
	private int groupId;
	private int userId;
	private int type;
	private int duration;
	private Date date;
	private boolean signed;
	
	/**
	 * Constructor which should be used when the time report
	 * already exists in the database since it will be possible
	 * to fetch all information needed in this case
	 * 
	 * @param conn A connection to the database
	 * @param id	The id of the time report
	 * @param userId The user id the time report belongs to
	 * @param groupId The group id the time reports belongs to
	 * @param type The type of the time report
	 * @param duration The duration of the time report
	 * @param date The date which the time report was created
	 * @param signed A boolean stating whether the time report is signed
	 */
	public TimeReport(Connection conn, int id, int userId, int groupId, 
			int type, int duration, Date date, boolean signed) {}
	
	/**
	 * Constructor which should be used when the time report is about
	 * to be created in the database
	 * 
	 * @param conn A connection to the database
	 * @param id	The id of the time report
	 * @param userId The user id the time report belongs to
	 * @param groupId The group id the time reports belongs to
	 * @param type The type of the time report
	 * @param duration The duration of the time report
	 * @param date The date which the time report was created
	 * @param signed A boolean stating whether the time report is signed
	 */
	public TimeReport(Connection conn, int userId, int groupId, int type, 
			int duration, Date date, boolean signed) {}
	
	
	/**
	 * @return True if the time report was signed successfully, false otherwise.
	 */
	public boolean signTimeReport() {
		return false;
	}
	
	/**
	 * @return True if the time report was unsigned successfully, false otherwise.
	 */
	public boolean unsignTimeReport() {
		return false;
	}
	
	/**
	 * @return True if the time report was edited successfully, false otherwise.
	 */
	public boolean editTimeReport() {
		return false;
	}
	
	/**
	 * 
	 * @return The id of the report.
	 */
	public int getId() {
		return -1;
	}
	
	/**
	 * 
	 * @return The group id to which the report belong.
	 */
	public int getGroupId() {
		return -1;
	}
	
	/**
	 * 
	 * @return The user id to which the report belong.
	 */
	public int getUserId() {
		return -1;
	}
	
	/**
	 * 
	 * @return The type of the report.
	 */
	public int getType() {
		return -1;
	}
	
	/**
	 * 
	 * @return Number of minutes reported.
	 */
	public int getDuration() {
		return -1;
	}
	
	/**
	 * 
	 * @return The date the time report was created.
	 */
	public Date getDate() {
		return null;
	}
	
	/**
	 * 
	 * @return True if the report is signed, false otherwise.
	 */
	public boolean getSigned() {
		return false;
	}

	/**
	 * Will produce an HTML representation of the time report depending on the
	 * user asking for it
	 * 
	 * @param user A user in the system who wants to display this time report.
	 * @return Returns the time report in HTML representation.
	 */
	public String toHTML(User user) {
		return null;
	}
	
	/**
	 * Will remove the time report from the database
	 * 
	 * @return True if the object manages to remove itself, otherwise false.
	 */
	public boolean removeMe() {
		//Does not matter whether it's signed or not, since this method
		//only should be called when we are certain that the object
		//actually is to be removed.
		return false;
	}
}
