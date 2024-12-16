package view;

import java.util.ArrayList;

import controller.UserController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegisterView {
	// Global variabel
	UserController controller;
	private Stage primaryStage;
	private Scene registerScene;

	// Constructor 
	public RegisterView(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.controller = new UserController();
	}

	// Khusus untuk scene register
	public Scene register() {
		// Insialisasi semua komponen UI yang diperlukan 
		Text title = new Text();
		title.setText("Register");
		
		VBox registerContainer = new VBox();
		Label usernameLbl = new Label("Username");
		TextField usernameField = new TextField();
		usernameField.setPromptText("Enter username");
		
		Label passwordLbl = new Label("Password");
		PasswordField passwordField = new PasswordField();
		passwordField.setPromptText("Enter password");
		
		Label confPasswordLbl = new Label("Confirm Password");
		PasswordField confPasswordField = new PasswordField();
		confPasswordField.setPromptText("Reenter password");
		
		Label phoneLbl = new Label("Phone Number");
		TextField phoneField = new TextField();
		phoneField.setPromptText("+62xxxxxxxx");
		
		Label addressLbl = new Label("Address");
		TextField addressField = new TextField();
		addressField.setPromptText("Address");
		
		ToggleGroup roleTG = new ToggleGroup();
		RadioButton sellerRoleBtn = new RadioButton("Seller");
		RadioButton buyerRoleBtn = new RadioButton("Buyer");
		sellerRoleBtn.setToggleGroup(roleTG);
		buyerRoleBtn.setToggleGroup(roleTG);
		
		Label messageLbl = new Label("");
		
		Button registerBtn = new Button("Register");
		
		Hyperlink loginLink = new Hyperlink("Already have an account? Login here.");
		
		// Pop up message jika register berhasil
		Alert successAlert = new Alert(AlertType.INFORMATION);
		successAlert.setContentText("Register successful!\n"
				+ "Welcome to CaLouseIF");
		
		// Handle aksi saat register button ditekan
		registerBtn.setOnMouseClicked(e -> {
			String username = usernameField.getText();
			String password = passwordField.getText();
			String confPassword = confPasswordField.getText();
			String phone_number = phoneField.getText();
			String address = addressField.getText();
			RadioButton selectedRole = (RadioButton) roleTG.getSelectedToggle();
			String role = null;

			// Cek jika role tidak dipilih
			try {
				role = selectedRole.getText();
			} catch (NullPointerException err) {
				messageLbl.setText("Please select a role!");
			}

			// Validasi input register user
			String validation = controller.CheckAccountValidation(username, password, confPassword, phone_number, address, role);
			if(!validation.equals("Valid")) {
				messageLbl.setText(validation);
				return;
			} else {

				// Validasi jika valid
				ArrayList<String> currentUser = controller.register(username, password, phone_number, address, role);
				String user_id = currentUser.get(0);

				// Memunculkan success jika berhasil register
				successAlert.show();

				// User alngsung diarahkan ke home page
				HomeView homeView = new HomeView(primaryStage);
				primaryStage.setScene(homeView.home(user_id, role));
				primaryStage.setTitle("CaLouseIF");
				primaryStage.show();
				cleanup();
				registerBtn.setOnMouseClicked(null);
				return;
			}
			
		});
		
		// handle untuk tekan link login
		loginLink.setOnAction(e -> {
			LoginView loginView = new LoginView(primaryStage);
			primaryStage.setScene(loginView.Login());
			primaryStage.setTitle("Login to CaLouseIF");
			primaryStage.show();
			cleanup();
			loginLink.setOnAction(null);
			return;
		});
		
		// Masukkan semua komponen ke container
		registerContainer.getChildren().addAll(title, usernameLbl,usernameField, passwordLbl, passwordField, confPasswordLbl, confPasswordField, phoneLbl, phoneField, addressLbl, addressField, sellerRoleBtn, buyerRoleBtn, messageLbl, registerBtn, loginLink);
		registerScene = new Scene(registerContainer, 750, 500);
		return registerScene;
		
	}
	
	// Untuk membershikan Stage
	public void cleanup() {
		registerScene = null;
		controller = null;
	}
}
