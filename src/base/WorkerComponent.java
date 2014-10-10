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
		String role = getRole();

		// Check so that the current user are eather a developer, tester or a system architect. 
		// Currently not giving the admin or PM access to WorkerComponent
		if (isLoggedIn(request) && (role.equals("Developer") || role.equals("SystemArchitect") || role.equals("Tester"))) {
			out.println("<h1> Worker page " + "</h1>");//Is not shown in mockup design!

			String userName = getName();
			Long userId = (Long) session.getAttribute("userId");
			//Prints username and project group ID
			User user = instance.getUser(userId);
			long groupId = user.getGroupId();
			ProjectGroup pg = instance.getProjectGroup(groupId);
			String projectGroupName = pg.getName();
			out.println("<p> Signed in as: " + userName + " </p>");
			out.println("<p> Assigned to project group: " + projectGroupName + " </p>");
			
			
			//Display all project members in project group
			ArrayList<User> groupMembers = instance.getUsersInGroup(instance.getUser(userName).getGroupId());
			printUserTable(out, groupMembers, null);
			
			ArrayList<TimeReport> timeReports = instance.getUsersTimeReports(userId);
			printTimeReportTable(out, timeReports);
			
			
			
			
			
			
			
			
			/* TAG DET FETASTE FRÅN KODEN NEDAN OCH LYFT UPP TILL ServletBase SÅ ATT ALLA COMPONENTS DRAR NYTTA AV DEN! */
			
			
			
			
			
//out.println("<div Style=\"display:inline-block\"> Group members");	
////			out.println("<div>");
////			out.println("<TABLE BORDER=1 CELLPADDING=0 CELLSPACING=0 WIDTH=35%> Group Members");
////			out.println("<tr><td><CENTER><B>NAME</B></CENTER></td>");
////			out.println("<td><CENTER><B>ROLE</B></CENTER></td></tr>");
////			for(int i = 0; i < groupMembers.size(); i++){
////				out.println("<tr><td><CENTER>" + groupMembers.get(i).getName() + "</CENTER></td>");
////				out.println("<td><CENTER>" + groupMembers.get(i).getRole() + "</CENTER></td></tr>");
////			}
////			out.println("</table>");
////			out.println("</div>");
//			out.println("<br>");
//			
//			//Display all time reports belonging to the logged in user. 
//			//out.println("<div Style=\"DISPLAY:inline-block\"> Time Reports");
//			out.println("<div>");
//			out.println("<TABLE BORDER=1 CELLPADDING=0 CELLSPACING=0 WIDTH=70%> Time Reports");
//			out.println("<tr><td><CENTER><B>DATE</B></CENTER></td>");
//			out.println("<td><CENTER><B>WEEK</B></CENTER></td>");
//			out.println("<td><CENTER><B>TIME (MIN)</B></CENTER></td>");
//			out.println("<td><CENTER><B>TYPE</B></CENTER></td>");
//			out.println("<td><CENTER><B>STATE</B></CENTER></td>");
//			out.println("<td><CENTER><B>EDIT</B></CENTER></td>");
//			out.println("<td><CENTER><B>REMOVE</B></CENTER></td></tr>");
//			
//			ArrayList<TimeReport> timeReports = instance.getUsersTimeReports(userId);
//			
//			for(int i = 0; i < timeReports.size(); ++i){
//				Long timeReportId = timeReports.get(i).getId();
//				String editURL = "workercomponent?edittimereport="+timeReportId;
//		    	String editCode = "<a href=" + formElement(editURL) +" onclick="+formElement("return confirm('Are you sure you want to edit time report "+timeReportId+"?')") + "> edit </a>";
//		    	String deleteURL = "workercomponent?deletetimereport="+timeReportId;
//		    	String deleteCode = "<a href=" + formElement(deleteURL) +" onclick="+formElement("return confirm('Are you sure you want to delete time report "+timeReportId+"?')") + "> delete </a>";
//				
//				out.println("<tr><td><CENTER>" + timeReports.get(i).getDate() + "</CENTER></td>");
//				out.println("<td><CENTER>" + timeReports.get(i).getWeek() + "</CENTER></td>");
//				out.println("<td><CENTER>" + timeReports.get(i).getDuration() + "</CENTER></td>");
//				out.println("<td><CENTER>" + timeReports.get(i).getType() + "</CENTER></td>");
//				out.println("<td><CENTER>" + timeReports.get(i).isSigned() + "</CENTER></td>");
//				out.println("<td><CENTER>" + editCode +  "</CENTER></td>");
//				out.println("<td><CENTER>" + deleteCode + "</CENTER></td></tr>");
//			}
//			out.println("</table>");
//			out.println("</div>");
			
			out.println("<button type=button>Add Time Report</button>");

			out.println("<p><a href =" + formElement("logincomponent") + "> Log out </p>");

			
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
