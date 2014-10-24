package base;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
	
	 /**
	 * Checks if a user name corresponds to the requirements for user names.
	 * The requirements are fulfilled if the user name is between 5 and 20 characters long
	 * and only contains characters with encodings 48-57, 65-90 and 97-122 in the ASCII table.
	 * @param userName The investigated user name
	 * @return True if the user name corresponds to the requirements
	 */
	private boolean checkNewName(String userName) {
		int length = userName.length();
		boolean ok = (length >= 5 && length <= 10);
		if (ok)
			for (int i = 0; i < length; i++) {
				int ci = (int) userName.charAt(i);
				boolean thisOk = ((ci >= 48 && ci <= 57) || (ci >= 65 && ci <= 90) || (ci >= 97 && ci <= 122));
				ok = ok && thisOk;
			}
		return ok;
	}
	
	/**
	 * Checks if a group name corresponds to the requirements for group names.
	 * The requirements are fulfilled if the group name is between 1 and 20 characters long
	 * and only contains characters with encodings 48-57, 65-90 and 97-122 in the ASCII table.
	 * @param groupName The investigated group name
	 * @return True if the group name corresponds to the requirements
	 */
	private boolean checkNewGroupName(String groupName) {
		System.out.println("Name of group that admin wants to add: " + groupName);
		int length = groupName.length();
		boolean ok = (length >= 1 && length <= 20);
		if (ok)
			for (int i = 0; i < length; i++) {
				int ci = (int) groupName.charAt(i);
				boolean thisOk = ((ci >= 48 && ci <= 57) || (ci >= 65 && ci <= 90) || (ci >= 97 && ci <= 122));
				ok = ok && thisOk;
			}
		return ok;
	}
	
	/**
	 * Checks if a password corresponds to the requirements for passwords.
	 * The requirements are fulfilled if the password is 6 characters long
	 * and only contains characters with encodings 97-122 in the ASCII table.
	 * @param password The investigated password
	 * @return True if the password corresponds to the requirements
	 */
	private boolean checkNewPassword(String password) {
		System.out.println("my password: "  + password);
		int length = password.length();
		boolean ok = (length == PASSWORD_LENGTH);
		if (ok)
			for (int i = 0; i < length; i++) {
				int ci = (int) password.charAt(i);
				boolean thisOk = ci >= 97 && ci <= 122;
				ok = ok && thisOk;
			}
		return ok;
	}

	/**
	 * Handles input from the administrator and displays information for
	 * administration.
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
	
	/**
	 * Checks whether a request is made to edit an existing user. A request has been made if the request parameter oldUsername is not null.
	 * If a request is made, tries to update the users information with the new information sent in the request. Returns a success message or 
	 * failure message depending on the success of the action. If the action fails, the message contains the reason for the failure.
	 * @param request Contains information for user to be updated, if a request was sent
	 * @param out Writer for printing HTML-code to Webpage
	 * @return null if no update is requested, or an HTML tag containing a failure or success message depending on the success of the action.
	 */
	private String editExistingUser(HttpServletRequest request, PrintWriter out) {
		String oldUserName = request.getParameter("oldUserName");
		if(oldUserName != null) {
			String newUserName = request.getParameter("newUsername");
			String newPassword = request.getParameter("newPassword");
			String newGroupName = request.getParameter("group");
			String role = request.getParameter("role");
			if(newGroupName.toLowerCase().equals("admingroup")) {
				return createMessage("Only administrators are allowed to be a part of this group. Please choose another one and try again", false);
			}
			User oldUser = instance.getUser(oldUserName);
			String currentRole = oldUser.getRole();
			boolean pmDemotionOrPromotion = (currentRole.equals("ProjectManager") || role.equals("ProjectManager")) && !currentRole.equals(role);
			boolean groupChanged = !instance.getProjectGroup(oldUser.getGroupId()).getName().equals(newGroupName);
			if(checkNewName(newUserName) && checkNewPassword(newPassword)) {
				ProjectGroup p = instance.getProjectGroup(newGroupName);
				if(p != null) {
					int amountOfPMs = instance.getProjectGroup(newGroupName).getNumberOfPMs();
					if(amountOfPMs < 5 || !role.equals("ProjectManager")) {
						boolean res = instance.editUser(oldUserName, newUserName, newPassword, newGroupName, role);
						if(res) {
							if(pmDemotionOrPromotion || groupChanged){
								instance.getUser(newUserName).killSession();
							}
							return createMessage("User edited succesfully.", true);
						} else {
							return createMessage("User not edited.", false);
						}
					} else {
						return createMessage("Amount of project managers exceeded.", false);
					}
				} else {
					return createMessage("The given groupname does not exist.", false);
				}
			} else {
				return createMessage("Wrong format on input! Please try again", false);
			}
		}
		return null;
	}
	
	/**
	 * Checks whether a request is made to add a new user. A request has been made if the request parameter addNewUser is not null.
	 * If a request is made, tries to add the new user information with the information sent in the request. Returns a success message or 
	 * failure message depending on the success of the action. If the action fails, the message contains the reason for the failure.
	 * @param request Contains information for user to be added, if a request was sent
	 * @param out Writer for printing HTML-code to Webpage
	 * @return null if no user addition is requested, or an HTML tag containing a failure or success message depending on the success of the action.
	 */
	private String addNewUser(HttpServletRequest request, PrintWriter out) {
		String username = request.getParameter("addNewUser");
		if(username != null) {
			String newPassword = request.getParameter("password");
			String groupName = request.getParameter("group");
			String role = request.getParameter("role");
			if(groupName.toLowerCase().equals("admingroup")) {
				return createMessage("Only administrators are allowed to be a part of this group. Please choose another one and try again", false);
			}
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
							return createMessage("Amount of project managers exceeded.", false);
						}
						if(!res){	
							return createMessage("Username does already exist! Please choose another one and try again!", false);
						} else {
							return createMessage("Project member was created successfully!", true);
						}
					} else {
						return createMessage("The group does not exist! Please enter a valid group and try again!", false);
					}
				} else {
					return createMessage("Wrong format on input! Please try again", false);
				}	
		}
		return null;
	}
	
	/**
	 * Checks whether a request is made to remove a user. A request has been made if the request parameter deleteuser is not null.
	 * If a request is made, tries to remove the user with the information sent in the request. Returns a success message or 
	 * failure message depending on the success of the action.
	 * @param request Contains information for user to be deleted, if a request was sent
	 * @param out Writer for printing HTML-code to Webpage
	 * @return null if no user deletion is requested, or an HTML tag containing a failure or success message depending on the success of the action.
	 */
	private String deleteUser(HttpServletRequest request) {
		String deleteUser = request.getParameter("deleteuser");
		if(deleteUser != null) {
			return instance.getUser(deleteUser).removeMe() ? createMessage("User was removed successfully.", true): createMessage("Could not removed user.", false);
		}
		return null;
	}
	
	/**
	 * Checks whether a request is made to remove a group. A request has been made if the request parameter deletegroup is not null.
	 * If a request is made, tries to remove the group with the information sent in the request. Returns a success message or 
	 * failure message depending on the success of the action.
	 * @param request Contains information for group to be deleted, if a request was sent
	 * @param out Writer for printing HTML-code to Webpage
	 * @return null if no group deletion is requested, or an HTML tag containing a failure or success message depending on the success of the action.
	 */
	private String deleteGroup(HttpServletRequest request) {
		String deleteGroup = request.getParameter("deletegroup");
		if (deleteGroup != null) {
			long groupNumber = Long.parseLong(deleteGroup);
			return instance.getProjectGroup(groupNumber).removeMe() ? createMessage("Group deleted successfully.", true) : createMessage("Failed to delete group.", false);
		}
		return null;
	}
	
	/**
	 * Checks whether a request is made to edit an existing group. A request has been made if the request parameter editgroup is not null.
	 * If a request is made, tries to update the group information with the new information sent in the request. Returns a success message or 
	 * failure message depending on the success of the action. If the action fails, the message contains the reason for the failure.
	 * @param request Contains information for group to be updated, if a request was sent
	 * @param out Writer for printing HTML-code to Webpage
	 * @return null if no update is requested, or an HTML tag containing a failure or success message depending on the success of the action.
	 */
	private String editGroup(HttpServletRequest request, PrintWriter out) {
		String editGroup = request.getParameter("editgroup");
		if (editGroup != null) {
			long groupNumber = Long.parseLong(editGroup);
			String newGroupName = request.getParameter("groupname");
			if(newGroupName != null) {
				if(checkNewGroupName(newGroupName)) {
					boolean res = instance.changeGroupName(groupNumber, newGroupName);	
					if(!res) {
						return createMessage("Group name already taken, please try a new one.", false);
					} else {
						return createMessage("Group name has been updated.", true);
					}
				} else {
					return createMessage("Wrong format on input! Please try again!", false);
				}
			}
		}
		return null;
	}
	
	/**
	 * Checks whether a request is made to create a new group. A request has been made if the request parameter addNewGroup is not null.
	 * If a request is made, tries to create the new group with the information sent in the request. Returns a success message or 
	 * failure message depending on the success of the action. If the action fails, the message contains the reason for the failure.
	 * @param request Contains information for group to be created, if a request was sent
	 * @param out Writer for printing HTML-code to Webpage
	 * @return null if no group creation is requested, or an HTML tag containing a failure or success message depending on the success of the action.
	 */
	private String createNewGroup(HttpServletRequest request, PrintWriter out) {
		String createNewGroup = request.getParameter("addNewGroup");
		if(createNewGroup != null) {
			if(checkNewGroupName(createNewGroup)) {
				System.out.println("New name of the group: " + createNewGroup);
				boolean res = instance.addProjectGroup(new ProjectGroup(createNewGroup));
				if(res) {
					return createMessage("Project group was created successfully!", true);
				} else {
					return createMessage("The project group does already exist! Please enter another project group name and try again", false);
				}
			} else {
				return createMessage("Wrong format on input! Please try again!", false);
			}
		}
		return null;
	}
	
	/**
	 * Lists all groups in the database
	 * 
	 * @param out
	 * @param groupActionMessage
	 */
	public void listGroups(PrintWriter out, String groupActionMessage) { 
		out.print("<table data-filter=\"#groupfilter\" id=\"grouptable\"  class=\"footable\" border=" + formElement("1") + ">");
		out.print("<thead><tr><th data-sort-initial=\"true\">Group</th><th data-sort-ignore=\"true\">Remove</th></tr></thead>");
//		out.print("<thead><tr><th data-sort-initial=\"true\">Group</th><th data-sort-ignore=\"true\">Edit</th><th data-sort-ignore=\"true\">Remove</th></tr></thead>");
		List<ProjectGroup> projectGroups = instance.getAllProjectGroups();		 
		for(int i = 0; i < projectGroups.size(); i++) {
			long id = projectGroups.get(i).getId();
			String name = projectGroups.get(i).getName();
			String deleteURL = "administrationcomponent?deletegroup="+id;
		    String deleteCode = "<a href=" + formElement(deleteURL) + " onclick="+formElement("return deleteGroup(this, '" + name + "')") + "value=\"Delete group\">Delete</a>";
//		    String editCode = "<a class=\"cursor-pointer\" onclick=" + formElement("return editGroup(" + projectGroups.get(i).getId() + ", '" + projectGroups.get(i).getName() + "')") + " id=\"editGroupNameLink\" value=\"Edit group\">Edit group</a>";
			
		    if(name.toLowerCase().equals("admingroup")) {
//		    	editCode = "";
		    	deleteCode = "";
		    }
		    
		    out.println("<tr>");
	    	out.println("<td data-value='" + name + "'><a href='ProjectManagerComponent?adminGroupRequestId=" + id +"'>" + name + "</a></td>");
//	    	out.println("<td>" + editCode + "</td>");
	    	out.println("<td>" + deleteCode + "</td>");
	    	out.println("</tr>");
		 }
//		String editForm = "<div id=\"editGroupName\" title=\"Edit groupname\">New groupname:<br /><br />" +
//    			"<input type=\"text\" id=\"newGroupName\"/>" +
//    			"</div>";
//    	out.println(editForm);
    	String deleteForm =  "<div id=\"deleteGroup\" title=\"Delete group\"> " +
			    "<p>Are you sure that you want to delete <span id=\"nameOfGroupToBeDeleted\"></span>? <p>" +
				"</div>";
    	out.println(deleteForm);
		String tFoot = "<tfoot>" +
					"<tr>" +
					"<td colspan='3'>" +
					 "<div class=\"centerPag\">" +
						"<div class=\"pagination pagination-centered\"></div> " +
					 "</div>"+ 
					"</td>" +
					"</tr>" +
				"</tfoot>";
		 out.println(tFoot);
		 out.println("</table>");
		 if(groupActionMessage != null){
			 out.print(groupActionMessage);			 
		 }
	}
	
	private String createMessage(String message, boolean isSuccess){
		return "<p class=\"" + (isSuccess ? "success" : "failure") + "-message\">" + message + "</p>";
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
	
	/**
	 * Determines whether this is a worker component
	 * 
	 * @return 	true if it is a worker component, 
	 * 			false otherwise
	 */
	protected boolean isWorkerComponent() {
		return false;
	}
	
	/**
	 * Determines whether this is an project manager component
	 * 
	 * @return 	true if it is an project manager component, 
	 * 			false otherwise
	 */
	protected boolean isProjectManagerComponent() {
		return false;
	}
	
}
