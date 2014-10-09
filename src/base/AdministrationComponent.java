package base;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import database.User;
import database.WorkspaceInstance;
import java.util.ArrayList;
import database.ProjectGroup;
import java.util.List;
import java.util.Random;

/**
 * Servlet implementation class Administration. Constructs a page for
 * administration purpose. Checks first if the user is logged in and then if it
 * is the administrator. The validation whether a user is allowed to perform a
 * specific action should be carried out in this class, whereas the format on
 * the input should not be taken care of here.
 * 
 * @author SG
 * @version 0.2
 */
@WebServlet("/administrationcomponent")
public class AdministrationComponent extends ServletBase {
	private static final long serialVersionUID = 1L;
	private static final int PASSWORD_LENGTH = 6;

	private WorkspaceInstance instance = WorkspaceInstance.getInstance(conn);
       
//    /**
//     * generates a form for adding new users
//     * @return HTML code for the form
//     */
    private String addUserForm() {
    	String html;
    	html = "<p> <form name=" + formElement("input");
    	html += " method=" + formElement("get");
    	html += "<p> Add user name: <input type=" + formElement("text") + " name=" + formElement("addname") + '>';    	
    	html += "<input type=" + formElement("submit") + "value=" + formElement("Add user") + '>';
    	html += "</form>";
    	return html;
    }



	/**
	 * @see ServletBase#ServletBase()
	 */
	public AdministrationComponent() {
		super();
		// TODO Auto-generated constructor stub
	}



	// /**
	// * Checks if a username corresponds to the requirements for user names.
	// * @param name The investigated username
	// * @return True if the username corresponds to the requirements
	// */
	private boolean checkNewName(String name) {
		int length = name.length();
		boolean ok = (length >= 5 && length <= 10);
		if (ok)
			for (int i = 0; i < length; i++) {
				int ci = (int) name.charAt(i);
				boolean thisOk = ((ci >= 48 && ci <= 57)
						|| (ci >= 65 && ci <= 90) || (ci >= 97 && ci <= 122));
				// String extra = (thisOk ? "OK" : "notOK");
				// System.out.println("bokst:" + name.charAt(i) + " " +
				// (int)name.charAt(i) + " " + extra);
				ok = ok && thisOk;
			}
		return ok;
	}

	// /**
	// * Creates a random password.
	// * @return a randomly chosen password
	// */
	private String createPassword() {
		String result = "";
		Random r = new Random();
		for (int i = 0; i < PASSWORD_LENGTH; i++)
			result += (char) (r.nextInt(26) + 97); // 122-97+1=26
		return result;
	}

	// /**
	// * Adds a user and a randomly generated password to the database.
	// * @param name Name to be added
	// * @return true if it was possible to add the name. False if it was not,
	// e.g.
	// * because the name already exist in the database.
	// */
	private boolean addUser(String name) {

		return WorkspaceInstance.getInstance(conn).addUser(name,
				createPassword());
		// try{

		// Statement stmt = conn.createStatement();
		// String statement = "insert into users (name, password) values('" +
		// name + "', '" +
		// createPassword() + "')";
		// System.out.println(statement);
		// stmt.executeUpdate(statement);
		// stmt.close();

		// } catch (SQLException ex) {
		// resultOk = false;
		// System.out.println("SQLException: " + ex.getMessage());
		// System.out.println("SQLState: " + ex.getSQLState());
		// System.out.println("VendorError: " + ex.getErrorCode());
		// }

	}

	// /**
	// * Deletes a user from the database.
	// * If the user does not exist in the database nothing happens.
	// * @param name name of user to be deleted.
	// */
	private void deleteUser(String name) {
		WorkspaceInstance.getInstance(conn).deleteUser(name);
		// try{
		// Statement stmt = conn.createStatement();
		// String statement = "delete from users where name='" + name + "'";
		// System.out.println(statement);
		// stmt.executeUpdate(statement);
		// stmt.close();
		//
		// } catch (SQLException ex) {
		// System.out.println("SQLException: " + ex.getMessage());
		// System.out.println("SQLState: " + ex.getSQLState());
		// System.out.println("VendorError: " + ex.getErrorCode());
		// }
	}

	/**
	 * Handles input from the administrator and displays information for
	 * administration.
	 * 
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println(getPageIntro());

		String myName = "";
		HttpSession session = request.getSession(true);
		Object nameObj = session.getAttribute("name");
		if (nameObj != null) {
			myName = (String) nameObj; // if the name exists typecast the name
										// to a string
		}
		System.out.println("MyName: " + myName);
		// check that the user is logged in
		if (!isLoggedIn(request))
			response.sendRedirect("logincomponent");
		else
			if (myName.equals("admin")) {
				out.println("<h1>Administration page " + "</h1>");
				
				// check if the administrator wants to add a new user in the form
				String newName = request.getParameter("addname");
				if (newName != null) {
					if (checkNewName(newName)) {
						boolean addPossible = addUser(newName);
						if (!addPossible)
							out.println("<p>Error: Suggested user name not possible to add</p>");
					}	else
						out.println("<p>Error: Suggesten name not allowed</p>");
				}
				
				String deleteGroup = request.getParameter("deletegroup");
				if (deleteGroup != null) {
					long groupNumber = Long.parseLong(deleteGroup);
					instance.getProjectGroup(groupNumber).removeMe();
				}
				
				String editGroup = request.getParameter("editgroup");
				if (editGroup != null) {
					long groupNumber = Long.parseLong(editGroup);
					String newGroupName = request.getParameter("name");
					if(newGroupName != null){
						boolean res = instance.changeGroupName(groupNumber, newGroupName);	
						if(!res) {
							String code ="alert(\"Group name already taken, please try a new one\")";
							script(out, code);
						} else {
							String code ="alert(\"Group name has been updated!\")";
							script(out, code);
						}
					}
				}
				
				String deleteUser = request.getParameter("deleteuser");
				if(deleteUser != null) {
					instance.getUser(deleteUser).removeMe();
				}
//				listUsers(out);
				listGroups(out);
				out.println("<p><a href =" + formElement("logincomponent") + "> Log out </p>");
				out.println("</body></html>");
			} else  // name not admin
				response.sendRedirect("functionality.html");	
		}
	
	public void listUsers(PrintWriter out) {
		out.println("<p>Registered users:</p>");
	    out.println("<table border=" + formElement("1") + ">");
	    out.println("<tr><td>Name</td><td>Group</td><td>Role</td><td>Password</td><td>Edit</td><td>Remove</td></tr>");
		List<User> users = instance.getUsers();
		for(int i = 0; i < users.size(); i++) {
	    	String name = users.get(i).getName();
	    	String pw = "null"; // users.get(i).password();
	    	String role = users.get(i).getRole();
	    	String group = instance.getProjectGroup(users.get(i).getGroupId()).getProjectName();
	    	String editURL = "administrationcomponent?edituser="+name;
	    	String editCode = "<a href=" + formElement(editURL) +" onclick="+formElement("return confirm('Are you sure you want to edit "+name+"?')") + "> edit </a>";
	    	String deleteURL = "administrationcomponent?deleteuser="+name;
	    	String deleteCode = "<a href=" + formElement(deleteURL) +" onclick="+formElement("return confirm('Are you sure you want to delete "+name+"?')") + "> delete </a>";
	    	if (name.equals("admin")){
	    		deleteCode = "";
	    	}
	    	out.println("<tr>");
	    	out.println("<td>" + name + "</td>");
	    	out.println("<td>" + group + "</td>");
	    	out.println("<td>" + role + "</td>");
	    	out.println("<td>" + pw + "</td>");
	    	out.println("<td>" + editCode + "</td>");
	    	out.println("<td>" + deleteCode + "</td>");
	    	out.println("</tr>");
		}
		out.println("</table>");
	}
	
	public void listGroups(PrintWriter out) { 
		String javascriptCode = "function editGroup(link){ var name = prompt('Please enter a new name for the group.'); if (name != null) { alert(link.id); link.href= link.href+\"&name=\"+name; return true; } return false;}";
		javascriptCode += "function createGroup() { var name = prompt('Please enter a name for the new group.');  if (name != null) { var theUrl = \"administrationcomponent?addNewGroup=\"+name; var xmlHttp = new XMLHttpRequest();xmlHttp.open( \"GET\", theUrl, false );xmlHttp.send( null );}}";
		script(out, javascriptCode);
		out.println("<p> Groups </p>");
		 out.println("<table border=" + formElement("1") + ">");
		 out.println("<tr><td>Group</td><td>Edit</td><td>Remove</td></tr>");
		 List<ProjectGroup> projectGroups = instance.getProjectGroups();
		 
		 String editURL2 = "administrationcomponent?editgroup="+"1";
	     String editCode2 = "<a href=" + formElement(editURL2) +
	    			            " id=\"1\" onclick="+formElement("return editGroup(this);") + 
	    			            "> edit </a>";
		out.println("<tr>");
    	out.println("<td>" + editCode2 + "</td>");
		 
		 for(int i = 0; i < projectGroups.size(); i++) {
			 long id = projectGroups.get(i).getId();
			 String name = projectGroups.get(i).getProjectName();
			 
			 String deleteURL = "administrationcomponent?deletegroup="+id;
		     String deleteCode = "<a href=" + formElement(deleteURL) + " onclick="+formElement("return confirm('Are you sure you want to delete "+name+"?')") + "> delete </a>";
			 String editURL = "administrationcomponent?editgroup="+id;
		     String editCode = "<a href=" + formElement(editURL) + "id=" + formElement(String.valueOf(id)) + "\" onclick="+formElement("return editGroup(this);") + "> edit </a>";
			out.println("<tr>");
	    	out.println("<td>" + name + "</td>");
	    	out.println("<td>" + editCode + "</td>");
	    	out.println("<td>" + deleteCode + "</td>");
	    	out.println("</tr>");
		 }
		 out.println("</table>");
		 out.println("<button type=\"button\" onclick=" + formElement("return createGroup();") + ">Add new</button>");
	}
	
	private void script(PrintWriter out, String code){
		out.print("<script>" + code + "</script>");
	}

}
