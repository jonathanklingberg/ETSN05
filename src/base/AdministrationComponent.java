package base;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import data.Role;
import database.ProjectGroup;
import database.User;

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

	private boolean addUser(String name) {
		return instance.addUser(name,
				createPassword());
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

		String role = "";
		HttpSession session = request.getSession(true);
		Object roleObj = session.getAttribute("role");
		if (roleObj != null) {
			role = (String) roleObj;
		}
		// check that the user is logged in as admin, otherwise redirect back to loginComponent
		if (isLoggedIn(request) && role.equals("Admin")) {
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
			
			deleteGroup(request);
			editGroup(request, out);
			createNewGroup(request, out);
			deleteUser(request);
			addNewUser(request, out);
			
			ArrayList<User> users = instance.getUsers();
			listUsers(out, users);
			out.println("<div id=\"createUser\" title=\"Add a new user\">");
			out.println("Username: <input type=\"text\" id=\"name\"></input>");
			out.println("Group: <input type=\"text\" id=\"group\"></input>");
			out.println("Project Manager: <input type=\"checkbox\" id=\"pm\">");
			out.println("</div><br />");
			out.println("<input type=\"button\" id=\"createUserButton\" value=\"Add new\" />");
			listGroups(out);
			out.println("<p><a href =" + formElement("logincomponent") + "> Log out </p>");
			out.println("</body></html>");

		} else {
			System.err.println("Illigal action performed as: " + role + "; tried to access AdministrationComponent.");
			response.sendRedirect("logincomponent");
		}	
	}

	private void addNewUser(HttpServletRequest request, PrintWriter out) {
		String failMsg = "";
		String username = request.getParameter("addNewUser");
		String group = request.getParameter("group");
		String pmChecked = request.getParameter("pm");
		if(username != null) {
			if(checkNewName(username)) {
				if(instance.getProjectId(group) != -1) {
					if(pmChecked.equals("true")) {
						instance.addUser(new User(username, createPassword(), "ProjectManager", instance.getProjectId(group)));
	 				} else {
						instance.addUser(new User(username, createPassword(), "Unspecified", instance.getProjectId(group)));
	 				}
				} else {
					failMsg += "Group does not exist! \n";
				}
			} else {
				failMsg += "Username already taken! Please try a new one";
			}
			if(!failMsg.isEmpty()) {
				String code = "alert('" + failMsg + "')";
				script(out, code);
			}
		}
	}

	private void deleteUser(HttpServletRequest request) {
		String deleteUser = request.getParameter("deleteuser");
		if(deleteUser != null) {
			instance.getUser(deleteUser).removeMe();
		}
	}

	private void deleteGroup(HttpServletRequest request) {
		String deleteGroup = request.getParameter("deletegroup");
		if (deleteGroup != null) {
			long groupNumber = Long.parseLong(deleteGroup);
			System.out.println(instance.getProjectGroup(groupNumber).removeMe() ? "success" : "fail");
		}
	}

	private void editGroup(HttpServletRequest request, PrintWriter out) {
		String editGroup = request.getParameter("editgroup");
		if (editGroup != null) {
			long groupNumber = Long.parseLong(editGroup);
			String newGroupName = request.getParameter("groupname");
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
	}

	private void createNewGroup(HttpServletRequest request, PrintWriter out) {
		String createNewGroup = request.getParameter("addNewGroup");
		if(createNewGroup != null) {
			boolean res = instance.addProjectGroup(new ProjectGroup(createNewGroup));
			if(res) {
				String code ="alert(\"Group has been added!\")";
				script(out, code);
			} else {
				String code ="alert(\"Group name already taken, please try a new one\")";
				script(out, code);
			}
		}
	}
	
	public void listGroups(PrintWriter out) { 
	 	 out.println("<p> Groups </p>");
		 out.println("<table border=" + formElement("1") + ">");
		 out.println("<tr><td>Group</td><td>Edit</td><td>Remove</td></tr>");
		 List<ProjectGroup> projectGroups = instance.getProjectGroups();		 
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
		 out.println("<br/><a href=\"administrationcomponent?addNewGroup=\" onclick="+ formElement("return createGroup(this);") + "><input type=\"button\" value=\"Add new\"/></a>");
	}
	
	private void script(PrintWriter out, String code){
		out.print("<script>" + code + "</script>");
	}

	protected String getUserTableName() {
		return "<p>System users: </p>";
	}

	protected String getUserTable() {			
		return "<tr><td>Name</td><td>Group</td><td>Role</td><td>Password</td><td>Edit</td><td>Remove</td></tr>";
	}

	protected boolean isAdminOrProjectManager() {
		return true;
	}

	protected boolean isAdmin() {
		return true;
	}
}
