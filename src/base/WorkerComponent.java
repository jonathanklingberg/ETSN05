package base;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.ProjectGroup;
import database.TimeReport;
import database.User;
import database.DatabaseHandlerInstance;

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
		session = request.getSession(true);
		out.println(getPageIntro());
		String timeReportActionMessage;
		String role = getRole();

		// Check so that the current user is either a developer, tester or a system architect. 
		// Currently not giving the admin or PM access to WorkerComponent
		if (isLoggedIn(request) && (role.equals("Developer") || role.equals("SystemArchitect") || role.equals("Tester"))) {
			out.println("<h1> Worker page " + "</h1>");//Is not shown in mockup design!

			String userName = getName();
			Long userId = (Long) session.getAttribute("userId");
			//Prints username and project group name
			User user = instance.getUser(userId);
			long groupId = user.getGroupId();
			ProjectGroup pg = instance.getProjectGroup(groupId);
			String projectGroupName = pg.getName();
			out.println("<p> Signed in as: " + userName + " </p>");
			out.println("<p> Assigned to project group: " + projectGroupName + " </p>");
				
			timeReportActionMessage = deleteTimeReport(request);
			
			//Display all project members in project group
			ArrayList<User> groupMembers = instance.getUsersInGroup(instance.getUser(userName).getGroupId());
			printUserTable(out, groupMembers, null);
			
			//Display all time reports belonging to the logged in user
			ArrayList<TimeReport> timeReports = instance.getUsersTimeReportsOfUser(userId);
			printTimeReportTable(out, timeReports);
			
			out.println("<button type=button>Add Time Report</button>");

			out.println("<p><a href =" + formElement("logincomponent") + "> Log out </p>");

			
		} else {
			System.err.println("Illegal action performed as: " + role + "; tried to access WorkerComponent.");
			response.sendRedirect("logincomponent");
		}
	}
	
	
	private String deleteTimeReport(HttpServletRequest request) {
		String timeReportId = request.getParameter("deletetimereport");
		if(timeReportId != null) {
			long deleteTimeReport = Long.parseLong(timeReportId);
			return instance.getTimeReport(deleteTimeReport).removeMe() ? "Time reports was removed successfully.": "Could not remove time report.";
		}
		return null;
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

	protected boolean isAdminOrProjectManagerComponent() {
		return false;
	}

	protected boolean isAdminComponent() {
		return false;
	}

	protected String getTimeReportTableName() {
		return "Your time reports";
	}

}
