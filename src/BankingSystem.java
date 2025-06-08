package com.lab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BankingSystem {

    public static void main(String[] args) {
        Connection conn = null;
        try {
            // establish the connection to MySQL
            conn = getDatabaseConnection();
            // check connection is not null before using it
            if (conn != null) {
                // Start a transaction
                conn.setAutoCommit(false);
                BankingSystem.createDatabase(conn);
                BankingSystem.createTables(conn);

                Customer customer1 = new Customer(0, "John Doe", "123 Main St");
                BankingSystem.createCustomerAccount(conn, customer1, 500.00);
                Customer customer2 = new Customer(0, "Jane Smith", "456 Oak St");
                BankingSystem.createCustomerAccount(conn, customer2, 1000.00);

                customer1.setAddress("456 New Address");
                BankingSystem.updateCustomerDetails(conn, customer1);

                BankingSystem.viewAllCustomers(conn);
                BankingSystem.deleteCustomerAccount(conn, 1);
            }
        } catch (SQLException e) {
            System.err.println("Main SQLException :" + e.getMessage());
        } finally {
            // close the connection if it was successfully opened
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    System.out.println("Auto-commit restored and connection closed.");
                } catch (SQLException e) {
                    System.err.println("Error closing the database connection: " + e.getMessage());
                }
            }
        }
    }

    // Task 1: Create a new customer and their account
    public static void createCustomerAccount(Connection conn, Customer customer, double initialBalance) {
        String customerInsertSQL = "INSERT INTO customers (name, address) VALUES (?, ?)";
        String accountInsertSQL = "INSERT INTO accounts (customer_id, balance) VALUES (?, ?)";

        try {
            // Insert new customer into customers table
            try (PreparedStatement customerStmt = conn.prepareStatement(customerInsertSQL,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                customerStmt.setString(1, customer.getName());
                customerStmt.setString(2, customer.getAddress());

                int rowsInserted = customerStmt.executeUpdate();
                if (rowsInserted == 0) {
                    throw new SQLException("Failed to insert customer, no rows affected.");
                }

                // Get generated customer ID
                try (ResultSet generatedKeys = customerStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int customerId = generatedKeys.getInt(1);
                        customer.setId(customerId);

                        // Insert into accounts table
                        try (PreparedStatement accountStmt = conn.prepareStatement(accountInsertSQL)) {
                            accountStmt.setInt(1, customerId);
                            accountStmt.setDouble(2, initialBalance);

                            int accountRowsInserted = accountStmt.executeUpdate();
                            if (accountRowsInserted == 0) {
                                throw new SQLException("Failed to insert account, no rows affected.");
                            }
                        }
                    } else {
                        throw new SQLException("Failed to retrieve customer ID.");
                    }
                }

                // Commit the transaction
                conn.commit();
                System.out.println("Account created for " + customer.getName() + " successfully.");
            } catch (SQLException e) {
                conn.rollback(); // Roll back if any error occurs
                System.err.println("Error creating account for " + customer.getName() + ": " + e.getMessage());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Task 2: Update customer details
    public static void updateCustomerDetails(Connection conn, Customer customer) {

        String sql = "UPDATE customers SET address = ? WHERE id = ?";
        int rowsAffected = 0;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, customer.getAddress()); // new address
            pstmt.setInt(2, customer.getId()); // identify customer by id

            rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(customer.getName() + " details updated successfully.");
            } else {
                System.out.println("Customer not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating customer details: " + e.getMessage());
        }
    }

    public static void deleteCustomerAccount(Connection conn, int accountId) {
        try {
            conn.setAutoCommit(false); // Start transaction
    
            // Step 1: Retrieve customer ID associated with the account
            int customerId = -1;
            String selectCustomerIdSql = "SELECT customer_id FROM accounts WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectCustomerIdSql)) {
                pstmt.setInt(1, accountId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        customerId = rs.getInt("customer_id");
                    } else {
                        System.out.println("Account not found.");
                        conn.rollback();
                        return;
                    }
                }
            }
    
            // Step 2: Delete the specified account
            String deleteAccountSql = "DELETE FROM accounts WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteAccountSql)) {
                pstmt.setInt(1, accountId);
                int rowsDeleted = pstmt.executeUpdate();
                if (rowsDeleted == 0) {
                    System.out.println("Failed to delete account.");
                    conn.rollback();
                    return;
                }
            }
    
            // Step 3: Check if the customer has other accounts
            String checkOtherAccountsSql = "SELECT COUNT(*) AS count FROM accounts WHERE customer_id = ?";
            int remainingAccounts = 0;
            try (PreparedStatement pstmt = conn.prepareStatement(checkOtherAccountsSql)) {
                pstmt.setInt(1, customerId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        remainingAccounts = rs.getInt("count");
                    }
                }
            }
    
            // Step 4: If no other accounts, delete the customer
            if (remainingAccounts == 0) {
                String deleteCustomerSql = "DELETE FROM customers WHERE id = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(deleteCustomerSql)) {
                    pstmt.setInt(1, customerId);
                    int customerDeleted = pstmt.executeUpdate();
                    if (customerDeleted == 0) {
                        System.out.println("Failed to delete customer.");
                        conn.rollback();
                        return;
                    }
                }
            }
    
            // Commit transaction only after all steps succeed
            conn.commit();
            System.out.println("Bank account belong to " + customerId + " deleted successfully.");
    
        } catch (SQLException e) {
            System.out.println("Error deleting customer account: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Rollback failed: " + rollbackEx.getMessage());
            }
        } finally {
            try {
                conn.setAutoCommit(true); // Reset autocommit to default
            } catch (SQLException e) {
                System.out.println("Failed to reset autocommit: " + e.getMessage());
            }
        }
    }
    




    // Task 4: View all customers and their account details
    public static void viewAllCustomers(Connection conn) {
        String sql = "SELECT c.name, c.address, a.account_id, a.balance " +
                "FROM customers c " +
                "JOIN accounts a ON c.id = a.customer_id";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                String address = rs.getString("address");
                int accountId = rs.getInt("account_id");
                double balance = rs.getDouble("balance");

                System.out.printf("Customer: %s, Address: %s, Account: %d, Balance: %.2f%n",
                        name, address, accountId, balance);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving customer data: " + e.getMessage());
        }
    }

    // Helper method to get customer ID from account ID
    private static int getCustomerIdFromAccountId(Connection conn, int accountId) throws SQLException {
        String query = "SELECT customer_id FROM accounts WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("customer_id");
            }
        }
        return -1;
    }

    // Helper method to check if the customer has other accounts
    private static boolean hasOtherAccounts(Connection conn, int customerId) throws SQLException {
        String query = "SELECT COUNT(*) FROM accounts WHERE customer_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // Method to create the Inventory Management database
    public static void createDatabase(Connection conn) throws SQLException {
        String query = "CREATE DATABASE IF NOT EXISTS BankDB";
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(query);
        System.out.println("Database 'BankDB' created successfully (if it didn’t exist).");
    }

    // Create tables for customers and accounts
    public static void createTables(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            // use 'BankDB' database
            stmt.executeUpdate("USE BankDB");
            String createCustomersTableSQL = "CREATE TABLE IF NOT EXISTS customers ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(255),"
                    + "address VARCHAR(255))";

            String createAccountsTableSQL = "CREATE TABLE IF NOT EXISTS accounts ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "customer_id INT,"
                    + "balance DOUBLE,"
                    + "FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE)";

            // Create customers table
            stmt.execute(createCustomersTableSQL);

            // Create accounts table
            stmt.execute(createAccountsTableSQL);

            System.out.println("Tables created successfully.");

        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
    }

    public static Connection getDatabaseConnection() {
        String url = "jdbc:mysql://localhost:3306/BankDB";
        String user = "root";
        String password = "password";

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Connection failed SQLException: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Connection failed Exception: " + e.getMessage());
            return null;
        }
    }
}
