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
	 	out.println("Filter Users: <input id=\"userfilter\" type=\"text\"></input>");
    	out.println("<table id=\"usertable\" data-filter=\"#userfilter\" class=\"footable\" border=" + formElement("1") + ">");	
    	printUserTableHeader(out);
    	System.out.println("Total number of users in system: " + userList.size());
    	for(int i = 0; i < userList.size(); ++i) {			
    		String name = userList.get(i).getName();
    		System.out.println(name);
    		String pw = userList.get(i).getPassword();
    		String role = userList.get(i).getRole();
    		String group = instance.getProjectGroup(userList.get(i).getGroupId()).getName();

    		String editCode = "";
    		if(isAdminOrProjectManagerComponent()){
    		if(isAdminComponent()) {
    			editCode = "<a href=\"#\" onclick=" + formElement("return editUser('" + name + "','" + pw + "','" + group + "')") + " >Edit user</a>";
    		} else {
    			String editURL = "administrationcomponent?edituser="+name;
    			editCode = "<a href=" + formElement(editURL) +" onclick="+formElement("return confirm('Are you sure you want to edit "+name+"?')") + ">Edit</a>";
    		}
    		}
    		String deleteURL = "administrationcomponent?deleteuser="+name;
    		String deleteCode = "<a href=" + formElement(deleteURL) +" onclick="+formElement("return confirm('Are you sure you want to delete "+name+"?')") + "> delete </a>";
    		if (name.equals("admin")){
    			deleteCode = "";
    		}
    		printUser(out, name, role, group, editCode, pw, deleteCode);
    	}		
    	String editForm = "<div id=\"editUser\" title=\"Edit user\">Username: " +
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
    	out.println("<p>Your timereports:</p>");
    	out.println("<table class=\"footable\" border=" + formElement("1") + ">");	
    	printTimeReportTableHeader(out);
    	for(int i = 0; i < timeReports.size(); ++i){
    		Long timeReportId = timeReports.get(i).getId();
    		String editURL = "workercomponent?edittimereport="+timeReportId;
    		String editCode = "<a href=" + formElement(editURL) +" onclick="+formElement("return confirm('Are you sure you want to edit time report "+timeReportId+"?')") + "> edit </a>";
    		String deleteURL = "workercomponent?deletetimereport="+timeReportId;
    		String deleteCode = "<a href=" + formElement(deleteURL) +" onclick="+formElement("return confirm('Are you sure you want to delete time report "+timeReportId+"?')") + "> delete </a>";	
    		printTimeReport(out, editCode, deleteCode, timeReports.get(i));
    	}
    	out.println("</table>");
    	if(userActionMessage != null){
    		out.print("<p>"+ userActionMessage +"</p>"); // style text red please! /J
    	}
    }

    //TODO JavaDoc
    /**
     * 
     * @param out
     * @param editCode
     * @param deleteCode
     * @param tr
     */
	private void printTimeReport(PrintWriter out, String editCode,
			String deleteCode, TimeReport tr) {
		User user = instance.getUser(tr.getUserId());
		out.println("<tr>");
		out.println(isAdminOrProjectManagerComponent()? "<td>" + user.getName() + "</td>" : "");
		out.println(isAdminOrProjectManagerComponent()? "<td>" + user.getRole() + "</td>" : "");
		out.println(isAdminOrProjectManagerComponent()? "<td>" + tr.getWeek() + "</td>" : "");
		out.println("<td>" + tr.getDate() + "</td>");
		out.println("<td>" + tr.getDuration() + "</td>");
		out.println("<td>" + tr.getType() + "</td>");
		//out.println("<td>" + tr.isSigned() + "</td>");
		if(session.getAttribute("role").equals("ProjectManager") || session.getAttribute("role").equals("Admin")){
			String checkedAttribute = tr.isSigned() ? "checked" : "";
//			out.println("<td><form action=\"projectmanagercomponent\" method=\"POST\"><input type=\"hidden\" name=\"reportid\" value=\""+tr.getId()+"\"></input><input type="+ formElement("checkbox") +" name="+formElement("signed") +" class=\"signedCheckbox\"  checked =" +isSigned +"></input><input type=\"submit\" value=\"Submit\" /></form></td>");
			out.println("<td><input type=\"hidden\" class=\"timereportid\" name=\"reportid\" value=\""+tr.getId()+"\"></input><input type="+ formElement("checkbox") +" name="+formElement("signed") +" class=\"signedCheckbox\" "+checkedAttribute +"></input></td>");
		}		
		out.println("<td>" + editCode +  "</td>");
		out.println("<td>" + deleteCode + "</td></tr>");
	}

	//TODO JavaDoc
	private void printTimeReportTableHeader(PrintWriter out) {
		out.println("<tr>");
		out.println(isAdminOrProjectManagerComponent()? "<td><B>Username</B></td>" : "");
		out.println(isAdminOrProjectManagerComponent()? "<td><B>Role</B></td>" : "");
		out.println(isAdminOrProjectManagerComponent()? "<td><B>Week</B></td>" : "");
		out.println("<td><B>Date</B></td>");
		out.println("<td><B>Time (min)</B></td>");
		out.println("<td><B>Type</B></td>");
		out.println("<td><B>Signed</B></td>");
		out.println("<td><B>Edit</B></td>");
		out.println("<td><B>Remove</B></td>");	
		out.println("</tr>");
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

	protected abstract boolean isAdminOrProjectManagerComponent();
	protected abstract boolean isAdminComponent();

}
