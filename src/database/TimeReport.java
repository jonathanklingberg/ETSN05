package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import data.Role;

/**
 *	This class represents a Time report in the system, and thus
 *	contains methods which are applicable for time reports such
 *  as signing time reports or adding one the database. This
 *  is done by making queries to the MySQL database via the connection
 *  attribute.
 *  
 *  The class also extends the AbstractCointainer which will provide
 *  methods for generating the project to HTML, as well as removing the
 *  project from the database
 *  
 * @author SG
 * @version 0.3
 */

//TODO Don't forget to close both preparedstatements and resultsets!
// Please redirect errors to handleSqlErrors(error) as error handler /J
public class TimeReport extends AbstractCointainer {
	private long id;
	private long groupId;
	private long userId;
	private String role;
	private char type;
	private long duration;
	private long week;
	private long number;
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
	 * @param role TODO
	 * @param groupId The group id the time reports belongs to
	 * @param type The type of the time report
	 * @param duration The duration of the time report
	 * @param week The week which the time report concerns
	 * @param date The date which the time report was created
	 * @param signed A boolean stating whether the time report is signed
	 * @param number A number that represents the type category
	 */
	public TimeReport(Connection conn, long id, long userId, String role, 
			long groupId, char type, long duration, long week, Date date, boolean signed, long number) {
			this.conn = conn;
			this.id = id;
			this.userId = userId;
			this.groupId = groupId;
			this.role = role;
			this.type = type;
			this.duration = duration;
			this.week = week;
			this.date = date;
			this.signed = signed;
			this.number = number;
	}
	
	/**
	 * Constructor which only should be used when the time report is about
	 * to be created in the database
	 * @param groupId The group id the time reports belongs to
	 * @param role TODO
	 * @param type The type of the time report
	 * @param userId The user id the time report belongs to
	 * @param duration The duration of the time report
	 * @param week The week which the time report concerns
	 * @param date The date which the time report was created
	 * @param signed A boolean stating whether the time report is signed
	 * @param number A number that represents the type category
	 */
	public TimeReport(long groupId, String role, char type, 
			long userId, long duration, long week, Date date, boolean signed, long number) {
		this.userId = userId;
		this.groupId = groupId;
		this.role = role;
		this.type = type;
		this.duration = duration;
		this.week = week;
		this.date = date;
		this.signed = signed;
		this.number = number;
	}
	
	
	/**
	 * Signs the current time report. If it already is signed, it will
	 * still be signed after this operation.
	 * 
	 * @return True if the time report was signed successfully, false otherwise.
	 */
	public boolean signTimeReport() {
		boolean wasSigned = false;
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE TimeReports SET signed='1' WHERE id=" + id + ";" );
			int result = ps.executeUpdate();
			if(result>0){
				wasSigned = true;				
			}
			ps.close();
			
		} catch (SQLException e) {
			handleSqlErrors(e);
		}
		return wasSigned;
	}
	
	/**
	 * Unsigns the current time report.  If it already is unsigned, it will
	 * still be unsigned after this operation.
	 * 
	 * @return True if the time report was unsigned successfully, false otherwise.
	 */
	public boolean unsignTimeReport() {
		boolean wasUnsigned = false;
		try {
			PreparedStatement ps = conn.prepareStatement("UPDATE TimeReports SET signed='0' WHERE id=" + id + ";" );
			int result = ps.executeUpdate();
			if(result>0){
				wasUnsigned = true;				
			}
			ps.close();
		} catch (SQLException e) {
			handleSqlErrors(e);
		}
		return wasUnsigned;
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
		boolean successfullyExecutedStatement = false;
		try {
			int signStatus = signed? 1: 0;
			PreparedStatement ps = conn.prepareStatement("UPDATE TimeReports SET userId= " + userId + ", groupId= " + groupId + ", role= '" + role + "', date= '" + date.toString() +
					"', duration= " + duration + ", type= '" + type + "', number=" + number + ", week= " + week + ", signed= " + signStatus +
					" WHERE id= " + id);
			int result = ps.executeUpdate();
			if(result>0){
				successfullyExecutedStatement = true;				
			}
			ps.close();
		} catch (SQLException e) {
			handleSqlErrors(e);
		}
		return successfullyExecutedStatement;
	}
	
	/**
	 * Getter for the id of the time report.
	 * 
	 * @return The id of the report.
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Getter for the group id of the time report.
	 * 
	 * @return The group id to which the report belong.
	 */
	public long getGroupId() {
		return groupId;
	}
	
	/**
	 * Getter for the user id of the time report.
	 * 
	 * @return The user id to which the report belong.
	 */
	public long getUserId() {
		return userId;
	}
	
	/**
	 * Getter for the number of the time report.
	 * 
	 * @return The number to which the report belong.
	 */
	public long getNumber() {
		return number;
	}
	
	/**
	 * Getter for the type of this time report.
	 * 
	 * @return The type of the report.
	 */
	public char getType() {
		return type;
	}
	
	/**
	 * Getter for the duration of this time report.
	 * 
	 * @return Number of minutes reported.
	 */
	public long getDuration() {
		return duration;
	}
	
	/**
	 * Getter for the date of this time report.
	 * 
	 * @return The date the time report was created.
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Getter for the week of this time report.
	 * 
	 * @return The week which the time report concerns.
	 */
	public long getWeek() {
		return week;
	}
	
	/**
	 * Determines whether the report is signed
	 * 
	 * @return True if the report is signed, false otherwise.
	 */
	public boolean isSigned() {
		return signed;
	}
	
	/**
	 * Setter for the id of the time report
	 * 
	 * @param id The id to change to.
	 */
	public void setId(long id) {
		this.id=id;
	}
	
	/**
	 * Setter for the user id of the time report.
	 * 
	 * @param userId The userId to change to.
	 */
	public void setUserId(long userId) {
		this.userId=userId;
	}
	
	/**
	 * Setter for the group id of this time report.
	 * 
	 * @param groupId The groupId to change to.
	 */
	public void setGroupId(long groupId) {
		this.groupId=groupId;
	}
	
	/**
	 * Setter for the type of this time report
	 * 
	 * @param type The type to change to.
	 */
	public void setType(char type) {
		//TODO Validate type number if not already done /J
		//This is done before a TimeReport is created.
		this.type = type;
	}
	
	/**
	 * Setter for the number of this time report
	 * 
	 * @param number The number to change to.
	 */
	public void setNumber(long number) {
		this.number = number;
	}
	
	/**
	 * Setter for the duration of this time report
	 * 
	 * @param duration The duration to change to.
	 */
	public void setDuration(long duration) {
		//TODO Check SRS for conditions for this! /J
		//This is done before a TimeReport is created.
		this.duration=duration;
	}
	
	/**
	 * Setter for the week of this time report.
	 * 
	 * @param week The week to change to.
	 */
	private void setWeek(long week) {
		//TODO check no future week /J
		//This should be done when trying to submit a time report.
		this.week=week;
		//See setId
		
		//Moreover the user will never enter an explicit value
		//for the week, this will be calculated based on the date
		//instead before calling this method. Thus if setWeek
		//is called, setDate should have been called as well
	}
	
	/**
	 * Setter for the date of this time report.
	 * This will in turn call on the setWeek method automatically.
	 * @param date The date to change to.
	 */
	public void setDate(Date date) {
		//TODO check not future date! /J
		//This should be done when trying to submit a time report.
		this.date=date;
		java.util.Calendar calenderWeek = java.util.Calendar.getInstance();
		calenderWeek.setTime(date);
		setWeek(calenderWeek.get(java.util.Calendar.WEEK_OF_YEAR));
	}
	
	/**
	 * Will produce an HTML representation of the time report depending on the
	 * user asking for it
	 * 
	 * @param requestingUserRole A user in the system who wants to display this time report.
	 * @return Returns the time report in HTML representation.
	 */
	public String toHTML(Role requestingUserRole) {
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
		boolean successfullyRemovedTimeReport = false;
		try {
			//TODO Does this work in reality? Are there really no foreign keys that are connected to this table? /J
			PreparedStatement ps = conn.prepareStatement("DELETE from TimeReports WHERE id= " + id);
			ps.executeUpdate();
			successfullyRemovedTimeReport = true;
			ps.close();
		} catch (SQLException e) {
			handleSqlErrors(e);
		}
		return successfullyRemovedTimeReport;
	}

	public String getRole() {
		return role;
	}
}
