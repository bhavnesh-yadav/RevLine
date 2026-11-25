package ravline;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// All SQL is kept here so the menu code stays easy to follow.
public class Database {
    // SQLite stores the database in this local file. No server or account is needed.
    private static final String URL = "jdbc:sqlite:ravline.db";

    // Opens the database and turns on foreign-key checking for this connection.
    public static Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(URL);
        try (Statement st = con.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");
            st.execute("PRAGMA busy_timeout = 5000");
        }
        return con;
    }

    // Saves a new car. Question marks are filled with values to keep SQL input safe.
    public static void addCar(Car car) throws SQLException {
        String sql = "INSERT INTO cars (brand, model, year, price) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, car.getBrand()); ps.setString(2, car.getModel());
            ps.setInt(3, car.getYear()); ps.setDouble(4, car.getPrice());
            ps.executeUpdate();
        }
    }

    // Loads cars into a list so Main can show them one at a time with Next/Previous.
    public static List<Car> getCars() throws SQLException {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT car_id, brand, model, year, price, status FROM cars ORDER BY car_id";
        try (Connection con = getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) cars.add(carFrom(rs));
        }
        return cars;
    }

    // Changes an available car; sold cars are kept unchanged as a sale record.
    public static void updateCar(int id, String brand, String model, int year, double price) throws SQLException {
        String sql = "UPDATE cars SET brand=?, model=?, year=?, price=? WHERE car_id=? AND status='AVAILABLE'";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, brand); ps.setString(2, model); ps.setInt(3, year);
            ps.setDouble(4, price); ps.setInt(5, id);
            if (ps.executeUpdate() == 0) System.out.println("Car not found or already sold.");
            else System.out.println("Car updated.");
        }
    }

    // Deletes only an available car. A sold car is protected by its sale record.
    public static void deleteCar(int id) throws SQLException {
        String sql = "DELETE FROM cars WHERE car_id=? AND status='AVAILABLE'";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) System.out.println("Car not found or already sold; no deletion made.");
            else System.out.println("Car deleted.");
        }
    }

    // Saves a customer's details and lets SQLite assign the customer ID.
    public static void addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers (name, phone, email) VALUES (?, ?, ?)";
        try (Connection con = getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, customer.getName()); ps.setString(2, customer.getPhone()); ps.setString(3, customer.getEmail());
            ps.executeUpdate();
        }
    }

    // Joins the three tables to show each sale with its car and customer.
    public static void viewSoldCars() throws SQLException {
        String sql = "SELECT s.sale_id, s.sale_date, s.sale_price, c.car_id, c.brand, c.model, cu.customer_id, cu.name "
                + "FROM sales s JOIN cars c ON s.car_id=c.car_id JOIN customers cu ON s.customer_id=cu.customer_id ORDER BY s.sale_id";
        try (Connection con = getConnection(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            System.out.printf("  %-5s %-20s %-28s %-20s %12s%n", "SALE", "DATE", "CAR", "CUSTOMER", "PRICE");
            System.out.println("  " + "-".repeat(90));
            while (rs.next()) {
                String car = rs.getInt("car_id") + " " + rs.getString("brand") + " " + rs.getString("model");
                String customer = rs.getInt("customer_id") + " " + rs.getString("name");
                System.out.printf("  %-5d %-20s %-28s %-20s Rs. %9.2f%n",
                        rs.getInt("sale_id"), rs.getTimestamp("sale_date"), car, customer, rs.getDouble("sale_price"));
            }
        }
    }

    // Marks a car sold and records its sale together. Rollback cancels both if anything fails.
    public static boolean sellCar(int carId, int customerId) throws SQLException {
        String updateSql = "UPDATE cars SET status='SOLD' WHERE car_id=? AND status='AVAILABLE'";
        String saleSql = "INSERT INTO sales (car_id, customer_id, sale_price) "
                + "SELECT c.car_id, cu.customer_id, c.price FROM cars c, customers cu "
                + "WHERE c.car_id=? AND cu.customer_id=? AND c.status='SOLD'";
        try (Connection con = getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                    ps.setInt(1, carId);
                    if (ps.executeUpdate() != 1) throw new SQLException("Car does not exist or has already been sold.");
                }
                try (PreparedStatement ps = con.prepareStatement(saleSql)) {
                    ps.setInt(1, carId);
                    ps.setInt(2, customerId);
                    if (ps.executeUpdate() != 1) throw new SQLException("Customer ID not found.");
                }
                con.commit();
                System.out.println("Purchase completed successfully.");
                return true;
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    // Converts the current SQL row into a Car object for the menu to display.
    private static Car carFrom(ResultSet rs) throws SQLException {
        return new Car(rs.getInt("car_id"), rs.getString("brand"), rs.getString("model"),
                rs.getInt("year"), rs.getDouble("price"), rs.getString("status"));
    }
}
