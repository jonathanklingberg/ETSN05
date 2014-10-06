package base;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This class will be used by project managers in order to
 * communicate with the system. Therefore this class will
 * handle requests regarding everything that a project manager
 * is allowed to do, such as listing and signing time reports, or assigning
 * roles to the users.
 * 
 * The validation whether a user is allowed to perform a specific 
 * action should be carried out in this class, whereas the format
 * on the input should not be taken care of here.
 * 
 * @author SG
 * @version 0.1
 * 
 */
public class ProjectManagerComponent extends ServletBase {
	
	
	 /**
	  * Handles input from the project manager and displays information. 
	  * 
	  * 
	  * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	  */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		//Don't forget that the administrator should be allowed to perform
		//all actions here as well, since he should have the same rights
		//as the administrator
	
	}
	

	 /**
	  * Handles input from the project manager and displays information. 
	  * 
	  * 
	  * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	  */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
	}

}
