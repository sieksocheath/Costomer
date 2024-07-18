import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class CustomerApplication {
    private JFrame frame;
    private JLabel idLabel, lastNameLabel, firstNameLabel, phoneLabel;
    private JTextField idField, lastNameField, firstNameField, phoneField;
    private JButton prevButton, nextButton;
    private ArrayList<Customer> customers;
    private int currentIndex = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CustomerApplication().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Customer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JPanel panel = new JPanel(new GridLayout(5, 2));

        idLabel = new JLabel("ID:");
        lastNameLabel = new JLabel("Last Name:");
        firstNameLabel = new JLabel("First Name:");
        phoneLabel = new JLabel("Phone:");

        idField = new JTextField();
        lastNameField = new JTextField();
        firstNameField = new JTextField();
        phoneField = new JTextField();

        idField.setEditable(false);
        lastNameField.setEditable(false);
        firstNameField.setEditable(false);
        phoneField.setEditable(false);

        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");

        panel.add(idLabel);
        panel.add(idField);
        panel.add(lastNameLabel);
        panel.add(lastNameField);
        panel.add(firstNameLabel);
        panel.add(firstNameField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(prevButton);
        panel.add(nextButton);

        frame.add(panel);

        prevButton.addActionListener(e -> showPreviousCustomer());
        nextButton.addActionListener(e -> showNextCustomer());

        loadCustomers();
        if (!customers.isEmpty()) {
            showCustomer(currentIndex);
        }

        frame.setVisible(true);
    }

    private void loadCustomers() {
        customers = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "092367169");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customer")) {

            while (rs.next()) {
                customers.add(new Customer(rs.getInt("customer_id"), rs.getString("customer_last_name"), rs.getString("customer_first_name"), rs.getString("customer_phone")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showCustomer(int index) {
        if (index >= 0 && index < customers.size()) {
            Customer customer = customers.get(index);
            idField.setText(String.valueOf(customer.getId()));
            lastNameField.setText(customer.getLastName());
            firstNameField.setText(customer.getFirstName());
            phoneField.setText(customer.getPhone());
        }
    }

    private void showPreviousCustomer() {
        if (currentIndex > 0) {
            currentIndex--;
            showCustomer(currentIndex);
        }
    }

    private void showNextCustomer() {
        if (currentIndex < customers.size() - 1) {
            currentIndex++;
            showCustomer(currentIndex);
        }
    }
}
