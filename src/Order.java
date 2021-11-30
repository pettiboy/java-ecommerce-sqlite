package src;

import java.util.Date;
import java.util.Scanner;
import java.util.Vector;

public class Order {
    int id;
    User user;
    Vector<src.Product> products = new Vector<>();
    String dateOrdered;
    boolean complete;

    Order(User user) {
        this.user = user;
    }

    // add items to order (cart)
    public void addToCart(Product product) {
        this.products.add(product);
    }

    public void getCartItems() {
        if (this.products.size() > 0) {
            Print.print(this.products, this.getCartTotal(this));
        } else {
            Print.print("your cart is empty...");
        }
    }

    /**
     * marks order as complete, updates date and saves info to DB
     */
    public void completeOrder(Scanner scanner) {
        if (this.products.size() > 0) {
            this.dateOrdered = new Date().toString();
            this.complete = true;

            Print.print("Your order total is: ₹" + getCartTotal(this), Print.YELLOW);

            String value = Utils.getStringInRange("Press 'y' to place your order:  ", 1, 1, scanner);

            if (value.equals("y")) {
                addOrderToDB();
                Print.print("Order Placed ✅", Print.GREEN);
            }
        } else {
            Print.print("Make sure you add products to your cart first...", Print.YELLOW);
        }
    }

    public Double getCartTotal(Order order) {
        Double total = (double) 0;
        for (Product product : this.products) {
            total += product.price;
        }
        return total;
    }

    public void addOrderToDB() {
        if (this.user.id == null)
            return;

        String data = this.csvString(this);

        Connect.runQuery("INSERT INTO orders (userId, dateOrdered, complete) VALUES " + data);
        int dbOrderId = Connect.getPrevRowId();

        try {
            for (Product product : this.products) {
                Connect.runQuery("INSERT INTO order_product (orderId, productId) VALUES (" + dbOrderId + ", '"
                        + product.id + "')");
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public String csvString(Order order) {
        return "(" + order.user.id + ", '" + order.dateOrdered + "', '" + order.complete + "');";
    }

}
