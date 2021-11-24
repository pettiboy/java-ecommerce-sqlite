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

    public User(String phoneOrId, Scanner scanner) {
        try {
            // this logic works only users are less than 99,99,99,999
            ResultSet user;
            if (phoneOrId.length() < 10) {
                user = Connect.runQuery("SELECT * from users WHERE id=" + phoneOrId);
            } else {
                user = Connect.runQuery("SELECT * from users WHERE phone=" + phoneOrId);
                this.phone = phoneOrId;
            }

            if (user.next()) {
                this.id = Integer.parseInt(user.getString("id"));
                this.phone = user.getString("phone");

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
            this.id = Connect.getPrevRowId();
        }
        this.inDB = true;
    }

    public void makeStaff(Scanner scanner) {
        // to make sure only staff can make other user staff
        // is checked in Driver.java
        String phone = "" + Utils.getBigIntInRange("📞 Phone: ", 10, 10, scanner);
        Connect.runQuery("UPDATE users SET isStaff = 'true' WHERE phone = '" + phone + "'");
        Print.print("User " + phone + " is now staff ✅");
    }

    public String csvString() {
        return "('" + this.phone + "', '" + this.address.toString() + "', '" + this.timestamp.toString() + "', '"
                + this.isStaff + "')";
    }

}
