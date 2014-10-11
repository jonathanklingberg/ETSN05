package base;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		String userActionMessage;
		String groupActionMessage;
		PrintWriter out = response.getWriter();
		session = request.getSession();
		out.println(getPageIntro());

		// check that the user is logged in as admin, otherwise redirect back to loginComponent
		if (isLoggedIn(request) && getRole().equalsIgnoreCase("Admin")) {
			
			out.println("<h1>Administration page " + "</h1>"); //Is not shown in mockup design!
			
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
			
			groupActionMessage = deleteGroup(request);
			groupActionMessage = editGroup(request, out);
			groupActionMessage = createNewGroup(request, out);
			userActionMessage = deleteUser(request);
			userActionMessage = addNewUser(request, out);
			userActionMessage = editExistingUser(request, out);
			
			ArrayList<User> users = instance.getAllUsers();
			printUserTable(out, users, userActionMessage);
			out.println("<div id=\"createUser\" title=\"Add a new user\">");
			out.println("Username: <input type=\"text\" id=\"name\"></input>");
			out.println("Group: <input type=\"text\" id=\"group\"></input>");
			out.println("Project Manager: <input type=\"checkbox\" id=\"pm\">");
			out.println("</div><br />");
			out.println("<input type=\"button\" id=\"createUserButton\" value=\"Add new\" />");
			listGroups(out, groupActionMessage);
			out.println("<br/><a href=\"administrationcomponent?addNewGroup=\" onclick="+ formElement("return createGroup(this);") + "><input type=\"button\" value=\"Add new\"/></a>");
			out.println("<p><a href =" + formElement("logincomponent") + "> Log out </p>");
			out.println("</body></html>");

		} else {
			System.err.println("Illigal action performed as: " + getRole() + "; tried to access AdministrationComponent.");
			response.sendRedirect("logincomponent");
		}	
	}

	private String editExistingUser(HttpServletRequest request, PrintWriter out) {
		String oldUserName = request.getParameter("oldUserName");
		String newUserName = request.getParameter("editUser");
		String newPassword = request.getParameter("password");
		String newGroupName = request.getParameter("group");
		boolean pmChoice = Boolean.parseBoolean(request.getParameter("pm"));
		ArrayList<ProjectGroup> groups = (ArrayList<ProjectGroup>) instance.getAllProjectGroups();
		boolean groupExists = false;
		for(int i = 0; i < groups.size(); i++) {
			if(groups.get(i).getName().equals(newGroupName)) {
				groupExists = true;
			}
		}
		if(oldUserName != null) {
			if(newPassword.length() == 6) {
				if(checkNewName(newUserName)) {
					if(groupExists) {
						boolean res = instance.editUser(oldUserName, newUserName, newPassword, newGroupName, pmChoice);
						if(res) {
							return "User edited succesfully.";
						} else {
							
							return "User not edited.";
						}
					} else {
						return "The given groupname does not exist.";
					}
				} else {
					return "Incorrect format of username.";
				}
			} else {
				return "Password must consist of 6 characters.";
			}
		}
		return null;
	}

	private String addNewUser(HttpServletRequest request, PrintWriter out) {
		String failMsg = null;
		String username = request.getParameter("addNewUser");
		String groupName = request.getParameter("group");
		boolean pmChecked = Boolean.parseBoolean(request.getParameter("pm"));
		System.out.println(pmChecked);
		if(username != null) {
			if(checkNewName(username)) {
				ProjectGroup p = instance.getProjectGroup(groupName);
				if(p != null) {
				long groupId = p.getId();
					boolean res;
					if(pmChecked) {
						res = instance.addUser(new User(username, createPassword(), "ProjectManager", groupId));
	 				} else {
						res = instance.addUser(new User(username, createPassword(), "Unspecified", groupId));
	 				}
					if(!res){	
						failMsg = "User already exists!";
					}
				} else {
					failMsg = "Group does not exist!";
				}
			} else {
				failMsg = "Incorrect username format!";
			}	
		}
		return failMsg;
	}

	private String deleteUser(HttpServletRequest request) {
		String deleteUser = request.getParameter("deleteuser");
		if(deleteUser != null) {
			return instance.getUser(deleteUser).removeMe() ? "User was removed successfully.": "Could not remove user.";
		}
		return null;
	}

	private String deleteGroup(HttpServletRequest request) {
		String deleteGroup = request.getParameter("deletegroup");
		if (deleteGroup != null) {
			long groupNumber = Long.parseLong(deleteGroup);
			return instance.getProjectGroup(groupNumber).removeMe() ? "Group deleted successfully." : "Failed to delete group.";
		}
		return null;
	}

	private String editGroup(HttpServletRequest request, PrintWriter out) {
		String editGroup = request.getParameter("editgroup");
		if (editGroup != null) {
			long groupNumber = Long.parseLong(editGroup);
			String newGroupName = request.getParameter("groupname");
			if(newGroupName != null){
				boolean res = instance.changeGroupName(groupNumber, newGroupName);	
				if(!res) {
					return "Group name already taken, please try a new one.";
				} else {
					return "Group name has been updated.";
				}
			}
		}
		return null;
	}

	private String createNewGroup(HttpServletRequest request, PrintWriter out) {
		String createNewGroup = request.getParameter("addNewGroup");
		if(createNewGroup != null) {
			boolean res = instance.addProjectGroup(new ProjectGroup(createNewGroup));
			if(res) {
				return "Group has been added.";
			} else {
				return "Group name already taken, please try a new one";
			}
		}
		return null;
	}
	
	public void listGroups(PrintWriter out, String groupActionMessage) { 
	 	 out.println("<p> Groups </p>");
	 	out.println("Filter: <input id=\"filter\" type=\"text\"></input>");
		 out.println("<table data-filter=\"#filter\" id=\"grouplist\"  class=\"footable\" border=" + formElement("1") + ">");
		 out.println("<thead><tr><th data-sort-initial=\"true\">Group</th><th>Edit</th><th>Remove</th></tr></thead>");
		 List<ProjectGroup> projectGroups = instance.getAllProjectGroups();		 
		 for(int i = 0; i < projectGroups.size(); i++) {
			long id = projectGroups.get(i).getId();
			String name = projectGroups.get(i).getName();
			String deleteURL = "administrationcomponent?deletegroup="+id;
		    String deleteCode = "<a href=" + formElement(deleteURL) + " onclick="+formElement("return confirm('Are you sure you want to delete "+name+"?')") + ">Delete</a>";
			String editURL = "administrationcomponent?editgroup="+id;
		    String editCode = "<a href=" + formElement(editURL) + "id=" + formElement(String.valueOf(id)) + "\" onclick="+formElement("return editGroup(this);") + ">Edit</a>";
			out.println("<tr>");
	    	out.println("<td data-value='" + name + "'>" + name + "</td>");
	    	out.println("<td>" + editCode + "</td>");
	    	out.println("<td>" + deleteCode + "</td>");
	    	out.println("</tr>");
		 }
		 out.println("</table>");
		 if(groupActionMessage != null)
			 out.print("<p>"+ groupActionMessage +"</p>");
	}

	protected String getUserTableName() {
		return "<p>System users: </p>";
	}

	protected boolean isAdminOrProjectManagerComponent() {
		return true;
	}

	protected boolean isAdminComponent() {
		return true;
	}

	protected String getTimeReportTableName() {
		return "All time reports";
	}
}
