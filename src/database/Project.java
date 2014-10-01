package database;

import java.sql.Connection;
import java.util.List;

public class Project implements DatabaseInterface {
	private int id;
	private String name;
	private Connection conn;
	
	public Project(Connection conn, int id, String name) {
		
	}
	
	public List<User> getUsers() {
		return null;
	}
	
	public User getUser(String userName) {
		return null;
	}
	
	public List<TimeReport> getTimeReports() {
		return null;
	}
	
	public boolean addTimeReport(TimeReport report) {
		return false;
	}
	
	public boolean removeTimeReport(TimeReport report) {
		return false;
	}
	
	public boolean signTimeReport(User signer, int reportId) {
		return false;
	}
	
	public boolean addUser(User user) {
		return false;
	}
	
	public boolean removeUser(User use) {
		return false;
	}
	
	@Override
	public String toHTML(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeMe() {
		// TODO Auto-generated method stub
		return false;
	}

}