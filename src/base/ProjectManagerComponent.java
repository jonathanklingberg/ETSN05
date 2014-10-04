package base;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
*
* This class describes the project managers' HTML page.
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
