package src;

import java.util.Date;
import java.util.Scanner;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    Integer id;
    String phone;
    String address;
    Date timestamp;
    boolean isStaff;
    private boolean inDB = false;

    public User(String phone, Scanner scanner) {
        try {
            ResultSet user = Connect.runQuery("SELECT * from users WHERE phone=" + phone);

            if (user.next()) {
                this.id = Integer.parseInt(user.getString("id"));

                // to prevent duplicate records
                this.inDB = true;

                // check permissions
                String isStaff = user.getString("isStaff");
                if (isStaff.equals("true")) {
                    this.isStaff = true;
                }

                this.address = user.getString("address");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.phone = phone;
        this.address = getOrAddAddress(phone, scanner);
        this.timestamp = new Date();

        addUserToDB(this);
    }

    public String getOrAddAddress(String phone, Scanner scanner) {
        // if user was not found get thier address
        if (this.address != null) {
            return this.address;
        }
        return Utils.getStringInRange("📍 Your Address: ", 1, 100, scanner);
    }

    public void addUserToDB(User user) {
        String data = this.csvString();

        if (inDB)
            return;
        if (this.phone != null) {
            Connect.runQuery("INSERT INTO users (phone, address, timestamp, isStaff) VALUES " + data);
        }
        this.inDB = true;
    }

    // make staff
    public void makeStaff(Scanner scanner) {
        String phone = "" + Utils.getBigIntInRange("📞 Phone: ", 10, 10, scanner);
        Connect.runQuery("UPDATE users SET isStaff = 'true' WHERE phone = '" + phone + "'");
    }

    public String csvString() {
        return "('" + this.phone + "', '" + this.address.toString() + "', '" + this.timestamp.toString() + "', '"
                + this.isStaff + "')";
    }

}
