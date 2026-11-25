package ravline;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

// Main contains only the menu, user input, and calls to Database.
public class Main {
    private static final Scanner input = new Scanner(System.in);

    // Starts the program, repeats the menu, and sends each choice to its function.
    public static void main(String[] args) {
        boolean keepRunning = true;

        while (keepRunning) {
            showMenu();
            int choice = readNumber("Choose an option: ");

            try {
                switch (choice) {
                    case 1: addCar(); break;
                    case 2: browseCars(false); break;
                    case 3: browseCars(true); break;
                    case 4: updateCar(); break;
                    case 5: Database.deleteCar(readId("Car ID: ")); break;
                    case 6: registerCustomer(); break;
                    case 7:
                        showSection("SOLD CARS");
                        Database.viewSoldCars();
                        break;
                    case 8: sellCar(); break;
                    case 9:
                        keepRunning = false;
                        System.out.println("\nThank you for using RevLine. Goodbye!\n");
                        break;
                    case 10: purchaseCar(); break;
                    default:
                        System.out.println("Please choose a number from 1 to 10.");
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            } catch (BackToMenuException e) {
                System.out.println("\n  Action cancelled. Returned to the main menu.");
            } catch (IllegalArgumentException e) {
                System.out.println("Input error: " + e.getMessage());
            }
        }
    }

    // Prints the numbered choices so the user can select a task.
    private static void showMenu() {
        System.out.println();
        System.out.println("+--------------------------------------------------+");
        System.out.println("|                    RevLine                     |");
        System.out.println("|          Car Selling Management System           |");
        System.out.println("+--------------------------------------------------+");
        System.out.println("|  1. Add Car             6. Register Customer     |");
        System.out.println("|  2. View All Cars       7. View Sold Cars        |");
        System.out.println("|  3. Search Car          8. Sell Car              |");
        System.out.println("|  4. Update Car          9. Exit                  |");
        System.out.println("|  5. Delete Car         10. Purchase Car          |");
        System.out.println("+--------------------------------------------------+");
        System.out.println("  Tip: enter B at any prompt to cancel and return here.");
    }

    // Shows cars one at a time. N/P move, a number jumps to that list position, S searches by name, and B returns.
    private static void browseCars(boolean search) throws SQLException {
        showSection(search ? "SEARCH CAR" : "CAR INVENTORY");
        List<Car> cars = Database.getCars();
        if (cars.isEmpty()) {
            System.out.println("There are no cars in the database yet.");
            return;
        }

        int position = 0;
        if (search) {
            int wantedId = readId("Car ID to search: ");
            position = -1;
            for (int i = 0; i < cars.size(); i++) {
                if (cars.get(i).getCarId() == wantedId) {
                    position = i;
                    break;
                }
            }
            if (position == -1) {
                System.out.println("Car ID not found.");
                return;
            }
        }

        boolean browsing = true;
        while (browsing) {
            System.out.println();
            System.out.println("  Car " + (position + 1) + " of " + cars.size());
            System.out.println("  ----------------------------------------------");
            System.out.printf("  ID       : %d%n", cars.get(position).getCarId());
            System.out.printf("  Vehicle  : %s %s%n", cars.get(position).getBrand(), cars.get(position).getModel());
            System.out.printf("  Year     : %d%n", cars.get(position).getYear());
            System.out.printf("  Price    : Rs. %.2f%n", cars.get(position).getPrice());
            System.out.printf("  Status   : %s%n", cars.get(position).getStatus());
            System.out.println("  ----------------------------------------------");
            System.out.println("  [N] Next   [P] Previous   [S] Search name   [B] Main menu");
            System.out.println("  Or enter a car number from 1 to " + cars.size() + " to jump directly.");
            String command = readText("  Your choice: ");

            if (command.equalsIgnoreCase("N")) {
                if (position < cars.size() - 1) position++;
                else System.out.println("This is the last car.");
            } else if (command.equalsIgnoreCase("P")) {
                if (position > 0) position--;
                else System.out.println("This is the first car.");
            } else if (command.equalsIgnoreCase("B")) {
                browsing = false;
            } else if (command.equalsIgnoreCase("S")) {
                String searchName = readText("  Enter brand or model to search: ").toLowerCase();
                int matchPosition = findCarPosition(cars, searchName);
                if (matchPosition == -1) {
                    System.out.println("No car matched that name. Still showing the current car.");
                } else {
                    position = matchPosition;
                    System.out.println("Showing the first matching car. Use N/P to browse nearby cars.");
                }
            } else {
                try {
                    int carNumber = Integer.parseInt(command);
                    if (carNumber >= 1 && carNumber <= cars.size()) {
                        position = carNumber - 1;
                    } else {
                        System.out.println("Enter a car number from 1 to " + cars.size() + ".");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Enter N, P, S, B, or a car number.");
                }
            }
        }
    }

    // Finds the first car whose brand or model contains the search text; returns -1 when there is no match.
    private static int findCarPosition(List<Car> cars, String searchName) {
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            String carName = (car.getBrand() + " " + car.getModel()).toLowerCase();
            if (carName.contains(searchName)) {
                return i;
            }
        }
        return -1;
    }

    // Reads car details and asks Database to save a new car.
    private static void addCar() throws SQLException {
        showSection("ADD A CAR");
        String brand = readText("Brand: ");
        String model = readText("Model: ");
        int year = readNumber("Year: ");
        if (year < 1886 || year > 2100) throw new IllegalArgumentException("Year must be from 1886 to 2100.");
        double price = readPrice("Price: ");

        Car car = new Car(brand, model, year, price);
        Database.addCar(car);
        System.out.println("\n  Car added successfully!");
    }

    // Reads replacement details and asks Database to update an available car.
    private static void updateCar() throws SQLException {
        showSection("UPDATE CAR DETAILS");
        int id = readId("Car ID: ");
        String brand = readText("New brand: ");
        String model = readText("New model: ");
        int year = readNumber("New year: ");
        if (year < 1886 || year > 2100) throw new IllegalArgumentException("Year must be from 1886 to 2100.");
        double price = readPrice("New price: ");

        Database.updateCar(id, brand, model, year, price);
    }

    // Reads customer contact details and asks Database to register the customer.
    private static void registerCustomer() throws SQLException {
        showSection("REGISTER CUSTOMER");
        String name = readText("Customer name: ");
        String phone = readText("Phone: ");
        String email = readText("Email: ");
        if (!email.contains("@")) throw new IllegalArgumentException("Enter a valid email containing @.");

        Customer customer = new Customer(name, phone, email);
        Database.addCustomer(customer);
        System.out.println("Customer registered.");
    }

    // Sells an available car directly by car ID and registered customer ID.
    private static void sellCar() throws SQLException {
        showSection("SELL A CAR");
        int carId = readId("Car ID: ");
        int customerId = readId("Customer ID: ");
        System.out.println("The car's listed price will be used.");
        String answer = readText("Confirm sale? (Y/N): ");

        if (answer.equalsIgnoreCase("Y")) {
            Database.sellCar(carId, customerId);
        } else {
            System.out.println("Sale cancelled.");
        }
    }

    // Purchase option for a customer; it uses the same safe sale transaction.
    private static void purchaseCar() throws SQLException {
        showSection("PURCHASE A CAR");
        int carId = readId("Car ID to purchase: ");
        int customerId = readId("Registered customer ID: ");
        System.out.println();
        System.out.println("  The listed car price will be charged.");
        String answer = readText("  Confirm purchase? (Y/N): ");

        if (answer.equalsIgnoreCase("Y")) {
            Database.sellCar(carId, customerId);
        } else {
            System.out.println("\n  Purchase cancelled. No changes were made.");
        }
    }

    // Prints a matching heading before each menu action.
    private static void showSection(String title) {
        System.out.println();
        System.out.println("+--------------------------------------------------+");
        System.out.printf("| %-48s |%n", title);
        System.out.println("+--------------------------------------------------+");
    }

    // Keeps asking until the user types a whole number.
    private static int readNumber(String message) {
        while (true) {
            try {
                return Integer.parseInt(readText(message));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    // Reads an ID and rejects zero or negative numbers.
    private static int readId(String message) {
        int id = readNumber(message);
        if (id <= 0) throw new IllegalArgumentException("ID must be greater than zero.");
        return id;
    }

    // Reads a positive price and rejects text, zero, and negative values.
    private static double readPrice(String message) {
        try {
            double price = Double.parseDouble(readText(message));
            if (price <= 0 || Double.isInfinite(price) || Double.isNaN(price)) {
                throw new IllegalArgumentException("Price must be a positive number.");
            }
            return price;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Enter a valid price.");
        }
    }

    // Reads text; B cancels the current action and returns to the main menu.
    private static String readText(String message) {
        System.out.print(message);
        String value = input.nextLine().trim();
        if (value.equalsIgnoreCase("B")) throw new BackToMenuException();
        if (value.isEmpty()) throw new IllegalArgumentException("This field cannot be blank.");
        return value;
    }

    // Stops an unfinished action without saving partial information.
    private static class BackToMenuException extends RuntimeException { }
}



