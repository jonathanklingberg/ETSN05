package base;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import data.Role;
import database.User;
import database.WorkspaceInstance;

/**
 *  This class is the superclass for all servlets in the application. 
 *  It includes basic functionality required by many servlets, like for example a page head 
 *  written by all servlets, and the connection to the database. 
 *  
 *  This application requires a database.
 *  For username and password, see the constructor in this class.
 *  
 *  For database creation statements, please refer to the corresponding STLDD
 *  for the project.
 *  
 *  @author Martin Host
 *  @version 1.0
 *  
 */
public abstract class ServletBase extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// Define states
	protected static final int LOGIN_FALSE = 0;
	protected static final int LOGIN_TRUE = 1;	

	protected Connection conn;
	protected WorkspaceInstance instance;
	protected HttpSession session;
	/**
	 * Constructs a servlet and makes a connection to the database. 
	 * It also writes all user names on the console for test purpose. 
	 */
	public ServletBase() {
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://vm26.cs.lth.se/puss1403?" +
					"user=puss1403&password=9dpa2oan");
			instance = WorkspaceInstance.getInstance(conn);
			Statement stmt = conn.createStatement();		    
			ResultSet rs = stmt.executeQuery("select * from Users"); // Just for testing purposes
			System.out.println("Successfully connected to database!"); // Success message in console
			stmt.close();

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Checks if a user is logged in or not.
	 * @param request The HTTP Servlet request (so that the session can be found)
	 * @return true if the user is logged in, otherwise false.
	 */
	protected boolean isLoggedIn(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		Object objectState = session.getAttribute("state");
		int state = LOGIN_FALSE;
		if (objectState != null) 
			state = (Integer) objectState; 
		return (state == LOGIN_TRUE);

	}

    
    /**
     * Can be used to construct form elements.
     * @param par Input string
     * @return output string = "par" 
     */
    protected String formElement(String par) {
    	return '"' + par + '"';
    }
    
    
    /**
     * Constructs the header of all servlets. 
     * @return String with html code for the header. 
     */
    protected String getPageIntro() {
    	//TODO include Footable js
    	String intro = "<html><head>" + 
    						"<script src=\"js/jquery-1.8.3.js\"></script>" +
							"<script src=\"js/jquery-ui-1.9.2.custom.min.js\"></script>" +
							"<script src=\"js/epuss.js\"></script>" +
							"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/jquery-ui-1.9.2.custom.min.css\"/>" +
    						"<title> The Base Block System </title></head><body>";
    	return intro;
    }
    protected String getViewLayoutStart(){
    	return "";
    }    
    protected String getViewLayoutSEnd(){
    	return "";
    }
    
// Print User Table according to mockup design in SRS
	protected void printUserTable(PrintWriter out, ArrayList<User> userList) {
		out.println(getUserTableName());
		out.println("<table border=" + formElement("1") + ">");	
		if(isAdmin()){
			out.println("<br/><a href=\"administrationcomponent?addNewUser=\" onclick="+ formElement("return createUser(this);") + "><input type=\"button\" value=\"Add new\"/></a>");
		}
		printTableHeader(out);	    
		for(int i = 0; i < userList.size(); i++) {			
			String name = userList.get(i).getName();
			System.out.println(name);
			String pw = userList.get(i).getPassword();
			String role = userList.get(i).getRole();
			String group = instance.getProjectGroup(userList.get(i).getGroupId()).getProjectName();
			String editURL = "administrationcomponent?edituser="+name;
			String editCode = "<a href=" + formElement(editURL) +" onclick="+formElement("return confirm('Are you sure you want to edit "+name+"?')") + "> edit </a>";
			String deleteURL = "administrationcomponent?deleteuser="+name;
			String deleteCode = "<a href=" + formElement(deleteURL) +" onclick="+formElement("return confirm('Are you sure you want to delete "+name+"?')") + "> delete </a>";
			if (name.equals("admin")){
				deleteCode = "";
			}
			printUser(out, name, role, group, editCode, deleteCode);
		}
		
		out.println("</table>");
	}
	//make private!!!!
	protected void printUser(PrintWriter out, String name, String role, String group,
			String editCode, String deleteCode) {
		out.println("<tr>");
		out.println("<td>" + name + "</td>");
		out.println(isAdmin()? ("<td>" + group + "</td>") : "");
		out.println("<td>" + role + "</td>");
		out.println(isAdmin()? ("<td>" + group + "</td>") : "");
		out.println(isAdminOrProjectManager()? ("<td>" + editCode + "</td>") : "");
		out.println(isAdmin()? ("<td>" + deleteCode + "</td>") : "");
		out.println("</tr>");
	}
	//make privatE !!!!
	protected void printTableHeader(PrintWriter out) {
		out.println("<tr>");
		out.println("<td><B>Name</B></td>");
		out.println(isAdmin()? "<td><B>Group</B></td>" : "");
		out.println("<td><B>Role</B></td>");
		out.println(isAdmin()? "<td><B>Password</B></td>" : "");
		out.println(isAdminOrProjectManager()? "<td><B>Edit</B></td>" : "");
		out.println(isAdmin()? "<td><B>Remove</B></td></tr>" : "");
	}
	protected String getRole(){
		Object roleObj = session.getAttribute("role");
		String role = "";
		if (roleObj != null) {
			role = (String) roleObj; // if the name exists typecast the role
		}
		return role;
	}
	
	protected String getName(){
		String name = "";
		Object nameObj = session.getAttribute("name");
		if (nameObj != null) {
			name = (String) nameObj; // if the name exists typecast the name
			// to a string
		}
		return name;
	}

	protected abstract boolean isAdminOrProjectManager();
	protected abstract boolean isAdmin();
	protected abstract String getUserTableName();

}