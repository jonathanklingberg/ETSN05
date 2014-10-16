package base;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://vm26.cs.lth.se/puss1403?" +
					"user=puss1403&password=9dpa2oan");
			instance = DatabaseHandlerInstance.getInstance(conn);
			//TODO see description below: /J
			/**
			 *  We're very dependent of this connection and some sort of 
			 *  re-connection functionality in case the connection closes is 
			 *  of high importance, please try if this actually works or if it
			 *  needs to be implemented, something prints out the following success-msg
			 *  multiple times but I can't see why there is? /J
			 *  
			 *  It's easy to test simply by manipulating the server name temporary 
			 *  to something that cannot be resolved for example. (vmxxx.cs.lth.se) /J
			 */
			System.out.println("Successfully connected to database!");
			//TODO BETTER ERROR HANDLING! /J
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
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
				"<script src=\"js/jquery-1.8.3.js\"></script>" +
				"<script src=\"js/jquery-ui-1.9.2.custom.min.js\"></script>" +
				"<script src=\"js/epuss.js\"></script>" +
				"<script src=\"js/footable.js\"></script>" +
				"<script src=\"js/footable.bookmarkable.js\"></script>" +
				"<script src=\"js/footable.filter.js\"></script>" +
				"<script src=\"js/footable.grid.js\"></script>" +
				"<script src=\"js/footable.memory.js\"></script>" +

//							Not needed for our project but some other footable plugins might need this so leave it commented
//							"<script src=\"js/footable.paginate.js\"></script>" + /J

							"<script src=\"js/footable.plugin.template.js\"></script>" +
							"<script src=\"js/footable.sort.js\"></script>" +
							"<script src=\"js/footable.striping.js\"></script>" +
							"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/epuss.css\"/>" +
							"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/footable.core.min.css\"/>" +
							"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/footable.metro.min.css\"/>" +
							"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/jquery-ui-1.9.2.custom.min.css\"/>" +
							"<title> The Base Block System </title></head><body>";
		return intro;
	}

	protected String getViewLayoutStart(){
		return "";
	}

	protected String getViewLayoutSEnd(){
		return "";
	}

	//TODO JavaDoc
	/**
	 *
	 * @param out
	 * @param userList
	 * @param userActionMessage
	 */
	protected void printUserTable(PrintWriter out, ArrayList<User> userList, String userActionMessage) {
		out.println("Filter Users: <input id=\"userfilter\" type=\"text\"></input><br/>");
		out.println("<table id=\"usertable\" data-filter=\"#userfilter\" class=\"footable\" border=" + formElement("1") + ">");	
		printUserTableHeader(out);
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
				editCode = "<a href=\"#\" onclick=" + formElement("return editUser('" + name + "','" + pw + "','" + group + "', '" + role + "')") + " >Edit user</a>";
			} else if(isProjectManagerComponent()){	
				editCode = "<a href=\"#\" onclick=" + formElement("return editRole(" + userId + ")") + " >Edit</a>";
			} else {
				String editURL = "administrationcomponent?edituser="+name;
				editCode = "<a href=" + formElement(editURL) +" onclick="+formElement("return confirm('Are you sure you want to edit "+name+"?')") + ">Edit</a>";
			}

			String deleteCode = "<a href='#' onclick="+formElement("return deleteUser('" + name + "')") + "> Delete </a>";
			if (name.equals("admin")){
				deleteCode = "";
			}

			printUser(out, name, role, group, editCode, pw, deleteCode);

		}
		String editForm = "";
		if(isAdminComponent()){		
			String deleteForm =  "<div id=\"deleteUser\" title=\"Delete user\"> " +
					"<p>Are you sure that you want to delete <span id=\"userNameText\"></span>? <p>" +
					"</div> <br />";
			out.println(deleteForm);
			editForm = "<div id=\"editUser\" title=\"Edit user\">Username: " +
					"<input type=\"text\" id=\"oldUserName\" />Password:" +
					" <input type=\"text\" id=\"oldPassWord\"/>Group: " +
					" <input type=\"text\" id=\"oldGroupName\"/>Assign role:<br/> " +
					"<select id=\"myselect2\"> " + 
					" <option value=\"Developer\">Developer</option> " +
					" <option value=\"ProjectManager\">ProjectManager</option> " +
					"<option value=\"SystemArchitect\">SystemArchitect</option>  " +
					"<option value=\"Tester\">Tester</option> "+
					" <option value=\"Unspecified\">Unspecified</option> "+
					"</select>"+
					" </div>";
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
		//TODO something with editForm! /J It already works, just leave the out.println(editForm) be /Soheil
		out.println("</table>");
		if(userActionMessage != null){
			out.print("<p>"+ userActionMessage +"</p>");
		}
	}

	// TODO javadoc
	/**
	 * 
	 * @param out
	 * @param timeReports
	 * @param userActionMessage
	 */
	protected void printTimeReportTable(PrintWriter out, ArrayList<TimeReport> timeReports, String userActionMessage){
		out.println("Filter Reports: <input id=\"reportsfilter\" type=\"text\" placeholder=\"Ex. week:2 type:D\"></input>");
		out.println("<table class=\"footable\" id=\"reportstable\" data-filter=\"#reportsfilter\" border=" + formElement("1") + ">");	
		printTimeReportTableHeader(out);
		for(int i = 0; i < timeReports.size(); ++i){
			printTimeReport(out,timeReports.get(i));
		}

		String deleteForm =  "<div id=\"deleteTimeReport\" title=\"Delete time report\"> " +
				"<p>Are you sure that you want to delete time report with id <span id=\"timeReportIDFix\"></span>? <p>" +
				"</div> <br />";
		out.println(deleteForm);
		printUserTableFooter(out);
		out.println("</table>");
		if(userActionMessage != null){
			out.print(userActionMessage); // style text red please! /J
		}
	}

	//TODO JavaDoc
	/**
	 * 
	 * @param out
	 * @param tr
	 */
	private void printTimeReport(PrintWriter out, TimeReport tr) {
		User user = instance.getUser(tr.getUserId());
		out.println("<tr>");
		out.println(isAdminOrProjectManagerComponent()? "<td data-value='username:" + user.getName() + "'>" + user.getName() + "</td>" : "");
		out.println(isAdminOrProjectManagerComponent()? "<td data-value='role:" + tr.getRole() + "'>" + tr.getRole() + "</td>" : "");
		out.println(isAdminOrProjectManagerComponent()? "<td data-value='week:" + tr.getWeek() + "' >" + tr.getWeek() + "</td>" : "");
		out.println("<td data-value='date:" + tr.getDate() + "'>" + tr.getDate() + "</td>");
		out.println("<td class=\"duration-value\" data-duration='" + tr.getDuration() + "' data-value='duration:" + tr.getDuration() + "'>" + tr.getDuration() + "</td>");
		out.println("<td data-value='type:" + tr.getType() + "'>" + tr.getType() + "</td>");
		out.println("<td data-value='number:" + tr.getNumber() + "'>" + tr.getNumber() + "</td>");		

		if(isProjectManagerComponent()){
			String checkedAttribute = tr.isSigned() ? "checked" : "";
			boolean signed = tr.isSigned();
			out.println("<td data-value='signed:" + signed + "'><input type=\"hidden\" class=\"timereportid\" name=\"reportid\" value=\""+tr.getId()+"\"></input><input type="+ formElement("checkbox") +" name="+formElement("signed") +" class=\"signedCheckbox\" "+checkedAttribute +"></input></td>");
		}	

		if(isWorkerComponent()){
			String editCode = "<a href=\"#\" onclick=" + formElement("return editTimeReport('" + tr.getDate() + "','" + tr.getDuration() + "','" + tr.getNumber() + "','" + tr.getId() + "')") + "> edit </a>";
			//		    String deleteURL = "workercomponent?deletetimereport="+tr.getId();


			String deleteCode = "<a href=\"#\" onclick="+formElement("return deleteTimeReport(" + tr.getId() + ")") + ">delete</a>";


			//   String deleteCode = "<a href=" + formElement(deleteURL) +" onclick="+formElement("return confirm('Are you sure you want to delete time report "+tr.getId()+"?')") + "> delete </a>";
			boolean signed = tr.isSigned();
			out.println("<td data-value='signed:" + signed + "'>" + signed + "</td>");
			if(signed){
				out.println("<td> </td>");
				out.println("<td> </td></tr>");
			} else {
				out.println("<td>" + editCode +  "</td>");
				out.println("<td>" + deleteCode + "</td></tr>");
			}

		}
	}

	//TODO JavaDoc
	private void printTimeReportTableHeader(PrintWriter out) {
		out.println("<thead><tr>");
		out.println(isAdminOrProjectManagerComponent()? "<th>Username</th>" : "");
		out.println(isAdminOrProjectManagerComponent()? "<th>Role</th>" : "");
		out.println(isAdminOrProjectManagerComponent()? "<th>Week</th>" : "");
		out.println("<th data-sort-initial=\"true\">Date</th>");
		out.println("<th title=\"To filter: time:120\">Time (min)</th>");
		out.println("<th>Type</th>");
		out.println("<th>Number</th>");
		out.println("<th title=\"To filter: signed:true\">Signed</th>");
		out.println(isWorkerComponent()? "<th data-sort-ignore=\"true\">Edit</th>" : "");
		out.println(isWorkerComponent()? "<th data-sort-ignore=\"true\">Remove</th>" : "");	
		out.println("</thead></tr>");
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

	private void printUserTableFooter(PrintWriter out) {
		out.println("<tfoot><tr>");
		int colspanTotalTimeTitle = 1;
		int colspanTotalTimeValue = 6;
		if(isAdminOrProjectManagerComponent()){
			colspanTotalTimeTitle = 4;
			colspanTotalTimeValue = 4;
		}
		out.println("<td colspan='"+ colspanTotalTimeTitle+"'>"
				+ "		<span style=\"font-weight:bold; float:right;\">Total:</span>"
				+ "</td>"
				+ "<td colspan='"+ colspanTotalTimeValue+"'>"
				+ "		<span id=\"totalTime\" style=\"font-weight:bold;\"></span> minutes"
				+ "</td>");
		out.println("</tr></tfoot>");
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

	protected  boolean isAdminOrProjectManagerComponent() {
		return isAdminComponent() || isProjectManagerComponent();
	}
	protected abstract boolean isWorkerComponent();
	protected abstract boolean isProjectManagerComponent();
	protected abstract boolean isAdminComponent();
}
