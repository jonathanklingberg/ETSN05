package base;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.User;
import database.WorkspaceInstance;

/**
 * This class will be used by project workers in order to
 * communicate with the system. Therefore this class will
 * handle requests regarding everything that a project worker
 * is allowed to do, such as listing project members, creating
 * new time reports and such.
 * 
 * The validation whether a user is allowed to perform a specific 
 * action should be carried out in this class, whereas the format
 * on the input should not be taken care of here.
 * 
 * @author SG
 * @version 0.2
 * 
 */
public class WorkerComponent extends ServletBase {
	
	 /**
	  * Handles input from the worker and displays information. 
	  * 
	  * 
	  * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	  */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println(getPageIntro());

		// Get session and username
		HttpSession session = request.getSession(true);
		String userName = (String) session.getAttribute("name");
		
				
		if (!isLoggedIn(request)) {
			response.sendRedirect("logincomponent");
		} else {
			if (!WorkspaceInstance.getInstance(conn).userIsProjectManager(userName) && !userName.equals("admin")) { // Check that the user is a "default user"
				out.println("<h1> Workercomponent " + "<h1>");
				
				//Prints username and project group ID
				long projectGroup = WorkspaceInstance.getInstance(conn).getUser(userName).getGroupId();
				out.println("<p> Logged in as: " + userName + " </p>");
				out.println("<p> Assigned to project group: " + projectGroup + " </p>");
				
				//Display all project members in project group
				//Anropa en metod för att hämta alla medlemmar i en viss grupp. Metoden bör skapas i WorkspaceInstance 
				out.println("<p>Group members</p>");
				out.println("<TABLE BORDER=1 CELLPADDING=0 CELLSPACING=0 WIDTH=70%>");
				out.println("<tr><td><CENTER><B>NAME</B></CENTER></td>");
				out.println("<td><CENTER><B>ROLE</B></CENTER></td></tr>");
				ArrayList<User> groupMembers = WorkspaceInstance.getInstance(conn).getGroupMembers(projectGroup);
				for(int i = 0; i < groupMembers.size(); i++){
					out.println("<tr><td><CENTER>" + " "  + "</CENTER></td>");
					out.println("<td><CENTER>" + " "  + "</CENTER></td></tr>");
				}
				
				
			} else {
					
			}
		}
	}
	

	 /**
	  * Handles input from the worker and displays information. 
	  * 
	  * 
	  * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	  */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
}
