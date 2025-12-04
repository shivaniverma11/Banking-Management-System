 import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

class Account {
    private String username;
    private String password;
    private double balance;

    public Account(String username, String password, double balance) {
        this.username = username;
        this.password = password;
        this.balance = balance;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}

class Bank {
    private Map<String, Account> accounts = new HashMap<>();

    public boolean signup(String username, String password, double initialBalance) {
        if (accounts.containsKey(username)) return false;
        accounts.put(username, new Account(username, password, initialBalance));
        return true;
    }

    public Account login(String username, String password) {
        Account acc = accounts.get(username);
        if (acc != null && acc.getPassword().equals(password)) return acc;
        return null;
    }

    public boolean deposit(Account acc, double amount) {
        if (amount <= 0) return false;
        acc.setBalance(acc.getBalance() + amount);
        return true;
    }

    public boolean withdraw(Account acc, double amount) {
        if (amount <= 0 || acc.getBalance() < amount) return false;
        acc.setBalance(acc.getBalance() - amount);
        return true;
    }
}

public class BankManagementSystem extends JFrame {
    private Bank bank = new Bank();
    private Account currentAccount;
    private JPanel loginPanel, signupPanel, connPanel, depositPanel, fastCashPanel, withdrawPanel;
    private CardLayout cardLayout;

    public BankManagementSystem() {
        setTitle("Bank Management System");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        createLoginPanel();
        createSignupPanel();
        createConnPanel();
        createDepositPanel();
        createFastCashPanel();
        createWithdrawPanel();

        add(loginPanel, "Login");
        add(signupPanel, "Signup");
        add(connPanel, "Conn");
        add(depositPanel, "Deposit");
        add(fastCashPanel, "FastCash");
        add(withdrawPanel, "Withdraw");

        cardLayout.show(getContentPane(), "Login");
    }

    private void createLoginPanel() {
        loginPanel = new JPanel(new GridLayout(4, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Go to Signup");

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginBtn);
        loginPanel.add(signupBtn);

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            currentAccount = bank.login(username, password);
            if (currentAccount != null) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                cardLayout.show(getContentPane(), "Conn");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!");
            }
        });

        signupBtn.addActionListener(e -> cardLayout.show(getContentPane(), "Signup"));
    }

    private void createSignupPanel() {
        signupPanel = new JPanel(new GridLayout(5, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JTextField balanceField = new JTextField();
        JButton signupBtn = new JButton("Signup");
        JButton backBtn = new JButton("Back to Login");

        signupPanel.add(new JLabel("Username:"));
        signupPanel.add(usernameField);
        signupPanel.add(new JLabel("Password:"));
        signupPanel.add(passwordField);
        signupPanel.add(new JLabel("Initial Balance:"));
        signupPanel.add(balanceField);
        signupPanel.add(signupBtn);
        signupPanel.add(backBtn);

        signupBtn.addActionListener(e -> {
            try {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                double balance = Double.parseDouble(balanceField.getText());
                if (bank.signup(username, password, balance)) {
                    JOptionPane.showMessageDialog(this, "Signup successful!");
                    cardLayout.show(getContentPane(), "Login");
                } else {
                    JOptionPane.showMessageDialog(this, "Username already exists!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid balance!");
            }
        });

        backBtn.addActionListener(e -> cardLayout.show(getContentPane(), "Login"));
    }

    private void createConnPanel() {
        connPanel = new JPanel(new GridLayout(6, 1));
        JButton depositBtn = new JButton("Deposit");
        JButton fastCashBtn = new JButton("Fast Cash");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton balanceBtn = new JButton("Check Balance");
        JButton logoutBtn = new JButton("Logout");

        connPanel.add(new JLabel("Welcome to Bank Management"));
        connPanel.add(depositBtn);
        connPanel.add(fastCashBtn);
        connPanel.add(withdrawBtn);
        connPanel.add(balanceBtn);
        connPanel.add(logoutBtn);

        depositBtn.addActionListener(e -> cardLayout.show(getContentPane(), "Deposit"));
        fastCashBtn.addActionListener(e -> cardLayout.show(getContentPane(), "FastCash"));
        withdrawBtn.addActionListener(e -> cardLayout.show(getContentPane(), "Withdraw"));
        balanceBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Balance: $" + currentAccount.getBalance()));
        logoutBtn.addActionListener(e -> {
            currentAccount = null;
            cardLayout.show(getContentPane(), "Login");
        });
    }

    private void createDepositPanel() {
        depositPanel = new JPanel(new GridLayout(4, 2));
        JTextField amountField = new JTextField();
        JButton depositBtn = new JButton("Deposit");
        JButton backBtn = new JButton("Back");

        depositPanel.add(new JLabel("Amount:"));
        depositPanel.add(amountField);
        depositPanel.add(depositBtn);
        depositPanel.add(backBtn);

        depositBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (bank.deposit(currentAccount, amount)) {
                    JOptionPane.showMessageDialog(this, "Deposit successful! New balance: $" + currentAccount.getBalance());
                    amountField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid amount!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount!");
            }
        });

        backBtn.addActionListener(e -> cardLayout.show(getContentPane(), "Conn"));
    }

    private void createFastCashPanel() {
        fastCashPanel = new JPanel(new GridLayout(5, 2));
        JButton twentyBtn = new JButton("$20");
        JButton fiftyBtn = new JButton("$50");
        JButton hundredBtn = new JButton("$100");
        JButton backBtn = new JButton("Back");

        fastCashPanel.add(new JLabel("Fast Cash Options:"));
        fastCashPanel.add(new JLabel(""));
        fastCashPanel.add(twentyBtn);
        fastCashPanel.add(fiftyBtn);
        fastCashPanel.add(hundredBtn);
        fastCashPanel.add(backBtn);

        ActionListener fastCashListener = e -> {
            double amount = Double.parseDouble(e.getActionCommand().substring(1));
            if (bank.withdraw(currentAccount, amount)) {
                JOptionPane.showMessageDialog(this, "Withdrawal successful! New balance: $" + currentAccount.getBalance());
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient funds!");
            }
        };

        twentyBtn.addActionListener(fastCashListener);
        fiftyBtn.addActionListener(fastCashListener);
        hundredBtn.addActionListener(fastCashListener);
        backBtn.addActionListener(e -> cardLayout.show(getContentPane(), "Conn"));
    }

    private void createWithdrawPanel() {
        withdrawPanel = new JPanel(new GridLayout(4, 2));
        JTextField amountField = new JTextField();
        JButton withdrawBtn = new JButton("Withdraw");
        JButton backBtn = new JButton("Back");

        withdrawPanel.add(new JLabel("Amount:"));
        withdrawPanel.add(amountField);
        withdrawPanel.add(withdrawBtn);
        withdrawPanel.add(backBtn);

        withdrawBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (bank.withdraw(currentAccount, amount)) {
                    JOptionPane.showMessageDialog(this, "Withdrawal successful! New balance: $" + currentAccount.getBalance());
                    amountField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient funds or invalid amount!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount!");
            }
        });

        backBtn.addActionListener(e -> cardLayout.show(getContentPane(), "Conn"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankManagementSystem().setVisible(true));
    }
}
