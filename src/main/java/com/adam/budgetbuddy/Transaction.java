package com.adam.budgetbuddy;

import java.time.LocalDate;

public class Transaction {
    private LocalDate date;
    private TransactionType type;
    private Category category;
    private double amount;
    private String description;

    public Transaction(
            LocalDate date,
            TransactionType type,
            Category category,
            double amount,
            String description
    ) throws InvalidTransactionException {

        if (!Double.isFinite(amount) || amount <= 0) {
            throw new InvalidTransactionException(
                "Transaction amount must be finite and greater than zero."
            );
        }

        this.date = date;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public TransactionType getType() {
        return type;
    }

    public Category getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return date + " | " + type + " | " + category + " | "
                + amount + " | " + description;
    }
}