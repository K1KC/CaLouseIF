package view;

import java.util.Optional;

import controller.ItemController;
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
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Item;

public class HomeView {
	ItemController controller;
	TransactionController transactionController;
	WishlistController wishlistController;
	private Stage primaryStage;
	private Scene homeViewScene;
	
	public HomeView(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.controller = new ItemController();
		this.transactionController = new TransactionController();
		this.wishlistController = new WishlistController();
	}
	
	@SuppressWarnings("unchecked")
	public Scene home(String user_id, String role) {
		VBox homeViewContainer = new VBox();
		
		Button backBtn = new Button("Back to login page");
		
		homeViewContainer.getChildren().add(backBtn);
		Label allItemsLbl = new Label("All Items");
		
		HBox browseContainer = new HBox();		
		Label browseLbl = new Label("Browse item from name");
		TextField browseTextField = new TextField();
		browseTextField.setPromptText("Type here...");
		Button browseBtn = new Button("Browse");
		
		browseContainer.getChildren().addAll(browseLbl, browseTextField, browseBtn);
		
		// Siapkan tabel untuk item
		TableView<Item> itemTable = new TableView<>();
		
		TableColumn<Item, String> item_nameColumn = new TableColumn<>("Item Name");
		item_nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_name()));
		
		TableColumn<Item, String> item_categoryColumn = new TableColumn<>("Item Category");
		item_categoryColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_category()));
		
		TableColumn<Item, String> item_sizeColumn = new TableColumn<>("Item Size");
		item_sizeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_size()));

		TableColumn<Item, String> item_priceColumn = new TableColumn<>("Item Price");
		item_priceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_price()));

		TableColumn<Item, String> item_offer_statusColumn = new TableColumn<>("Item Current Highest Offer");
		item_offer_statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_offer_status()));
		
		TableColumn<Item, Void> actionColumn = new TableColumn<>("Action");
		
		ObservableList<Item> allItem;
		
		// Tergantung role, home akan menampilkan berbeda
		if(role.equals("Buyer")) {
			// Populasikan list dengan semua item yang sudah diapprove
			allItem = FXCollections.observableArrayList(controller.viewItems());
			itemTable.setItems(allItem);
			Button wishlistBtn = new Button("Wishlist");
			Button transactionHistoryBtn = new Button("Transaction History");
			
			homeViewContainer.getChildren().addAll(wishlistBtn, transactionHistoryBtn, allItemsLbl, browseContainer);
			
			// Untuk kolom action
			actionColumn.setCellFactory(col -> {
				TableCell<Item, Void> actionCell = new TableCell<Item, Void>() {
					Button purchaseBtn = new Button("Purchase");
					Button offerBtn = new Button("Make Offer");
					Button addWishlistBtn = new Button("Add to Wishlist");
					HBox buttonContainer = new HBox(purchaseBtn, offerBtn, addWishlistBtn);
					{
						// Untuk handle purchase, kemudian akan menampilkan pop up konfirmasi jika berhasil
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
						
						// User akan diberi pop up untuk mengisi harga tawaran
						offerBtn.setOnMouseClicked(e -> {
							Stage makeOfferStage = new Stage(); 
							
							VBox makeOfferPopUpContainer = new VBox();
							makeOfferStage.initModality(Modality.APPLICATION_MODAL);
							makeOfferStage.setTitle("Make offer");
							
							Label makeOfferLbl = new Label("Enter offer price (Must be less than current price and not 0)");
							TextField makeOfferTextField = new TextField();
							
							Label messageLbl = new Label("");
							Button makeOfferBtn = new Button("Make Offer");
	
							makeOfferBtn.setOnMouseClicked(event -> {
								Item item = getTableView().getItems().get(getIndex());
								String item_id = item.getItem_id();
								String offer_price = makeOfferTextField.getText();
								try {
									Integer.parseInt(offer_price);

								} catch (NumberFormatException error) {
									messageLbl.setText("Price must be in number and not 0 (Ex. 50000)");
								}
								String offerResult = controller.offerPrice(item_id, offer_price, user_id);
								if(offerResult == "Offer Success") {
									makeOfferStage.close();	
								} else {
									messageLbl.setText(offerResult);
								}
							});
							
							makeOfferPopUpContainer.getChildren().addAll(makeOfferLbl, makeOfferTextField, messageLbl, makeOfferBtn);
							
							Scene makeOfferScene = new Scene(makeOfferPopUpContainer);
							makeOfferStage.setScene(makeOfferScene);
							makeOfferStage.showAndWait();
						});
						
						// Tombol untuk mengambil item di baris tersebut lalu masuk ke wishlist
						addWishlistBtn.setOnMouseClicked(e -> {
							Item item = getTableView().getItems().get(getIndex());
							String item_id = item.getItem_id();
							Alert wishlistAlert = new Alert(AlertType.INFORMATION);
							
							if(wishlistController.addWishlist(item_id, user_id)) {
								wishlistAlert.setContentText("Added to Wishlist!");
								wishlistAlert.showAndWait();									
							} else {
								wishlistAlert.setAlertType(AlertType.ERROR);
								wishlistAlert.setContentText("Already added in wishlist!");
								wishlistAlert.showAndWait();			
							}
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

			// Buyer pergi ke halaman wishlist
			wishlistBtn.setOnMouseClicked(e -> {
				WishlistView wishlistView = new WishlistView(primaryStage);
				primaryStage.setScene(wishlistView.wishlist(user_id, role));
				primaryStage.setTitle("Wishlist");
				primaryStage.show();
				cleanup();
				wishlistBtn.setOnMouseClicked(null);
				return;
			});
			
			// Buyer untuk pergi halaman riwayat transaksi
			transactionHistoryBtn.setOnMouseClicked(e -> {
				TransactionHistoryView transactionHistoryView = new TransactionHistoryView(primaryStage);
				primaryStage.setScene(transactionHistoryView.transactionHistory(user_id, role));
				primaryStage.setTitle("Transaction History");
				primaryStage.show();
				cleanup();
				transactionHistoryBtn.setOnMouseClicked(null);
				return;
			});
			
			itemTable.getColumns().addAll(item_nameColumn, item_categoryColumn, item_sizeColumn, item_priceColumn, item_offer_statusColumn, actionColumn);
			
		} else {
			// Home untuk seller. Tabel ada kolom status
			TableColumn<Item, String> item_statusColumn = new TableColumn<>("Item Status");
			item_statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItem_status()));
			
			// Item dimasukkan ke list untuk ke tabel
			allItem = FXCollections.observableArrayList(controller.adminViewItems());
			itemTable.setItems(allItem);
			Button viewOfferBtn = new Button("View Price Offers");
			Button uploadItemBtn = new Button("Upload New Item");
			homeViewContainer.getChildren().addAll(viewOfferBtn, uploadItemBtn, allItemsLbl, browseContainer);
			
			// Kolom action
			actionColumn.setCellFactory(col -> {
				TableCell<Item, Void> actionCell = new TableCell<Item, Void>() {
					Button editBtn = new Button("Edit");
					Button deleteBtn = new Button("Delete");
					HBox buttonContainer = new HBox(editBtn, deleteBtn);
					{
						// handle jika selelr ingin edit item
						editBtn.setOnMouseClicked(e -> {
							Item item = getTableView().getItems().get(getIndex());
							String item_id = item.getItem_id();
							EditView editView = new EditView(primaryStage);
							primaryStage.setScene(editView.edit(item_id, user_id, role));
							primaryStage.setTitle("Edit Item");
							primaryStage.show();
							cleanup();
							editBtn.setOnMouseClicked(null);
							return;
						});
						
						// handle jika seller ingin menghapus item
						deleteBtn.setOnMouseClicked(e -> {
							Item item = getTableView().getItems().get(getIndex());
							String item_id = item.getItem_id();
							if(controller.deleteItem(item_id)) {
								Alert successAlert = new Alert(AlertType.INFORMATION);
								successAlert.setContentText("Delete item successful!");
								successAlert.showAndWait();
								cleanup();
								backBtn.setOnMouseClicked(null);
								HomeView homeView = new HomeView(primaryStage);
								primaryStage.setScene(homeView.home(user_id, role));
								primaryStage.setTitle("CaLouseIF");
								primaryStage.show();
							}
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
			
			// seller dibawa ke halaman berisi semua tawaran yang menunggu
			viewOfferBtn.setOnMouseClicked(e -> {
				ViewOfferView viewOfferView = new ViewOfferView(primaryStage);
				primaryStage.setScene(viewOfferView.viewOffer(user_id, role));
				primaryStage.setTitle("All Offers");
				primaryStage.show();
				cleanup();
				viewOfferBtn.setOnMouseClicked(null);
				return;
			});
			
			// Seller akan dibawa ke halaman upload item
			uploadItemBtn.setOnMouseClicked(e -> {
				UploadItemView uploadItemView = new UploadItemView(primaryStage);
				primaryStage.setScene(uploadItemView.UploadItem(user_id, role));
				primaryStage.setTitle("Upload Item");
				primaryStage.show();
				cleanup();
				uploadItemBtn.setOnAction(null);
				return;
			});
			itemTable.getColumns().addAll(item_nameColumn, item_categoryColumn, item_sizeColumn, item_priceColumn, item_statusColumn, item_offer_statusColumn, actionColumn);
		}
		
		// Handle back button ke login
		backBtn.setOnMouseClicked(e -> {
			LoginView loginView = new LoginView(primaryStage);
			primaryStage.setScene(loginView.Login());
			primaryStage.setTitle("Login to CaLouseIF");
			primaryStage.show();
			cleanup();
			backBtn.setOnMouseClicked(null);
			return;
		});
		
		// Fitur search dengan nama
		browseBtn.setOnMouseClicked(e -> {
			String item_name = browseTextField.getText();
			if(!item_name.isBlank()) {
				itemTable.getItems().clear();
				ObservableList<Item> browsedItem = FXCollections.observableArrayList(controller.browseItem(item_name));
				itemTable.setItems(browsedItem);
			} else {
				itemTable.getItems().clear();
				itemTable.setItems(allItem);	
			}
			
		});			
		
		homeViewContainer.getChildren().add(itemTable);
		homeViewScene = new Scene(homeViewContainer, 750, 500);
		return homeViewScene;
	}
	
	public void cleanup() {
		homeViewScene = null;
		controller = null;
		
	}
	
}
