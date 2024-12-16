package model;

public class Item {
	private String item_id;
	private String item_name;
	private String item_category;
	private String item_size;
	private String item_price;
	private String item_status;
	private String item_offer_status;
	
	
	public Item(String item_id, String item_name, String item_category, String item_size, String item_price,
			String item_status, String item_offer_status) {
		super();
		this.item_id = item_id;
		this.item_name = item_name;
		this.item_category = item_category;
		this.item_size = item_size;
		this.item_price = item_price;
		this.item_status = item_status;
		this.item_offer_status = item_offer_status;
	}
	
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getItem_name() {
		return item_name;
	}
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}	
	public String getItem_category() {
		return item_category;
	}
	public void setItem_category(String item_category) {
		this.item_category = item_category;
	}
	public String getItem_size() {
		return item_size;
	}
	public void setItem_size(String item_size) {
		this.item_size = item_size;
	}
	public String getItem_price() {
		return item_price;
	}
	public void setItem_price(String item_price) {
		this.item_price = item_price;
	}
	public String getItem_status() {
		return item_status;
	}
	public void setItem_status(String item_status) {
		this.item_status = item_status;
	}

	public String getItem_offer_status() {
		return item_offer_status;
	}
	public void setItem_offer_status(String item_offer_status) {
		this.item_offer_status = item_offer_status;
	}

	
	
}
