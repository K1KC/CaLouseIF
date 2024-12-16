package controller;

import connect.Connect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserController {
	private Connect con;
	public PreparedStatement st;
	public ResultSet res;
	
	public UserController() {
		con = Connect.getInstance();
	}
	
	public ArrayList<String> register(String username, String password, String phone_number, String address, String role) {
		try {
			// Logic untuk membuat akun baru
			int lastUser_id = 0;
			st = con.getConn().prepareStatement("SELECT user_id FROM users ORDER BY created_at DESC LIMIT 1 ");
			res = st.executeQuery();
			if(res.next()) {
				lastUser_id = Integer.parseInt(res.getString("user_id"));
			}
			
			String newUser_id = Integer.toString(lastUser_id + 1);
			st = con.getConn().prepareStatement("INSERT INTO users (user_id, username, password, phone_number, address, role) VALUES (?, ?, ?, ?, ?, ?)");
			st.setString(1, newUser_id);
			st.setString(2, username);
			st.setString(3, password);
			st.setString(4, phone_number);
			st.setString(5, address);
			st.setString(6, role);
			st.executeUpdate();
			
			ArrayList<String> currentUser = login(username, password);
		return currentUser;	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String CheckAccountValidation(String username, String password, String conf_password, String phone_number, String address, String role) {
		// Validasi input user
		String messageLbl;
		if(username.isBlank()) {
			messageLbl = "Username cannot empty!";
			return messageLbl;
		}
		
		if(username.length() < 3) {
			messageLbl = "Username must be at least 3 characters long!";
			return messageLbl;
		}
		
		if(isUsernameExist(username)) {
			messageLbl = "Username already exist!";
			return messageLbl;
		}
		
		if(password.isBlank()) {
			messageLbl = "Password cannot be empty!";
			return messageLbl;
		}
		
		if(!password.equals(conf_password)) {
			messageLbl = "Password not match!";
			return messageLbl;
		}
		
		if(password.length() < 8) {
			messageLbl = "Password must be at least 8 characters long!";
			return messageLbl;
		}
		
//		
//		String[] symbols = {"!", "@", "#", "$", "%", "^", "&", "*"};
//		for (String symbol : symbols) {
//    	  	if (password.contains(symbol) == false) {
//    	  		messageLbl = "Password must include at least 1 symbol (!, @, #, $, %, ^, &, *)!";
//    	  		return messageLbl;
//    	  	}
//		}

        
		
		if(phone_number.length() < 10) {
			messageLbl = "Phone number must be at least 10 digits long!";
			return messageLbl;
		}
		
		if(phone_number.indexOf("+62") != 0) {
			messageLbl = "Phone number must contains +62 in the front (Ex. +62xxxxxxxx)!";
			return messageLbl;
		}
		
		if(address.isBlank()) {
			messageLbl = "Address cannot be empty!";
			return messageLbl;
		}
		
		if(role.isBlank()) {
			messageLbl = "Please choose a role!";
			return messageLbl;
		}
		
		return "Valid";
	}
	
	public boolean isUsernameExist(String username) {
		try {
			// Validasi apakah username sudah ada karena username harus unique
			st = con.getConn().prepareStatement("SELECT * FROM users WHERE username = ? LIMIT 1");
			st.setString(1, username);
			
			ResultSet res = st.executeQuery();
			if(res.next() == true) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<String> login(String username, String password) {
		try {
			// Logic untuk masuk dengan username dan password
			st = con.getConn().prepareStatement("SELECT * FROM users WHERE username = ? AND password = ? LIMIT 1");
			st.setString(1, username);
			st.setString(2, password);
			
			ResultSet res = st.executeQuery();
			ArrayList<String> currentUser = new ArrayList<>();
			if(res.next()) {
				String user_id = res.getString("user_id");
				String role = res.getString("role");
				currentUser.add(user_id);
				currentUser.add(role);
				return currentUser;
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
