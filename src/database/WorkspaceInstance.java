package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *  This class is a singleton and contains operations that act over the 
 *  entire database. It is typically used by the 'Component' classes
 *  when initially needing information about users or project groups
 *  in the database. Then after having retrieved these objects,
 *  operations can be further carried out on them instead. The class
 *  communicates with a MySQL database via the 'Connection' attribute.
 *  
 *  The class also implements the DatabaseInterface which will provide
 *  methods for generating the project to HTML, as well as removing the
 *  project from the database. However, the remove method will not be
 *  implemented by this class.
 *  
 * @author SG
 * @version 0.2
 * 
 */


public class WorkspaceInstance {
	private static WorkspaceInstance instance = null;
	private static Connection conn;
	protected WorkspaceInstance(Connection conn) {
		WorkspaceInstance.conn = conn;
		//Food for thought: Will this connection live on forever,
		//or will it be closed when the session of the user who created is
		//is closed? If so, there might be a need for a redesign, or setConnection
		//method
	}
	   
	/**
	 * Typical singleton method in order to retrieve the
	 * instance of the object
	 * 
	 * @param conn A connection to the database
	 * @return The WorkspaceInstance which will be the same
	 * in the entire system.
	 */
	public static WorkspaceInstance getInstance(Connection conn) {  
		if(instance == null) {
	         instance = new WorkspaceInstance(conn);
	     }
	      return instance;
	}
	
	/**
 	* Retrieves all project groups in the database
 	* 
 	* @return A list of all the project groups associated with this database,
 	* or an empty list if no project groups exist.
 	*/
	public synchronized List<ProjectGroup> getProjectGroups() {
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
	 * @param projectGroup The project group to add to the database.
	 * @return True if the project group is successfully added to the database,
	 * otherwise false
	 */
	public synchronized boolean addProjectGroup(ProjectGroup projectGroup) {
		boolean wasAdded = false;
		
		try {
			PreparedStatement ps = conn.prepareStatement("INSERT into ProjectGroups(id, groupName) VALUES ("+ projectGroup.id + ", '" + projectGroup.name + "')" );
			ps.executeUpdate();
			wasAdded = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return wasAdded;
	}

	/**
	 * Adds a user to the database
	 * 
	 * @param user The user to be added to the database
	 * @return True if the user is successfully added to the database,
	 * otherwise false
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
			PreparedStatement ps = conn.prepareStatement("INSERT into Users(id, userName, password, sessionId) VALUES('', '" + user.getName() + "', '" + user.getPassword() + "', '" + user.getSessionId() + "')" );
			ps.executeUpdate();
			wasAdded = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return wasAdded;
	}	
	
	/**
	 * Retrieves all users in the database
	 * 
	 * @return A list of all users in the database, or an empty list
	 * if no users exist in the system.
	 */
	public synchronized List<User> getUsers() {
		return null;
	}
	
	/** 
	 * Retrieves a specific user from the database
	 * 
	 * @param userName The username of the user that should be
	 * fetched from the database
	 * @return The user if it is found in the database, otherwise
	 * null
	 */
	public synchronized User getUser(String userName) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT * from Users WHERE userName = '" + userName + "'");
//			PreparedStatement ps = conn.prepareStatement("SELECT Users.id, Users.userName, Users.password, Users.sessionId, RoleInGroup.role FROM Users LEFT JOIN RoleInGroup ON RoleInGroup.groupId =" + id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			long id = rs.getLong("id");
			String password = rs.getString("password");
			String sessionId = rs.getString("sessionId");
//			HttpSession session = request.getSession(true);
//			String sessionId = session.getId();
			
			ps = conn.prepareStatement("SELECT * from RoleInGroup WHERE userId = '" + id + "'");
			rs = ps.executeQuery();
			rs.next();
			long groupId = rs.getLong("groupId");
			String role = rs.getString("role");
			System.out.println("groupId:" + groupId);
			System.out.println("role:" + role);
			ps.close();
			return new User(conn, userName, password, id, groupId, role, sessionId);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;	
		
		}

	/**
	 * Retrieves a specific project group from the database
	 * 
	 * @param id The id of the project group to retrieve
	 * @return The project group that maps to id in the database, or 
	 * null if no such project group is found
	 */
	public synchronized ProjectGroup getProjectGroup(long id) {
		return null;
	}

	
	/**
 	* Retrieves all users from a specified project group
 	* 
 	*  @param id The id of the project group
 	* @return A list of all the project members in a given group,
 	* or an empty list if there are no members in the group.
 	*/
	public synchronized ArrayList<User> getGroupMembers(long id){
		ArrayList<User> membersList = new ArrayList<User>();
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT Users.id, Users.userName, Users.password, Users.sessionId, RoleInGroup.role FROM Users LEFT JOIN RoleInGroup ON RoleInGroup.groupId =" + id);
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
	
	public boolean changeGroupName(long groupNumber, String newGroupName) {
		return false;
	}

	public boolean userIsProjectManager(String name) {
		boolean isManager = false;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Users where name='"+name +"'");
			rs.next();
			long id = rs.getLong("id");
			rs = stmt.executeQuery("select * from RoleInGroup where name='"+name +"'");
			rs.next();
			String role = rs.getString("role");
			isManager = role.equals("Project manager");	
			stmt.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}		
		return isManager;
	}

	public boolean addUser(String name, String password) {
		boolean resultOK = false;
	
		try{
		Statement stmt = conn.createStatement();
		String statement = "insert into Users (name, password) values('" + name
				+ "', '" + password + "')";
		System.out.println(statement);
		stmt.executeUpdate(statement);
		stmt.close();
		resultOK = true;
		} catch (SQLException ex) {
			resultOK = false;
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
	}
		return resultOK;
	}

	public void deleteUser(String name) {
		try{
			Statement stmt = conn.createStatement();
			String statement = "delete from Users where name='" + name + "'"; 
			System.out.println(statement);
		    stmt.executeUpdate(statement); 
		    stmt.close();
			} catch (SQLException ex) {
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		
	}

	public boolean inactivateUser(String name) {
		boolean resultOk = true;
		try{
			Statement stmt = conn.createStatement();
			String statement = "update Users set is_active = 0 where name = '" + name + "'";
			System.out.println(statement);
		    stmt.executeUpdate(statement); 
		    stmt.close();
		} catch (SQLException ex) {
		    resultOk = false;
		    // System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		return resultOk;
	}

	public ResultSet getUsersResultSet() {
		ResultSet rs = null;
		try{
		  Statement stmt = conn.createStatement();		    
		  rs = stmt.executeQuery("select * from Users order by name asc");
		  rs.next();
		  
		  stmt.close();
		} catch (SQLException e){
			System.err.println(e);
		}
		return rs;
	}

	public long getGroupIdOfUser(String name) {
		long groupId = 0;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Users where name='"+name +"'");
			rs.next();
			long id = rs.getLong("id");
			rs = stmt.executeQuery("select * from RoleInGroup where name='"+name +"'");
			rs.next();
			groupId = rs.getLong("groupId");
			stmt.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}		
		return groupId;
	}

	public String getProjectName(long groupId) {
		String groupName = "";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from ProjectGroups where id='"+groupId +"'");
			rs.next();	
			rs.getString("groupName");
			stmt.close();
		} catch (SQLException e) {			
			e.printStackTrace();
		}				
		return groupName;
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
//		//Moreover the setRole(ProjectManager) for the administrator object should not
//		//have any effect on the database, thus a control needs to be done in
//		//setRole which checks if it is the administrator who the role is set for,
//		//if so, just set the attribute internally for the class, but do not
//		//update the database.	
//		
//	}


}