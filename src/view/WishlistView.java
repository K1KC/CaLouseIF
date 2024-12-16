package view;

import java.util.Optional;

import controller.TransactionController;
import controller.WishlistController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Item;

public class WishlistView {
	WishlistController controller;
	TransactionController transactionController;
	private Stage primaryStage;
	private Scene wishlistViewScene;
	
	public WishlistView(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.controller = new WishlistController();
		this.transactionController = new TransactionController();
	}

	
	@SuppressWarnings("unchecked")
	public Scene wishlist(String user_id, String role) {
		VBox wishlistContainer = new VBox();
		
		Button backBtn = new Button("Back to home page");
		Label wishlistLbl = new Label("Wishlists");
		TableView<Item> itemTable = new TableView<>();
		
		TableColumn<Item, String> item_nameColumn = new TableColumn<>("Item Name");
		item_nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_name()));
		
		TableColumn<Item, String> item_categoryColumn = new TableColumn<>("Item Category");
		item_categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_category()));
		
		TableColumn<Item, String> item_sizeColumn = new TableColumn<>("Item Size");
		item_sizeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_size()));

		TableColumn<Item, String> item_priceColumn = new TableColumn<>("Item Price");
		item_priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_price()));
		
		TableColumn<Item, Void> actionColumn = new TableColumn<>("Action");	
		
		ObservableList<Item> allWishlist = FXCollections.observableArrayList(controller.viewWishlist(user_id));
		itemTable.setItems(allWishlist);	
		
		
		
		actionColumn.setCellFactory(col -> {
			TableCell<Item, Void> actionCell = new TableCell<Item, Void>() {
				Button removeWishlistBtn = new Button("Remove From Wishlist");
				Button purchaseBtn = new Button("Purchase");
				HBox buttonContainer = new HBox(removeWishlistBtn, purchaseBtn);
				{
					removeWishlistBtn.setOnMouseClicked(e -> {
						Item item = getTableView().getItems().get(getIndex());
						String item_id = item.getItem_id();
						if(controller.removeWishlist(item_id, user_id)) {
							Alert successAlert = new Alert(AlertType.INFORMATION);
							successAlert.setContentText("Delete wishlist successful!");
							successAlert.showAndWait();
							cleanup();
							WishlistView wishlistView = new WishlistView(primaryStage);
							primaryStage.setScene(wishlistView.wishlist(user_id, role));
							primaryStage.setTitle("Wishlist");
							primaryStage.show();
						}
					});
					
					purchaseBtn.setOnMouseClicked(e -> {
						Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
						confirmAlert.setTitle("Purchase Confirmation");
						confirmAlert.setContentText("Are you sure to purchase?");
						Optional<ButtonType> response = confirmAlert.showAndWait();
						
						if(response.isPresent() && response.get() == ButtonType.OK) {
							Item item = getTableView().getItems().get(getIndex());
							String item_id = item.getItem_id();
							if(transactionController.purchaseItem(item_id, user_id)) {
								confirmAlert.setAlertType(AlertType.INFORMATION);
								confirmAlert.setContentText("Purchase Successful");
								confirmAlert.showAndWait();									
							} else {
								confirmAlert.setAlertType(AlertType.ERROR);
								confirmAlert.setContentText("Purchase Error! Please try again!");
								confirmAlert.showAndWait();			
							}
						} else {
							confirmAlert.close();
						}
					
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
		

		itemTable.getColumns().addAll(item_nameColumn, item_categoryColumn, item_sizeColumn, item_priceColumn, actionColumn);
		
		backBtn.setOnMouseClicked(e -> {
			HomeView homeView = new HomeView(primaryStage);
			primaryStage.setScene(homeView.home(user_id, role));
			primaryStage.setTitle("CaLouseIF");
			primaryStage.show();
			cleanup();
			backBtn.setOnMouseClicked(null);
			return;
		});
		
		wishlistContainer.getChildren().addAll(backBtn, wishlistLbl, itemTable);
		wishlistViewScene = new Scene(wishlistContainer, 750, 500);
		return wishlistViewScene;
	}
	
	public void cleanup() {
		wishlistViewScene = null;
		controller = null;
	}
}
