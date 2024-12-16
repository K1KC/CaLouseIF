package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
	// Data untuk database
	private final String NAME = "calouseif";
	private final String USER = "root";
	private final String PASSWORD = "";
	private final String HOST = "localhost:3306";
	private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, NAME);
	
	private Connection con;
	
	private static Connect instance;
	
	public static Connect getInstance() {
		if(instance == null) {
			instance = new Connect();
		}
		return instance;
	}
	
	private Connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(CONNECTION, USER, PASSWORD);
			System.out.println("Connection established successfully.");
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Driver Error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public Connection getConn() {
		return con;
	}
}
