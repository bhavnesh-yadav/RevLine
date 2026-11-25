package ravline;

// Holds the details of one car returned from or entered into the database.
public class Car {
    private int carId;
    private String brand;
    private String model;
    private int year;
    private double price;
    private String status;

    public Car(int carId, String brand, String model, int year, double price, String status) {
        this.carId = carId;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.status = status;
    }

    public Car(String brand, String model, int year, double price) {
        this(0, brand, model, year, price, "AVAILABLE");
    }

    public int getCarId() { return carId; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }

    public void setBrand(String brand) { this.brand = brand; }
    public void setModel(String model) { this.model = model; }
    public void setYear(int year) { this.year = year; }
    public void setPrice(double price) { this.price = price; }

    // Polymorphic Object method: makes a Car easy to print in the console.
    @Override
    public String toString() {
        return String.format("%d | %s %s | %d | %.2f | %s", carId, brand, model, year, price, status);
    }
}
