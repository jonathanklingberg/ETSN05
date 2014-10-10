package base;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.Role;
import database.User;
import database.DatabaseHandlerInstance;

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
	 * Handles input from the user and displays information for login. 
	 * 
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Get the session
		session = request.getSession(true);
		
		int state;

		PrintWriter out = response.getWriter();
		out.println(getPageIntro());
		String name;
		String password;
		
		if (isLoggedIn(request)) {
			session.setAttribute("state", LOGIN_FALSE);
			session.invalidate(); // "session.destroy()"
			System.out.println("Session.destroy()");
			out.println("<p>You are now logged out</p>");
		}
		
		name = request.getParameter("userName"); // get the string that the user entered in the form
        password = request.getParameter("password"); // get the entered password
        
        if (name != null && password != null) {
        	System.out.println("User:" + name);
        	System.out.println("password:" + password);
        	User currUser = DatabaseHandlerInstance.getInstance(conn).getUser(name);
        	if(currUser.comparePassword(password)){
        		state = LOGIN_TRUE;
       			session.setAttribute("state", state);  // save the state in the session
       			session.setAttribute("name", name);  // save the name in the session
       			session.setAttribute("userId", currUser.getUserId());  // save the userId in the session
       			session.setAttribute("sessionid", session.getId());
       			session.setAttribute("role", currUser.getRole());
       			switch (currUser.getRole()) {
       			case "Admin":
       				System.out.println("***REDIRECT TO ADMIN-PAGE***");
       				response.sendRedirect("administrationcomponent");
       				break;
       			case "ProjectManager":
       				System.out.println("***REDIRECT TO PM-PAGE***");
       				response.sendRedirect("projectmanagercomponent");
       				break;
       			case "SystemArchitect": case "Developer": case "Tester": case "Unspecified":
       				System.out.println("***REDIRECT TO WORKER-PAGE***");
       				response.sendRedirect("workercomponent");
       				break;
       			default:
       				System.out.println("***REDIRECT TO FUNCTIONALITY-PAGE***");
       				response.sendRedirect("functionality.html");
       				break;
       			}
       		} else {
       			out.println("<p>That was not a valid user name / password. </p>");
       			out.println(loginRequestForm());
       		}
       	}else{ // name was null, probably because no form has been filled out yet. Display form.
       		out.println(loginRequestForm());
       		//TODO "wrong" username text goes here!
       	}
        
		out.println("</body></html>");
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
	protected String getUserTableName() {
		return "";
	}

	//Never used
	protected boolean isAdminOrProjectManager() {
		return false;
	}
	
	//Never used
	protected boolean isAdmin() {
		return false;
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
