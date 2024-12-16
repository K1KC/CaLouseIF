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

public class ViewOfferView {
	ItemController controller;
	private Stage primaryStage;
	private Scene viewOfferViewScene;
	
	public ViewOfferView(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.controller = new ItemController();
	}
	
	@SuppressWarnings("unchecked")
	public Scene viewOffer(String user_id, String role) {
		// Inisialisasi komponen UI yang diperlukan
		VBox viewOfferContainer = new VBox();
		Button backBtn = new Button("Back to home page");
		Label offerLbl = new Label("All Offers");
		
		// Table untuk item
		TableView<Item> itemTable = new TableView<>();
		
		TableColumn<Item, String> item_nameColumn = new TableColumn<>("Item Name");
		item_nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_name()));
		
		TableColumn<Item, String> item_categoryColumn = new TableColumn<>("Item Category");
		item_categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_category()));
		
		TableColumn<Item, String> item_sizeColumn = new TableColumn<>("Item Size");
		item_sizeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_size()));

		TableColumn<Item, String> item_priceColumn = new TableColumn<>("Item Price");
		item_priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_price()));

		TableColumn<Item, String> item_offer_statusColumn = new TableColumn<>("Item Offer");
		item_offer_statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_offer_status()));
		
		TableColumn<Item, Void> actionColumn = new TableColumn<>("Action");
		
		// Kolom untuk action button
		actionColumn.setCellFactory(col -> {
			TableCell<Item, Void> actionCell = new TableCell<Item, Void>() {
				Button acceptBtn = new Button("Accept Offer");
				Button declineBtn = new Button("Decline Offer");
				HBox buttonContainer = new HBox(acceptBtn, declineBtn);
				{
					// Handle saat accept button ditekan
					acceptBtn.setOnMouseClicked(e -> {
						// Ambil data item dari baris yang ditekan
						Item item = getTableView().getItems().get(getIndex());
						String itemId = item.getItem_id();
						controller.acceptOffer(itemId);
					});
					
					// Handle saat decline button ditekan
					declineBtn.setOnMouseClicked(e -> {
						// Pop up message saat gagal
						Stage declineStage = new Stage(); 
						
						VBox declinePopUpContainer = new VBox();
						declineStage.initModality(Modality.APPLICATION_MODAL);
						declineStage.setTitle("Decline Reason");
						
						// Sebelum decline, seller harus memberikan alasan
						Label declineReasonLbl = new Label("Enter reason to decline");
						TextField declineReasonField = new TextField();
						
						Button confirmDeclineBtn = new Button("Confirm Decline");
						
						// Konfirmasi penolakan
						confirmDeclineBtn.setOnMouseClicked(event -> {
							Item item = getTableView().getItems().get(getIndex());
							String itemId = item.getItem_id();

							// Penolakan akan menghapus offer yang ada
							controller.declineOffer(itemId);
							declineStage.close();						
						});						
						
						declinePopUpContainer.getChildren().addAll(declineReasonLbl, declineReasonField, confirmDeclineBtn);
						Scene declineScene = new Scene(declinePopUpContainer);
						declineStage.setScene(declineScene);
						declineStage.showAndWait();
						

						
					});
				}
			    @Override
			    protected void updateItem(Void item, boolean empty) {
			        super.updateItem(item, empty);
			        if (empty) {
			            setGraphic(null);
			        } else {
			            setGraphic(buttonContainer);
			        }
			    }
				
			};
			return actionCell;
		});
		
		// handle back button
		backBtn.setOnMouseClicked(e -> {
			HomeView homeView = new HomeView(primaryStage);
			primaryStage.setScene(homeView.home(user_id, role));
			primaryStage.setTitle("CaLouseIF");
			primaryStage.show();
			cleanup();
			backBtn.setOnMouseClicked(null);
			return;
		});
		
		// Populasi table dengan data item
		itemTable.getItems().clear();
		ObservableList<Item> allOfferItem = FXCollections.observableArrayList(controller.viewOfferItem());
		itemTable.setItems(allOfferItem);
		itemTable.getColumns().addAll(item_nameColumn, item_categoryColumn, item_sizeColumn, item_priceColumn, item_offer_statusColumn, actionColumn);
		
		viewOfferContainer.getChildren().addAll(backBtn, offerLbl, itemTable);
		viewOfferViewScene = new Scene(viewOfferContainer, 750, 500);
		return viewOfferViewScene;
	}
	
	public void cleanup() {
		viewOfferViewScene = null;
		controller = null;
	}
}
