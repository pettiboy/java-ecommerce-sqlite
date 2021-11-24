package src;

import java.io.File;
import java.io.FileNotFoundException;
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
                Print.print("✅ Order Placed.", Print.GREEN);
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
        // String lineSeperatedProductIds = "";
        // for (Product product: order.products) {
        // lineSeperatedProductIds += "|" + product.id;
        // }
        // return "" + order.id + "," + order.user.phone + "," + lineSeperatedProductIds
        // + "," + order.dateOrdered + "," + order.complete;
        return "(" + order.user.id + ", '" + order.dateOrdered + "', '" + order.complete + "');";
    }

}

class Orders {
    Vector<Order> orders = new Vector<>();

    Orders() {
        Products products = new Products();

        File file = new File("./data/orders.csv");
        Scanner fileScanner;
        try {
            fileScanner = new Scanner(file);
            // process the file, one line at a time
            while (fileScanner.hasNextLine()) {
                String[] line = fileScanner.nextLine().split(",");

                if (line[4].equals("true")) {
                    User user = new User(line[1], new Scanner(System.in));
                    Order order = new Order(user);
                    order.id = Integer.parseInt(line[0]);
                    String[] allProductIds = line[2].split("|");
                    for (String productId : allProductIds) {
                        if (productId.equals("|")) {
                            continue;
                        }
                        Product selectedProduct = products.productIdToProduct(Integer.parseInt(productId));
                        order.addToCart(selectedProduct);
                    }

                    order.dateOrdered = line[3];
                    order.complete = Boolean.parseBoolean(line[4]);

                    this.orders.add(order);
                }
            }
            fileScanner.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
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