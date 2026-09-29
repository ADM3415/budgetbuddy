package com.adam.budgetbuddy;

import java.util.List;

class Ledger {
    private Repository<Transaction> transactions;

    public Ledger() {
        this.transactions = new Repository<>();
    }

    public void addTransaction(Transaction t)
            throws InsufficientFundsException {

        if (t.getType() == TransactionType.EXPENSE
                && t.getAmount() > getBalance()) {
            throw new InsufficientFundsException(
                "Not enough funds for this transaction."
            );
        }

        this.transactions.add(t);
    }

    public List<Transaction> getAll() {
        return transactions.getAll();
    }

    public double getBalance() {
        double expense = 0;
        double income = 0;

        for (Transaction transaction : transactions.getAll()) {
            if (transaction.getType() == TransactionType.EXPENSE) {
                expense = expense + transaction.getAmount();
            } else {
                income = income + transaction.getAmount();
            }
        }

        return income - expense;
    }
}