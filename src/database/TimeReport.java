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
 * This class describes the time report in the database and provides a number of methods on this data.
 */
public class TimeReport implements DatabaseInterface {
	
	/**
	 * @param conn A connection to the database
	 * @param type A type, see documentation
	 * @param value Number of minutes to report, above zero
	 * @param signed If the time report is signed
	 * @param user The user that the time reports belong to
	 */
	public TimeReport(Connection conn, int type, int value, boolean signed, String user) {}
		
	private int type;
	private int value;
	private boolean signed;
	private String user;
	
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
