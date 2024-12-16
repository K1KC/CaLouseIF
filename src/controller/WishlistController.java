package controller;

import connect.Connect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Item;

public class WishlistController {
	private Connect con;
	public PreparedStatement st;
	
	public WishlistController() {
		con = Connect.getInstance();
	}
	
	public boolean addWishlist(String item_id, String user_id) {
		try {
			// Untuk menambah sebuah item ke wishlist user
			ResultSet res;
			int lastWishlist_id = 0;
			st = con.getConn().prepareStatement("SELECT wishlist_id FROM wishlists ORDER BY created_at DESC LIMIT 1");
			res = st.executeQuery();
			if(res.next()) {
				lastWishlist_id = Integer.parseInt(res.getString("wishlist_id"));
			}
			
			String newWishlist_id = Integer.toString(lastWishlist_id + 1);
			st = con.getConn().prepareStatement("SELECT * FROM wishlists WHERE item_id = ? AND user_id = ?");
			st.setString(1, item_id);
			st.setString(2, user_id);
			res = st.executeQuery();
			if(!res.next()) {
				st = con.getConn().prepareStatement("INSERT INTO wishlists (wishlist_id, item_id, user_id) VALUES (?, ?, ?)");
				st.setString(1, newWishlist_id);
				st.setString(2, item_id);
				st.setString(3, user_id);
				st.executeUpdate();	
				return true;
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<Item> viewWishlist(String user_id) {
		try {
			// Mengambil semua wishlist dengan user_id
			st = con.getConn().prepareStatement("SELECT items.* "
			         + "FROM wishlists "
			         + "JOIN items ON wishlists.item_id = items.item_id "
			         + "WHERE wishlists.user_id = ?");
			st.setString(1, user_id);
			ResultSet res = st.executeQuery();
			ArrayList<Item> itemWishlist = new ArrayList<>();
			while(res.next()) {
                String item_id = res.getString("item_id");
                String item_name = res.getString("item_name");
                String item_category = res.getString("item_category");
                String item_size = res.getString("item_size");
                String item_price = res.getString("item_price");
                String item_status = res.getString("item_status");
                String item_offer_status = res.getString("item_offer_status");
                
                itemWishlist.add(new Item(item_id, item_name, item_category, item_size, item_price, item_status, item_offer_status));
			}
			return itemWishlist;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean removeWishlist(String item_id, String user_id) {
		try {
			// Hapus wishlist dari daftar user
			st = con.getConn().prepareStatement("DELETE FROM wishlists WHERE item_id = ? AND user_id = ?");
			st.setString(1, item_id);
			st.setString(2, user_id);
			st.executeUpdate();
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
