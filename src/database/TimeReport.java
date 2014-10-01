package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 
 * @author seb
 * This class describes the time report in the database and 
 */
public class TimeReport implements DatabaseInterface {
	
	/**
	 * 
	 * @param conn
	 * @param type
	 * @param value
	 * @param signed
	 * @param user
	 */
	public TimeReport(Connection conn, int type, int value, boolean signed, String user) {}
	
	/**
	 * 
	 * @param conn
	 */
	public TimeReport(Connection conn) {}
	
	
	private int type;
	private int value;
	private boolean signed;
	private String user;
	
	/**
	 * 
	 * @param user The user who wan't to sign the time report.
	 * @return True if the time report was signed successfully, false otherwise.
	 */
	public boolean signTimeReport(User user) {
		return false;
	}
	
	/**
	 * 
	 * @param user The user who want's to edit the time report.
	 * @return True if the time report was edited successfully, false otherwise.
	 */
	public boolean editTimeReport(User user) {
		return false;
	}

	/**
	 * @param user Should be a ProjectManager or Administrator, otherwise null will be returned.
	 * @return Returns the user in HTML representation.
	 */
	public String toHTML(User user) {
		return null;
	}
	
	/**
	 * 
	 * @return True if the object manages to remove itself, otherwise false
	 */
	public boolean removeMe() {
		return false;
	}
	
	
	

}
