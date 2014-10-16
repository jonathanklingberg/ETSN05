package base;

import java.awt.image.RescaleOp;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import data.Number;
import data.Type;
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

		// Check so that the current user is either a developer, tester or a
		// system architect.
		// Currently not giving the admin or PM access to WorkerComponent?
		if (isLoggedIn(request)
				&& (role.equals("Developer") || role.equals("SystemArchitect") || role
						.equals("Tester"))) {
			// TODO This is not shown in mockup
			out.println("<h1> Worker page </h1>");

			String userName = getName();
			Long userId = (Long) session.getAttribute("userId");
			User user = instance.getUser(userId);
			long groupId = user.getGroupId();
			ProjectGroup pg = instance.getProjectGroup(groupId);
			String projectGroupName = pg.getName();
			out.println("<p> Signed in as: " + userName + " </p>");
			out.println("<p> Assigned to project group: " + projectGroupName
					+ " </p>");

			ArrayList<User> groupMembers = instance.getUsersInGroup(instance
					.getUser(userName).getGroupId());
			out.println("<p>Members in project:</p>");
			printUserTable(out, groupMembers, null);
			
			if(timeReportActionMessage == null)
				timeReportActionMessage = deleteTimeReport(request);
			if(timeReportActionMessage == null)
				timeReportActionMessage = editTimeReport(request, out, userId);
			if(timeReportActionMessage == null)
				timeReportActionMessage = addNewTimeReport(request, out, userId);
			
			// Display all time reports belonging to the logged in user
			ArrayList<TimeReport> timeReports = instance
					.getUsersTimeReportsOfUser(userId);
			out.println("<p>Your time reports:</p>");
			printTimeReportTable(out, timeReports, timeReportActionMessage);

			
			String editForm = "<div id=\"editTimeReport\" title=\"Edit time report\">" +
					"Date: <input type=\"text\" id=\"oldDate\" placeholder=\"YYYY-MM-dd\"></input><br>"+
					"Duration(min): <input type=\"text\" id=\"oldDuration\"></input><br>"+
					"Number: <input type=\"text\" id=\"oldNumber\"><br>" +
					"Type: <select id=\"oldType\"> " +
	    	           " <option value=\"D\">Development</option> " +
	    	           " <option value=\"I\">Informal</option> " +
	    	            "<option value=\"F\">Formal</option>  " +
	    	            "<option value=\"R\">Rework</option> "+
	    	           "</select>" +
	    	           " </div>";
			out.println(editForm);
			
			String addForm = "<div id=\"createTimeReport\" title=\"Add a new time report\">" + 
					"Date: <input type=\"text\" id=\"date\" placeholder=\"YYYY-MM-dd\"></input><br>" + 
					"Duration(min): <input type=\"text\" id=\"duration\"></input><br>" +
					"Number: <input type=\"text\" id=\"number\"><br>" +
					"Typer: <select id=\"myType\"> " +
					" <option value=\"D\">Development</option> " +
	    	           " <option value=\"I\">Informal</option> " +
	    	            "<option value=\"F\">Formal</option>  " +
	    	            "<option value=\"R\">Rework</option> "+
	    	           "</select>" + 
	    	           "</div>";
			out.println(addForm);
			
			out.println("<br/>");
			out.println("<input type=\"button\" id=\"createTimeReportButton\" value=\"Add new\" />");
			out.println("<p><a href =" + formElement("logincomponent")
					+ "> Log out </p>");

		} else {
			System.err.println("Illegal action performed as: " + role
					+ "; Tried to access WorkerComponent.");
			response.sendRedirect("logincomponent");
		}
	}
	
	/**
	 * This method handles a new time report and sets its values to the input given.
	 * 
	 * @param request This is the servlet request
	 * @param out the printwriter used to print out html code
	 * @param userId the id of the current user.
	 * @return String containing a result message either containing a success message or a failure message.
	 */
	private String addNewTimeReport(HttpServletRequest request,
			PrintWriter out, Long userId) {
		return handleTimeReports(request, out, userId, false);
	}
	
	/**
	 * This method handles a existing time report and updates its values to the input given.
	 * 
	 * @param request This is the servlet request
	 * @param out the printwriter used to print out html code
	 * @param userId the id of the current user.
	 * @return String containing a result message either containing a success message or a failure message.
	 */
	private String editTimeReport(HttpServletRequest request,
			PrintWriter out, Long userId){
		return handleTimeReports(request, out, userId, true);
	}
	
	/***
	 * 
	 * This method handles time reports, either updates or creates new time reports.
	 * 
	 * @param request This is the servlet request
	 * @param out the printwriter used to print out html code
	 * @param userId the id of the current user.
	 * @param existingReport boolean stating if the time report is new or previously existed.
	 * @return
	 */
	private String handleTimeReports(HttpServletRequest request,
			PrintWriter out, Long userId, boolean existingReport){
		String resultMsg = null;
		String idString = null;
		Long id = null;
		String date =  existingReport ? request.getParameter("newDate") : request.getParameter("date");
		String typeString = existingReport ? request.getParameter("newType") : request.getParameter("type");
		String durationString = existingReport ? request.getParameter("newDuration") : request.getParameter("duration");
		String numberString = existingReport ? request.getParameter("newNumber") : request.getParameter("number");
		if(existingReport && request.getParameter("newType")!=null){
			idString = request.getParameter("id");
			if(idString == null || idString.trim().equals("")){
				resultMsg = "<p class=\"failure-message\">Wrong format on input! Please try again!</p>";
				return resultMsg;
			}
		}
			
		if(date != null){
			if (checkDate(date)) {
				char type = typeString.charAt(0);
				if(typeString!=null && Type.isType(type)){
					if (durationString!=null && !durationString.trim().equals("") && numberString!=null && !numberString.trim().equals("")) {
						try{
							Long duration = Long.parseLong(durationString);
							Long number = Long.parseLong(numberString);
							if(existingReport){
								id = Long.parseLong(idString);
							}
							if(Number.isNumber(number)){
								User currentUser = instance.getUser(userId);
								java.util.Calendar calenderWeek = java.util.Calendar.getInstance();
								calenderWeek.setTime(Date.valueOf(date));
								long week = calenderWeek.get(java.util.Calendar.WEEK_OF_YEAR);
								
								if(existingReport){
									if(instance.editTimeReport(id, userId, currentUser.getGroupId(), type, duration, week, Date.valueOf(date), false, number )){
										resultMsg = existingReport ? "<pclass=\"success-message\">Time report was edited successfully!</p>" : "<p class=\"success-message\">Time report was created successfully!</p>";
									} else {
										resultMsg = "<p class=\"failure-message\">Wrong format on input! Please try again!</p>";
									}
								}else{
									instance.addTimeReport(new TimeReport(userId, currentUser.getGroupId(), type, 
											duration, week, Date.valueOf(date), false, number));
								}
//								resultMsg = existingReport ? "<pclass=\"success-message\">Time report was edited successfully!</p>" : "<p class=\"success-message\">Time report was created successfully!</p>";
							} else {
								resultMsg = "<p class=\"failure-message\">Wrong format on input! Please try again!</p>";
							}
						}catch(NumberFormatException e){
							resultMsg = "<p class=\"failure-message\">Wrong format on input! Please try again!</p>";
							return resultMsg;
						}
					}else{
						resultMsg = "<p class=\"failure-message\">Wrong format on input! Please try again!</p>";
					}
				} else {
					resultMsg = "<p class=\"failure-message\">Wrong format on input! Please try again!</p>";
				}
			} else {
				resultMsg = "<p class=\"failure-message\">Wrong format on input! Please try again!</p>";
			}
		}
		return resultMsg;	
	}

	/***
	 * This method checks if the date input is in both correct format,
	 * and that it is prior to 
	 * @param date taken from the input.
	 * @return a boolean that represents if the inputed date is a valid date or not.
	 */
	private static boolean checkDate(String date) {
		if(date==null){
			return false;
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.setLenient(false);
			java.util.Date inputDate = sdf.parse(date);
			System.out.println(inputDate);	
			
			java.util.Date toDaysDate = new java.util.Date();
			toDaysDate = sdf.parse(sdf.format(toDaysDate));

			return inputDate.compareTo(toDaysDate)<=0;
			
		} catch (ParseException e) {
		} catch (IllegalArgumentException e) {
		}
		return false;
	}
	
	/***
	 * This method deletes a existing time report and returns a success message if it is successfully removed.
	 * If the sql query is unsuccessful it will give a fail message.
	 * 
	 * @param request the servlet request.
	 * @return a String containing the result.
	 */
	private String deleteTimeReport(HttpServletRequest request) {
		String timeReportId = request.getParameter("deletetimereport");
		if (timeReportId != null) {
			long deleteTimeReport = Long.parseLong(timeReportId);
			return instance.getTimeReport(deleteTimeReport).removeMe() ? "Time report was removed successfully."
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
		doGet(request, response); // forward post-data to get-function /J
	}

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
