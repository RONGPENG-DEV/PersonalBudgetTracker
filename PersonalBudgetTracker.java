import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Map.Entry;

/**
 * PersonalBudgetTracker: A Java program to help users track monthly income, expenses, and savings.
 * Features: Add income/expense entries, view a formatted budget summary, and handle invalid user input.
 * Uses object-oriented principles (inheritance, classes) and dynamic lists (ArrayLists) for data storage.
 */
public class PersonalBudgetTracker {
    // ArrayList to store all income entries (each entry is an Income object)
    private ArrayList<Income> incomeList = new ArrayList<>();
    // ArrayList to store all expense entries (each entry is an Expense object)
    private ArrayList<Expense> expenseList = new ArrayList<>();
    // Scanner object to read user input from the console
    private Scanner scanner = new Scanner(System.in);

    /**
     * Main method: Entry point of the program.
     * Creates a PersonalBudgetTracker instance and launches the main menu.
     * @param args Command-line arguments (not used in this program)
     */
    public static void main(String[] args) {
        PersonalBudgetTracker tracker = new PersonalBudgetTracker();
        tracker.runMenu(); // Start the menu system
    }

    /**
     * runMenu: Displays the main menu and handles user choices.
     * Repeats until the user selects "Exit" (option 4).
     * Uses a do-while loop to ensure the menu shows at least once.
     */
    private void runMenu() {
        int userChoice;
        do {
            // Print menu header and options
            System.out.println("\n=== Personal Budget Tracker ===");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Budget Summary");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");
            
            // Get valid integer input (1-4) using helper method
            userChoice = getValidIntegerInput(1, 4);

            // Execute the user's selected action
            switch (userChoice) {
                case 1:
                    addIncome(); // Call method to add a new income entry
                    break;
                case 2:
                    addExpense(); // Call method to add a new expense entry
                    break;
                case 3:
                    viewBudgetSummary(); // Call method to show budget totals/breakdown
                    break;
                case 4:
                    System.out.println("Exiting program. Thank you!");
                    scanner.close(); // Close the scanner to free system resources
                    break;
            }
        } while (userChoice != 4); // Loop until user chooses to exit
    }

    /**
     * addIncome: Guides the user to enter income details (source, amount, date)
     * Validates all inputs (non-empty strings, positive amount) and adds the entry to incomeList.
     */
    private void addIncome() {
        System.out.println("\n--- Add New Income ---");
        
        // Get and validate income source (cannot be empty)
        System.out.print("Enter income source (e.g., Salary): ");
        String source = scanner.nextLine().trim();
        while (source.isEmpty()) {
            System.out.print("Source cannot be empty. Try again: ");
            source = scanner.nextLine().trim();
        }

        // Get and validate income amount (must be positive) using helper method
        System.out.print("Enter income amount: $");
        double amount = getValidPositiveDoubleInput();

        // Get and validate income date (cannot be empty)
        System.out.print("Enter income date (MM/DD/YYYY): ");
        String date = scanner.nextLine().trim();
        while (date.isEmpty()) {
            System.out.print("Date cannot be empty. Try again: ");
            date = scanner.nextLine().trim();
        }

        // Create a new Income object and add it to the incomeList
        Income newIncome = new Income(date, amount, source);
        incomeList.add(newIncome);

        // Print confirmation message (formatted to 2 decimal places for currency)
        System.out.printf("Success! Income from %s ($%.2f) added.%n", source, amount);
    }

    /**
     * addExpense: Guides the user to enter expense details (category, amount, date)
     * Validates all inputs (non-empty strings, positive amount) and adds the entry to expenseList.
     */
    private void addExpense() {
        System.out.println("\n--- Add New Expense ---");
        
        // Get and validate expense category (cannot be empty)
        System.out.print("Enter expense category (e.g., Rent): ");
        String category = scanner.nextLine().trim();
        while (category.isEmpty()) {
            System.out.print("Category cannot be empty. Try again: ");
            category = scanner.nextLine().trim();
        }

        // Get and validate expense amount (must be positive) using helper method
        System.out.print("Enter expense amount: $");
        double amount = getValidPositiveDoubleInput();

        // Get and validate expense date (cannot be empty)
        System.out.print("Enter expense date (MM/DD/YYYY): ");
        String date = scanner.nextLine().trim();
        while (date.isEmpty()) {
            System.out.print("Date cannot be empty. Try again: ");
            date = scanner.nextLine().trim();
        }

        // Create a new Expense object and add it to the expenseList
        Expense newExpense = new Expense(date, amount, category);
        expenseList.add(newExpense);

        // Print confirmation message (formatted to 2 decimal places for currency)
        System.out.printf("Success! Expense (%s: $%.2f) added.%n", category, amount);
    }

    /**
     * calculateTotalIncome: Computes the sum of all amounts in incomeList.
     * @return Total monthly income as a double (formatted to 2 decimals later)
     */
    private double calculateTotalIncome() {
        double total = 0.0;
        // Loop through each Income entry in incomeList and add its amount to total
        for (Income income : incomeList) {
            total += income.getAmount();
        }
        return total;
    }

    /**
     * calculateTotalExpenses: Computes the sum of all amounts in expenseList.
     * @return Total monthly expenses as a double (formatted to 2 decimals later)
     */
    private double calculateTotalExpenses() {
        double total = 0.0;
        // Loop through each Expense entry in expenseList and add its amount to total
        for (Expense expense : expenseList) {
            total += expense.getAmount();
        }
        return total;
    }

    /**
     * getExpenseBreakdown: Groups expenses by category and calculates the total for each category.
     * Uses a HashMap to store category names (keys) and their total amounts (values).
     * @return HashMap<String, Double> where key = category, value = total for category
     */
    private HashMap<String, Double> getExpenseBreakdown() {
        HashMap<String, Double> breakdown = new HashMap<>();
        
        // Loop through each Expense entry to build the breakdown
        for (Expense expense : expenseList) {
            String category = expense.getCategory();
            double amount = expense.getAmount();
            
            // If the category already exists in the map, add to its total; else, set to amount
            if (breakdown.containsKey(category)) {
                breakdown.put(category, breakdown.get(category) + amount);
            } else {
                breakdown.put(category, amount);
            }
        }
        
        return breakdown;
    }

    /**
     * viewBudgetSummary: Displays total income, total expenses, net savings, and expense breakdown.
     * Formats currency to 2 decimal places and percentages to 1 decimal place for readability.
     */
    private void viewBudgetSummary() {
        System.out.println("\n=== Budget Summary ===");
        
        // Calculate key financial metrics using helper methods
        double totalIncome = calculateTotalIncome();
        double totalExpenses = calculateTotalExpenses();
        double netSavings = totalIncome - totalExpenses;
        HashMap<String, Double> expenseBreakdown = getExpenseBreakdown();

        // Print totals (formatted for currency)
        System.out.printf("Total Income: $%.2f%n", totalIncome);
        System.out.printf("Total Expenses: $%.2f%n", totalExpenses);
        System.out.printf("Net Savings: $%.2f%n", netSavings);

        // Print expense breakdown (if there are expenses)
        if (expenseBreakdown.isEmpty()) {
            System.out.println("\nNo expense entries yet.");
        } else {
            System.out.println("\nExpense Breakdown by Category:");
            // Loop through the breakdown map to print each category's total and percentage
            for (Entry<String, Double> entry : expenseBreakdown.entrySet()) {
                String category = entry.getKey();
                double categoryTotal = entry.getValue();
                // Calculate percentage of total expenses (avoid division by zero if totalExpenses is 0)
                double percentage = (totalExpenses > 0) ? (categoryTotal / totalExpenses) * 100 : 0.0;
                System.out.printf("- %s: $%.2f (%.1f%%)%n", category, categoryTotal, percentage);
            }
        }
    }

    /**
     * getValidIntegerInput: Helper method to get a valid integer within a specified range.
     * Handles non-integer input (InputMismatchException) and out-of-range values.
     * @param min Minimum valid integer value
     * @param max Maximum valid integer value
     * @return Valid integer input from the user
     */
    private int getValidIntegerInput(int min, int max) {
        int input;
        while (true) {
            try {
                input = scanner.nextInt();
                scanner.nextLine(); // Clear the leftover newline character from nextInt()
                
                // Check if input is within the allowed range
                if (input >= min && input <= max) {
                    break; // Exit loop if input is valid
                } else {
                    System.out.printf("Please enter a number between %d and %d: ", min, max);
                }
            } catch (InputMismatchException e) {
                // Handle cases where user enters a non-integer (e.g., "abc")
                System.out.print("Invalid input. Please enter a whole number: ");
                scanner.nextLine(); // Clear the invalid input from the scanner buffer
            }
        }
        return input;
    }

    /**
     * getValidPositiveDoubleInput: Helper method to get a valid positive double (for currency).
     * Handles non-numeric input (InputMismatchException) and non-positive values.
     * @return Valid positive double input from the user
     */
    private double getValidPositiveDoubleInput() {
        double input;
        while (true) {
            try {
                input = scanner.nextDouble();
                scanner.nextLine(); // Clear the leftover newline character from nextDouble()
                
                // Check if input is a positive number
                if (input > 0) {
                    break; // Exit loop if input is valid
                } else {
                    System.out.print("Amount must be positive. Try again: $");
                }
            } catch (InputMismatchException e) {
                // Handle cases where user enters a non-numeric value (e.g., "xyz")
                System.out.print("Invalid input. Please enter a number: $");
                scanner.nextLine(); // Clear the invalid input from the scanner buffer
            }
        }
        return input;
    }
}

/**
 * BudgetEntry: Parent class for all budget entries (Income and Expense).
 * Stores common attributes (date, amount) and provides getters to access them.
 */
class BudgetEntry {
    private String date; // Date of the entry (format: MM/DD/YYYY)
    private double amount; // Monetary amount of the entry (positive value)

    /**
     * Constructor for BudgetEntry: Initializes date and amount.
     * @param date Date of the entry (MM/DD/YYYY)
     * @param amount Monetary amount (positive)
     */
    public BudgetEntry(String date, double amount) {
        this.date = date;
        this.amount = amount;
    }

    /**
     * Getter for date attribute.
     * @return Date of the entry (MM/DD/YYYY)
     */
    public String getDate() {
        return this.date;
    }

    /**
     * Getter for amount attribute.
     * @return Monetary amount of the entry
     */
    public double getAmount() {
        return this.amount;
    }
}

/**
 * Income: Child class of BudgetEntry, representing an income entry.
 * Adds a "source" attribute (e.g., "Salary") to track where the income came from.
 */
class Income extends BudgetEntry {
    private String source; // Source of the income (e.g., Salary, Freelance)

    /**
     * Constructor for Income: Calls parent BudgetEntry constructor and initializes source.
     * @param date Date of the income (MM/DD/YYYY)
     * @param amount Amount of the income (positive)
     * @param source Source of the income (e.g., "Freelance Project")
     */
    public Income(String date, double amount, String source) {
        super(date, amount); // Pass date and amount to parent class constructor
        this.source = source;
    }

    /**
     * Getter for source attribute.
     * @return Source of the income
     */
    public String getSource() {
        return this.source;
    }
}

/**
 * Expense: Child class of BudgetEntry, representing an expense entry.
 * Adds a "category" attribute (e.g., "Rent") to track what the expense was for.
 */
class Expense extends BudgetEntry {
    private String category; // Category of the expense (e.g., Rent, Groceries)

    /**
     * Constructor for Expense: Calls parent BudgetEntry constructor and initializes category.
     * @param date Date of the expense (MM/DD/YYYY)
     * @param amount Amount of the expense (positive)
     * @param category Category of the expense (e.g., "Groceries")
     */
    public Expense(String date, double amount, String category) {
        super(date, amount); // Pass date and amount to parent class constructor
        this.category = category;
    }

    /**
     * Getter for category attribute.
     * @return Category of the expense
     */
    public String getCategory() {
        return this.category;
    }
}
