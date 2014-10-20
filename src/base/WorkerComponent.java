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

/**
 * This class will be used by project workers in order to communicate with the
 * system. Therefore this class will handle requests regarding everything that a
 * project worker is allowed to do, such as listing project members, creating
 * new time reports and such.
 * 
 * The validation whether a user is allowed to perform a specific action should
 * be carried out in this class, whereas the format on the input should not be
 * taken care of here.
 * 
 * @author SG
 * @version 0.3
 * 
 */
@WebServlet("/workercomponent")
public class WorkerComponent extends ServletBase {

	private static final long serialVersionUID = 1L;

	/**
	 * Handles input from the worker and displays information.
	 * 
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		session = request.getSession(true);
		out.println(getPageIntro());
		String timeReportActionMessage = null;
		String role = getRole();
		
		if (isLoggedIn(request)
				&& (role.equals("Developer") || role.equals("SystemArchitect") || role
						.equals("Tester"))) {
			out.println("<h1> Worker page </h1>");

			String userName = getName();
			Long userId = (Long) session.getAttribute("userId");
			User user = instance.getUser(userId);
			long groupId = user.getGroupId();
			ProjectGroup pg = instance.getProjectGroup(groupId);
			String projectGroupName = pg.getName();
			out.println("<p> User:<strong> " + userName + " </strong></p>");
			out.println("<p> Group: <strong>" + projectGroupName + "</strong> </p>");

			ArrayList<User> groupMembers = instance.getUsersInGroup(instance
					.getUser(userName).getGroupId());
			printUserTable(out, groupMembers, null);
			
			//TODO this is not a good approach, see my comment on AdministratorComponent,
			// Consider define a GET parameter called action which can be switch-cased instead. /J
			if(timeReportActionMessage == null)
				timeReportActionMessage = deleteTimeReport(request);
			if(timeReportActionMessage == null)
				timeReportActionMessage = editTimeReport(request, out, userId, role);
			if(timeReportActionMessage == null)
				timeReportActionMessage = addNewTimeReport(request, out, userId, role);
			
			ArrayList<TimeReport> timeReports = instance
					.getUsersTimeReportsOfUser(userId);
			printTimeReportTable(out, timeReports, timeReportActionMessage, userId);

			out.println(getEditTimeReportForm());
			out.println(getAddTimeReportForm());
			
			//TODO Move to servetBase.getPageOutro();
			out.println("<a class=\"btn btn-block btn-lg btn-danger\" href =" + formElement("logincomponent") + "> Log out </a>");
		} else {
			System.err.println("Illegal action performed as: " + role
					+ "; Tried to access WorkerComponent.");
			response.sendRedirect("logincomponent");
		}
	}
	

	/**
	 * Handles input from the worker and displays information.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response); // forward post-data to get-function /J
	}
	
	//TODO please look at the JavaDoc guidelines for how to define return-parameters correctly. /J
	/***
	 * Method stating that this component isn't the admin component through a boolean variable false.
	 */
	protected boolean isAdminComponent() {
		return false;
	}

	/***
	 * Method stating that this component is the worker component through a boolean variable true.
	 */
	protected boolean isWorkerComponent() {
		return true;
	}
	
	/***
	 * Method stating that this component isn't the projectmanager component through a boolean variable false.
	 */
	protected boolean isProjectManagerComponent() {
		return false;
	}
}
