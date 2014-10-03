package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *
 * This class describes the time report in the database and provides a number of methods on this data.
 * @author SG
 * @version 0.1
 */
public class TimeReport implements DatabaseInterface {
	

	public TimeReport(Connection conn, int id, int userId, int groupId, int type, int duration, Date date, boolean signed) {}
	
	public TimeReport(Connection conn, int userId, int groupId, int type, int duration, Date date, boolean signed) {}
	
	private int id;
	private int groupId;
	private int userId;
	private int type;
	private int duration;
	private Date date;
	private boolean signed;
	
	/**
	 * @param user The user who wants to sign the time report.
	 * @return True if the time report was signed successfully, false otherwise.
	 */
	public boolean signTimeReport(User user) {
		return false;
	}
	
	/**
	 * @param user The user who wants to unsign the time report.
	 * @return True if the time report was unsigned successfully, false otherwise.
	 */
	public boolean unsignTimeReport(User user) {
		return false;
	}
	
	/**
	 * @param user The user who wants to edit the time report.
	 * @return True if the time report was edited successfully, false otherwise.
	 */
	public boolean editTimeReport(User user) {
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
	 * @param user A user in the system who wants to display this time report.
	 * @return Returns the time report in HTML representation.
	 */
	public String toHTML(User user) {
		return null;
	}
	
	/**
	 * @return True if the object manages to remove itself, otherwise false.
	 */
	public boolean removeMe() {
		return false;
	}
}
