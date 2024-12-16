package controller;

import connect.Connect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Item;

public class ItemController {
	private Connect con;
	public PreparedStatement st;
	public ResultSet res;
	TransactionController transactioncontroller;
	
	public ItemController() {
		con = Connect.getInstance();
		transactioncontroller = new TransactionController();
	}
	public void uploadItem(String item_name, String item_category, String item_size, String item_price) {
			try {
				// Mencari id tertinggi
				int lastItem_id = 0;
				st = con.getConn().prepareStatement("SELECT item_id FROM items ORDER BY created_at DESC LIMIT 1");
				res = st.executeQuery();
				if(res.next()) {
					lastItem_id = Integer.parseInt(res.getString("item_id"));
				}
				
				// Id baru akan dibuat untuk item baru
				String newItem_id = Integer.toString(lastItem_id + 1);
				st = con.getConn().prepareStatement("INSERT INTO items (item_id, item_name, item_category, item_size, item_price, item_status, item_offer_status) VALUES (?, ?, ?, ?, ?, ?, ?)");
				st.setString(1, newItem_id);
				st.setString(2, item_name);
				st.setString(3, item_category);
				st.setString(4, item_size);
				st.setString(5, item_price);
				st.setString(6, "pending");
				st.setString(7, "none");
				st.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	
	public String checkItemValidation(String item_name, String item_category, String item_size, String item_price) {
		// Validasi data item
		String messageLbl;
		if(item_name.isBlank()) {
			messageLbl = "Item name cannot be empty!";
			return messageLbl;
		}
		
		if(item_name.length() < 3) {
			messageLbl = "Item name must be at least 3 characters long!";
			return messageLbl;
		}
		
		if(item_category.isBlank()) {
			messageLbl = "Item category cannot be empty!";
			return messageLbl;
		}
		
		if(item_category.length() < 3) {
			messageLbl = "Item category must be at least 3 characters long!";
			return messageLbl;
		}
		
		if(item_size.isBlank()) {
			messageLbl = "Item size cannot be empty!";
			return messageLbl;
		}
		
		if(item_price.isBlank()) {
			messageLbl = "Item price cannot be empty!";
			return messageLbl;
		}
		
		try {
			int item_price_num = Integer.parseInt(item_price);
			if(item_price_num == 0) {
				messageLbl = "Item price cannot be 0!";
				return messageLbl;
			}
		} catch (NumberFormatException error) {
			messageLbl = "Item price must be in number!";
			return messageLbl;
		}
		
		return "Item Valid";
	}
	
	public ArrayList<Item> viewItems() {
		try {
			// Ambil semua item yang ada, khusus approved untuk ditampilkan
			st = con.getConn().prepareStatement("SELECT * FROM items WHERE item_status = 'approved' AND item_status != 'sold'");
			res = st.executeQuery();
			ArrayList<Item> allItemList = new ArrayList<>();
			while(res.next()) {
                String item_id = res.getString("item_id");
                String item_name = res.getString("item_name");
                String item_category = res.getString("item_category");
                String item_size = res.getString("item_size");
                String item_price = res.getString("item_price");
                String item_status = res.getString("item_status");
                String item_offer_status = res.getString("item_offer_status");
                
                allItemList.add(new Item(item_id, item_name, item_category, item_size, item_price, item_status, item_offer_status));
			}
			return allItemList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<Item> adminViewItems() {
		try {
			// Ambil semua items untuk tampilan admin
			st = con.getConn().prepareStatement("SELECT * FROM items");
			res = st.executeQuery();
			ArrayList<Item> allItemList = new ArrayList<>();
			while(res.next()) {
                String item_id = res.getString("item_id");
                String item_name = res.getString("item_name");
                String item_category = res.getString("item_category");
                String item_size = res.getString("item_size");
                String item_price = res.getString("item_price");
                String item_status = res.getString("item_status");
                String item_offer_status = res.getString("item_offer_status");
                
                allItemList.add(new Item(item_id, item_name, item_category, item_size, item_price, item_status, item_offer_status));
			}
			return allItemList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ArrayList<Item> viewRequestedItems() {
		try {
			// Ambil semua items yang sedang ditunggu untuk di approve
			st = con.getConn().prepareStatement("SELECT * FROM items WHERE item_status = 'pending'");
			res = st.executeQuery();
			ArrayList<Item> allRequestedItemList = new ArrayList<>();
			while(res.next()) {
                String item_id = res.getString("item_id");
                String item_name = res.getString("item_name");
                String item_category = res.getString("item_category");
                String item_size = res.getString("item_size");   
                String item_price = res.getString("item_price");
                String item_status = res.getString("item_status");
                String item_offer_status = res.getString("item_offer_status");
                
                allRequestedItemList.add(new Item(item_id, item_name, item_category, item_size, item_price, item_status, item_offer_status));
			}
			return allRequestedItemList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void approveItem(String item_id) {
		try {
			// Logic untuk meng approve item dengan mengubah status menjadi approved
			st = con.getConn().prepareStatement("UPDATE items SET item_status = 'approved' WHERE item_id = ?");
			st.setString(1, item_id);
			st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void declineItem(String item_id) {
		try {
			// Logic untuk menghapus item langsung
			st = con.getConn().prepareStatement("DELETE FROM items WHERE item_id = ?");
			st.setString(1, item_id);
			st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<Item> getItemForEdit(String item_id) {
		try {
			// Ambil item dari item_id
			st = con.getConn().prepareStatement("SELECT * FROM items WHERE item_id = ?");
			st.setString(1, item_id);
			res = st.executeQuery();
			ArrayList<Item> itemForEdit = new ArrayList<>();
			while(res.next()) {
                String item_name = res.getString("item_name");
                String item_category = res.getString("item_category");
                String item_size = res.getString("item_size");
                String item_price = res.getString("item_price");
                String item_status = res.getString("item_status");
                String item_offer_status = res.getString("item_offer_status");
                
                itemForEdit.add(new Item(item_id, item_name, item_category, item_size, item_price, item_status, item_offer_status));
			}
			return itemForEdit;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean editItem(String item_id, String item_name, String item_category, String item_size, String item_price) {
		try {
			// ubah info item
			st = con.getConn().prepareStatement("UPDATE items SET item_name = ?, item_category = ?, item_size = ?, item_price = ? WHERE item_id = ?");
			st.setString(1, item_name);
			st.setString(2, item_category);
			st.setString(3, item_size);
			st.setString(4, item_price);
			st.setString(5, item_id);
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean deleteItem(String item_id) {
		try {
			// Logic untuk hapus item
			st = con.getConn().prepareStatement("DELETE FROM items WHERE item_id = ?");
			st.setString(1, item_id);
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<Item> browseItem(String item_name){
		try {
			// Logic untuk mencari item dari nama
			st = con.getConn().prepareStatement("SELECT * FROM items WHERE item_name = ?");
			st.setString(1, item_name);
			res = st.executeQuery();
			ArrayList<Item> itemResult = new ArrayList<>();
			while(res.next()) {
				String item_id = res.getString("item_id");
                String item_category = res.getString("item_category");
                String item_size = res.getString("item_size");
                String item_price = res.getString("item_price");
                String item_status = res.getString("item_status");
                String item_offer_status = res.getString("item_offer_status");
                
                itemResult.add(new Item(item_id, item_name, item_category, item_size, item_price, item_status, item_offer_status));
			}
			return itemResult;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String offerPrice(String item_id, String offer_price, String user_id) {
		try {
			// Logic untuk mengajukan tawaran harga ke seller
			st = con.getConn().prepareStatement("SELECT item_price, item_offer_status FROM items WHERE item_id = ?");
			st.setString(1, item_id);
			res = st.executeQuery();
			if(res.next()) {
				int current_price = Integer.parseInt(res.getString("item_price"));
				int highest_offer = 0;
				if(!res.getString("item_offer_status").equals("none")) {
					highest_offer = Integer.parseInt(res.getString("item_offer_status"));
				}
	
				if(Integer.parseInt(offer_price) < current_price && Integer.parseInt(offer_price) > highest_offer) {
					st = con.getConn().prepareStatement("SELECT username FROM users WHERE user_id = ?");
					st.setString(1, user_id);
					ResultSet resUsername = st.executeQuery();
					String username = null;
					while(resUsername.next()) {
						username = resUsername.getString("username");
					}
					st = con.getConn().prepareStatement("UPDATE items SET item_offer_status = CONCAT(?, ' by ', ?) WHERE item_id = ?");
					st.setString(1, offer_price);
					st.setString(2, username);
					st.setString(3, item_id);
					st.executeUpdate();
				} else if(Integer.parseInt(offer_price) > current_price){
					return "Offer price higher than the initial price";
				} else if(Integer.parseInt(offer_price) == 0) {
					return "Offer price cannot be 0";
				} else if(Integer.parseInt(offer_price) < highest_offer) {
					return "Offer cannot be lower than current highest offer";
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Offer Success";
		
	}
	
	public ArrayList<Item> viewOfferItem() {
		try {
			// Mengambil semua item yang sedang ditawar
			st = con.getConn().prepareStatement("SELECT * FROM items WHERE item_offer_status != 'none'");
			res = st.executeQuery();
			ArrayList<Item> offeredItem = new ArrayList<>();
			while(res.next()) {
				String item_id = res.getString("item_id");
				String item_name = res.getString("item_name");
                String item_category = res.getString("item_category");
                String item_size = res.getString("item_size");
                String item_price = res.getString("item_price");
                String item_status = res.getString("item_status");
                String item_offer_status = res.getString("item_offer_status");
                
                offeredItem.add(new Item(item_id, item_name, item_category, item_size, item_price, item_status, item_offer_status));
			}
			return offeredItem;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void acceptOffer(String item_id) {
		// Logic untuk menerima harga tawaran
		String offerStatus = null;
		try {
			st = con.getConn().prepareStatement("SELECT item_offer_status FROM items WHERE item_id = ?");
			st.setString(1, item_id);
			res = st.executeQuery();		
			
			while(res.next()) {
				offerStatus = res.getString("item_offer_status");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] offerStatusSliced = offerStatus.split(" by ");
		String offer_price = offerStatusSliced[0].trim();
		String username = offerStatusSliced[1].trim();
		
		try {
			// Tawaran diterima berarti status offer sudah tidak ada
			st = con.getConn().prepareStatement("UPDATE items SET item_price = ?, item_offer_status = 'none' WHERE item_id = ?");
			st.setString(1, offer_price);
			st.setString(2, item_id);
			st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String user_id = null;
		try {
			// Mencari user yang diterima tawarannya untuk langsung di purchase
			st = con.getConn().prepareStatement("SELECT user_id FROM users WHERE username = ?");
			st.setString(1, username);
			res = st.executeQuery();
			while(res.next()) {
				user_id = res.getString("user_id");
			}
			
			transactioncontroller.purchaseItem(item_id, user_id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void declineOffer(String item_id) {
		try {
			// Logic untuk menolak tawaran
			st = con.getConn().prepareStatement("UPDATE items SET item_offer_status = 'none' WHERE item_id = ?");
			st.setString(1, item_id);
			st.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
