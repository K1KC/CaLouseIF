package view;

import java.util.ArrayList;

import controller.ItemController;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Item;

public class EditView {
	ItemController controller;
	private Stage primaryStage;
	private Scene editviewScene;
	
	public EditView(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.controller = new ItemController();
	}

	public Scene edit(String item_id, String user_id, String role) {
		VBox editContainer = new VBox();
		Button backBtn = new Button("Back to home");
		
		Label editItemLbl = new Label("Edit Item Information");
		
		Label itemNameLbl = new Label("Item Name");
		TextField itemNameField = new TextField();
		
		Label itemCategoryLbl = new Label("Item Category");
		TextField itemCategoryField = new TextField();
		
		Label itemSizeLbl = new Label("Item Size");
		TextField itemSizeField = new TextField();
		
		Label itemPriceLbl = new Label("Item Price");
		TextField itemPriceField = new TextField();
		
		Label messageLbl = new Label("");
		
		Button editItemBtn = new Button("Confirm Edit");

		
		ArrayList<Item> currentItemForEdit = controller.getItemForEdit(item_id);
		for(Item item : currentItemForEdit) {
			itemNameField.setPromptText(item.getItem_name());
			itemCategoryField.setPromptText(item.getItem_category());
			itemSizeField.setPromptText(item.getItem_size());
			itemPriceField.setPromptText(item.getItem_price());
		}
		
		editItemBtn.setOnMouseClicked(e -> {
			String newItem_name = itemNameField.getText();
			String newItem_category = itemCategoryField.getText();
			String newItem_size = itemSizeField.getText();
			String newItem_price = itemPriceField.getText();
			
			if(controller.editItem(item_id, newItem_name, newItem_category, newItem_size, newItem_price)) {
				Alert successAlert = new Alert(AlertType.INFORMATION);
				successAlert.setContentText("Edit item successful!");
				successAlert.showAndWait();
				HomeView homeView = new HomeView(primaryStage);
				primaryStage.setScene(homeView.home(user_id, role));
				primaryStage.setTitle("CaLouseIF");
				primaryStage.show();
				cleanup();
				backBtn.setOnMouseClicked(null);
				return;
			} else {
				Alert failedAlert = new Alert(AlertType.WARNING);
				failedAlert.setContentText("Edit item failed! Please try again!");
				failedAlert.showAndWait();
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
		
		editContainer.getChildren().addAll(backBtn, editItemLbl, itemNameLbl, itemNameField, itemCategoryLbl, itemCategoryField, itemSizeLbl, itemSizeField, itemPriceLbl, itemPriceField, messageLbl, editItemBtn);
		editviewScene = new Scene(editContainer, 750, 500);
		return editviewScene;
	}
	
	public void cleanup() {
		editviewScene = null;
		controller = null;
	}
}
