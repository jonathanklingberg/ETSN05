package base;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.Role;
import database.TimeReport;
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
@WebServlet("/workercomponent")
public class WorkerComponent extends ServletBase {

	private static final long serialVersionUID = 1L;


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
		String role = (String) session.getAttribute("role");
		
		// Check so that the current user are eather a developer, tester or a system architect. 
		// Currently not giving the admin or PM access to WorkerComponent
		if (isLoggedIn(request) && (role.equals("Developer") || role.equals("SystemArchitect") || role.equals("Tester"))) {
			out.println("<h1> Workercomponent " + "</h1>");
			String userName = (String) session.getAttribute("name");
			//Prints username and project group ID
			long projectGroup = WorkspaceInstance.getInstance(conn).getUser(userName).getGroupId();
			out.println("<p> Logged in as: " + userName + " </p>");
			out.println("<p> Assigned to project group: " + projectGroup + " </p>");
			
			
			 
			
			//Display all project members in project group
			//out.println("<div Style=\"display:inline-block\"> Group members");
			out.println("<div>");
			out.println("<TABLE BORDER=1 CELLPADDING=0 CELLSPACING=0 WIDTH=35%> Group Members");
			out.println("<tr><td><CENTER><B>NAME</B></CENTER></td>");
			out.println("<td><CENTER><B>ROLE</B></CENTER></td></tr>");
			ArrayList<User> groupMembers = WorkspaceInstance.getInstance(conn).getGroupMembers(projectGroup);
			for(int i = 0; i < groupMembers.size(); i++){
				out.println("<tr><td><CENTER>" + groupMembers.get(i).getName() + "</CENTER></td>");
				out.println("<td><CENTER>" + groupMembers.get(i).getRole() + "</CENTER></td></tr>");
			}
			out.println("</table>");
			out.println("</div>");
			out.println("<br>");
			//Display all time reports belonging to the logged in user. 
			//out.println("<div Style=\"DISPLAY:inline-block\"> Time Reports");
			out.println("<div>");
			out.println("<TABLE BORDER=1 CELLPADDING=0 CELLSPACING=0 WIDTH=60%> Time Reports");
			out.println("<tr><td><CENTER><B>DATE</B></CENTER></td>");
			out.println("<td><CENTER><B>WEEK</B></CENTER></td>");
			out.println("<td><CENTER><B>TIME (MIN)</B></CENTER></td>");
			out.println("<td><CENTER><B>TYPE</B></CENTER></td>");
			out.println("<td><CENTER><B>STATE</B></CENTER></td>");
			out.println("<td><CENTER><B>EDIT</B></CENTER></td>");
			out.println("<td><CENTER><B>REMOVE</B></CENTER></td></tr>");
//			ArrayList<TimeReport> timeReports = WorkspaceInstance.getInstance(conn).getUsersTimeReports(userId);
			
			out.println("</table>");
			out.println("</div>");
			

			
		} else {
			System.err.println("Illigal action performed as: " + role + "; tried to access WorkerComponent.");
			response.sendRedirect("logincomponent");
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

	protected String getUserTableName() {		
		return "<p>Members in project:</p>";
	}

	protected String getUserTable() {		
		return "<tr><td>Name</td><td>Role</td></tr>";
	}

	protected boolean isAdminOrProjectManager() {
		return false;
	}

	protected boolean isAdmin() {
		return false;
	}

}
