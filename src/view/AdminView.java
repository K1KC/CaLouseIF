package view;

import controller.ItemController;
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

public class AdminView {
	// Global variable
	ItemController controller;
	private Stage primaryStage;
	private Scene adminViewScene;

	// Constructor
	public AdminView(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.controller = new ItemController();
	}
	
	@SuppressWarnings("unchecked")
	public Scene AdminItemView() {
		VBox adminContainer = new VBox();
		Button backBtn = new Button("Back to login page");
		Button GoToRequestListViewBtn = new Button("Go to request item list page");
		Label adminLbl = new Label("All Items");
		
		// Table untuk menampilkan semua item yang ada
		TableView<Item> itemTable = new TableView<>();
		
		TableColumn<Item, String> item_idColumn = new TableColumn<>("Item ID");
		item_idColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_id()));
		
		TableColumn<Item, String> item_nameColumn = new TableColumn<>("Item Name");
		item_nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_name()));
		
		TableColumn<Item, String> item_categoryColumn = new TableColumn<>("Item Category");
		item_categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_category()));	
		
		TableColumn<Item, String> item_sizeColumn = new TableColumn<>("Item Size");
		item_sizeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_size()));
		
		TableColumn<Item, String> item_priceColumn = new TableColumn<>("Item Price");
		item_priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_price()));
		
		TableColumn<Item, String> item_statusColumn = new TableColumn<>("Item Status");
		item_statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_status()));
		
		TableColumn<Item, String> item_offer_statusColumn = new TableColumn<>("Item Offer Status");
		item_offer_statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_offer_status()));

		
		ObservableList<Item> allItem = FXCollections.observableArrayList(controller.adminViewItems());
		
		itemTable.setItems(allItem);
		itemTable.getColumns().addAll(item_idColumn, item_nameColumn, item_categoryColumn, item_sizeColumn, item_priceColumn, item_statusColumn, item_offer_statusColumn);
		
		backBtn.setOnMouseClicked(e -> {
			LoginView loginView = new LoginView(primaryStage);
			primaryStage.setScene(loginView.Login());
			primaryStage.setTitle("Login to CaLouseIF");
			primaryStage.show();
			cleanup();
			backBtn.setOnMouseClicked(null);
			return;
		});
		
		GoToRequestListViewBtn.setOnMouseClicked(e -> {
			RequestListView requestListView = new RequestListView(primaryStage);
			primaryStage.setScene(requestListView.RequestList());
			primaryStage.setTitle("Requested Item");
			primaryStage.show();
			cleanup();
			GoToRequestListViewBtn.setOnMouseClicked(null);
			return;
		});
		
		adminContainer.getChildren().addAll(backBtn, GoToRequestListViewBtn ,adminLbl, itemTable);
		adminViewScene = new Scene(adminContainer, 750, 500);
		
		return adminViewScene;
	}
	
	public void cleanup() {
		adminViewScene = null;
		controller = null;
	}
	
}
