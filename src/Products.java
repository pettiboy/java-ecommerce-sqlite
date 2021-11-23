package src;

import java.util.Scanner;
import java.util.Vector;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Products {
    Vector<Product> products = new Vector<>();

    /**
     * on instantiation loads data from the database to a vector called 'products'
     * in the class
     **/
    Products() {
        ResultSet products = Connect.runQuery("SELECT * FROM products WHERE isActive = 'true'");

        try {
            while (products.next()) {
                Product product = new Product(Integer.parseInt(products.getString("id")), products.getString("name"),
                        Double.parseDouble(products.getString("price")), products.getString("description"));
                this.products.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Vector<Product> getProducts() {
        return this.products;
    }

    public Product productIdToProduct(int productId) {
        for (Product product : products) {
            if (product.id == productId) {
                return product;
            }
        }
        return null;
    }

    public void addProduct(Scanner scanner) {
        String name = Utils.getStringInRange("Product Name: ", 1, 30, scanner);
        Double price = Utils.getDoubleInRange("Product Price: ", 1.0, 1000.0, scanner);
        String description = Utils.getStringInRange("Product Description: ", 1, 100, scanner);
        int id = Utils.getNextId("products");

        Product product = new Product(id, name, price, description);
        this.products.add(product);
        addProductToDB(product);
    }

    public void addProductToDB(Product product) {
        String data = product.csvString();
        try {
            Connect.runQuery("INSERT INTO products (name, price, description, isActive) VALUES " + data);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    // deactivate product

}

class Product {
    Integer id;
    String name;
    Double price;
    String description;
    boolean isActive = true;

    Product(Integer id, String name, double price, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String toString() {
        return "ID: " + this.id.toString() + "\n" + "Name: " + this.name + "\n" + "Price: " + this.price.toString()
                + "\n" + "Description: " + this.description.toString();
    }

    public String csvString() {
        return "('" + this.name + "', '" + this.price.toString() + "', '" + this.description + "', '" + this.isActive
                + "')";
    }
}
