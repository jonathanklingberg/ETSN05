package base;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.User;
import database.WorkspaceInstance;

/**
 * Servlet implementation class LoginComponent
 * 
 * A log-in page. 
 * 
 * The first thing that happens is that the user is logged out if he/she is logged in. 
 * Then the user is asked for name and password. 
 * If the user is logged in he/she is directed to the page corresponding to his rights
 * in the system. (Administrator, Project Manager, Project Worker). 
 * 
 * @author SG
 * @version 0.2
 *
 * 
 */


@WebServlet("/logincomponent")
public class LoginComponent extends ServletBase {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginComponent() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
     * Generates a form for login. 
     * @return HTML code for the form
     */
    protected String loginRequestForm() {
    	String html = "<p>Please enter your name and password in order to log in:</p>";
    	html += "<p> <form name=" + formElement("input");
    	html += " method=" + formElement("post");
    	html += "<p> Name: <input type=" + formElement("text") + " name=" + formElement("userName") + '>'; 
    	html += "<p> Password: <input type=" + formElement("password") + " name=" + formElement("password") + '>';  
    	html += "<p> <input type=" + formElement("submit") + "value=" + formElement("Submit") + '>';
    	return html;
    }
    
    
    /**
     * Checks with the database if the user should be accepted
     * @param name The name of the user
     * @param password The password of the user
     * @return true if the user should be accepted
     */
    private boolean checkUser(String name, String password) {
    	return instance	.getUser(name).comparePassword(password);
	}

    
    
    /**
	 * Handles input from the user and displays information for login. 
	 * 
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Get the session
		HttpSession session = request.getSession(true);
		
		int state;

		PrintWriter out = response.getWriter();
		out.println(getPageIntro());
		String name;
		String password;
		
		if (isLoggedIn(request)) {
			session.setAttribute("state", LOGIN_FALSE);
			out.println("<p>You are now logged out</p>");
		}
		
		name = request.getParameter("userName"); // get the string that the user entered in the form
        password = request.getParameter("password"); // get the entered password
        
        System.out.println("User:" + name);
        System.out.println("password:" + password);
        if (name != null && password != null) {
        	if (checkUser(name, password)) {
	        		state = LOGIN_TRUE;
	       			session.setAttribute("state", state);  // save the state in the session
	       			session.setAttribute("name", name);  // save the name in the session
//	       			This will be needed as soon as the database has been created.
//	       			saveRoleToSession(session);
	       			response.sendRedirect("functionality.html");
       		}
       		else {
       			out.println("<p>That was not a valid user name / password. </p>");
       			out.println(loginRequestForm());
       		}
       	}else{ // name was null, probably because no form has been filled out yet. Display form.
       		out.println(loginRequestForm());
       	}
        
		out.println("</body></html>");
	}
	
	/**
	 * Performa another sql query to fetch the role of logged in user and store this info to session.
	 */
	private void saveRoleToSession(HttpSession session) {	
		try{
			Statement stmt = conn.createStatement();		    
		    ResultSet rs = stmt.executeQuery("select role from RolesInGroup where userid='+ userID +'"); 
		    while (rs.next()) {
		    	String role = rs.getString("role");
		    	if (role == null && session.getAttribute("name") == "admin") { // admin role=null in db, need special handling
		    		role = "admin";
		    	}
		    	session.setAttribute("role", role);
		    }
		    stmt.close();
		} catch (SQLException ex) {
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * All requests are forwarded to the doGet method. 
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	// Never actually lists users, thus does not need a heading for such a table
	protected String getUserTableHeading() {
		return "";
	}
	
//	protected boolean setActive(String username){
//    	boolean resultOk = true;
//    	try{
//			Statement stmt = conn.createStatement();
//			String statement = "update users set is_active = 1 where name = '" + username + "'";
//			System.out.println(statement);
//		    stmt.executeUpdate(statement); 
//		    stmt.close();
//			
//		} catch (SQLException ex) {
//		    resultOk = false;
//		    // System.out.println("SQLException: " + ex.getMessage());
//		    System.out.println("SQLState: " + ex.getSQLState());
//		    System.out.println("VendorError: " + ex.getErrorCode());
//		}
//    	return resultOk;
//	}
}
