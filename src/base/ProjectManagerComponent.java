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
 * This class will be used by project managers in order to communicate with the
 * system. Therefore this class will handle requests regarding everything that a
 * project manager is allowed to do, such as listing and signing time reports,
 * or assigning roles to the users.
 * 
 * The validation whether a user is allowed to perform a specific action should
 * be carried out in this class, whereas the format of the input should not be
 * taken care of here.
 * 
 * @author SG
 * @version 0.2
 * 
 */
public class ProjectManagerComponent extends ServletBase {

	/**
	 * Handles input from the project manager and displays information.
	 * 
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
		HttpServletResponse response) throws ServletException, IOException {
		String myName = "";
		PrintWriter out = response.getWriter();
		out.println(getPageIntro());

		HttpSession session = request.getSession(true);
		Object nameObj = session.getAttribute("name");
		if (nameObj != null) {
			myName = (String) nameObj; // if the name exists typecast the name
										// to a string
		}
		if (!isLoggedIn(request)) {
			response.sendRedirect("logincomponent");
		} else {
			if (instance.userIsProjectManager(myName)) {
//			if (instance
//					.userIsProjectManager(myName) || myName.equals("admin")) {
				//out.println("<h1>Project management page " + "</h1>");
				long groupId = instance.getGroupIdOfUser(myName);
				out.println("<p>Signed in as: Project Manager</p>");
				out.println("<p>Assigned to project: "+instance.getProjectName(groupId) +"</p>");
				out.println("<p>Members in project</p>");
			    out.println("<table border=" + formElement("1") + ">");
			    out.println("<tr><td>Name</td><td>Role</td><td>Edit (role)</td></tr>");
			    
			    ArrayList<User> usersInGroup = instance.getUsersInGroup(groupId);
			    for(User user : usersInGroup) {
			    	user.toHTML(instance.getUser(myName));
			    }
			    
			    
				
				/* Do alot of stuff according to the SRS:
				 * See all members of his group
				 * See all groupmembers timereports
				 * See summation of timereports. (time, role, activity etc etc) See SRS 6.7) (footable)
				 * Sort all data in table on (time, role, activity etc etc in ascending order) (footable)
				 * Sign and unsign all groupmembers' timereports
				 * Assign and change roles in the project to group members
				 * 
				 */
				
				
				
				
			} else { // user is not admin or project manager
				response.sendRedirect("functionality.html");
			}
		}
		// Don't forget that the administrator should be allowed to perform
		// all actions here as well, since he should have the same rights
		// as the project manager

	}

	/**
	 * Handles input from the project manager and displays information.
	 * 
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
