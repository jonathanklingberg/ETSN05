package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *  This class is a singleton and contains operations that act over the 
 *  entire database. It is typically used by the 'Component' classes
 *  when initially needing information about users or project groups
 *  in the database. Then after having retrieved these objects,
 *  operations can be further carried out on them instead. The class
 *  communicates with a MySQL database via the 'Connection' attribute.
 *  
 * @author SG
 * @version 0.3
 * 
 */

//TODO Remember to always close BOTH resultset and eventual preparedStatements!
//TODO Please in case you catch a sql-error then redirect the error-object to handleSqlErrors(e); /J

public class DatabaseHandlerInstance {
	private static DatabaseHandlerInstance instance = null;
	private static Connection conn;
	protected DatabaseHandlerInstance() {
//		DatabaseHandlerInstance.conn = conn;
		try{

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection("jdbc:mysql://vm26.cs.lth.se/puss1403?" +
				"user=puss1403&password=9dpa2oan");
		
		System.out.println("Successfully connected to database!");

		//TODO BETTER ERROR HANDLING! /J
		} catch (SQLException e) {
			handleSqlErrors(e);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Typical singleton method in order to retrieve the instance of the object
	 * 
	 * @param conn
	 *            A connection to the database
	 * @return The WorkspaceInstance which will be the same in the entire
	 *         system.
	 */
	public static DatabaseHandlerInstance getInstance() {  
		if(instance == null) {
			instance = new DatabaseHandlerInstance();
		}
		return instance;
	}

	/**
	 * Retrieves all project groups in the database
	 * 
	 * @return A list of all the project groups associated with this database,
	 * or an empty list if no project groups exist.
	 */
	public synchronized List<ProjectGroup> getAllProjectGroups() {
		List<ProjectGroup> pgList = new ArrayList<ProjectGroup>();		
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ProjectGroups");
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
				long id = rs.getLong("id");
				String groupName = rs.getString("groupName");
				pgList.add(new ProjectGroup(conn, id, groupName));
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			handleSqlErrors(e);
		}
		return pgList;
	}

	/**
	 * Adds a project group to the database
	 * 
	 * @param projectGroup
	 *            The project group to add to the database.
	 * @return True if the project group is successfully added to the database,
	 * otherwise false
	 */
	public synchronized boolean addProjectGroup(ProjectGroup projectGroup) {
		boolean wasAdded = false;
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT into ProjectGroups(groupName) VALUES ('" + projectGroup.name + "')" );
			ps.executeUpdate();
			wasAdded = true;
			ps.close();
		} catch (SQLException e) {
			handleSqlErrors(e);
		}		
		return wasAdded;
	}

	/**
	 * Adds a user to the database
	 * 
	 * @param user
	 *            The user to be added to the database
	 * @return True if the user is successfully added to the database, otherwise
	 *         false
	 */
	public synchronized boolean addUser(User user) {
		//TODO validate that the user object has all of the needed attributes,
		//for example the user need to be assigned a group when created, 
		//also make sure the password was valid according to length aso. /J

		boolean wasAdded = false;
		try {
			//TODO Doesn't feel like this check is needed, the userName column should be defined unique in the database already?
			// this is just another unnecessary call to the database. /J
			//Answer: This is to check that admin doesnt try to add someone with the same username. If so, we return false, and write appropiate fail msg /M
			PreparedStatement ps = conn.prepareStatement("select * from Users where userName = '" + user.getName() + "'");
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return false;
			}
			ps = conn.prepareStatement("INSERT into Users(userName, password) VALUES('" + user.getName() + "', '" + user.getPassword() + "')");
			ps.executeUpdate();
			ps = conn.prepareStatement("select id from Users where userName = '" + user.getName() + "'");
			rs = ps.executeQuery();
			rs.next();
			long userid = Integer.parseInt(rs.getString("id"));
			ps.close();
			wasAdded = true;
			User usr = new User(conn, user.getName(), user.getPassword(), userid, user.getGroupId(), user.getRole());
			//TODO really don't get the idea behind adding the user to instance-group, but it might just be me? /J
			//also why no check if this was succeeded if it's needed? 
			//right now we say it went OK even though we could not add the user to the instance-group? /J
			instance.getProjectGroup(user.getGroupId()).addUser(usr);
		} catch (SQLException e) {
			handleSqlErrors(e);
		}
		return wasAdded;
	}	

	/**
	 * Retrieves all users in the database
	 * 
	 * @return A list of all users in the database, or an empty list if no users
	 *         exist in the system.
	 */
	public synchronized ArrayList<User> getAllUsers() {
		ArrayList<User> users = new ArrayList<User>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT Users.id, Users.userName, Users.password, RoleInGroup.role, RoleInGroup.groupId FROM Users JOIN RoleInGroup On (Users.id = RoleInGroup.userId)"
					+ " WHERE RoleInGroup.isActiveInGroup = 1");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				long userId = rs.getLong("id");
				String username = rs.getString("userName");
				String password = rs.getString("password");
				long groupId = rs.getLong("groupID");
				String role = rs.getString("role");
				users.add(new User(conn, username, password, userId, groupId, role));
			}
			ps.close();
			rs.close();
		}catch (SQLException e) {
			handleSqlErrors(e);
		}
		return users;
	}


	/** 
	 * Retrieves a specific user from the database
	 * 
	 * @param userName
	 *            The user name of the user that should be fetched from the
	 *            database
	 * @return The user if it is found in the database, otherwise null
	 */
	public synchronized User getUser(String userName) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT Users.id, Users.password, RoleInGroup.groupId, RoleInGroup.role FROM Users JOIN RoleInGroup On (Users.id = RoleInGroup.userId)"
					+ " WHERE userName='"+userName+"' AND RoleInGroup.isActiveInGroup = 1");
			ResultSet rs = ps.executeQuery();
			if(rs.next()){			
				long userId = rs.getLong("id");
				String password = rs.getString("password");
				Long groupId = rs.getLong("groupId");
				String role = rs.getString("role");
				ps.close();
				rs.close();
				return new User(conn, userName, password, userId, groupId, role);
			}
		}catch (SQLException e) {
			handleSqlErrors(e);
		}
		return null;			
	}
	
	//TODO JavaDoc which is not good /J
	/** 
	 * Retrieves a specific user from the database
	 * 
	 * @param userId
	 *            The userId of the user that should be fetched from the
	 *            database
	 * @return The user if it is found in the database, otherwise null
	 */
	public User getUser(long userId) {
		User user = null;
		try {
			PreparedStatement ps;
			ps = conn.prepareStatement("SELECT * from Users WHERE id =" + userId);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				user = getUser(rs.getString("userName"));
			} else {
				//TODO Me no like at all! manipulating user attributes in this manner is not accepted.
				// I see what you want to achieve but this is not the correct approach for that!
				// Please refactor asap! /J
				user = new User("<i>**removed user**</i>", "<i>**removedUserPassword**</i>", "<i>**removed user**</i>", -1);
			}
			ps.close();
			rs.close();
		} catch (SQLException e) {
			handleSqlErrors(e);
		}
		return user;
	}


	/**
	 * Retrieves a specific project group from the database
	 * 
	 * @param id
	 *            The id of the project group to retrieve
	 * @return The project group that maps to id in the database, or null if no
	 *         such project group is found
	 */
	public synchronized ProjectGroup getProjectGroup(long id) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ProjectGroups where id =" + id + ";");
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				String groupName = rs.getString("groupName");
				rs.close();
				ps.close();
				return new ProjectGroup(conn, id, groupName);
			}
		}catch (SQLException e) {
			handleSqlErrors(e);
		}
		return null;
	}	
	
	
	/**
	 * Retrieves a specific project group from the database
	 * 
	 * @param groupName
	 *            The groupName of the project group to retrieve
	 * @return The project group that maps to groupName in the database, or null if no
	 *         such project group is found
	 */
	public ProjectGroup getProjectGroup(String groupName) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM ProjectGroups where groupName ='" + groupName + "';");
			ResultSet rs = ps.executeQuery();
			if(rs.next()){				
				long id = rs.getLong("id");
				rs.close();
				ps.close();
				return new ProjectGroup(conn, id, groupName);
			}
		}catch (SQLException e) {
			handleSqlErrors(e);
		}
		return null;
	}


	/**
	 * Change the name of a project group
	 * 
	 * @param groupNumber The groupId
	 * @param newGroupName The new group name
	 * @return True if successful, otherwise false.
	 */
	public boolean changeGroupName(long groupNumber, String newGroupName) {
		try{
			PreparedStatement p = conn.prepareStatement("select * from ProjectGroups where groupName='" + newGroupName + "'");
			ResultSet rs = p.executeQuery();
			if(!rs.next()) {
				PreparedStatement ps2 = conn.prepareStatement("update ProjectGroups set groupName='" + newGroupName + "'" + "where id= '" + groupNumber + "'");
				ps2.executeUpdate();
				ps2.close();
				p.close();
				rs.close();
				return true;
			}
		} catch (SQLException ex) {
			handleSqlErrors(ex);
		}
		return false;
	}

	/**
	 * Add a new user to the system
	 * 
	 * @param name The name of the new user
	 * @param password The new user's password
	 * @return True if successful, otherwise false
	 */
	public boolean addUser(String name, String password) {
		boolean resultOK = false;	
		try{
			PreparedStatement ps = conn.prepareStatement("insert into Users (name, password) values('"
					+ name + "', '" + password + "')");
			ps.executeUpdate();
			ps.close();
			resultOK = true;
		} catch (SQLException ex) {
			handleSqlErrors(ex);
		}
		return resultOK;
	}

	/**
	 * Delete a user from the system
	 * @param name The user name of the user that should be deleted
	 */
	public void deleteUser(String name) {
		try{
			PreparedStatement ps = conn.prepareStatement("delete from Users where name='" + name + "'");
			ps.executeUpdate();
			ps.close();
		} catch (SQLException ex) {
			handleSqlErrors(ex);
		}
	}

	/**
	 * Set users status to inactive
	 * 
	 * @param name The user name of the user to inactivate
	 * @return True if successful, otherwise false
	 */
	public boolean inactivateUser(String name) {
		boolean resultOk = true;
		try{
			PreparedStatement ps = conn.prepareStatement("update Users set is_active = 0 where name = '"
					+ name + "'");
			ps.executeUpdate();
			ps.close();
		} catch (SQLException ex) {
			resultOk = false;
			handleSqlErrors(ex);
		}
		return resultOk;
	}

	/**
	 * Get users in a project group
	 * 
	 * @param groupId The groupId of the project group that the users are in 
	 * @return an ArrayList of Users or empty set.
	 */
	public ArrayList<User> getUsersInGroup(long groupId) {
		ArrayList<User> usersInGroup = new ArrayList<User>();
		for(User u : getAllUsers()) {
			if(u.getGroupId() == groupId){
				usersInGroup.add(u);
			}
		}
		return usersInGroup;
	}

	
	/** 
	 * Retrieves a specific time report from the database
	 * 
	 * @param id  The id of the time report that should be fetched from the
	 *            database
	 * @return The time report if it is found in the database, otherwise null
	 */
	public TimeReport getTimeReport(long id){
		TimeReport timeReport = null;
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * from TimeReports WHERE id = " + id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			long userId = rs.getLong("userId"); 
			long groupId = rs.getLong("groupId");
			String role = rs.getString("role");
			Date date = rs.getDate("date");
			long duration = rs.getLong("duration");
			String type = rs.getString("type");
			long week = rs.getLong("week");
			long number = rs.getLong("number");
			boolean signed = rs.getBoolean("signed");
			char charType = type.charAt(0);
			timeReport = new TimeReport(conn, id, userId, role, groupId, charType, duration, week, date, signed, number);
			rs.close();
			ps.close();
		}catch (SQLException e) {
			handleSqlErrors(e);
		}
		return timeReport;
	}

	/**
	 * Retrieves all time reports belonging to a specified user
	 * 
	 *  @param userId The id of the user
	 * @return A list of all the time reports belonging to a specified user,
	 * or an empty list if there are no time reports.
	 */
	public ArrayList<TimeReport> getUsersTimeReportsOfUser(long userId){
		ArrayList<TimeReport> list = new ArrayList<TimeReport>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeReports WHERE userId = " + userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				list.add(createTimeReport(rs));	
			}			
			rs.close();
			ps.close();
		} catch (SQLException e) {			
			handleSqlErrors(e);
		}				
		return list;
	}

	/**
	 * Retrieve a project groups time reports
	 * 
	 * @param groupId The project group id from where the time reports should be retrieved
	 * @return An ArrayList of time reports
	 */
	public ArrayList<TimeReport> getTimeReportsOfGroup(long groupId) {
		ArrayList<TimeReport> list = new ArrayList<TimeReport>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * FROM TimeReports WHERE groupId = " + groupId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){	
				list.add(createTimeReport(rs));
			}			
			rs.close();
			ps.close();
		} catch (SQLException e) {			
			handleSqlErrors(e);
		}				
		return list;
	}
	
	private TimeReport createTimeReport(ResultSet rs){
		TimeReport timeReport = null;
		try{
			long id = rs.getLong("id");
			long groupId = rs.getLong("groupId");
			String role = rs.getString("role");
			long userId = rs.getLong("userId"); 
			Date date = rs.getDate("date");
			long duration = rs.getLong("duration");
			String type = rs.getString("type");
			long week = rs.getLong("week");
			boolean signed = rs.getBoolean("signed");
			long number = rs.getLong("number");
			char charType = type.charAt(0);
			timeReport = new TimeReport(conn, id, userId, role, groupId, charType, duration, week, date, signed, number);
		} catch(SQLException e){
			handleSqlErrors(e);
		}
		return timeReport;
	}

	/**
	 * Add a time report tr to the system
	 * @param tr The time report to be added
	 */
	public void addTimeReport(TimeReport tr) {
		try{
			PreparedStatement ps = conn.prepareStatement("insert into TimeReports (userId, groupId, role, date, duration, type, number ,week, signed) values("
					+ tr.getUserId() + ", " 
					+ tr.getGroupId() + ", '"
					+ tr.getRole() + "', '"
					+ tr.getDate().toString() + "', "
					+ tr.getDuration() + ", '" 
					+ tr.getType() + "', "  
					+ tr.getNumber() + ", " 
					+ tr.getWeek() + ", 0);");
			ps.executeUpdate();
			ps.close();
		} catch (SQLException ex) {
			handleSqlErrors(ex);
		}
	}
	
	/**
	 * Edit a timereport in the system
	 *
	 * @param tr The time report to be edited
	 * @return True if update was successful, otherwise false
	 */
	public boolean editTimeReport(long id, long userId, long groupId,
			String role, char type, long duration, long week, Date date, boolean signed, long number){
		if(!instance.getTimeReport(id).isSigned()){
			TimeReport tr = new TimeReport(conn, id, userId, role, groupId, type, duration, week, date, signed, number);
			return tr.updateTimeReport();
		}
		return false;
	}


	/**
	 * Edit a user in the system
	 * 
	 * @param oldUserName Current user name
	 * @param newUserName New user name
	 * @param newPassword New password
	 * @param newGroupName new project group name
	 * @param role new role
	 * @return True if update is successful, false otherwise
	 */
	public boolean editUser(String oldUserName, String newUserName,
			String newPassword, String newGroupName, String role) {
		try{
			PreparedStatement ps;
			ResultSet rs;
			if(!oldUserName.equals(newUserName)){
				//TODO Do not touch code that works!!!! and if you do, check that it works for the thousand time!!!!!! /S
				ps = conn.prepareStatement("select * from Users where userName = '" + newUserName + "'");
				rs = ps.executeQuery();
				if(rs.next()) {
					return false; // return false if userName already exists in Users table
				}
			}
			ps = conn.prepareStatement("update Users set userName = '" + newUserName + "', password = '" + newPassword + "' where userName = '" + oldUserName + "'");
			ps.executeUpdate(); // Update actual Users table
			ps = conn.prepareStatement("select * from Users where userName = '"  + newUserName + "'");
			rs = ps.executeQuery(); // Fetch the ID of updated User. 
			//TODO Rather not perform a separate query to fetch userID! Why not simply include it with the function in-paramters, feels more natural to me? /J
			if(rs.next()){
				long userId = rs.getLong("id");	
				long groupId = instance.getProjectGroup(newGroupName).getId();
				ps = conn.prepareStatement("select * from RoleInGroup where userId = " + userId + " AND groupId = " + groupId + " AND isActiveInGroup=true");
				rs = ps.executeQuery();
				if(!rs.next()) {
					//The userId-groupId pair did not exist in the RoleInGroup table!
					ps = conn.prepareStatement("update RoleInGroup set isActiveInGroup = false where userId = " + userId);
					ps.executeUpdate(); // inactivate user in old group(s).
					ps = conn.prepareStatement("insert into RoleInGroup(userId, groupId, role ,isActiveInGroup) VALUES(" +userId + ", " +groupId + ", '" + role + "', true)");
					ps.executeUpdate(); //insert new entry into the RoleInGroup table.
				} else {
					//The userId-groupId pair already assigned in RoleInGroup, update with separate function.
					changeRoleOfUser(role, userId);
				}
				ps.close();
			}
		} catch(SQLException e) {
			handleSqlErrors(e);
			return false;
		}
		return true;
	}
	
	
	/**
	 * Changes the role of a given user. 
	 * 
	 * @param newRole the users new role
	 * @param userId the users user id
	 * @return true if the role is changed, otherwise false
	 */
	public boolean changeRoleOfUser(String newRole, long userId){
		boolean successfullyChanged = false;
		PreparedStatement ps;
		try {
			ps = conn.prepareStatement("update RoleInGroup set role = '" + newRole + "' where userId = " + userId + " AND isActiveInGroup=true");
			if(ps.executeUpdate() > 0) {
				successfullyChanged = true;
			}
			ps.close();
		} catch (SQLException e) {
			handleSqlErrors(e);
		}
		return successfullyChanged;
	}
	
	//TODO JavaDoc better name would be toggleSignature....
	/**
	 * Changes the signature of a given time report
	 * 
	 * @param timereportId the time reports id number
	 * @return true if the signature is changed, otherwise false
	 */
	public boolean changeSignatureOfTimeReport(String timereportId) {
		boolean changedSignature = false;
		TimeReport tr = instance.getTimeReport(Long.parseLong(timereportId));
		if(tr != null){
			boolean isSigned = tr.isSigned();		
			if(isSigned){
				tr.unsignTimeReport();
			}else{
				tr.signTimeReport();
			}
			changedSignature = true;
		}
		return changedSignature;
	}
	
	//TODO JavaDoc
	//TODO Create a link to the AbstractContainer->handleSqlErrors instead of implementing it here.
	private void handleSqlErrors(SQLException e){
		e.printStackTrace();
	}
}
