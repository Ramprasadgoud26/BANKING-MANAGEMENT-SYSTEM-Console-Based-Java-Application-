package com.project;

import java.io.*;
import java.util.*;

class Account implements Serializable {
    private String accNumber;
    private String holderName;
    private double balance;

    public Account(String accNumber, String holderName) {
        this.accNumber = accNumber;
        this.holderName = holderName;
        this.balance = 0.0;
    }

    public String getAccNumber() { return accNumber; }
    public String getHolderName() { return holderName; }
    public double getBalance() { return balance; }

    public void deposit(double amt) {
        if (amt <= 0) throw new IllegalArgumentException("Invalid amount!");
        balance += amt;
    }

    public void withdraw(double amt) {
        if (amt <= 0) throw new IllegalArgumentException("Invalid amount!");
        if (amt > balance) throw new IllegalArgumentException("Insufficient Balance!");
        balance -= amt;
    }

    @Override
    public String toString() {
        return accNumber + " - " + holderName + " - Balance: " + balance;
    }
}

class Bank {
    private HashMap<String, Account> accounts = new HashMap<>();
    private final String FILE_NAME = "bankData.ser";

    public Bank() { loadFromFile(); }

    public void createAccount(String number, String name) {
        accounts.put(number, new Account(number, name));
        saveToFile();
    }

    public Account getAccount(String number) {
        return accounts.get(number);
    }

    public void deposit(String acc, double amt) {
        Account a = getAccount(acc);
        if (a == null) throw new RuntimeException("Account not found!");
        a.deposit(amt);
        saveToFile();
    }

    public void withdraw(String acc, double amt) {
        Account a = getAccount(acc);
        if (a == null) throw new RuntimeException("Account not found!");
        a.withdraw(amt);
        saveToFile();
    }

    public void showAllAccounts() {
        for (Account a : accounts.values()) {
            System.out.println(a);
        }
    }

    private void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(accounts);
        } catch (Exception e) { System.out.println("Error saving file: " + e); }
    }

    private void loadFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            accounts = (HashMap<String, Account>) in.readObject();
        } catch (Exception e) { accounts = new HashMap<>(); }
    }
}

public class BankApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Bank bank = new Bank();

        while (true) {
            System.out.println("\n=== BANKING MANAGEMENT SYSTEM ===");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Show All Accounts");
            System.out.println("6. Exit");
            System.out.print("Enter Choice: ");

            int ch = sc.nextInt();

            try {
                switch (ch) {
                    case 1:
                        System.out.print("Enter Account Number: ");
                        String num = sc.next();
                        System.out.print("Enter Name: ");
                        String name = sc.next();
                        bank.createAccount(num, name);
                        System.out.println("Account Created!");
                        break;
                    case 2:
                        System.out.print("Enter Account Number: ");
                        num = sc.next();
                        System.out.print("Amount: ");
                        double amt = sc.nextDouble();
                        bank.deposit(num, amt);
                        System.out.println("Deposit Successful!");
                        break;
                    case 3:
                        System.out.print("Enter Account Number: ");
                        num = sc.next();
                        System.out.print("Amount: ");
                        amt = sc.nextDouble();
                        bank.withdraw(num, amt);
                        System.out.println("Withdrawal Successful!");
                        break;
                    case 4:
                        System.out.print("Enter Account Number: ");
                        num = sc.next();
                        Account a = bank.getAccount(num);
                        if (a != null) System.out.println("Balance: " + a.getBalance());
                        else System.out.println("Account not found!");
                        break;
                    case 5:
                        bank.showAllAccounts();
                        break;
                    case 6:
                        System.out.println("Thank you!");
                        return;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
