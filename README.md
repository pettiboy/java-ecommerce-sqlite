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

### otp

```sql
CREATE TABLE otps(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    phone INT NOT NULL,
    otp INT NOT NULL
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
