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
 * @version 0.1
 *
 * 
 */


@WebServlet("/LoginComponent")
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
    	html += "<p> Name: <input type=" + formElement("text") + " name=" + formElement("user") + '>'; 
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
		
		boolean userOk = false;
		boolean userChecked = false;
		
		try{
			Statement stmt = conn.createStatement();		    
		    ResultSet rs = stmt.executeQuery("select * from users"); 
		    while (rs.next( ) && !userChecked) {
		    	String nameSaved = rs.getString("name"); 
		    	String passwordSaved = rs.getString("password");
		    	int isActive = rs.getInt("is_active");
		    	System.out.println("is_active: " + isActive);
		    	if (name.equals(nameSaved)) {
		    		userChecked = true;
		    		if(password.equals(passwordSaved) && isActive == 1){
	    				userOk = true;
		    		}
		    	}
		    }
		    stmt.close();
		} catch (SQLException ex) {
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		return userOk;
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
		
		if (loggedIn(request)) {
			session.setAttribute("state", LOGIN_FALSE);
			out.println("<p>You are now logged out</p>");
		}
		
		name = request.getParameter("user"); // get the string that the user entered in the form
        password = request.getParameter("password"); // get the entered password
        if (name != null && password != null) {
        	if (checkUser(name, password)) {
	        		state = LOGIN_TRUE;
	       			session.setAttribute("state", state);  // save the state in the session
	       			session.setAttribute("name", name);  // save the name in the session
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
	 * All requests are forwarded to the doGet method. 
	 * 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
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
