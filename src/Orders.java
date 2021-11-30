package src;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.Vector;


public class Orders {
    Vector<Order> orders = new Vector<>();

    Orders() {
        Products products = new Products();
        try {
            // get all completed orders
            ResultSet allOrders = Connect.runQuery("SELECT * FROM orders WHERE complete = 'true';");
            // loop over allOrders
            while (allOrders.next()) {
                // get user and product
                User user = new User(allOrders.getString("userId"), new Scanner(System.in));
                Order order = new Order(user);

                order.id = Integer.parseInt(allOrders.getString("id"));

                // get all product ids for this product
                ResultSet allProductIds = Connect.runQuery("SELECT productId FROM order_product WHERE orderId = "+ order.id + ";");
                // loop over each productId
                while (allProductIds.next()) {
                    // convert productId to Product object
                    Product selectedProduct = products.productIdToProduct(Integer.parseInt(allProductIds.getString("productId")));
                    // add that product to cart
                    order.addToCart(selectedProduct);
                }

                // save order's date and complete states
                order.dateOrdered = allOrders.getString("dateOrdered");
                order.complete = Boolean.parseBoolean(allOrders.getString("complete"));

                // add order to orders vector
                this.orders.add(order);
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public Vector<Order> getAllOrders() {
        return this.orders;
    }

    public void printCartOf(int orderId) {
        Order useOrder;
        for (Order order : this.orders) {
            if (order.id == orderId) {
                useOrder = order;
                useOrder.getCartItems();
                return;
            }
        }
        Print.print("Invalid Order ID...", Print.RED);
    }

}
