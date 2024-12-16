package view;

import controller.TransactionController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Item;

public class TransactionHistoryView {
	TransactionController controller;
	private Stage primaryStage;
	private Scene transactionHistoryScene;
	
	public TransactionHistoryView(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.controller = new TransactionController();
	}

	@SuppressWarnings("unchecked")
	public Scene transactionHistory(String user_id, String role) {
		VBox transactionHistoryContainer = new VBox();
		
		Button backBtn = new Button("Back to home page");
		Label transactionHistoryLbl = new Label("Transaction History");
		TableView<Item> itemTable = new TableView<>();
		
		TableColumn<Item, String> item_nameColumn = new TableColumn<>("Item Name");
		item_nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_name()));
		
		TableColumn<Item, String> item_categoryColumn = new TableColumn<>("Item Category");
		item_categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_category()));
		
		TableColumn<Item, String> item_sizeColumn = new TableColumn<>("Item Size");
		item_sizeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_size()));

		TableColumn<Item, String> item_priceColumn = new TableColumn<>("Item Price");
		item_priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_price()));
		

		
		backBtn.setOnMouseClicked(e -> {
			HomeView homeView = new HomeView(primaryStage);
			primaryStage.setScene(homeView.home(user_id, role));
			primaryStage.setTitle("CaLouseIF");
			primaryStage.show();
			cleanup();
			backBtn.setOnMouseClicked(null);
			return;			
		});
		
		ObservableList<Item> transactionHistory = FXCollections.observableArrayList(controller.viewHistory(user_id));
		itemTable.setItems(transactionHistory);	
		itemTable.getColumns().addAll(item_nameColumn, item_categoryColumn, item_sizeColumn, item_priceColumn);	
		
		transactionHistoryContainer.getChildren().addAll(backBtn, transactionHistoryLbl, itemTable);
		transactionHistoryScene = new Scene(transactionHistoryContainer, 750, 500);
		return transactionHistoryScene;
	}
	 public void cleanup() {
		 transactionHistoryScene = null;
		 controller = null;
	 }
 }
