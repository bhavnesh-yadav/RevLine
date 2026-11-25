package ravline;

// Holds one customer's contact information.
public class Customer {
    private int customerId;
    private String name;
    private String phone;
    private String email;

    public Customer(int customerId, String name, String phone, String email) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Customer(String name, String phone, String email) {
        this(0, name, phone, email);
    }

    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return String.format("%d | %s | %s | %s", customerId, name, phone, email);
    }
}
