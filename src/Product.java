package src;

public class Product {
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