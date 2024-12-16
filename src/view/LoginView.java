package view;

import java.util.ArrayList;

import controller.UserController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginView {
	// Global variable yang akan digunakan berulang
	UserController controller;
	private Stage primaryStage;
	private Scene loginScene;
	
	// Constructor
	public LoginView(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.controller = new UserController();
	}
	
	// Scene khusus untuk login
	public Scene Login() {

		VBox loginContainer = new VBox(20);
		
		Text title = new Text("Login");
		Label usernameLbl = new Label("Username");
		TextField usernameField = new TextField();
		usernameField.setPromptText("Enter username");
		
		Label passwordLbl = new Label("Password");
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter password");
		
		Label messageLbl = new Label("");
		
		Button loginBtn = new Button("Login");
		
		Hyperlink registerLink = new Hyperlink("Don't have an account? Register here!");
		
		// Handle saat login button ditekan
		loginBtn.setOnMouseClicked(e -> {
			String username = usernameField.getText();
			String password = passwordField.getText();
			
			// Validasi admin
			if(username.equals("admin") && password.equals("admin")) {
				AdminView adminView = new AdminView(primaryStage);
				primaryStage.setScene(adminView.AdminItemView());
				primaryStage.setTitle("CaLouseIF (Admin)");
				primaryStage.show();
				cleanup();
				loginBtn.setOnMouseClicked(null);
				return;
			}
			
			if(username.isEmpty()) {
				messageLbl.setText("Username cannot empty");
				return;
			}
			
			if(password.isEmpty()) {
				messageLbl.setText("Password cannot empty");
				return;
			}			
			
			ArrayList<String> currentUser = controller.login(username, password);
			
			if(currentUser == null) {
				messageLbl.setText("Invalid credential");
				return;
			}
			
			// Untuk menampung user_id dan role dari user saat ini 
			String user_id = currentUser.get(0);
			String role = currentUser.get(1);
			
			

			// Diarahkan ke home view setelah berhasil login
			HomeView homeView = new HomeView(primaryStage);
			primaryStage.setScene(homeView.home(user_id, role));
			primaryStage.setTitle("CaLouseIF");
			primaryStage.show();
			cleanup();
			loginBtn.setOnMouseClicked(null);
			return;
			

		});
		
		// Handle untuk navigasi ke register page
		registerLink.setOnAction(e -> {
			RegisterView registerView = new RegisterView(primaryStage);
			primaryStage.setScene(registerView.register());
			primaryStage.setTitle("Register to CaLouseIF");
			primaryStage.show();
			cleanup();
			registerLink.setOnAction(null);
			return;
		});
		
		// Masukkan semua ke dalam container
		loginContainer.getChildren().addAll(title, usernameLbl, usernameField, passwordLbl, passwordField, messageLbl, loginBtn, registerLink);
		loginScene = new Scene(loginContainer, 750, 500);
		return loginScene;
	}
	
	// Untuk membersihkan Scene
	public void cleanup() {
		loginScene = null;
		controller = null;
	}
}
