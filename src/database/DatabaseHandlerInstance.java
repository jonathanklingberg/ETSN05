package database;

import java.sql.Connection;
import java.sql.Date;
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


public class DatabaseHandlerInstance {
	private static DatabaseHandlerInstance instance = null;
	private static Connection conn;
	protected DatabaseHandlerInstance(Connection conn) {
		DatabaseHandlerInstance.conn = conn;
		//Food for thought: Will this connection live on forever,
		//or will it be closed when the session of the user who created is
		// is closed? If so, there might be a need for a redesign, or
		// setConnection
		//method
	}

	/**
	 * Typical singleton method in order to retrieve the instance of the object
	 * 
	 * @param conn
	 *            A connection to the database
	 * @return The WorkspaceInstance which will be the same in the entire
	 *         system.
	 */
	public static DatabaseHandlerInstance getInstance(Connection conn) {  
		if(instance == null) {
			instance = new DatabaseHandlerInstance(conn);
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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		//Since the user will contain the group id as well,
		//calls both to the "Users" table as well as the
		//"RoleInGroup" table could (should?) be made here, since
		//the user needs to be assigned to the group instantly.

		//Also note that according to the requirements in the base
		//system, the password needs to be 6 characters long, and
		//exactly 6 characters long. This might be a stupid requirement
		//we would like to change later on, just keep it in mind when
		//implementing for now though

		boolean wasAdded = false;
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT into Users(userName, password, isActive) VALUES('" + user.getName() + "', '" + user.getPassword() + "', True)" );
			ps.executeUpdate();
			ps = conn.prepareStatement("select id from Users where userName = '" + user.getName() + "'");
			ResultSet rs = ps.executeQuery();
			rs.next();
			long userid = Integer.parseInt(rs.getString("id"));
			ps.close();
			wasAdded = true;
			User usr = new User(conn, user.getName(), user.getPassword(), userid, user.getGroupId(), user.getRole(), null);
			instance.getProjectGroup(user.getGroupId()).addUser(usr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			PreparedStatement ps = conn.prepareStatement("SELECT Users.id, Users.userName, Users.password, Users.sessionId, RoleInGroup.role, RoleInGroup.groupId FROM Users JOIN RoleInGroup On (Users.id = RoleInGroup.userId)"
					+ " WHERE RoleInGroup.isActiveInGroup = 1 AND Users.isActive = 1");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				long userId = rs.getLong("id");
				String username = rs.getString("userName");
				String password = rs.getString("password");
				String sessionId = rs.getString("sessionId");
				long groupId = rs.getLong("groupID");
				String role = rs.getString("role");
				users.add(new User(conn, username, password, userId, groupId, role, sessionId));
			}
			ps.close();
			rs.close();
		}catch (SQLException e) {
			e.printStackTrace();
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
			PreparedStatement ps = conn.prepareStatement("SELECT * from Users WHERE userName= '" + userName + "'");
			ResultSet rs = ps.executeQuery();
			rs.next();
			long id = rs.getLong("id");
			String password = rs.getString("password");
			String sessionId = rs.getString("sessionId");
			ps = conn.prepareStatement("SELECT * from RoleInGroup WHERE userId = " + id);
			rs = ps.executeQuery();
			rs.next();
			long groupId = rs.getLong("groupId");
			String role = rs.getString("role");
			ps.close();
			return new User(conn, userName, password, id, groupId, role, sessionId);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;			
	}

	/** 
	 * Retrieves a specific user from the database
	 * 
	 * @param userId
	 *            The userId of the user that should be fetched from the
	 *            database
	 * @return The user if it is found in the database, otherwise null
	 */
	public User getUser(long userId) {
		try {
			PreparedStatement ps;
			ps = conn.prepareStatement("SELECT * from Users WHERE id =" + userId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return getUser(rs.getString("userName"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
			rs.next();
			String groupName = rs.getString("groupName");
			rs.close();
			ps.close();
			return new ProjectGroup(conn, id, groupName);
		}catch (SQLException e) {
			System.err.println(e);
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
			rs.next();
			long id = rs.getLong("id");
			System.out.println("Groupid: " + id);
			rs.close();
			ps.close();
			return new ProjectGroup(conn, id, groupName);
		}catch (SQLException e) {
			System.err.println(e);
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
			System.out.println(newGroupName);
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
			ex.printStackTrace();
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
			resultOK = false;
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
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
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
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
			// System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return resultOk;
	}

	/**
	 * Get users in a project group
	 * 
	 * @param groupId The groupId of the project group that the users are in 
	 * @return an ArrayList of Users
	 */
	public ArrayList<User> getUsersInGroup(long groupId) {
		ArrayList<User> usersInGroup = new ArrayList<User>();
		for(User u : getAllUsers()) {
			if(u.getGroupId() == groupId)
			{
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
			Date date = rs.getDate("date");
			long duration = rs.getLong("duration");
			long type = rs.getLong("type");
			long week = rs.getLong("week");
			boolean signed = rs.getBoolean("signed");
			timeReport = new TimeReport(conn, id, userId, groupId, type, duration, week, date, signed);
			rs.close();
			ps.close();
		}catch (SQLException e) {
			System.out.println("FAIL I getTimeReport");
			System.err.println(e);	
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
			e.printStackTrace();
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
			e.printStackTrace();
		}				
		return list;
	}
	private TimeReport createTimeReport(ResultSet rs){
		TimeReport timeReport = null;
		try{
			long id = rs.getLong("id");
			long groupId = rs.getLong("groupId");
			long userId = rs.getLong("userId"); 
			Date date = rs.getDate("date");
			long duration = rs.getLong("duration");
			long type = rs.getLong("type");
			long week = rs.getLong("week");
			boolean signed = rs.getBoolean("signed");
			timeReport = new TimeReport(conn, id, userId, groupId, type, duration, week, date, signed);
		} catch(SQLException e){
			System.err.println(e);
		}
		return timeReport;
	}

	/**
	 * Add a time report tr to the system
	 * @param tr The time report to be added
	 */
	public void addTimeReport(TimeReport tr) {
		try{
			PreparedStatement ps = conn.prepareStatement("insert into TimeReports (userId, groupId, date, duration, type, week, signed) values("
					+ tr.getUserId() + ", " 
					+ tr.getGroupId() + ", '"
					+ tr.getDate().toString() + "', "
					+ tr.getDuration() + ", " 
					+ tr.getType() + ", " 
					+ tr.getWeek() + ", 0);");
			ps.executeUpdate();
			ps.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
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
			PreparedStatement ps = conn.prepareStatement("update Users set userName = '" + newUserName + "', password = '" + newPassword + "' where userName = '" + oldUserName + "'");
			ps.executeUpdate();
			ps = conn.prepareStatement("select * from Users where userName = '"  + newUserName + "'");
			ResultSet rs = ps.executeQuery();
			rs.next();
			long userId = rs.getLong("id");
			long groupId = instance.getProjectGroup(newGroupName).getId();
			ps = conn.prepareStatement("update RoleInGroup set groupId = " + groupId + ", role = '" + role + "' where userId = " + userId);
			ps.executeUpdate();
			ps.close();
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void changeSignatureOfTimeReport(String timereportId) {
		TimeReport tr = instance.getTimeReport(Long.parseLong(timereportId));
		boolean isSigned = tr.isSigned();		
		if(isSigned){
			tr.unsignTimeReport();
		}else{
			tr.signTimeReport();
		}
	}


	//	/**
	//	 * Method for generating the overall structure for the different
	//	 * component classes. Will typically differ depending on the user who
	//	 * is sent as input.
	//	 * 
	//	 * @param user The user who wants to print a project group.
	//	 * @return Returns the components of the page in HTML representation.
	//	 */
	//	public synchronized String toHTML(User user) {
	//		return null;
	//		//If administrator, getProjects and getUsers should be called
	//		//and then for each on each of those objects and call toHTML()
	//		//on them. If project worker, call appropriate methods to
	//		//generate this page instead, etc.
	//		
	//		//If the administrator would like to get the view of a project
	//		//manager (which should be possible since the administrator also
	//		//has the same rights as project manager), an 'ugly' solution
	//		//is proposed here, but feel free to refine it if you find it too 
	//		//ugly.
	//		
	//		//Simply change the role for the user (who is the administrator)
	//		//to "ProjectManager" (before calling this method), 
	//		//and then check what role the user has in this method. That is,
	//		//always check the role to determine which page that should be generated
	//		
	//		//Problem if done this way is that the administrator does not have any
	//		//role defined, however, then it should be null, so if the role is null
	//		//we can be certain that the user is the administrator who wants 
	//		//the administrator page. But as stated earlier, this is kind of
	//		//ugly so there might be a better way to solve it!
	//		
	// //Moreover the setRole(ProjectManager) for the administrator object
	// should not
	//		//have any effect on the database, thus a control needs to be done in
	// //setRole which checks if it is the administrator who the role is set
	// for,
	//		//if so, just set the attribute internally for the class, but do not
	//		//update the database.	
	//		
	//	}


}
