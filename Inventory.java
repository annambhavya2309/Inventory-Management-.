
import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String URL = "jdbc:mysql://localhost:3306/inventory_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // empty as you said

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== INVENTORY MENU =====");
            System.out.println("1. Add Product");
            System.out.println("2. Update Quantity");
            System.out.println("3. View All Products");
            System.out.println("4. Delete Product");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> addProduct(sc);
                case 2 -> updateQuantity(sc);
                case 3 -> viewProducts();
                case 4 -> deleteProduct(sc);
                case 5 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static void addProduct(Scanner sc) {
        try (Connection con = connect()) {
            System.out.print("Enter name: ");
            String name = sc.next();
            System.out.print("Enter quantity: ");
            int qty = sc.nextInt();
            System.out.print("Enter price: ");
            double price = sc.nextDouble();

            String query = "INSERT INTO products(name, quantity, price) VALUES(?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, qty);
            ps.setDouble(3, price);
            ps.executeUpdate();

            System.out.println("✔ Product added!");
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private static void updateQuantity(Scanner sc) {
        try (Connection con = connect()) {
            System.out.print("Enter product ID: ");
            int id = sc.nextInt();
            System.out.print("Enter new quantity: ");
            int qty = sc.nextInt();

            String query = "UPDATE products SET quantity=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, qty);
            ps.setInt(2, id);

            if (ps.executeUpdate() > 0)
                System.out.println("✔ Quantity updated!");
            else
                System.out.println("❌ Product not found!");

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private static void viewProducts() {
        try (Connection con = connect()) {
            String query = "SELECT * FROM products";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.println("\nID | NAME | QTY | PRICE");
            System.out.println("-----------------------------");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getString("name") + " | " +
                    rs.getInt("quantity") + " | " +
                    rs.getDouble("price")
                );
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private static void deleteProduct(Scanner sc) {
        try (Connection con = connect()) {
            System.out.print("Enter product ID: ");
            int id = sc.nextInt();

            String query = "DELETE FROM products WHERE id=?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, id);

            if (ps.executeUpdate() > 0)
                System.out.println("✔ Product deleted!");
            else
                System.out.println("❌ Product not found!");

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
