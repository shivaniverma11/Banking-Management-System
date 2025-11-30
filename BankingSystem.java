import java.util.*;

abstract class BankAccount {
    private String accountNumber;
    private String accountHolderName;
    private String pin;
    protected double balance;

    public BankAccount(String accountNumber, String accountHolderName, double initialBalance, String pin) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.pin = pin;
    }

    public boolean authenticate(String enteredpin) {
        return pin.equals(enteredpin);
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public abstract void withdraw(double amount);

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }
}

class SavingsAccount extends BankAccount {
    private double minimumBalance = 500.0;

    public SavingsAccount(String accountNumber, String accountHolderName, double initialBalance, String pin) {
        super(accountNumber, accountHolderName, initialBalance, pin);
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && (balance - amount) >= minimumBalance) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }
}

class CurrentAccount extends BankAccount {
    private double overdraftLimit = 1000.0;

    public CurrentAccount(String accountNumber, String accountHolderName, double initialBalance, String pin) {
        super(accountNumber, accountHolderName, initialBalance, pin);
    }

    @Override
    public void withdraw(double amount) {
        if (amount > 0 && (balance - amount) >= -overdraftLimit) {
            balance -= amount;
            System.out.println("Withdrawn: " + amount);
        } else {
            System.out.println("Overdraft limit exceeded or invalid amount.");
        }
    }
}

public class BankingSystem {
    private static Map<String, BankAccount> accounts = new HashMap<String, BankAccount>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int option;
        do {
            System.out.println("\n--- Banking System Menu ---");
            System.out.println("1. Create Account");
            System.out.println("2. Login to Account");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    createAccount(scanner);
                    break;
                case 2:
                    login(scanner);
                    break;
                case 3:
                    System.out.println("Thank you for using the Banking System!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (option != 3);

        scanner.close();
    }

    private static void createAccount(Scanner scanner) {
        scanner.nextLine(); 
        System.out.print("Enter Account Type (1 for Savings, 2 for Current): ");
        int accountType = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.nextLine();

        if (accounts.containsKey(accountNumber)) {
            System.out.println("Account already exists with this number!");
            return;
        }

        System.out.print("Enter Account Holder Name: ");
        String accountHolderName = scanner.nextLine();

        System.out.print("Enter Initial Balance: ");
        double initialBalance = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Set 4-digit PIN: ");
        String pin = scanner.nextLine();

        BankAccount account;
        if (accountType == 1) {
            account = new SavingsAccount(accountNumber, accountHolderName, initialBalance, pin);
        } else {
            account = new CurrentAccount(accountNumber, accountHolderName, initialBalance, pin);
        }

        accounts.put(accountNumber, account);
        System.out.println("Account created successfully!");
    }

    private static void login(Scanner scanner) {
        scanner.nextLine();
        System.out.print("Enter Account Number: ");
        String accNum = scanner.nextLine();

        BankAccount account = accounts.get(accNum);
        if (account == null) {
            System.out.println("Account not found!");
            return;
        }

        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        if (!account.authenticate(pin)) {
            System.out.println("Invalid PIN!");
            return;
        }

        int choice;
        do {
            System.out.println("\n--- Account Menu ---");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Balance Inquiry");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    account.withdraw(withdrawAmount);
                    break;
                case 3:
                    System.out.println("Current Balance: " + account.getBalance());
                    break;
                case 4:
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 4);
    }
}

