package view;

import controller.ItemController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UploadItemView {
	ItemController controller;
	private Stage primaryStage;
	private Scene uploadItemScene;
	
	public UploadItemView(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.controller = new ItemController();
	}
	
	public Scene UploadItem(String user_id, String role) {
		VBox uploadItemContainer = new VBox();
		Button backBtn = new Button("Back to home");
		
		Label itemNameLbl = new Label("Item Name");
		TextField itemNameField = new TextField();
		itemNameField.setPromptText("Enter item name");
		
		Label itemCategoryLbl = new Label("Item Category");
		TextField itemCategoryField = new TextField();
		itemCategoryField.setPromptText("Enter item category");
		
		Label itemSizeLbl = new Label("Item Size");
		TextField itemSizeField = new TextField();
		itemSizeField.setPromptText("Enter item size");
		
		Label itemPriceLbl = new Label("Item Price");
		TextField itemPriceField = new TextField();
		itemPriceField.setPromptText("Enter item price");
		

		
		Label messageLbl = new Label("");
		
		Button uploadItemBtn = new Button("Upload Item");
		Alert successAlert = new Alert(AlertType.INFORMATION);
		successAlert.setContentText("Upload item successful!"
				+ "Awaiting admin approval");
		
		uploadItemBtn.setOnMouseClicked(e -> {
			String item_name = itemNameField.getText();
			String item_category = itemCategoryField.getText();
			String item_size = itemSizeField.getText();
			String item_price = itemPriceField.getText();
			
			String itemValidation = controller.checkItemValidation(item_name, item_category, item_size, item_price);
			if(!itemValidation.equals("Item Valid")) {
				messageLbl.setText(itemValidation);
				return;
			} else {
				controller.uploadItem(item_name, item_category, item_size, item_price);
				successAlert.show();
			}
		});
		
		backBtn.setOnMouseClicked(e -> {
			HomeView homeView = new HomeView(primaryStage);
			primaryStage.setScene(homeView.home(user_id, role));
			primaryStage.setTitle("CaLouseIF");
			primaryStage.show();
			cleanup();
			backBtn.setOnMouseClicked(null);
			return;
		});
		
		uploadItemContainer.getChildren().addAll(backBtn, itemNameLbl, itemNameField, itemCategoryLbl, itemCategoryField, itemSizeLbl, itemSizeField, itemPriceLbl, itemPriceField, messageLbl, uploadItemBtn);
		uploadItemScene = new Scene(uploadItemContainer, 750, 500);
		return uploadItemScene; 
	}
	
	public void cleanup() {
		uploadItemScene = null;
		controller = null;
	}
	
	
	
}
