package controller;

import connect.Connect;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import model.Item;

public class TransactionController {
	private Connect con;
	public PreparedStatement st;
	
	public TransactionController() {
		con = Connect.getInstance();
	}
	
	public boolean purchaseItem(String item_id, String user_id) {
		try {
			// Logic untuk membuat transaction baru setelah adanya purchase
			int lastTransaction_id = 0;
			st = con.getConn().prepareStatement("SELECT transaction_id FROM transactions ORDER BY created_at DESC LIMIT 1" );
			ResultSet res = st.executeQuery();
			if(res.next()) {
				lastTransaction_id = Integer.parseInt(res.getString("transaction_id"));
			}
			
			String newTransaction_id = Integer.toString(lastTransaction_id + 1);
			st = con.getConn().prepareStatement("INSERT INTO transactions (user_id, item_id, transaction_id) VALUES (?, ?, ?)");
			st.setString(1, user_id);
			st.setString(2, item_id);
			st.setString(3, newTransaction_id);
			st.executeUpdate();
			
			st = con.getConn().prepareStatement("UPDATE items SET item_status = 'sold' WHERE item_id = ?");
			st.setString(1, item_id);
			st.executeUpdate();
			
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
	
	public ArrayList<Item> viewHistory(String user_id) {
		try {
			// Tampilkan riwayat transaksi
			st = con.getConn().prepareStatement("SELECT items.* " +
                    "FROM items " +
                    "JOIN transactions ON items.item_id = transactions.item_id " +
                    "WHERE transactions.user_id = ?");
			st.setString(1, user_id);
			ResultSet res = st.executeQuery();
			ArrayList<Item> historyList = new ArrayList<>();
			while(res.next()) {
                String item_id = res.getString("item_id");
                String item_name = res.getString("item_name");
                String item_category = res.getString("item_category");
                String item_size = res.getString("item_size");
                String item_price = res.getString("item_price");
                String item_status = res.getString("item_status");
                String item_offer_status = res.getString("item_offer_status");
                
                historyList.add(new Item(item_id, item_name, item_category, item_size, item_price, item_status, item_offer_status));
			}
			return historyList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
