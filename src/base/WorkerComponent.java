package base;

import java.io.IOException;
import java.io.PrintWriter;

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

				// Get the session
				HttpSession session = request.getSession(true);
				
				
				
				String userName = (String) session.getAttribute("name");

				
				long projectGroup = WorkspaceInstance.getInstance(conn).getUser(userName).getGroupId();
				
								
				out.println("<p> Logged in as: " + userName + " </p>");
				
				out.println("<p> Assigned to project: project group" + projectGroup + " </p>");

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
