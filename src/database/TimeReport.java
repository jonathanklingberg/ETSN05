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
 * @version 0.2
 */
public class TimeReport extends DatabaseInterface {
	private long id;
	private long groupId;
	private long userId;
	private int type;
	private long duration;
	private long week;
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
	 * @param week The week which the time report concerns
	 * @param date The date which the time report was created
	 * @param signed A boolean stating whether the time report is signed
	 */
	public TimeReport(Connection conn, long id, long userId, long groupId, 
			long type, long duration, long week, Date date, boolean signed) {}
	
	/**
	 * Constructor which only should be used when the time report is about
	 * to be created in the database
	 * 
	 * @param userId The user id the time report belongs to
	 * @param groupId The group id the time reports belongs to
	 * @param type The type of the time report
	 * @param duration The duration of the time report
	 * @param week The week which the time report concerns
	 * @param date The date which the time report was created
	 * @param signed A boolean stating whether the time report is signed
	 */
	public TimeReport(long userId, long groupId, long type, 
			long duration, long week, Date date, boolean signed) {}
	
	
	/**
	 * Signs the current time report. If it already is signed, it will
	 * still be signed after this operation.
	 * 
	 * @return True if the time report was signed successfully, false otherwise.
	 */
	public boolean signTimeReport() {
		return false;
	}
	
	/**
	 * Unsigns the current time report.  If it already is unsigned, it will
	 * still be unsigned after this operation.
	 * 
	 * @return True if the time report was unsigned successfully, false otherwise.
	 */
	public boolean unsignTimeReport() {
		return false;
	}
	
	/**
	 * Performs an update on the object. Should typically be called after having
	 * used one or more setters, since the setters themselves do not update
	 * the database. It is not until this method is called that the actual
	 * changes will be pushed to it.
	 * 
	 * @return True if the time report was updated successfully, false otherwise.
	 */
	public boolean updateTimeReport() {
		return false;
	}
	
	/**
	 * Getter for the id of the time report.
	 * 
	 * @return The id of the report.
	 */
	public long getId() {
		return -1;
	}
	
	/**
	 * Getter for the group id of the time report.
	 * 
	 * @return The group id to which the report belong.
	 */
	public long getGroupId() {
		return -1;
	}
	
	/**
	 * Getter for the user id of the time report.
	 * 
	 * @return The user id to which the report belong.
	 */
	public long getUserId() {
		return -1;
	}
	
	/**
	 * Getter for the type of this time report.
	 * 
	 * @return The type of the report.
	 */
	public int getType() {
		return -1;
	}
	
	/**
	 * Getter for the duration of this time report.
	 * 
	 * @return Number of minutes reported.
	 */
	public long getDuration() {
		return -1;
	}
	
	/**
	 * Getter for the date of this time report.
	 * 
	 * @return The date the time report was created.
	 */
	public Date getDate() {
		return null;
	}
	
	/**
	 * Getter for the week of this time report.
	 * 
	 * @return The week which the time report concerns.
	 */
	public long getWeek() {
		return -1;
	}
	
	/**
	 * Determines whether the report is signed
	 * 
	 * @return True if the report is signed, false otherwise.
	 */
	public boolean isSigned() {
		return false;
	}
	
	/**
	 * Setter for the id of the time report
	 * 
	 * @param id The id to change to.
	 */
	public void setId(long id) {
		//Note that this should just be set internally here
		//like this.id = id, and the changes are actually pushed
		//to the database when updateTimeReport() is called.
		//(The SQL statement is created here)
	}
	
	/**
	 * Setter for the user id of the time report.
	 * 
	 * @param userId The userId to change to.
	 */
	public void setUserId(long userId) {
		//See setId
	}
	
	/**
	 * Setter for the group id of this time report.
	 * 
	 * @param groupId The groupId to change to.
	 */
	public void setGroupId(long groupId) {
		//See setId
	}
	
	/**
	 * Setter for the type of this time report
	 * 
	 * @param type The type to change to.
	 */
	public void setType(int type) {
		//See setId
	}
	
	/**
	 * Setter for the duration of this time report
	 * 
	 * @param duration The duration to change to.
	 */
	public void setDuration(long duration) {
		//See setId
	}
	
	/**
	 * Setter for the week of this time report
	 * 
	 * @param week The week to change to.
	 */
	public void setWeek(long week) {
		//See setId
		
		//Moreover the user will never enter an explicit value
		//for the week, this will be calculated based on the date
		//instead before calling this method. Thus if setWeek
		//is called, setDate should have been called as well
	}
	
	/**
	 * Setter for the date of this time report.
	 * 
	 * @param date The date to change to.
	 */
	public void setDate(Date date) {
		//See setId
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
