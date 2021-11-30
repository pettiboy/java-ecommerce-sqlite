# Java E-Commerce CLI Using SQLite

## Usage

### Clone repo

```
git clone git@github.com:pettiboy/java-ecommerce-sqlite.git
cd java-ecommerce-sqlite
```

### Run code

run the main method in `src/Driver.java`

## Tables

### phone_otp

```sql
CREATE TABLE phone_otp(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    phone TEXT NOT NULL,
    otp INTEGER NOT NULL
);
```

### users

```sql
CREATE TABLE users(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    phone TEXT NOT NULL,
    address TEXT NOT NULL,
    timestamp TEXT NOT NULL,
    isStaff TEXT NOT NULL
);
```

### products

```sql
CREATE TABLE products(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    price TEXT NOT NULL,
    description TEXT NOT NULL,
    isActive TEXT NOT NULL
);
```

### orders

```sql
CREATE TABLE orders(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    userId INTEGER NOT NULL,
    dateOrdered TEXT NOT NULL,
    complete TEXT NOT NULL,
    FOREIGN KEY(userId) REFERENCES users(id)
);

CREATE TABLE order_product (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    orderId INTEGER NOT NULL,
    productId INTEGER NOT NULL,
    FOREIGN KEY(orderId) REFERENCES orders(id),
    FOREIGN KEY(productId) REFERENCES products(id)
);
```

#### Why am I using TEXT instead of other datatypes?

This is because the values will be returned as `String` either way in `java`.
