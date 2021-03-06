package base;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.User;

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
 * @version 0.3
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
    	String html = "<form name=\"input\" method=\"post\">";
		  html += "<div class=\"login-screen\">";
		  html += "<div id=\"loginheader\"><h1>EPUSS</h1></div>";
		  html += "<div id=\"logincredentials\" class=\"login-form\">";
	      html += "  <div class=\"form-group\">";
	      html += "    <input type=\"text\" id=\"login-name\" placeholder=\"Enter your name\" value=\"\" class=\"form-control login-field\" name=\"userName\">";
	      html += "    <label for=\"login-name\" class=\"login-field-icon fui-user\"></label>";
	      html += "  </div>";
	      html += "  <div class=\"form-group\">";
	      html += "    <input type=\"password\" id=\"login-pass\" placeholder=\"Password\" value=\"\" class=\"form-control login-field\" name=\"password\">";
	      html += "    <label for=\"login-pass\" class=\"login-field-icon fui-lock\"></label>";
	      html += "  </div>";
	      html += "   <input type=\"submit\" value=\"Log in\" class=\"btn btn-primary btn-lg btn-block\"></input>";
	      html += "</div>";
	      html += "</div>";
		  html += "</form>";
    	return html;
    }

    /**
	 * Handles input from the user and displays information for login. 
	 * 
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		session = request.getSession(true);
		
		int state = LOGIN_FALSE;
		boolean loginAttempt = false;

		PrintWriter out = response.getWriter();
		out.println(getPageIntro());
		String name;
		String password;
		
		if (isLoggedIn(request)) {
			session.setAttribute("state", LOGIN_FALSE);
			session.invalidate();
			out.println("<p>You are now logged out</p>"); //TODO Style to match mockup
		}
		
		name = request.getParameter("userName"); // get the string that the user entered in the form
        password = request.getParameter("password"); // get the entered password
        
        if (name != null && password != null) {
        	loginAttempt = true;
        	if(name.length() > 0 && password.length() > 0){ // Check if fields where empty at submit
	        	User currUser = instance.getUser(name);
	        	if(currUser != null && currUser.comparePassword(password)){
	        		System.out.println("Login success!");
	        		session.setMaxInactiveInterval(20*60);
	        		session.setAttribute("user", currUser); // Used to store logged in users into hashMap (usersSessions)
	        		currUser.printActiveSessions(); // Used for testing to see which users are currently online..
	        		state = LOGIN_TRUE;
	       			session.setAttribute("state", state);  // save the state in the session
	       			session.setAttribute("name", name);  // save the name in the session
	       			session.setAttribute("userId", currUser.getUserId());  // save the userId in the session
	       			session.setAttribute("sessionid", session.getId());
	       			session.setAttribute("role", currUser.getRole());
	       			switch (currUser.getRole()) { // Redirect user to matching page
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
	       			}
	       		}
        	}
       	}
        if(state == LOGIN_FALSE){
        	if(loginAttempt){
        		out.println("<p style=\"color:red;\">That was not a valid username / password. </p>");        		
        	}
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

	protected boolean isAdminComponent() {
		return false;
	}

	protected boolean isWorkerComponent() {
		return false;
	}

	protected boolean isProjectManagerComponent() {
		return false;
	}
}
