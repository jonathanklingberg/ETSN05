package base;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.Type;
import database.ProjectGroup;
import database.TimeReport;
import database.User;
import database.DatabaseHandlerInstance;

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		session = request.getSession(true);
		out.println(getPageIntro());
		String timeReportActionMessage;
		String role = getRole();

		// Check so that the current user is either a developer, tester or a
		// system architect.
		// Currently not giving the admin or PM access to WorkerComponent
		if (isLoggedIn(request)
				&& (role.equals("Developer") || role.equals("SystemArchitect") || role
						.equals("Tester"))) {
			out.println("<h1> Worker page " + "</h1>");// Is not shown in mockup
														// design!

			String userName = getName();
			Long userId = (Long) session.getAttribute("userId");
			// Prints username and project group name
			User user = instance.getUser(userId);
			long groupId = user.getGroupId();
			ProjectGroup pg = instance.getProjectGroup(groupId);
			String projectGroupName = pg.getName();
			out.println("<p> Signed in as: " + userName + " </p>");
			out.println("<p> Assigned to project group: " + projectGroupName
					+ " </p>");

			timeReportActionMessage = deleteTimeReport(request);

			// Display all project members in project group
			ArrayList<User> groupMembers = instance.getUsersInGroup(instance
					.getUser(userName).getGroupId());
			printUserTable(out, groupMembers, null);
			String userActionMessage = null;
			userActionMessage = addNewTimeReport(request, out, userId);
			// Display all time reports belonging to the logged in user
			ArrayList<TimeReport> timeReports = instance
					.getUsersTimeReportsOfUser(userId);
			printTimeReportTable(out, timeReports, userActionMessage);

			out.println("<div id=\"createTimeReport\" title=\"Add a new time report\">");
			out.println("Date: <input type=\"text\" id=\"date\" placeholder=\"YYYY-MM-dd\"></input>");
			out.println("Duration(min): <input type=\"text\" id=\"duration\"></input>");
			out.println("Type: <input type=\"text\" id=\"type\">");
			out.println("</div><br />");
			out.println("<input type=\"button\" id=\"createTimeReportButton\" value=\"Add new\" />");

			out.println("<p><a href =" + formElement("logincomponent")
					+ "> Log out </p>");

		} else {
			System.err.println("Illegal action performed as: " + role
					+ "; tried to access WorkerComponent.");
			response.sendRedirect("logincomponent");
		}
	}

	private String addNewTimeReport(HttpServletRequest request,
			PrintWriter out, Long userId) {
		String failMsg = null;

		// Get the parameters.
		String date = request.getParameter("addNewTimeReport");
		String durationString = request.getParameter("duration");
		String typeString = request.getParameter("type");

		if (date != null) {
			if (checkDate(date)) {
				if (durationString!=null && Integer.parseInt(durationString)>0) {
					
					if (typeString!=null && !(typeString.trim().equals(""))) {
						
						int typeInt = Integer.parseInt(typeString);
						if(Type.isType(typeInt)){
							User currentUser = instance.getUser(userId);
							java.util.Calendar calenderWeek = java.util.Calendar.getInstance();
							calenderWeek.setTime(Date.valueOf(date));
							long week = calenderWeek.get(java.util.Calendar.WEEK_OF_YEAR);
							instance.addTimeReport(new TimeReport(userId, currentUser.getGroupId(), typeInt, 
									Long.parseLong(durationString), week, Date.valueOf(date), false));							
						}else{
							failMsg = "Unknown type!";
						}
					} else {
						failMsg = "Incorrect type format!";
					}
				} else {
					failMsg = "Incorrect duration format!";
				}
			} else {
				failMsg = "Incorrect date format!";
			}
		}
		return failMsg;
	}

	private boolean validType(int parseInt) {
		boolean definedType = false;
		if ((parseInt > 10 && parseInt < 20)
				|| (parseInt > 20 && parseInt < 24) || parseInt == 30
				|| (parseInt > 39 && parseInt < 45) || parseInt == 100) {
			definedType = true;
		}
		return definedType;
	}

	private static boolean checkDate(String date) {
		boolean rightFormat = false;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setLenient(false);
			sdf.parse(date);
			rightFormat = true;
		} catch (ParseException e) {
			rightFormat = false;
		} catch (IllegalArgumentException e) {
			rightFormat = false;
		}
		return rightFormat;
	}

	private String deleteTimeReport(HttpServletRequest request) {
		String timeReportId = request.getParameter("deletetimereport");
		if (timeReportId != null) {
			long deleteTimeReport = Long.parseLong(timeReportId);
			return instance.getTimeReport(deleteTimeReport).removeMe() ? "Time reports was removed successfully."
					: "Could not remove time report.";
		}
		return null;
	}

	/**
	 * Handles input from the worker and displays information.
	 * 
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
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
