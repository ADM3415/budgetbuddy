package com.adam.budgetbuddy;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.IOException;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        Ledger ledger = new Ledger();
        Repository<Budget> budgets = new Repository<>();
        // --------------------------- LOading previous budgets and Transactions into the ledger
        TransactionFileStorage transactionStorage = new TransactionFileStorage();
        BudgetFileStorage budgetStorage = new BudgetFileStorage();
        try {
            for (Transaction transaction : transactionStorage.load("transactions.csv")) {
                ledger.addTransaction(transaction);
            }
        } catch (IOException | InsufficientFundsException e) {
            System.out.println("Could not load transactions: " + e.getMessage());
            scanner.close();
            return;
        }
        try {
            for (Budget budget : budgetStorage.load("budgets.csv")) {
                budgets.add(budget);
            }
        } catch (IOException e) {
            System.out.println("Could not load budgets: " + e.getMessage());
            scanner.close();
            return;
        }
        // ---------------------------
        
        while (running) {
            System.out.println("\n=== BudgetBuddy ===");
            System.out.println("1. Add transaction");
            System.out.println("2. View all transactions");
            System.out.println("3. View category summary");
            System.out.println("4. Set a budget");
            System.out.println("5. Check budget status");
            System.out.println("6. Save & exit");
            System.out.print("Your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1 : {
                        try {
                            System.out.println("Type (INCOME / EXPENSE): ");
                            // type
                            TransactionType type = TransactionType.valueOf(scanner.nextLine().trim().toUpperCase());
                            System.out.println("Category ( FOOD / TRANSPORT / ENTERTAINMENT / SALARY / OTHER ) : ");
                            // category
                            Category category = Category.valueOf(scanner.nextLine().trim().toUpperCase());
                            System.out.println("Amount :");
                            // amount
                            double amount = Double.parseDouble(scanner.nextLine().trim());
                            System.out.println("Description :");
                            // description
                            String description = scanner.nextLine();
                            System.out.println("Adding Transaction ...");
                            // The transcation
                            Transaction transaction = new Transaction(
                                LocalDate.now(),
                                type,
                                category,
                                amount,
                                description
                            );
                            // adding transaction to the ledger
                            ledger.addTransaction(transaction);
                            System.out.println("Transaction added succesfully");
                        }
                        catch (InvalidTransactionException | InsufficientFundsException e){
                            System.out.println(e.getMessage());
                        }
                        catch (IllegalArgumentException e){
                            System.out.println("Invalid Type, category or number. Try again");
                        }
                        break;
                    }
                    case 2 : {
                        if(ledger.getAll().isEmpty()){
                            System.out.println("No transactions yet.");
                            break;
                        }
                        System.out.println("Showing all transactions...");
                        for (Transaction transaction : ledger.getAll()) {
                            System.out.println(transaction);
                        }
                    };
                    case 3 : {
                        System.out.println("Showing category summary...");
                        for (Category category : Category.values()) {
                            double income = 0;
                            double expenses = 0;
                            for (Transaction transaction : ledger.getAll()) {
                                if(transaction.getCategory() == category){
                                    if(transaction.getType() == TransactionType.INCOME){
                                        income += transaction.getAmount();
                                    }
                                    else{
                                        expenses += transaction.getAmount();
                                    }
                                }
                            }
                            // Display totals after checking all transactions
                            System.out.println(
                                category + " | Income: " + income
                                        + " MAD | Expenses: " + expenses + " MAD"
                            );
                        }
                        break;
                    }
                    case 4 : {
                        
                        try{
                            System.out.println("What category would you like to put a budget towards ( FOOD / TRANSPORT / ENTERTAINMENT / SALARY / OTHER ) :");
                            Category category = Category.valueOf(scanner.nextLine().trim().toUpperCase());
                            System.out.println("What amount to put towards the category :");
                            double monthlyAmount = Double.parseDouble(scanner.nextLine().trim());
                            System.out.println("Setting a budget...");
                            Budget budget = new Budget(category,monthlyAmount);
                            budgets.add(budget);
                            System.out.println("Budget has been set");
                        }
                        catch (IllegalArgumentException e){
                            System.out.println(e.getMessage());
                        }
                        break;
                    }
                    case 5 : {
                        System.out.println("Checking budget status...");
                        LocalDate today = LocalDate.now();
                        if(budgets.getAll().isEmpty()){
                            System.out.println("No budgets set yet.");
                        }
                        for(Budget budget : budgets.getAll()){
                            double spent = 0;
                            for(Transaction transaction : ledger.getAll()){
                                if(transaction.getType() == TransactionType.EXPENSE
                                    && transaction.getCategory() == budget.getCategory()
                                    && transaction.getDate().getMonth() == today.getMonth()
                                    && transaction.getDate().getYear() == today.getYear())
                                    {
                                        spent += transaction.getAmount();
                                    }
                            }
                            double remaining = budget.getMonthlyLimit() - spent;
                            System.out.println("\nCategory :"+budget.getCategory());
                            System.out.printf("Monthly limit: %.2f MAD%n", budget.getMonthlyLimit());
                            System.out.printf("Spent: %.2f MAD%n", spent);
                            if (remaining >= 0) {
                                System.out.printf("Remaining: %.2f MAD%n", remaining);
                            } else {
                                System.out.printf("Over budget by: %.2f MAD%n", -remaining);
                            }
                        }
                        break;
                    }
                    case 6 : {
                        try {
                            transactionStorage.save(ledger.getAll(), "transactions.csv");
                            budgetStorage.save(budgets.getAll(), "budgets.csv");
                            System.out.println("Transactions and budgets saved. Goodbye!");
                            running = false;
                        } catch (IOException e) {
                            System.out.println("Could not complete saving: " + e.getMessage());
                        }
                        break;
                    }
                    default : System.out.println(
                        "Please choose a number between 1 and 6."
                    );
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }

        scanner.close();
    }
}