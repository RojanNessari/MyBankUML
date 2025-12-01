package domain.users;
import database.Database;


import domain.accounts.Card;
import domain.accounts.Checking;
import domain.accounts.Savings;
import domain.enums.UserRole;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseAdministratorAccount implements IUser {

    private String adminID;
    private String username;
    private String passwordHash;
    private String firstname;
    private String lastname;
    Database database;
    Scanner scanner = new Scanner(System.in);

    // No-arg constructor for reflection/deserialization
    public DatabaseAdministratorAccount() {}

    public DatabaseAdministratorAccount(String adminID, String username, String firstname, String lastname, String passwordHash) {
        this.adminID = adminID;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.passwordHash = passwordHash;
        this.database = Database.getInstance(); //create an initialized database instance
    }

    //empty constructor
    public DatabaseAdministratorAccount() {
        this.adminID = "";
        this.username = "";
        this.firstname = "";
        this.lastname = "";
        this.passwordHash = "";
        this.database = Database.getInstance();
    }

    //Testing helper constructor for a mock db
    public DatabaseAdministratorAccount(String adminID, String username, String firstname, String lastname, String passwordHash, Database database) {
        this.adminID = adminID;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.passwordHash = passwordHash;
        this.database = database;
    }

    public String getAdminID() {
        return adminID;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    // Interface getters
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public UserRole getRole() {
        return UserRole.ADMIN;
    }


    public void manageTellerAccounts() {

        System.out.println("Managing teller accounts");
        System.out.println("Select one the following options:\n" +
                "1. Add a new bank teller account\n" +
                "2. Remove a bank teller account\n" +
                "3. Edit a bank teller id");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                //use a gui form
                viewTellerList();
                System.out.println("Enter ID: ");
                String bankTellerID = scanner.nextLine();
                System.out.println("Enter  username: ");
                String username = scanner.nextLine();
                System.out.println("Enter  first name: ");
                String firstname = scanner.nextLine();
                System.out.println("Enter  last name: ");
                String lastname = scanner.nextLine();
                System.out.println("Enter password: ");
                String password = scanner.nextLine();
                System.out.println("Enter branch: ");
                String branchId = scanner.nextLine();
                createTeller(bankTellerID, username,firstname, lastname, password, branchId);
                break;
            case 2:
                viewTellerList();
                System.out.println("Enter ID: ");
                String bankTellerID2 = scanner.nextLine();
                removeTellerAccount(bankTellerID2);
                break;
            case 3:
                viewTellerList();
                System.out.println("Enter ID to change: ");
                String currentId = scanner.nextLine();
                System.out.println("Enter new ID: ");
                String newId = scanner.nextLine();
                changeTellerUsername(currentId, newId);
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }

    }

    //manage teller additional private functions (only available within the database admin)
    private void viewTellerList() {
        System.out.println(database.getAllTellers());

    }

    private void createTeller(String bankTellerID, String username, String firstname, String lastname, String passwordHash, String branch) {
        if (database.retrieveTeller(bankTellerID) != null) {
            System.out.println("Bank Teller with this ID already exists");
        } else {
            BankTellerAccount bankTellerAccount = new BankTellerAccount(bankTellerID, username, firstname, lastname, passwordHash, branch, database);
            database.addTeller(bankTellerAccount);
        }
    }

    private void changeTellerUsername(String currentTellerID, String newTellerID) {
        if (database.retrieveTeller(currentTellerID) == null) {
            System.out.println("Teller does not exist");
        } else {
            BankTellerAccount currentTeller = database.retrieveTeller(currentTellerID);
            BankTellerAccount newTeller = new BankTellerAccount(newTellerID, currentTeller.getUsername(), 
                    currentTeller.getFirstName(), currentTeller.getLastName(),currentTeller.getPasswordHash(), currentTeller.getBranchID(), database);
            database.updateTeller(currentTellerID, newTeller);
        }

    }

    private void removeTellerAccount(String bankTellerID) {
        if (database.retrieveTeller(bankTellerID) == null) {
            System.out.println("Teller does not exist");
        } else {
            database.removeTeller(bankTellerID);
        }

    }

    //...

    public void manageCustomerAccounts() {
        System.out.println("Managing Customer accounts");
        System.out.println("Select one the following options:\n" +
                "1. Add a new customer account\n" +
                "2. Remove a customer account\n" +
                "3. Edit a customer account's id");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                //use a gui form
                viewCustomerAccounts();
                System.out.println("Enter ID: ");
                String id = scanner.nextLine();
                System.out.println("Enter username: ");
                String username = scanner.nextLine();
                System.out.println("Enter first name: ");
                String firstname = scanner.nextLine();
                System.out.println("Enter last name: ");
                String lastname = scanner.nextLine();
                System.out.println("Enter password: ");
                String password = scanner.nextLine();
                System.out.println("Enter balance");
                double balance = scanner.nextDouble();
                System.out.println("Enter branch: ");
                String branchId = scanner.nextLine();

                //List of accounts (savings, checking, card [in that order])
                List<Account> accounts = new ArrayList<>();
                System.out.println("Does this customer have a Savings account? (y/n): ");
                if(scanner.nextLine().equals("y")) {
                    System.out.println("Enter account number: ");
                    String accountNumber = scanner.nextLine();
                    Savings saving = new Savings(id, accountNumber, balance, 0.25, 100.00);
                    accounts.add(saving);
                }

                System.out.println("Does this customer have a Checking account? (y/n): ");
                if(scanner.nextLine().equals("y")) {
                    System.out.println("Enter account number: ");
                    String accountNumber = scanner.nextLine();
                   Checking check = new Checking(id, accountNumber, balance, 100.00, 100.00, 0.00);
                   accounts.add(check);
                }



                System.out.println("Does this customer have a Card account? (y/n): ");
                if(scanner.nextLine().equals("y")) {
                    System.out.println("Enter account number: ");
                    String accountNumber = scanner.nextLine();
                    Card card = new Card(id, accountNumber, balance,1000.00, 0.25, 25.00);
                    accounts.add(card);
                }

                createCustomerAccounts(id, username, firstname, lastname, password, branchId, accounts);
                break;
            case 2:
                viewCustomerAccounts();
                System.out.println("Enter customer ID: ");
                String customerID = scanner.nextLine();
                removeCustomer(customerID);
                break;
            case 3:
                viewCustomerAccounts();
                System.out.println("Enter customer ID to change: ");
                String currentId = scanner.nextLine();
                System.out.println("Enter new customer ID: ");
                String newId = scanner.nextLine();
                changeCustomerID(currentId, newId);
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }

    }

    //manage customer accounts with additional private functions (only available within the database admin)
    private void removeCustomer(String customerID) {
        if (database.retrieveUserAccount(customerID) == null) {
            System.out.println("Customer does not exist");
        } else {
            database.removeUserAccount(customerID);
        }

    }

    private void createCustomerAccounts(String userID, String username,  String firstName, String lastName, String passwordHash, String branchID, List<Account> accounts) {
        if (database.retrieveUserAccount(userID) != null) {
            System.out.println("Customer already exists");
        } else {
            UserAccount newUser = new UserAccount(userID, username, firstName, lastName, passwordHash, branchID, accounts);
            database.addAccount(newUser);
        }

    }

    private void viewCustomerAccounts() {
        System.out.println(database.getAllAccounts());
    }


    private void changeCustomerID(String currentCustomerID, String newCustomerID) {
        if (database.retrieveUserAccount(currentCustomerID) == null) {
            System.out.println("Customer does not exist");
        } else {
            UserAccount currentUser = database.retrieveUserAccount(currentCustomerID);
            UserAccount newUser = new UserAccount(newCustomerID, currentUser.getUsername(), currentUser.getFirstName(), currentUser.getLastName(), getPasswordHash(), currentUser.getBranchID(), currentUser.getAccounts());
            database.updateUserAccount(currentCustomerID, newUser);
        }

    }


    //...


    public void generateReports() {
        int totalBankTellers = database.getAllTellers().size();
        int totalCustomers = database.getAllAccounts().size();
        int totalAdmins = database.getAllAdmins().size();
        int totalBranches = database.getAllBranches().size();
        int totalTransactions = database.getAllTransactions().size();
        int totalBanks = database.getAllBanks().size();

        String report = "-----BANK SYSTEM REPORT-----\n" +
                "Total User Accounts: " + totalCustomers +
                "Total Bank Teller Accounts: " + totalBankTellers +
                "Total Admin Accounts: " + totalAdmins +
                "Total Banks: " + totalBanks +
                "Total Branches: " + totalBranches +
                "Total Transactions: " + totalTransactions;
    }

    //changed this to boolean
    public boolean searchAccounts(String id) {
        return database.retrieveUserAccount(id) != null && database.retrieveTeller(id) != null;
    }


    public void reverseTransactions(String id) {
        database.retrieveTransaction(id).reverseTransaction();
    }


}
