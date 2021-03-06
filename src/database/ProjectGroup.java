package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import data.Role;

/**
 *	This class represents a Project Group in the system, and thus
 *	contains methods which are applicable for project groups such
 *  as listing users or time reports belonging to the group. This
 *  is done by making queries to the MySQL database via the connection
 *  attribute.
 *  
 *  The class also extends the AbstractCointainer which will provide
 *  methods for generating the project to HTML, as well as removing the
 *  project from the database
 *  
 *  @author SG
 *  @version 0.3
 */
public class ProjectGroup extends AbstractCointainer {
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
		ArrayList<User> membersList = new ArrayList<User>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT Users.id, Users.userName, Users.password, RoleInGroup.role FROM Users JOIN RoleInGroup On (Users.id = RoleInGroup.userId)"
					+ " WHERE RoleInGroup.groupId = " + id + " AND RoleInGroup.isActiveInGroup = 1");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				long userId = rs.getLong("id");
				String username = rs.getString("userName");
				String password = rs.getString("password");
				String role = rs.getString("role");
				membersList.add(new User(conn, username, password, userId, id, role));
			}
			ps.close();
			rs.close();
		}catch (SQLException e) {
			handleSqlErrors(e);
		}
		return membersList;
	}

	
	/**
	 * Gets the total amount of project managers in a project group
	 * 
	 * @return the number of project managers in the project group
	 */
	public int getNumberOfPMs() {
		int amountOfPMs = 0;
		ArrayList<User> users = (ArrayList<User>) getUsers();
		for(int i = 0; i < users.size(); i++) {
			if(users.get(i).getRole().equals("ProjectManager")) {
				amountOfPMs++;
			}
		}
		return amountOfPMs;
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
				String role = rs.getString("role");
				String type = rs.getString("type");
				long duration = rs.getLong("duration");
				long week = rs.getLong("week");
				Date date = rs.getDate("date");
				boolean signed = rs.getBoolean("signed");
				long number = rs.getLong("number");
				char charType = type.charAt(0);
				timeReportList.add(new TimeReport(conn, reportId, userId, role, id, charType, duration, week, date, signed, number));
			}
			ps.close();
			rs.close();
		}catch (SQLException e) {
			handleSqlErrors(e);
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
			String role = rs.getString("role");
			String type = rs.getString("type");
			long duration = rs.getLong("duration");
			long week = rs.getLong("week");
			Date date = rs.getDate("date");
			boolean signed = rs.getBoolean("signed");
			long number = rs.getLong("number");
			char charType = type.charAt(0);
			ps.close();
			rs.close();
			return new TimeReport(conn, reportId, userId, role, id, charType, duration, week, date, signed, number);
		}catch (SQLException e) {
			handleSqlErrors(e);
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
			if(ps.executeUpdate() > 0){
				wasAdded = true;
			}
			ps.close();
		} catch (SQLException e) {
			handleSqlErrors(e);
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
	public boolean removeTimeReport(TimeReport report, String userName) {
		boolean wasRemoved = false;
		try {
			PreparedStatement ps = null;
			if(userName.equals("admin")) {
				ps = conn.prepareStatement("DELETE FROM TimeReports WHERE id = " + report.getId());
			} else {
				ps = conn.prepareStatement("DELETE FROM TimeReports WHERE id = " + report.getId() + " AND signed = 0");
			}
			if(ps.executeUpdate() > 0){
				wasRemoved = true;
			}
			ps.close();
		} catch (SQLException e) {
			handleSqlErrors(e);
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
		boolean wasAdded = false;
		if(!user.getName().equals("admin")){
			try {
				PreparedStatement ps = conn.prepareStatement("insert into RoleInGroup(userId, groupId, role, isActiveInGroup) values("+ user.getUserId() +", " +
					user.getGroupId() +", '" + user.getRole() + "', 1);");
				if(ps.executeUpdate() > 0){
					wasAdded = true;
				}
				ps.close();
			} catch (SQLException e) {
				wasAdded = false;
				handleSqlErrors(e);
			}
		}
		return wasAdded;
	}
	
	/**
	 * Removes a user from the project
	 * 
	 * @param user The user to be removed from the project
	 * @return True if the user was successfully removed,
	 * otherwise false
	 */
	//TODO This method should not be necessary since a user has to be in a group, he/she must be moved instead (which is done in the user class).
//	public boolean removeUser(User user) {
	
	/**
	 * Will produce an HTML representation of the project depending on the
	 * user asking for it
	 * 
	 * @param requestingUserRole A user in the system who wants to display this project.
	 * @return Returns the project in HTML representation.
	 */
	public String toHTML(Role requestingUserRole) {
		// TODO See if any functionality could be moved here. /J
		return null;
	}
	
	/**
	 * Will return the name of the project group.
	 * @return The name of the project group.
	 */
	public String getName(){
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
		boolean successfullyDeleted = false;
		try {
			List<User> usersTobeDeleted = getUsers();
			List<TimeReport> timeReportsToDelete = getTimeReports();
			
			for(int i = 0; i < usersTobeDeleted.size(); i++) {
				usersTobeDeleted.get(i).removeMe();
			}
			for(int i = 0; i < timeReportsToDelete.size(); i++) {
				removeTimeReport(timeReportsToDelete.get(i), "admin");
			}
			PreparedStatement ps;
			//Kan inte k�ra kontroll p� executeUpdate, eftersom denna returnerar noll om det inte finns n�gon roleInGroup f�rknippat med gruppen!! Detta funkar, �ndra 
			//INTE / S & M
			ps = conn.prepareStatement("DELETE FROM RoleInGroup WHERE groupId = " + id);
			ps.executeUpdate();
			
			ps = conn.prepareStatement("DELETE FROM ProjectGroups WHERE id = " + id );
			if(ps.executeUpdate() > 0){
				successfullyDeleted = true;
			}
			ps.close();
			return successfullyDeleted;
		}catch(SQLException e) {
			handleSqlErrors(e);
			return successfullyDeleted;
		}
	}
}
