package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import data.Roles;

/**
 *	This class represents a Project Group in the system, and thus
 *	contains methods which are applicable for project groups such
 *  as listing users or time reports belonging to the group. This
 *  is done by making queries to the MySQL database via the connection
 *  attribute.
 *  
 *  The class also implements the DatabaseInterface which will provide
 *  methods for generating the project to HTML, as well as removing the
 *  project from the database
 *  
 *  @author SG
 *  @version 0.2
 */

public class ProjectGroup extends DatabaseInterface {
	protected String name;
	protected long id;
	
	/**
	 * Constructor which should be used when the project group
	 * already exists in the database since it will be possible
	 * to fetch all information needed in this case
	 * 
	 * @param conn The connection to the database
	 * @param id The id of the project group
	 * @param name The name of the project group
	 */
	public ProjectGroup(Connection conn, long id, String name) {
		super();
		this.conn = conn;
		this.id = id;
		this.name = name;
	}
	
	/**
	 * Constructor which should only be used when the project group is about
	 * to be created in the database.
	 * 
	 * @param name The name of the project group
	 */
	public ProjectGroup(String name) {
		this.name = name;
	}
	
	/**
	 * Retrieves all users currently active in the group
	 * 
	 * @return A list of User objects, all of whom are active in
	 * this group. Will return an empty list if no users are active
	 * in the group
	 */
	public List<User> getUsers() {
		//Make SQL statement to get Users from Database,
		//Probably will need to join on Users table
		//and "RoleInGroup", and select that the
		//"activeInGroup" is yes, and project id is the id given
		//in this class
		ArrayList<User> membersList = new ArrayList<User>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT Users.id, Users.userName, Users.password, Users.sessionId, RoleInGroup.role "
					+ "FROM Users LEFT JOIN RoleInGroup ON RoleInGroup.groupId =" + id + " AND Users.isActive = 1");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				long userId = rs.getLong("id");
				String username = rs.getString("userName");
				String password = rs.getString("password");
				String sessionId = rs.getString("sessionId");
				String role = rs.getString("role");
				membersList.add(new User(conn, username, password, userId, id, role, sessionId));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return membersList;
	}
	
	
	/**
	 * Retrieves all time reports in the project group
	 *
	 * @return A list of time reports in this group.
	 * Will return an empty list if no time reports
	 * are yet available
	 */
	public List<TimeReport> getTimeReports() {
		ArrayList<TimeReport> timeReportList = new ArrayList<TimeReport>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * from TimeReports where groupId =" + id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				long reportId = rs.getLong("id");
				long userId = rs.getLong("userId");
				long type = rs.getLong("type");
				long duration = rs.getLong("duration");
				long week = rs.getLong("week");
				Date date = rs.getDate("date");
				boolean signed = rs.getBoolean("signed");		
				timeReportList.add(new TimeReport(conn, reportId, userId, id, type, duration, week, date, signed));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return timeReportList;
	}
	
	/**
	 * Retrieves a time report from the database based on
	 * an given id of the time report
	 * 
	 * @param reportId The id of the time report to be fetched
	 * @return The time report if it exists, otherwise null
	 */
	public TimeReport getTimeReport(long reportId) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * from TimeReports where id =" + reportId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			long userId = rs.getLong("userId");
			long type = rs.getLong("type");
			long duration = rs.getLong("duration");
			long week = rs.getLong("week");
			Date date = rs.getDate("date");
			boolean signed = rs.getBoolean("signed");		
			return new TimeReport(conn, reportId, userId, id, type, duration, week, date, signed);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public long getId() {
		return id;
	}
	
	/**
	 * Adds a time report to the system
	 * 
	 * @param report The time report to add
	 * @return True if the time report was successfully added,
	 * otherwise false
	 */
	public boolean addTimeReport(TimeReport report) {
		boolean wasAdded = false;
		try {
			int signed = report.isSigned() ? 1 : 0;
			PreparedStatement ps = conn.prepareStatement("INSERT into TimeReports(id, userId, groupId, date, duration, type, week, signed) "
					+ "VALUES('', '" + report.getUserId() + "', '" + report.getGroupId() + "', '" + report.getDate() + "', '" + report.getDuration() + "',"
							+ " '" + report.getType() + "', '" + report.getWeek() + "', '" + signed + ")" );
			ps.executeUpdate();
			wasAdded = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wasAdded;
	}
	
	/**
	 * Removes a time report from the project group
	 * 
	 * @param report The time report to be removed
	 * @return True if the time report was successfully removed,
	 * otherwise false
	 */
	public boolean removeTimeReport(TimeReport report) {
		//Don't forget to check that the time report is unsigned!
		boolean wasRemoved = false;
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM TimeReports WHERE id = '" + report.getId() + "' AND signed = 0");
			if(ps.executeUpdate() == 1){
				wasRemoved = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return wasRemoved;
	}
	
	/**
	 * Adds a user to the project
	 * 
	 * @param user The user to be added to the project
	 * @return True if the user was successfully added,
	 * otherwise false
	 */
	public boolean addUser(User user) {
		//Don't forget to check that the user is not
		//the administrator, since he isn't allowed to
		//be a part of a group
		
		return false;
	}
	
	/**
	 * Removes a user from the project
	 * 
	 * @param user The user to be removed from the project
	 * @return True if the user was successfully removed,
	 * otherwise false
	 */
	public boolean removeUser(User user) {
		return false;
	}
	
	/**
	 * Will produce an HTML representation of the project depending on the
	 * user asking for it
	 * 
	 * @param user A user in the system who wants to display this project.
	 * @return Returns the project in HTML representation.
	 */
	public String toHTML(User user) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Will return the name of the project group.
	 * @return The name of the project group.
	 */
	public String getProjectName(){
		return name;
	}

	/**
	 * Will remove the project group from the database, which
	 * in turn will remove all users that are active in the group
	 * as well as all time reports associated with the group
	 * 
	 * @return True if the object successfully managed to remove itself,
	 * otherwise false
	 */
	public boolean removeMe() {
		try {
			System.out.println(id);
			List<User> usersTobeDeleted = getUsers();
			List<TimeReport> timeReportsToDelete = getTimeReports();
			PreparedStatement ps;
			for(int i = 0; i < usersTobeDeleted.size(); i++) {
				ps = conn.prepareStatement("DELETE FROM Users WHERE id = '" + usersTobeDeleted.get(i).getUserId() + "'");
				ps.executeUpdate();
				ps.close();
			}
			for(int i = 0; i < timeReportsToDelete.size(); i++) {
				removeTimeReport(timeReportsToDelete.get(i));
			}
			ps = conn.prepareStatement("DELETE FROM ProjectGroups WHERE id = '" + id + "'");
			ps.executeUpdate();
			ps.close();
			return true;
		}catch(SQLException e) {
			return false;
		}
	}
}
