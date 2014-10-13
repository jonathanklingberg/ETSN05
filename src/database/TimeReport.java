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
public class TimeReport extends AbstractCointainer {
	private long id;
	private long groupId;
	private long userId;
	private long type;
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
			long type, long duration, long week, Date date, boolean signed) {
			this.conn = conn;
			this.id = id;
			this.userId = userId;
			this.groupId = groupId;
			this.type = type;
			this.duration = duration;
			this.week = week;
			this.date = date;
			this.signed = signed;
	}
	
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
			long duration, long week, Date date, boolean signed) {
		this.userId = userId;
		this.groupId = groupId;
		this.type = type;
		this.duration = duration;
		this.week = week;
		this.date = date;
		this.signed = signed;
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
			ps.executeUpdate();
			wasSigned = true;
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			ps.executeUpdate();
			wasUnsigned = true;
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(date);
			PreparedStatement ps = conn.prepareStatement("UPDATE TimeReports SET signed=" + signStatus + 
					", week=" + week + 
					", type=" + type + 
					", duration="+ duration + 
					", date=" + dateFormat +
					", groupId=" + groupId + 
					", userId=" + userId + 
					" WHERE id=" + id +";" );
			ps.executeUpdate();
			successfullyExecutedStatement = true;
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	 * Getter for the type of this time report.
	 * 
	 * @return The type of the report.
	 */
	public long getType() {
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
	public void setType(long type) {
		this.type = type;
	}
	
	/**
	 * Setter for the duration of this time report
	 * 
	 * @param duration The duration to change to.
	 */
	public void setDuration(long duration) {
		this.duration=duration;
	}
	
	/**
	 * Setter for the week of this time report.
	 * 
	 * @param week The week to change to.
	 */
	private void setWeek(long week) {
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
			PreparedStatement ps = conn.prepareStatement("DELETE from TimeReports WHERE id= " + id);
			ps.executeUpdate();
			successfullyRemovedTimeReport = true;
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return successfullyRemovedTimeReport;
	}
}
