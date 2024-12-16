package view;

import controller.ItemController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Item;

public class RequestListView {
	ItemController controller;
	private Stage primaryStage;
	private Scene requestListViewScene;

	public RequestListView(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.controller = new ItemController();
	}
	
	@SuppressWarnings("unchecked")
	public Scene RequestList() {
		VBox requestListContainer = new VBox();
		Button backBtn = new Button("Back to all items page");
		Label requestedLbl = new Label("All Requested Items");
		
		 
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
		
		TableColumn<Item, Void> actionColumn = new TableColumn<>("Action");
		
		actionColumn.setCellFactory(col -> {
			TableCell<Item, Void> actionCell = new TableCell<Item, Void>() {
				
				Button approveBtn = new Button("Approve");
				Button declineBtn = new Button("Decline");
				HBox buttonContainer = new HBox(approveBtn, declineBtn);
				{
					approveBtn.setOnMouseClicked(e -> {
						Item item = getTableView().getItems().get(getIndex());
						String itemId = item.getItem_id();
						controller.approveItem(itemId);
						cleanup();
						RequestListView requestListView = new RequestListView(primaryStage);
						primaryStage.setScene(requestListView.RequestList());
						primaryStage.setTitle("Requested Item");
						primaryStage.show();
					});
					
					declineBtn.setOnMouseClicked(e -> {
						Stage declineStage = new Stage(); 
						
						VBox declinePopUpContainer = new VBox();
						declineStage.initModality(Modality.APPLICATION_MODAL);
						declineStage.setTitle("Decline Reason");
						
						Label declineReasonLbl = new Label("Enter reason to decline");
						TextField declineReasonField = new TextField();
						
						Button confirmDeclineBtn = new Button("Confirm Decline");
						
						declinePopUpContainer.getChildren().addAll(declineReasonLbl, declineReasonField, confirmDeclineBtn);
						Scene declineScene = new Scene(declinePopUpContainer);
						declineStage.setScene(declineScene);
						declineStage.show();
						
						confirmDeclineBtn.setOnMouseClicked(event -> {
							Item item = getTableView().getItems().get(getIndex());
							String itemId = item.getItem_id();
							controller.declineItem(itemId);
							declineStage.close();
							
							cleanup();
							RequestListView requestListView = new RequestListView(primaryStage);
							primaryStage.setScene(requestListView.RequestList());
							primaryStage.setTitle("Requested Item");
							primaryStage.show();							
						});
						

					});
				}
				
			    @Override
			    protected void updateItem(Void item, boolean empty) {
			        super.updateItem(item, empty);
			        if (empty) {
			            setGraphic(null); // Clear cell content
			        } else {
			            setGraphic(buttonContainer); // Show buttons
			        }
			    }
				
			};
			return actionCell;
		});
		
		ObservableList<Item> allRequestedItem = FXCollections.observableArrayList(controller.viewRequestedItems());
		
		itemTable.setItems(allRequestedItem);
		itemTable.getColumns().addAll(item_idColumn, item_nameColumn, item_categoryColumn, item_sizeColumn, item_priceColumn, item_statusColumn, item_offer_statusColumn, actionColumn);
		
		
		backBtn.setOnMouseClicked(e -> {
			AdminView adminView = new AdminView(primaryStage);
			primaryStage.setScene(adminView.AdminItemView());
			primaryStage.setTitle("CaLouseIF");
			primaryStage.show();
			cleanup();
			backBtn.setOnMouseClicked(null);
			return;
		});
		
		requestListContainer.getChildren().addAll(backBtn , requestedLbl, itemTable);
		requestListViewScene = new Scene(requestListContainer, 750, 500);
		
		return requestListViewScene;
	}
	
	public void cleanup() {
		requestListViewScene = null;
		controller = null;
	}
	
}
