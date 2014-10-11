package base;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

	// Define states
	protected static final int LOGIN_FALSE = 0;
	protected static final int LOGIN_TRUE = 1;	
	
	protected Connection conn;
	protected DatabaseHandlerInstance instance;
	protected HttpSession session;
	/**
	 * Constructs a servlet and makes a connection to the database. 
	 * It also writes all user names on the console for test purpose. 
	 */
    public ServletBase() {
    	try{
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://vm26.cs.lth.se/puss1403?" +
			           "user=puss1403&password=9dpa2oan");
			instance = DatabaseHandlerInstance.getInstance(conn);
			Statement stmt = conn.createStatement();		    
		    ResultSet rs = stmt.executeQuery("select * from Users"); // Just for testing purposes
		    System.out.println("Successfully connected to database!"); // Success message in console
		    stmt.close();
			
		} catch (SQLException ex) {
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
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
		if (objectState != null) 
			state = (Integer) objectState; 
		return (state == LOGIN_TRUE);
	}

    
    /**
     * Can be used to construct form elements.
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
							"<script src=\"js/footable.paginate.js\"></script>" +
							"<script src=\"js/footable.plugin.template.js\"></script>" +
							"<script src=\"js/footable.sort.js\"></script>" +
							"<script src=\"js/footable.striping.js\"></script>" +
							"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/footable.core.min.css\"/>" +
							"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/footable.metro.min.css\"/>" +
//							"<link rel=\"stylesheet\" type=\"text/css\" href=\"css/footable.standalone.min.css\"/>" +
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

    // Print User Table according to mockup design in SRS
    protected void printUserTable(PrintWriter out, ArrayList<User> userList, String userActionMessage) {
    	out.println(getUserTableName());
    	out.println("<table border=" + formElement("1") + ">");	
    	printUserTableHeader(out);	    
    	for(int i = 0; i < userList.size(); ++i) {			
    		String name = userList.get(i).getName();
    		System.out.println(name);
    		String pw = userList.get(i).getPassword();
    		String role = userList.get(i).getRole();
    		String group = instance.getProjectGroup(userList.get(i).getGroupId()).getName();
    		String editURL = "administrationcomponent?edituser="+name;
    		String editCode = "<a href=" + formElement(editURL) +" onclick="+formElement("return confirm('Are you sure you want to edit "+name+"?')") + "> edit </a>";
    		String deleteURL = "administrationcomponent?deleteuser="+name;
    		String deleteCode = "<a href=" + formElement(deleteURL) +" onclick="+formElement("return confirm('Are you sure you want to delete "+name+"?')") + "> delete </a>";
    		if (name.equals("admin")){
    			deleteCode = "";
    		}
    		printUser(out, name, role, group, editCode, pw, deleteCode);
    	}		
    	out.println("</table>");
    	if(userActionMessage != null)
    		out.print("<p>"+ userActionMessage +"</p>");
    }
    // Print Time Report Table according to mockup design in SRS
    protected void printTimeReportTable(PrintWriter out, ArrayList<TimeReport> timeReports){
    	out.println("<BR>");
    	out.println(getTimeReportTableName());
    	out.println("<table border=" + formElement("1") + ">");	
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
    	out.println("<div id=\"testcont\">testcont</div>");
    }

	private void printTimeReport(PrintWriter out, String editCode,
			String deleteCode, TimeReport tr) {
		User user = instance.getUser(tr.getUserId());
		out.println("<tr>");
		out.println(isAdminOrProjectManagerComponent()? "<td><B>" + user.getName() + "</B></td>" : "");
		out.println(isAdminOrProjectManagerComponent()? "<td><B>" + user.getRole() + "</B></td>" : "");
		out.println(isAdminOrProjectManagerComponent()? "<td><B>" + tr.getWeek() + "</B></td>" : "");
		out.println("<td>" + tr.getDate() + "</td>");
		out.println("<td>" + tr.getDuration() + "</td>");
		out.println("<td>" + tr.getType() + "</td>");			
		//out.println("<td>" + tr.isSigned() + "</td>");
		if(session.getAttribute("role").equals("ProjectManager") || session.getAttribute("role").equals("Admin")){
			String isSigned = tr.isSigned() ? "checked" : "";		
//			out.println("<td><form action=\"projectmanagercomponent\" method=\"POST\"><input type=\"hidden\" name=\"reportid\" value=\""+tr.getId()+"\"></input><input type="+ formElement("checkbox") +" name="+formElement("signed") +" class=\"signedCheckbox\"  checked =" +isSigned +"></input><input type=\"submit\" value=\"Submit\" /></form></td>");
			out.println("<td><input type=\"hidden\" class=\"timereportid\" name=\"reportid\" value=\""+tr.getId()+"\"></input><input type="+ formElement("checkbox") +" name="+formElement("signed") +" class=\"signedCheckbox\" checked=" +isSigned +"></input></td>");
		}
		
		out.println("<td>" + editCode +  "</td>");
		out.println("<td>" + deleteCode + "</td></tr>");
	}  

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

	private void printUser(PrintWriter out, String name, String role, String group,
		String editCode, String pw, String deleteCode) {
		out.println("<tr>");
		out.println("<td>" + name + "</td>");
		out.println(isAdminComponent()? ("<td>" + group + "</td>") : "");
		out.println("<td>" + role + "</td>");
		out.println(isAdminComponent()? ("<td>" + pw + "</td>") : "");
		out.println(isAdminOrProjectManagerComponent()? ("<td>" + editCode + "</td>") : "");
		out.println(isAdminComponent()? ("<td>" + deleteCode + "</td>") : "");
		out.println("</tr>");
	}

	private void printUserTableHeader(PrintWriter out) {
		out.println("<tr>");
		out.println("<td><B>Name</B></td>");
		out.println(isAdminComponent()? "<td><B>Group</B></td>" : "");
		out.println("<td><B>Role</B></td>");
		out.println(isAdminComponent()? "<td><B>Password</B></td>" : "");
		out.println(isAdminOrProjectManagerComponent()? "<td><B>Edit</B></td>" : "");
		out.println(isAdminComponent()? "<td><B>Remove</B></td></tr>" : "");
	}
	
	protected String getRole(){
		Object roleObj = session.getAttribute("role");
		String role = "";
		if (roleObj != null) {
			role = (String) roleObj; // if the name exists typecast the role
		}
		return role;
	}
	
	protected String getName(){
		String name = "";
		Object nameObj = session.getAttribute("name");
		if (nameObj != null) {
			name = (String) nameObj; // if the name exists typecast the name
			// to a string
		}
		return name;
	}

	protected abstract String getTimeReportTableName();
	protected abstract boolean isAdminOrProjectManagerComponent();
	protected abstract boolean isAdminComponent();
	protected abstract String getUserTableName();

}