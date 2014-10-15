package base;

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

			
			// TODO something with timeReportActionMessage??
            // System.out.println("Please do seomthing to me: " + timeReportActionMessage);
			// Display all project members in project group
			ArrayList<User> groupMembers = instance.getUsersInGroup(instance
					.getUser(userName).getGroupId());
			out.println("<p>Members in project:</p>");
			printUserTable(out, groupMembers, null);
			String userActionMessage = null;
			
			if(timeReportActionMessage == null)
				timeReportActionMessage = deleteTimeReport(request);
			if(timeReportActionMessage == null)
				timeReportActionMessage = editTimeReport(request, out, userId);
			if(timeReportActionMessage == null)
				timeReportActionMessage = addNewTimeReport(request, out, userId);
			
			
//			if(timeReportActionMessage!=null){
//				userActionMessage=timeReportActionMessage;
//			}
			// Display all time reports belonging to the logged in user
			ArrayList<TimeReport> timeReports = instance
					.getUsersTimeReportsOfUser(userId);
			out.println("<p>Your time reports:</p>");
			printTimeReportTable(out, timeReports, timeReportActionMessage);

			
			String editForm = "<div id=\"editTimeReport\" title=\"Edit time report\">" +
					"Date: <input type=\"text\" id=\"oldDate\" placeholder=\"YYYY-MM-dd\"></input>"+
					"Duration(min): <input type=\"text\" id=\"oldDuration\"></input>"+
					"Number: <input type=\"text\" id=\"oldNumber\">" +
					"<select id=\"oldType\"> " +
	    	           " <option value=\"D\">Development</option> " +
	    	           " <option value=\"I\">Informal</option> " +
	    	            "<option value=\"F\">Formal</option>  " +
	    	            "<option value=\"R\">Rework</option> "+
	    	           "</select>" +
						" </div>";
			out.println(editForm);
			
			
			out.println("<div id=\"createTimeReport\" title=\"Add a new time report\">");
			out.println("Date: <input type=\"text\" id=\"date\" placeholder=\"YYYY-MM-dd\"></input>");
			out.println("Duration(min): <input type=\"text\" id=\"duration\"></input>");
			out.println("Number: <input type=\"text\" id=\"number\">");
			String t = "<select id=\"myType\"> " + 
	    	           " <option value=\"D\">Development</option> " +
	    	           " <option value=\"I\">Informal</option> " +
	    	            "<option value=\"F\">Formal</option>  " +
	    	            "<option value=\"R\">Rework</option> "+
	    	           "</select>";
			out.println(t);
			out.println("</div><br />");
			out.println("<input type=\"button\" id=\"createTimeReportButton\" value=\"Add new\" />");

			out.println("<p><a href =" + formElement("logincomponent")
					+ "> Log out </p>");

		} else {
			System.err.println("Illegal action performed as: " + role
					+ "; Tried to access WorkerComponent.");
			response.sendRedirect("logincomponent");
		}
	}
	//TODO JavaDoc
	/**
	 * 
	 * @param request
	 * @param out
	 * @param userId
	 * @return
	 */
	private String addNewTimeReport(HttpServletRequest request,
			PrintWriter out, Long userId) {
		String resultMsg = null;


		// Get the parameters.
		String date = request.getParameter("date");
		String durationString = request.getParameter("duration");
		String typeString = request.getParameter("type");
		String numberString = request.getParameter("number");
		if(date != null){
			if (checkDate(date)) {
				if (durationString!=null && !durationString.trim().equals("")) {
					try{
						Integer.parseInt(durationString);
					}catch(NumberFormatException e){
						resultMsg = "<p style=\"color=red;\">Wrong format on input! Please try again!</p>";
						return resultMsg;
					}
					if (numberString!=null && !numberString.trim().equals("")) {
						try{
							int numberInt = Integer.parseInt(numberString);
							if(Number.isNumber(numberInt)){
								if(typeString!=null && typeString.length()==1 && Type.isType(typeString.charAt(0))){
									User currentUser = instance.getUser(userId);
									java.util.Calendar calenderWeek = java.util.Calendar.getInstance();
									calenderWeek.setTime(Date.valueOf(date));
									long week = calenderWeek.get(java.util.Calendar.WEEK_OF_YEAR);
									instance.addTimeReport(new TimeReport(userId, currentUser.getGroupId(), typeString.charAt(0), 
											Long.parseLong(durationString), week, Date.valueOf(date), false, numberInt));	
									resultMsg = "<p>Time report was created successfully!</p>";
								}else{
									resultMsg = "<p style=\"color=red;\">Wrong format on input! Please try again!</p>";
								}
							}else{
								resultMsg = "<p style=\"color=red;\">Wrong format on input! Please try again!</p>";
							}
						}catch(NumberFormatException e){
							resultMsg = "<p style=\"color=red;\">Wrong format on input! Please try again!</p>";
							return resultMsg;
						}
					} else {
						resultMsg = "<p style=\"color=red;\">Wrong format on input! Please try again!</p>";
					}
				} else {
					resultMsg = "<p style=\"color=red;\">Wrong format on input! Please try again!</p>";
				}
			} else {
				resultMsg = "<p style=\"color=red;\">Wrong format on input! Please try again!</p>";
			}
		}
		return resultMsg;
	}
	
	
	private String editTimeReport(HttpServletRequest request,
			PrintWriter out, Long userId){
		String resultMsg = null;
		String date =  request.getParameter("newDate");
		String typeString = request.getParameter("newType");
		String durationString = request.getParameter("newDuration");
		String numberString = request.getParameter("newNumber");
		String idString = request.getParameter("id");

		System.out.println("date: " + date);
		System.out.println("typeString: " + typeString);
		System.out.println("durationString: " + durationString);
		System.out.println("numberString: " + numberString);
		System.out.println("idString: " + idString);

		if(date != null){
			if (checkDate(date)) {
				if(typeString!=null && Type.isType(typeString.charAt(0))){
					if (durationString!=null && !durationString.trim().equals("") && numberString!=null && !numberString.trim().equals("")
							&& idString != null && !idString.trim().equals("")) {
						try{
							Long duration = Long.parseLong(durationString);
							Long number = Long.parseLong(numberString);
							Long id = Long.parseLong(idString);
							User currentUser = instance.getUser(userId);
							java.util.Calendar calenderWeek = java.util.Calendar.getInstance();
							calenderWeek.setTime(Date.valueOf(date));
							long week = calenderWeek.get(java.util.Calendar.WEEK_OF_YEAR);
							instance.editTimeReport(id, userId, currentUser.getGroupId(), typeString.charAt(0), duration, week, Date.valueOf(date), false, number );
							resultMsg = "<p>Time report was edited successfully!</p>";
						}catch(NumberFormatException e){
							resultMsg = "<p style=\"color=red;\">Wrong format on input! Please try again!</p>";
							System.out.println("fel i number format");
							return resultMsg;
						}
					}else{
						resultMsg = "<p style=\"color=red;\">Wrong format on input! Please try again!</p>";
						System.out.println("Fel i duration/number/id");
					}
				} else {
					resultMsg = "<p style=\"color=red;\">Wrong format on input! Please try again!</p>";
					System.out.println("Fel i type");
				}
			} else {
				resultMsg = "<p style=\"color=red;\">Wrong format on input! Please try again!</p>";
				System.out.println("Fel i andra date");
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
	
	//TODO JavaDoc
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

	protected boolean isAdminComponent() {
		return false;
	}

	protected boolean isWorkerComponent() {
		return true;
	}
	
	protected boolean isProjectManagerComponent() {
		return false;
	}
}
