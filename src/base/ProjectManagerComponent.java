package base;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.WorkspaceInstance;

/**
 * This class will be used by project managers in order to communicate with the
 * system. Therefore this class will handle requests regarding everything that a
 * project manager is allowed to do, such as listing and signing time reports,
 * or assigning roles to the users.
 * 
 * The validation whether a user is allowed to perform a specific action should
 * be carried out in this class, whereas the format of the input should not be
 * taken care of here.
 * 
 * @author SG
 * @version 0.2
 * 
 */
public class ProjectManagerComponent extends ServletBase {

	/**
	 * Handles input from the project manager and displays information.
	 * 
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String myName = "";
		PrintWriter out = response.getWriter();
		out.println(getPageIntro());

		HttpSession session = request.getSession(true);
		Object nameObj = session.getAttribute("name");
		if (nameObj != null) {
			myName = (String) nameObj; // if the name exists typecast the name
										// to a string
		}
		if (!isLoggedIn(request)) {
			response.sendRedirect("logincomponent");
		} else {
			if (WorkspaceInstance.getInstance(conn)
					.userIsProjectManager(myName) || myName.equals("admin")) {
				out.println("<h1>Project management page " + "</h1>");
				
				//Do alot of stuff according to the STLDD
				
				
				
				
				
			} else { // user is not admin or project manager
				response.sendRedirect("functionality.html");
			}
		}
		// Don't forget that the administrator should be allowed to perform
		// all actions here as well, since he should have the same rights
		// as the project manager

	}

	/**
	 * Handles input from the project manager and displays information.
	 * 
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}

}
