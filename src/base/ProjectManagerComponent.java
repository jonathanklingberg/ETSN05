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
//		System.out.println("groupIdAttribute = " + session.getAttribute("groupId"));
		PrintWriter out = response.getWriter();
		out.println(getPageIntro());
		String timeReportActionMessage = null;
		
		String myName = getName();
		String role = getRole();		
		boolean isAdmin = getRole().equalsIgnoreCase("admin");
		
		//TODO Give admin access to this component! /J
		if (isLoggedIn(request) && (getRole().equalsIgnoreCase("projectmanager") || isAdmin)) {			
			out.println("<h1>Project management page</h1>");
			if(getRole().equalsIgnoreCase("admin")){
				out.print("<a id=\"back-btn\" class=\"btn btn-block btn-lg btn-warning\" href=\"administrationcomponent\">Go back</a>");
			}
			
			String adminGroupRequestString = request.getParameter("adminGroupRequestId");
			Object adminGroupSessionObject = session.getAttribute("adminGroupRequestId");
			
			long groupId = -1;
			if(adminGroupRequestString != null && isAdmin) { // An admin has been redirected to ProjectManagerComponent for the first time
				groupId = Long.parseLong(adminGroupRequestString);
				session.setAttribute("adminGroupRequestId", groupId);
			} else if(adminGroupSessionObject != null && isAdmin){  // An admin has clicked on something in ProjectManagerComponent view and page is rendered again

				groupId = Long.parseLong(adminGroupSessionObject.toString());
			}else{ 													// User is projectmanager
				groupId = instance.getUser(myName).getGroupId();
			}
			
			System.out.println("groupId = " + groupId);
			out.println("<p>User:<strong> " +getRole() +"</strong></p>");
			ProjectGroup pg = instance.getProjectGroup(groupId);
			out.println("<p>Group:<strong> "+ pg.getName() +"</strong></p>");
			String timeReportId = request.getParameter("signtimereport");
			
			if(timeReportId != null && timeReportId.length() > 0){
				System.out.println("TimeReportId: " + timeReportId);
				boolean changed = instance.changeSignatureOfTimeReport(timeReportId);
				if(!changed){
					System.out.println("TimeReportId does not exist!");
				}
			}else{
				System.out.println("TimeReportId does not exist!");
			}
			
			String roleIdString = request.getParameter("roleId");
			if (roleIdString != null) {				
				Long roleId = Long.parseLong(roleIdString);
				String newRole = request.getParameter("role");
				boolean changeRole = instance.changeRoleOfUser(newRole, roleId);
				if(!changeRole){
					System.out.println("UserId does not exist, or the new role is not a valid role!");
				}
			} else {
				System.out.println("UserId does not exist!");
			}
			
			//Prints a table with users that are in the same group
			ArrayList<User> usersInGroup = instance.getUsersInGroup(groupId);
			printUserTable(out, usersInGroup, null);
			
			Long userId = (Long) session.getAttribute("userId");
			
			//TODO I'm pretty sceptical about this approach, a simple switch-case where requested action was specified would have been better. 
			// See my comment on adminComponent, implement if you have the time /J
			if(timeReportActionMessage == null)
				timeReportActionMessage = deleteTimeReport(request);
			if(timeReportActionMessage == null)
				timeReportActionMessage = editTimeReport(request, out, userId, role);
			if(timeReportActionMessage == null)
				timeReportActionMessage = addNewTimeReport(request, out, userId, role);
			
			
			// Prints a table with time reports from users of the same group
			//TODO groupId can never be null or what happens then? do you handle this? /J
			ArrayList<TimeReport> timeReports = instance.getTimeReportsOfGroup(groupId);
			System.out.println("Got all time reports for group " + groupId + ": " + instance.getProjectGroup(groupId).getName());
			System.out.println("There were " + timeReports.size() + " of them");
			//TODO what if there're no reports? should we just print an empty table? or show a message? Make sure there's no exception whatsoever. /J
			printTimeReportTable(out, timeReports, timeReportActionMessage, userId);
			
			out.println(getEditTimeReportForm());
			out.println(getAddTimeReportForm());
			
			//TODO are these items done? remove comment then /J
			/* Do alot of stuff according to the SRS:
			 * See all members of his group			X
			 * See all groupmembers timereports		X
			 * See summation of timereports. (time, role, activity etc etc) See SRS 6.7) (footable)
			 * Sort all data in table on (time, role, activity etc etc in ascending order) (footable)
			 * Sign and unsign all groupmembers' timereports	X
			 * Assign and change roles in the project to group members	X
			 * 
			 * Maybe:
			 * Be able to write and edit his own time reports
			 */

			out.println(getPageOutro());
			out.println("</body></html>");


		} else { // user is not admin or project manager
			System.err.println("Illigal action performed as: " + getRole() + "; Tried to access ProjectManagerComponent.");
			response.sendRedirect("logincomponent");
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
		doGet(request, response); // redirect post-data to GET-action
	}
	

	/**
	 * Determines whether this is an admin component
	 * 
	 * @return 	true if it is an admin component, 
	 * 			false otherwise
	 */
	protected boolean isAdminComponent() {
		return false;
	}
	
	/**
	 * Determines whether this is a worker component
	 * 
	 * @return 	true if it is a worker component, 
	 * 			false otherwise
	 */
	protected boolean isWorkerComponent() {
		return false;
	}
	
	/**
	 * Determines whether this is a project manager component or not
	 * 
	 * @return 	true if it is a project manager component, 
	 * 			false otherwise
	 */
	protected boolean isProjectManagerComponent() {
		return true;
	}
}
