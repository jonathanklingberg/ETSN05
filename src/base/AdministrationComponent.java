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

import database.WorkspaceInstance;

import java.util.Random;

/**
 * Servlet implementation class Administration. 
 * Constructs a page for administration purpose. 
 * Checks first if the user is logged in and then if it is the administrator. 
 * The validation whether a user is allowed to perform a specific 
 * action should be carried out in this class, whereas the format
 * on the input should not be taken care of here.
 * 
 *  @author SG
 *  @version 0.2
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
    
//    /**
//     * Checks if a username corresponds to the requirements for user names. 
//     * @param name The investigated username
//     * @return True if the username corresponds to the requirements
//     */
    private boolean checkNewName(String name) {
    	int length = name.length();
    	boolean ok = (length>=5 && length<=10);
    	if (ok)
    		for (int i=0; i<length; i++) {
    			int ci = (int)name.charAt(i);
    			boolean thisOk = ((ci>=48 && ci<=57) || 
    					          (ci>=65 && ci<=90) ||
    					          (ci>=97 && ci<=122));
    			//String extra = (thisOk ? "OK" : "notOK");
    			//System.out.println("bokst:" + name.charAt(i) + " " + (int)name.charAt(i) + " " + extra);
    			ok = ok && thisOk;
    		}    	
    	return ok;
    }
    
//    /**
//     * Creates a random password.
//     * @return a randomly chosen password
//     */
    private String createPassword() {
    	String result = "";
    	Random r = new Random();
    	for (int i=0; i<PASSWORD_LENGTH; i++)
    		result += (char)(r.nextInt(26)+97); // 122-97+1=26
    	return result;
    }
    
    
//    /**
//     * Adds a user and a randomly generated password to the database.
//     * @param name Name to be added
//     * @return true if it was possible to add the name. False if it was not, e.g. 
//     * because the name already exist in the database. 
//     */
    private boolean addUser(String name) {
    	
    	return WorkspaceInstance.getInstance(conn)
    			.addUser(name, createPassword());
//    	try{
    		
//			Statement stmt = conn.createStatement();
//			String statement = "insert into users (name, password) values('" + name + "', '" + 
//			                     createPassword() + "')";
//			System.out.println(statement);
//		    stmt.executeUpdate(statement); 
//		    stmt.close();
			
//		} catch (SQLException ex) {
//		    resultOk = false;
//		    System.out.println("SQLException: " + ex.getMessage());
//		    System.out.println("SQLState: " + ex.getSQLState());
//		    System.out.println("VendorError: " + ex.getErrorCode());
//		}
    	
		
    }
    
//    /**
//     * Deletes a user from the database. 
//     * If the user does not exist in the database nothing happens. 
//     * @param name name of user to be deleted. 
//     */
    private boolean deleteUser(String name) {
    	return WorkspaceInstance.getInstance(conn)
    			.deleteUser(name);
//    	try{
//			Statement stmt = conn.createStatement();
//			String statement = "delete from users where name='" + name + "'"; 
//			System.out.println(statement);
//		    stmt.executeUpdate(statement); 
//		    stmt.close();
//			
//		} catch (SQLException ex) {
//		    System.out.println("SQLException: " + ex.getMessage());
//		    System.out.println("SQLState: " + ex.getSQLState());
//		    System.out.println("VendorError: " + ex.getErrorCode());
//		}
    }


	/**
	 * Handles input from the administrator and displays information for administration. 
	 * 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		out.println(getPageIntro());
		
		String myName = "";
    	HttpSession session = request.getSession(true);
    	Object nameObj = session.getAttribute("name");
    	if (nameObj != null){
    		myName = (String)nameObj;  // if the name exists typecast the name to a string
    	}
    	System.out.println("MyName: "+ myName);
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
					
				// check if the administrator wants to delete a user by clicking the URL in the list
				String deleteName = request.getParameter("deletename");
				if (deleteName != null) {
//					if (checkNewName(deleteName)) {
						deleteUser(deleteName);
//					}	else
//						out.println("<p>Error: URL wrong</p>");
				}
				
				String inactivateName = request.getParameter("inactivatename");
				if (inactivateName != null) {
//					if (checkNewName(inactivateName)) {
						inactivateUser(inactivateName);
//					}	else
//						out.println("<p>Error: URL wrong</p>");
				}
				
				try {
					Statement stmt = conn.createStatement();		    
				    ResultSet rs = stmt.executeQuery("select * from users order by name asc");
				    out.println("<p>Registered users:</p>");
				    out.println("<table border=" + formElement("1") + ">");
				    out.println("<tr><td>NAME</td><td>PASSWORD</td><td></td></tr>");
				    while (rs.next( )) {
				    	String name = rs.getString("name");
				    	String pw = rs.getString("password");
				    	String deleteURL = "administrationcomponent?deletename="+name;
				    	String deleteCode = "<a href=" + formElement(deleteURL) +
				    			            " onclick="+formElement("return confirm('Are you sure you want to delete "+name+"?')") + 
				    			            "> delete </a>";
				    	String inactivateURL = "administrationcomponent?inactivatename="+name;
				    	String inactivateCode = "<a href=" + formElement(inactivateURL) +
	    			            " onclick="+formElement("return confirm('Are you sure you want to inactivate "+name+"?')") + 
	    			            "> inactivate </a>";
				    	if (name.equals("admin")){
				    		deleteCode = "";
				    		inactivateCode = "";
				    	}
				    	out.println("<tr>");
				    	out.println("<td>" + name + "</td>");
				    	out.println("<td>" + pw + "</td>");
				    	out.println("<td>" + deleteCode + "</td>");
				    	out.println("<td>" + inactivateCode + "</td>");
				    	out.println("</tr>");
				    }
				    out.println("</table>");
				    stmt.close();
				} catch (SQLException ex) {
				    System.out.println("SQLException: " + ex.getMessage());
				    System.out.println("SQLState: " + ex.getSQLState());
				    System.out.println("VendorError: " + ex.getErrorCode());
				}
				out.println(addUserForm());
				
				out.println("<p><a href =" + formElement("functionality.html") + "> Functionality selection page </p>");
				out.println("<p><a href =" + formElement("logincomponent") + "> Log out </p>");
				out.println("</body></html>");
			} else  // name not admin
				response.sendRedirect("functionality.html");
	}

	/**
	 * Handles input from the administrator and displays information for administration. 
	 * 
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private boolean inactivateUser(String username) {
		boolean resultOk = true;
		try{
			Statement stmt = conn.createStatement();
			String statement = "update users set is_active = 0 where name = '" + username + "'";
			System.out.println(statement);
		    stmt.executeUpdate(statement); 
		    stmt.close();
		} catch (SQLException ex) {
		    resultOk = false;
		    // System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		return resultOk;
	}

}
