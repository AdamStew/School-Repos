package package1;

import java.util.*;

public class Order {
	private String customerName;
	private ArrayList<OrderLine> lines = new ArrayList<OrderLine>();

	public Order(String customerName) {
		this.customerName = customerName;
	}
	
	public boolean addOrderLine(Product product, int quantity) {
		OrderLine line = new OrderLine();
		line.setProduct(product);
		line.setQuantity(quantity);
		lines.add(line);
		return true;
	}
}
