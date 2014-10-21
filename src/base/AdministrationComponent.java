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
 * @version 0.3
 */

//TODO add styled paragraphs! You should never print text that aren't wrapped inside any html-element, 
//			paragraph or span are the most common ones for simple text
// example: <p class="action-success">User added successfully!</p>
//		& 	<p class="action-fail">Failed to add user!</p> 
// These classes can be easily styled with simple css then!/J
@WebServlet("/administrationcomponent")
public class AdministrationComponent extends ServletBase {
	private static final long serialVersionUID = 1L;
	private static final int PASSWORD_LENGTH = 6;

	/**
	 * @see ServletBase#ServletBase()
	 */
	public AdministrationComponent() {
		super();
	}
	
	//TODO Please try describe the function a little bit better. /J
	 /**
	 * Checks if a username corresponds to the requirements for user names.
	 * @param name The investigated username
	 * @return True if the username corresponds to the requirements
	 */
	private boolean checkNewName(String name) {
		int length = name.length();
		boolean ok = (length >= 5 && length <= 10);
		if (ok)
			for (int i = 0; i < length; i++) {
				int ci = (int) name.charAt(i);
				boolean thisOk = ((ci >= 48 && ci <= 57) || (ci >= 65 && ci <= 90) || (ci >= 97 && ci <= 122));
				ok = ok && thisOk;
			}
		return ok;
	}
	
	//TODO JavaDoc with good description /J
	private boolean checkNewGroupName(String name) {
		System.out.println("Name of group that admin wants to add: " + name);
		int length = name.length();
		boolean ok = (length >= 1 && length <= 20);
		if (ok)
			for (int i = 0; i < length; i++) {
				int ci = (int) name.charAt(i);
				boolean thisOk = ((ci >= 48 && ci <= 57) || (ci >= 65 && ci <= 90) || (ci >= 97 && ci <= 122));
				ok = ok && thisOk;
			}
		return ok;
	}
	
	//TODO JavaDoc with good description /J
	private boolean checkNewPassword(String name) {
		System.out.println("my password: "  + name);
		int length = name.length();
		boolean ok = (length == PASSWORD_LENGTH);
		if (ok)
			for (int i = 0; i < length; i++) {
				int ci = (int) name.charAt(i);
				boolean thisOk = ci >= 97 && ci <= 122;
				ok = ok && thisOk;
			}
		return ok;
	}
	
	//TODO please try to describe a little bit better, how many chars aso. /J
	 /**
	 * Creates a random password.
	 * @return a randomly chosen password
	 */
	private String createPassword() {
		String result = "";
		Random r = new Random();
		for (int i = 0; i < PASSWORD_LENGTH; i++)
			result += (char) (r.nextInt(26) + 97); // 122-97+1=26
		return result;
	}
	
	//TODO JavaDoc description
	/**
	 * 
	 * @param name
	 */
	private boolean addUser(String name) {
		return instance.addUser(name, createPassword());
	}

	/**
	 * Handles input from the administrator and displays information for
	 * administration.
	 * 
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String userActionMessage = null;
		String groupActionMessage = null;
		PrintWriter out = response.getWriter();
		session = request.getSession();
		out.println(getPageIntro());

		// check that the user is logged in as admin, otherwise redirect back to loginComponent
		if (isLoggedIn(request) && getRole().equalsIgnoreCase("Admin")) {
			
			out.println("<h1>Administration page</h1>"); 
			
			// check if the administrator wants to add a new user in the form
			String newName = request.getParameter("addname");
			if (newName != null) {
				if (checkNewName(newName)) {
					boolean addPossible = addUser(newName);
					if (!addPossible)
						//TODO style red please
						out.println("<p>Error: Suggested user name not possible to add</p>"); 
				}	else
					out.println("<p>Error: Suggesten name not allowed</p>");
			}
			
			//TODO try to refactor this please! very hard to read and understand? /J
			// This is ineffective, you're currently executing all actions until you find the correct one that doesn't return null, am I right?
			// This means you're trying to perform request.getAttribute/getParameter on alot of parameters that doesn't exists.
			// And for example in editExistingUser you always perform the instance.getAllProjectGroups() no matter if the parameters exists or not... No good!!
			// What you normally do is that you do a switch-case on the action you're trying to execute. for example:
			// AdministratorComponent?action=addNewUser&userid=<id> and in that way you'll be able to perform a simple switch-case on the action parameter instead.
			// Do you follow me?
			// You don't have to do this if you don't have time but just so that you know /J
			if(groupActionMessage == null)
				groupActionMessage = deleteGroup(request);
			if(groupActionMessage == null)
				groupActionMessage = editGroup(request, out);
			if(groupActionMessage == null)
				groupActionMessage = createNewGroup(request, out);
			if(userActionMessage == null)
				userActionMessage = deleteUser(request);
			if(userActionMessage == null)
				userActionMessage = addNewUser(request, out);
			if(userActionMessage == null)
				userActionMessage = editExistingUser(request, out);
			
			ArrayList<User> users = instance.getAllUsers();
			out.println("<div class=\"usertable-tools\"><input class=\"form-control filter\" id=\"userfilter\" type=\"text\" placeholder=\"Filter Users\"></input>");
			out.println("<a class=\"btn btn-block btn-lg btn-primary\" id=\"createUserButton\" >Add user</a></div>");
			printUserTable(out, users, userActionMessage);
			out.println("<div id=\"createUser\" title=\"Add a new user\">");
			out.println("Username: <input type=\"text\" id=\"name\"></input>");
			out.println("Password: <input type=\"text\" id=\"password\"></input>");
			out.println("Group: <input type=\"text\" id=\"group\"></input><br/>");
			out.print("<input type=\"checkbox\" id=\"pmCheckbox\"> Project Manager");
			out.print("</div>");
			String groupFilter = "<input id=\"groupfilter\" class=\"form-control\" type=\"text\" placeholder=\"Filter groups\">";
			String addGroupButton = "<a id=\"addNewGroupButton\" class=\"btn btn-block btn-lg btn-primary\" onclick="+ formElement("return createGroup();") + ">Add group</a>";
			out.print("<div class=\"grouptable-tools\">" + addGroupButton + groupFilter + "</div>");
			listGroups(out, groupActionMessage);
			String addGroupModal = "<div id=\"addGroup\" title=\"Add new Group\"> " +
							"Group name:<br><br><input type=\"text\" id=\"addGroupName\"/> "+ 
							" </div>";
			out.println(addGroupModal);

			out.println(getPageOutro());
			out.println("</body></html>");

		} else {
			System.err.println("Illigal action performed as: " + getRole() + "; Tried to access AdministrationComponent.");
			response.sendRedirect("logincomponent");
		}
	}
	//TODO JavaDoc
	/**
	 * 
	 * @param request
	 * @param out
	 * @return
	 */
	private String editExistingUser(HttpServletRequest request, PrintWriter out) {
		String oldUserName = request.getParameter("oldUserName");
		String newUserName = request.getParameter("newUsername");
		String newPassword = request.getParameter("newPassword");
		String newGroupName = request.getParameter("group");
		String role = request.getParameter("role");
		ArrayList<ProjectGroup> groups = (ArrayList<ProjectGroup>) instance.getAllProjectGroups();
		if(oldUserName != null) {
			boolean groupExists = false;
			for(int i = 0; i < groups.size(); i++) {
				if(groups.get(i).getName().equals(newGroupName)) {
					groupExists = true;
				}
			}
			User oldUser = instance.getUser(oldUserName);
			String currentRole = oldUser.getRole();
			boolean pmDemotionOrPromotion = (currentRole.equals("ProjectManager") || role.equals("ProjectManager")) && !currentRole.equals(role);
			boolean groupChanged = !instance.getProjectGroup(oldUser.getGroupId()).getName().equals(newGroupName);
			if(checkNewName(newUserName) && checkNewPassword(newPassword)) {
				if(groupExists) {
					int amountOfPMs = instance.getProjectGroup(newGroupName).getNumberOfPMs();
					if(amountOfPMs < 5 || !role.equals("ProjectManager")) {
						boolean res = instance.editUser(oldUserName, newUserName, newPassword, newGroupName, role);
						if(res) {
							if(pmDemotionOrPromotion || groupChanged){
								instance.getUser(newUserName).killSession();
							}
							//TODO add styled paragraph!  /J
							return "User edited succesfully.";
						} else {
							return "User not edited.";
						}
					} else {
						return "Amount of project managers exceeded.";
					}
				} else {
				return "The given groupname does not exist.";
				}
			} else {
				return "Wrong format on input! Please try again";
			}
		}
		return null;
	}
	//TODO Javaoc
	/**
	 * 
	 * @param request
	 * @param out
	 * @return
	 */
	private String addNewUser(HttpServletRequest request, PrintWriter out) {
		String failMsg = null;
		String username = request.getParameter("addNewUser");
		String newPassword = request.getParameter("password");
		String groupName = request.getParameter("group");
		String role = request.getParameter("role");
		if(username != null) {
			System.out.println("the new userName: " + username);
			if(checkNewName(username) && checkNewPassword(newPassword)) {
				ProjectGroup p = instance.getProjectGroup(groupName);
				if(p != null) {
					long groupId = p.getId();
						boolean res = false;
						int amountOfPMs = instance.getProjectGroup(groupName).getNumberOfPMs();
						if(!role.equals("ProjectManager") || amountOfPMs < 5) {
								res = instance.addUser(new User(username, newPassword, role, groupId));
						}else{
							//TODO add styled paragraph!  /J
							return "Amount of project managers exceeded.";
						}
						if(!res){	
							failMsg = "Username does already exist! Please choose another one and try again!";
						} else {
							failMsg = "Project member was created successfully!";
						}
					} else {
						failMsg = "The group does not exist! Please enter a valid group and try again!";
					}
				} else {
					failMsg = "Wrong format on input! Please try again";
				}	
		}
		return failMsg;
	}
	//TODO Javaoc
	/**
	 * 
	 * @param request
	 * @return
	 */
	private String deleteUser(HttpServletRequest request) {
		String deleteUser = request.getParameter("deleteuser");
		if(deleteUser != null) {
			//TODO add styled paragraph!  /J
			return instance.getUser(deleteUser).removeMe() ? "User was removed successfully.": "Could not removed user.";
		}
		return null;
	}

	//TODO Javaoc
	/**
	 * 
	 * @param request
	 * @return
	 */
	private String deleteGroup(HttpServletRequest request) {
		String deleteGroup = request.getParameter("deletegroup");
		if (deleteGroup != null) {
			long groupNumber = Long.parseLong(deleteGroup);
			//TODO add styled paragraph!  /J
			return instance.getProjectGroup(groupNumber).removeMe() ? "Group deleted successfully." : "Failed to delete group.";
		}
		return null;
	}
	
	//TODO Javaoc
	/**
	 * 
	 * @param request
	 * @param out
	 * @return
	 */
	private String editGroup(HttpServletRequest request, PrintWriter out) {
		String editGroup = request.getParameter("editgroup");
		if (editGroup != null) {
			long groupNumber = Long.parseLong(editGroup);
			String newGroupName = request.getParameter("groupname");
			if(newGroupName != null) {
				if(checkNewGroupName(newGroupName)) {
					boolean res = instance.changeGroupName(groupNumber, newGroupName);	
					//TODO add styled paragraph!  /J
					if(!res) {
						return "Group name already taken, please try a new one.";
					} else {
						return "Group name has been updated.";
					}
				} else {
					return "Wrong format on input! Please try again!";
				}
			}
		}
		return null;
	}
	
	//TODO Javaoc
	/**
	 * 
	 * @param request
	 * @param out
	 * @return
	 */
	private String createNewGroup(HttpServletRequest request, PrintWriter out) {
		String createNewGroup = request.getParameter("addNewGroup");
		if(createNewGroup != null) {
			if(checkNewGroupName(createNewGroup)) {
				System.out.println("New name of the group: " + createNewGroup);
				boolean res = instance.addProjectGroup(new ProjectGroup(createNewGroup));
				if(res) {
					//TODO add styled paragraph!  /J
					return "Project group was created successfully!";
				} else {
					return "The project group does already exist! Please enter another project group name and try again";
				}
			} else {
				return "Wrong format on input! Please try again!";
			}
		}
		return null;
	}
	
	/**
	 * Lists all groups of the system
	 * 
	 * @param out
	 * @param groupActionMessage
	 */
	public void listGroups(PrintWriter out, String groupActionMessage) { 
		out.print("<table data-filter=\"#groupfilter\" id=\"grouptable\"  class=\"footable\" border=" + formElement("1") + ">");
		out.print("<thead><tr><th data-sort-initial=\"true\">Group</th><th data-sort-ignore=\"true\">Edit</th><th data-sort-ignore=\"true\">Remove</th></tr></thead>");
		List<ProjectGroup> projectGroups = instance.getAllProjectGroups();		 
		for(int i = 0; i < projectGroups.size(); i++) {
			long id = projectGroups.get(i).getId();
			String name = projectGroups.get(i).getName();
			String deleteURL = "administrationcomponent?deletegroup="+id;

		    String deleteCode = "<a href=" + formElement(deleteURL) + " onclick="+formElement("return deleteGroup(this, '" + name + "')") + "value=\"Delete group\">Delete</a>";
		    String editCode = "<a class=\"cursor-pointer\" onclick=" + formElement("return editGroup(" + projectGroups.get(i).getId() + ", '" + projectGroups.get(i).getName() + "')") + " id=\"editGroupNameLink\" value=\"Edit group\">Edit group</a>";
			out.println("<tr>");
	    	out.println("<td data-value='" + name + "'><a href='ProjectManagerComponent?adminGroupRequestId=" + id +"'>" + name + "</a></td>");
	    	out.println("<td>" + editCode + "</td>");
	    	out.println("<td>" + deleteCode + "</td>");
	    	out.println("</tr>");
		 }
		String editForm = "<div id=\"editGroupName\" title=\"Edit groupname\">New groupname:<br /><br />" +
    			"<input type=\"text\" id=\"newGroupName\"/>" +
    			"</div>";
    	out.println(editForm);
    	//TODO consider use a better id than "text". In case someone else uses this id in something that gets printed on the same page as this, then there will be major errors.
    	// First of all try to use id as seldom as possible, much better to just use classes to get rid of this "eventual" problem
    	// But if you really need to use id's then consider use names that describe what the element are used for and pick a name not plausible someone else would use. //J
    	String deleteForm =  "<div id=\"deleteGroup\" title=\"Delete group\"> " +
			    "<p>Are you sure that you want to delete <span id=\"text\"></span>? <p>" +
				"</div>";
    	out.println(deleteForm);
		 out.println("</table>");
		 if(groupActionMessage != null){
			 //TODO consider switch inline-style to some sort of error-class that gets styled instead. =)
			 out.print("<p style=\"color:red\">"+ groupActionMessage +"</p>");			 
		 }
	}


	/**
	 * Determines whether this is an admin component
	 * 
	 * @return 	true if it is an admin component, 
	 * 			false otherwise
	 */
	protected boolean isAdminComponent() {
		return true;
	}
	//TODO JavaDoc
	protected boolean isWorkerComponent() {
		return false;
	}
	//TODO JavaDoc
	protected boolean isProjectManagerComponent() {
		return false;
	}
}
