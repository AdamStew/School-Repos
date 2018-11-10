package package1;

public class Test {
	public static void main(String[] args) {
		Order order = new Order("John Doe");
		Product product = new Product("IPad",750);
		order.addOrderLine(product,6);
	}
}
