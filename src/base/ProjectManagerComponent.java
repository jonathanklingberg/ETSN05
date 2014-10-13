package base;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.ProjectGroup;
import database.TimeReport;
import database.User;

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
 * @version 0.3
 * 
 */
@WebServlet("/projectmanagercomponent")
public class ProjectManagerComponent extends ServletBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Handles input from the project manager and displays information.
	 * 
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		session = request.getSession(true);
		PrintWriter out = response.getWriter();
		out.println(getPageIntro());
		
		String myName = getName();
		
		if (isLoggedIn(request) && getRole().equalsIgnoreCase("projectmanager")) {
			out.println("<h1>Project management page " + "</h1>"); //Is not shown in mockup design!
			long groupId = instance.getUser(myName).getGroupId();
			out.println("<p>Signed in as: Project Manager</p>");
			
			System.out.println("groupId = ");
			System.out.println(groupId);
			ProjectGroup pg = instance.getProjectGroup(groupId);
			out.println("<p>Assigned to project: "+ pg.getName() +"</p>");;
			
			
			
		
			
			String timereportId = request.getParameter("signtimereport");
			
			if(timereportId != null && timereportId.length() > 0){
				System.out.println("TimeReportId: " + timereportId);
				instance.changeSignatureOfTimeReport(timereportId);
			}else{
				System.out.println("TimeReportId not existing!");
			}
			
			//Prints a table with users that are in the same group
			ArrayList<User> usersInGroup = instance.getUsersInGroup(groupId);
			printUserTable(out, usersInGroup, null);
			
			// Prints a table with time reports from users of the same group
			ArrayList<TimeReport> timeReports = instance.getTimeReportsOfGroup(groupId);
			printTimeReportTable(out, timeReports, null);




			/* Do alot of stuff according to the SRS:
			 * See all members of his group			X
			 * See all groupmembers timereports
			 * See summation of timereports. (time, role, activity etc etc) See SRS 6.7) (footable)
			 * Sort all data in table on (time, role, activity etc etc in ascending order) (footable)
			 * Sign and unsign all groupmembers' timereports
			 * Assign and change roles in the project to group members
			 * 
			 */








			out.println("<p><a href =" + formElement("logincomponent") + "> Log out </p>");
			out.println("</body></html>");


		} else { // user is not admin or project manager
//			response.sendRedirect("functionality.html");
			// TODO FIX
		}
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
		// Update db signed.....
//		String checkbox = request.getParameter("signed");
//		String reportid = request.getParameter("reportid");
//		System.out.println("Signed: " + checkbox);
//		System.out.println("reportid: " + reportid);
//		PrintWriter out = response.getWriter();
		
//		boolean signOK = instance.changeTimeReportSignature(reportid);
		
//		if("OK_from_db" == true){
//			out.print("1"); // Everything OK / 
//		}else{
//			out.print("Something went wrong! / 0");
//		}
	}

	protected String getUserTableName() {		
		return 	"<p>Members in project:</p>";
	}

	protected boolean isAdminOrProjectManagerComponent() {
		return true;
	}

	protected boolean isAdminComponent() {
		return false;
	}

	protected String getTimeReportTableName() {
		return "Time reports in project";
	}

}
