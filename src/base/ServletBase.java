package base;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import data.Number;
import data.Type;
import database.TimeReport;
import database.User;
import database.DatabaseHandlerInstance;

/**
 *  This class is the superclass for all servlets in the application. 
 *  It includes basic functionality required by many servlets, like for example a page head 
 *  written by all servlets, and the connection to the database. 
 *  
 *  This application requires a database.
 *  For username and password, see the constructor in this class.
 *  
 *  For database creation statements, please refer to the corresponding STLDD
 *  for the project.
 *  
 *  @author Martin Host
 *  @version 1.0
 *  
 */
public abstract class ServletBase extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected static final int LOGIN_FALSE = 0;
	protected static final int LOGIN_TRUE = 1;	

	protected Connection conn;
	protected DatabaseHandlerInstance instance;
	protected HttpSession session;
	/**
	 * Constructs a servlet and makes a connection to the database.
	 */
	public ServletBase() {			
			instance = DatabaseHandlerInstance.getInstance();
	}

	/**
	 * Checks if a user is logged in or not.
	 * @param request The HTTP Servlet request (so that the session can be found)
	 * @return true if the user is logged in, otherwise false.
	 */
	protected boolean isLoggedIn(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		Object objectState = session.getAttribute("state");
		int state = LOGIN_FALSE;
		if (objectState != null){
			state = (Integer) objectState; 			
		}
		return (state == LOGIN_TRUE);
	}


	/**
	 * Just eases the use of quotes when printing java-variables inside html-elements.
	 * @param par Input string
	 * @return output string = "par" 
	 */
	protected String formElement(String par) {
		return '"' + par + '"';
	}


	/**
	 * Constructs the header of all servlets. 
	 * @return String with html code for the header. 
	 */
	protected String getPageIntro() {
		String intro = "<html><head>" + 
			// Import javaScript-libraries
			"<script src=\"js/jquery-1.8.3.js\"></script>" +
			"<script src=\"js/jquery-ui-1.9.2.custom.min.js\"></script>" +
			"<script src=\"js/epuss.js\"></script>" +
			"<script src=\"js/footable.js\"></script>" +
			"<script src=\"js/footable.bookmarkable.js\"></script>" +
			"<script src=\"js/footable.filter.js\"></script>" +
			"<script src=\"js/footable.grid.js\"></script>" +
			"<script src=\"js/footable.memory.js\"></script>" +
			"<script src=\"js/footable.plugin.template.js\"></script>" +
			"<script src=\"js/footable.sort.js\"></script>" +
			"<script src=\"js/footable.striping.js\"></script>" +
			"<script src=\"js/footable.paginate.js\" type=\"text/javascript\"></script>" + 
			
			// Import stylesheets
			"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/bootstrap.min.css\"/>" +
			"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/flat-ui.css\"/>" +
			"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/footable.core.min.css\"/>" +
			"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/footable.metro.min.css\"/>" +
			"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/jquery-ui-1.9.2.custom.min.css\"/>" +
			"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/epuss.css\"/>" +
			
			// Set the title used for browser-tabs etc.
			"<title> The EPUSS System </title></head><body>";
		
		return intro;
	}

	//TODO JavaDoc
	/**
	 *
	 * @param out
	 * @param userList
	 * @param userActionMessage
	 * @param isAdmin 
	 */
	protected void printUserTable(PrintWriter out, ArrayList<User> userList, String userActionMessage, boolean isAdmin) {
		out.println("<table id=\"usertable\" data-filter=\"#userfilter\" data-page-navigation=\".pagination\" class=\"footable\" data-page-size=\"10\" border=" + formElement("1") + ">");	
		int amount = 0;
		System.out.println("Total number of users in system: " + userList.size());
		for(int i = 0; i < userList.size(); ++i) {	
			User user = userList.get(i);
			Long userId = user.getUserId();
			String name = user.getName();
			System.out.println(name);
			String pw = user.getPassword();
			String role = user.getRole();
			String group = instance.getProjectGroup(user.getGroupId()).getName();

			String editCode = "";
			if(isAdminComponent()) {
				amount = 6;
				editCode = "<a class=\"cursor-pointer\" onclick=" + formElement("return editUser('" + name + "','" + pw + "','" + group + "', '" + role + "')") + " >Edit user</a>";
			} else if(isProjectManagerComponent()){	
				amount = 3;
				if(!role.equalsIgnoreCase("Admin") && (isAdmin || !role.equalsIgnoreCase("ProjectManager"))){
				editCode = "<a class=\"cursor-pointer\" onclick=" + formElement("return editRole(" + userId + ")") + " >Edit</a>";
				}
			} else {
				amount = 2;
				String editURL = "administrationcomponent?edituser="+name;
				editCode = "<a href=" + formElement(editURL) +" onclick="+formElement("return confirm('Are you sure you want to edit "+name+"?')") + ">Edit</a>";
			}

			String deleteCode = "<a class=\"cursor-pointer\" onclick="+formElement("return deleteUser('" + name + "')") + "> Delete </a>";
			if (name.equals("admin")){
				deleteCode = "";
				editCode = "";
			}
			printUser(out, name, role, group, editCode, pw, deleteCode);
		}
		String tFoot = "<tfoot>" +
							"<tr>" +
							"<td colspan='" + amount + "'>" +
							 "<div class=\"centerPag\">" +
								"<div class=\"pagination pagination-centered\"></div> " +
							 "</div>"+ 
							"</td>" +
							"</tr>" +
						"</tfoot>";
		out.println(tFoot);
		printUserTableHeader(out);

		String editForm = "";
		if(isAdminComponent()){		
			String deleteForm =  "<div id=\"deleteUser\" title=\"Delete user\"> " +
							    	"<p>Are you sure that you want to delete <span id=\"userNameText\"></span>? <p>" +
						    	 "</div>";
	    	out.println(deleteForm);
			editForm = "<div id=\"editUser\" title=\"Edit user\">Username: " +
						" <input type=\"text\" id=\"oldUserName\" />Password:" +
						" <input type=\"text\" id=\"oldPassWord\"/>Group: " +
						" <input type=\"text\" id=\"oldGroupName\"/>Assign role:<br/> " +
						"<select id=\"oldRoleType\"> " + 
						" <option value=\"Developer\">Developer</option> " +
						" <option value=\"ProjectManager\">ProjectManager</option> " +
						" <option value=\"SystemArchitect\">SystemArchitect</option>  " +
						" <option value=\"Tester\">Tester</option> "+
						" <option value=\"Unspecified\">Unspecified</option> "+
						"</select>"+
					   "</div>";
		}else if(isProjectManagerComponent()){
			editForm = "<div id=\"editRole\" title=\"Edit\">" +
					"Role: <br /><select id=\"roleType\">" +
					"<option value=\"Developer\">Developer</option>" +
					"<option value=\"SystemArchitect\">System Architect</option>" +
					"<option value=\"Tester\">Tester</option>" +
					"<option value=\"Unspecified\">Unspecified</option>" +
					"</select>" +
					"</div>";
		}
		out.println(editForm);
		out.println("</table>");
		if(userActionMessage != null){
			out.print(userActionMessage);
		}
	}

	// TODO javadoc
	/**
	 * 
	 * @param out
	 * @param timeReports
	 * @param userActionMessage
	 */
	protected void printTimeReportTable(PrintWriter out, ArrayList<TimeReport> timeReports, String userActionMessage, long userId){
		out.println("<div class=\"reportstable-tools\"><input class=\"form-control\" id=\"reportsfilter\" type=\"text\" placeholder=\"Filter Reports\"></input>");
		if(getRole().equalsIgnoreCase("admin")){
		out.println(isWorkerComponent()? "<a type=\"button\" class=\"btn btn-block btn-lg btn-primary create-timereport-btn\" data-create-url-component=workercomponent disabled>Add report</a></div>" :"");
		out.println(isProjectManagerComponent()? "<a type=\"button\" class=\"btn btn-block btn-lg btn-primary create-timereport-btn\" data-create-url-component=projectmanagercomponent disabled>Add report</a></div>" :"");
		}else{
		out.println(isWorkerComponent()? "<a type=\"button\" class=\"btn btn-block btn-lg btn-primary create-timereport-btn\" data-create-url-component=workercomponent>Add report</a></div>" :"");
		out.println(isProjectManagerComponent()? "<a type=\"button\" class=\"btn btn-block btn-lg btn-primary create-timereport-btn\" data-create-url-component=projectmanagercomponent>Add report</a></div>" :"");	
		}
		out.println("<table class=\"footable\" id=\"reportstable\" data-filter=\"#reportsfilter\" border=" + formElement("1") + ">");	
		printTimeReportTableHeader(out);
		for(int i = 0; i < timeReports.size(); ++i){
			printTimeReport(out,timeReports.get(i), userId);
		}
		
		String deleteForm = "<div id=\"deleteTimeReport\" title=\"Delete time report\">" +
				"<p>Are you sure that you want to delete time report with id <span id=\"timeReportIDFix\"></span>? <p>" +
				"</div> <br />";
		out.println(deleteForm);
		printTimeReportTableFooter(out);
		out.println("</table>");
		if(userActionMessage != null){
			out.print(userActionMessage); 
		}
	}

	//TODO JavaDoc
	/**
	 * 
	 * @param out
	 * @param tr
	 */
	private void printTimeReport(PrintWriter out, TimeReport tr, long userId) {
		User user = instance.getUser(tr.getUserId());
		out.println("<tr>");
		out.println(isAdminOrProjectManagerComponent()? "<td data-value='username:" + user.getName() + ",'>" + user.getName() + "</td>" : "");
		out.println(isAdminOrProjectManagerComponent()? "<td data-value='role:" + tr.getRole() + ",'>" + tr.getRole() + "</td>" : "");
		out.println("<td data-value='date:" + tr.getDate() + ",'>" + tr.getDate() + "</td>");
		out.println("<td data-sort-value='" + tr.getWeek() + "' data-value='week:" + tr.getWeek() + ",' >" + tr.getWeek() + "</td>");
		out.println("<td class=\"duration-value\" data-duration='" + tr.getDuration() + "' data-sort-value='" + tr.getDuration() + "' data-value='duration:" + tr.getDuration() + ",'>" + tr.getDuration() + "</td>");
		out.println("<td data-value='type:" + tr.getType() + ",'>" + tr.getType() + "</td>");
		out.println("<td data-sort-value='" + tr.getNumber() + "' data-value='number:" + tr.getNumber() + ",'>" + tr.getNumber() + "</td>");		

		if(isProjectManagerComponent()){
			String checkedAttribute = tr.isSigned() ? "checked" : "";
			boolean signed = tr.isSigned();
			out.println("<td data-value='signed:" + signed + "'><input type=\"hidden\" class=\"timereportid\" name=\"reportid\" value=\""+tr.getId()+"\"></input><input type="+ formElement("checkbox") +" name="+formElement("signed") +" class=\"signedCheckbox\" "+checkedAttribute +"></input></td>");
			
			String editCodePM = "<a class=\"edit-timereport-btn cursor-pointer\" data-edit-date="+ formElement(tr.getDate().toString()) +" data-edit-duration=" + formElement(Long.toString(tr.getDuration())) + "data-edit-number=" + formElement(Long.toString(tr.getNumber())) +" data-edit-id=" + formElement(Long.toString(tr.getId())) + " data-edit-url-component=projectmanagercomponent> Edit </a>";
			String deleteCodePM = "<a class=\"delete-timereport-btn cursor-pointer\" data-delete-id="+ formElement(Long.toString(tr.getId())) + " data-delete-url-component=projectmanagercomponent> Delete </a>";

			if(userId == tr.getUserId()){
				out.println("<td>" + editCodePM +  "</td>");
				out.println("<td>" + deleteCodePM + "</td></tr>");
			} else {
				out.println("<td> </td>");
				out.println("<td> </td></tr>");
			}
		}	

		if(isWorkerComponent()){
			boolean signed = tr.isSigned();
			String signedString = signed ? "Yes":"No";
			out.println("<td data-value='signed:" + signed + "'>" + signedString + "</td>");
			
			String editCodeWorker = "<a class=\"edit-timereport-btn cursor-pointer\" data-edit-date="+ formElement(tr.getDate().toString()) +" data-edit-duration=" + formElement(Long.toString(tr.getDuration())) + "data-edit-number=" + formElement(Long.toString(tr.getNumber())) +" data-edit-id=" + formElement(Long.toString(tr.getId())) + " data-edit-url-component=workercomponent> Edit </a>";
			String deleteCodeWorker = "<a class=\"delete-timereport-btn cursor-pointer\" data-delete-id="+ formElement(Long.toString(tr.getId())) + " data-delete-url-component=workercomponent> Delete </a>";
			
			if(signed){
				out.println("<td> </td>");
				out.println("<td> </td></tr>");
			} else {
				out.println("<td>" + editCodeWorker +  "</td>");
				out.println("<td>" + deleteCodeWorker + "</td></tr>");
			}

		}
	}

	//TODO JavaDoc
	private void printTimeReportTableHeader(PrintWriter out) {
		out.println("<thead><tr>");
		out.println(isAdminOrProjectManagerComponent()? "<th>Username</th>" : "");
		out.println(isAdminOrProjectManagerComponent()? "<th>Role</th>" : "");
		out.println("<th data-type=\"numeric\" data-sort-initial=\"true\">Date</th>");
		out.println("<th data-type=\"numeric\">Week</th>");
		out.println("<th data-type=\"numeric\" title=\"To filter: time:120\">Time (min)</th>");
		out.println("<th>Type</th>");
		out.println("<th data-type=\"numeric\">Number</th>");
		out.println("<th title=\"To filter: signed:true\">Signed</th>");
		out.println((isWorkerComponent() || isProjectManagerComponent())? "<th data-sort-ignore=\"true\">Edit</th>" : "");
		out.println((isWorkerComponent() || isProjectManagerComponent())? "<th data-sort-ignore=\"true\">Remove</th>" : "");	
		out.println("</tr></thead>");
	}

	//TODO JavaDocs
	private void printUserTableHeader(PrintWriter out) {
		out.println("<thead><tr>");
		out.println("<th data-sort-initial=\"true\">Name</th>");
		out.println(isAdminComponent()? "<th>Group</th>" : "");
		out.println("<th>Role</th>");
		out.println(isAdminComponent()? "<th>Password</th>" : "");
		if(isAdminOrProjectManagerComponent()) {
			out.println("<th data-sort-ignore=\"true\">Edit" + (isAdminComponent() ? "" : " (role)") + "</th>");
		}
		out.println(isAdminComponent()? "<th data-sort-ignore=\"true\">Remove</th>" : "");
		out.println("</tr></thead>");
	}
	
	
	private void printTimeReportTableFooter(PrintWriter out) {
		out.println("<tfoot><tr>");
		int colspanTotalTimeTitle = 3;
		int colspanTotalTimeValue = 6;
		if(isAdminOrProjectManagerComponent()){
			colspanTotalTimeTitle = 4;
		}
		out.println("<td colspan='"+ colspanTotalTimeTitle+"'>"
				+ "		<span style=\"font-weight:bold; float:right;\">Total:</span>"
				+ "</td>"
				+ "<td colspan='"+ colspanTotalTimeValue+"'>"
				+ "		<span id=\"totalTime\" style=\"font-weight:bold;\"></span> minutes"
				+ "</td>");
		out.println("</tr></tfoot>");
	}

	
	//TODO Javadoc
	public String getEditTimeReportForm(){
		String editForm = "<div id=\"editTimeReport\" title=\"Edit time report\">" +
				"Date: <input type=\"text\" id=\"edit-date\" placeholder=\"YYYY-MM-dd\"></input><br>"+
				"Duration(min): <input type=\"text\" id=\"edit-duration\"></input><br>"+
				"Number: <input type=\"text\" id=\"edit-number\"><br>" +
				"Type: <select id=\"edit-type\"> " +
    	           " <option value=\"D\">Development</option> " +
    	           " <option value=\"I\">Informal</option> " +
    	            "<option value=\"F\">Formal</option>  " +
    	            "<option value=\"R\">Rework</option> "+
    	           "</select>" +
    	           " </div>";
		return editForm;
	}
	
	//TODO Javadoc
	public String getAddTimeReportForm(){
		String addForm = "<div id=\"createTimeReport\" title=\"Add a new time report\">" +
				"Date: <input type=\"text\" id=\"date\" placeholder=\"YYYY-MM-dd\"></input><br>" + 
				"Duration(min): <input type=\"text\" id=\"duration\"></input><br>" +
				"Number: <input type=\"text\" id=\"number\"><br>" +
				"Type: <select id=\"myType\"> " +
				" <option value=\"D\">Development</option> " +
    	           " <option value=\"I\">Informal</option> " +
    	            "<option value=\"F\">Formal</option>  " +
    	            "<option value=\"R\">Rework</option> "+
    	           "</select>" + 
    	           "</div>";
		return addForm;
	}
	
	
	/**
	 * This method handles a new time report and sets its values to the input given.
	 * 
	 * @param request This is the servlet request
	 * @param out the printwriter used to print out html code
	 * @param userId the id of the current user.
	 * @return String containing a result message either containing a success message or a failure message.
	 */
	public String addNewTimeReport(HttpServletRequest request,
			PrintWriter out, Long userId, String role) {
		return handleTimeReports(request, out, userId, role, false);
	}
	
	/**
	 * This method handles a existing time report and updates its values to the input given.
	 * 
	 * @param request This is the servlet request
	 * @param out the printwriter used to print out html code
	 * @param userId the id of the current user.
	 * @return String containing a result message either containing a success message or a failure message.
	 */
	public String editTimeReport(HttpServletRequest request,
			PrintWriter out, Long userId, String role){
		return handleTimeReports(request, out, userId, role, true);
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
			PrintWriter out, Long userId, String role, boolean existingReport){
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
							if(duration < 0) {
								return "<p class=\"failure-message\">Wrong format on input! Please try again!</p>";
							}
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
									if(instance.editTimeReport(id, userId, currentUser.getGroupId(), role, type, duration, week, Date.valueOf(date), false, number )){
										resultMsg = existingReport ? "<p class=\"success-message\">Time report was edited successfully!</p>" : "<p class=\"success-message\">Time report was created successfully!</p>";
									} else {
										resultMsg = "<p class=\"failure-message\">Time report was signed while you were editing it</p>";
									}
								}else{
									instance.addTimeReport(new TimeReport(currentUser.getGroupId(), role, type, 
											userId, duration, week, Date.valueOf(date), false, number));
									resultMsg = existingReport ? "<pclass=\"success-message\">Time report was edited successfully!</p>" : "<p class=\"success-message\">Time report was created successfully!</p>";
								}
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
			
		//TODO Better error handling /J	
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
	public String deleteTimeReport(HttpServletRequest request) {
		String timeReportId = request.getParameter("deletetimereport");
		if (timeReportId != null) {
			long deleteTimeReport = Long.parseLong(timeReportId);
			return instance.getTimeReport(deleteTimeReport).removeMe() ? "Time report was removed successfully."
					: "Could not remove time report.";
		}
		return null;
	}

	
	//TODO Try to move this to USER.toHTML() /J
	private void printUser(PrintWriter out, String name, String role, String group,
			String editCode, String pw, String deleteCode) {
		out.println("<tr>");
		out.println("<td data-value='name:" + name + "'>" + name + "</td>");
		out.println(isAdminComponent() ? ("<td data-value='group:" + group + "'>" + group + "</td>") : "");
		out.println("<td data-value='role:" + role + "'>" + role + "</td>");
		out.println(isAdminComponent()? ("<td data-value='" + pw + "'>" + pw + "</td>") : "");
		out.println(isAdminOrProjectManagerComponent()? ("<td>" + editCode + "</td>") : "");
		out.println(isAdminComponent()? ("<td>" + deleteCode + "</td>") : "");
		out.println("</tr>");
	}

	//TODO  JavaDoc
	protected String getRole(){
		Object roleObj = session.getAttribute("role");
		String role = "";
		if (roleObj != null) {
			role = (String) roleObj; // if the name exists typecast the role
		}
		return role;
	}

	//TODO JavaDoc
	protected String getName(){
		String name = "";
		Object nameObj = session.getAttribute("name");
		if (nameObj != null) {
			name = (String) nameObj; // if the name exists typecast the name
		}
		return name;
	}
	
	protected String getPageOutro() {
		String outro = "<a class=\"btn btn-block btn-lg btn-danger\" href =" + formElement("logincomponent") + "> Log out </a>" +
		"</body></html>";
		return outro;
	}

	protected  boolean isAdminOrProjectManagerComponent() {
		return isAdminComponent() || isProjectManagerComponent();
	}
	protected abstract boolean isWorkerComponent();
	protected abstract boolean isProjectManagerComponent();
	protected abstract boolean isAdminComponent();
}
