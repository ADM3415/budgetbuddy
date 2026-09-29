package com.adam.budgetbuddy;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LedgerTest {

    @Test
    void addingIncomeIncreasesBalance() throws InvalidTransactionException,InsufficientFundsException {
        // Arrange: prepare an empty ledger and an income transaction.
        Ledger ledger = new Ledger();

        Transaction income = new Transaction(
            LocalDate.of(2026, 9, 25),
            TransactionType.INCOME,
            Category.FOOD,
            100.0,
            "Food refund"
        );

        // Act: perform the operation we want to test.
        ledger.addTransaction(income);

        // Assert: check that the result matches our expectations.
        assertEquals(100.0, ledger.getBalance(), 0.001);
        assertEquals(1, ledger.getAll().size());
    }
    @Test
    void zeroAmountThrowsInvalidTransactionException() {
        assertThrows(InvalidTransactionException.class, () -> {
            new Transaction(
                LocalDate.of(2026, 9, 25),
                TransactionType.INCOME,
                Category.FOOD,
                0.0,
                "Invalid zero amount"
            );
        });
    }
    @Test
    void negativeAmountThrowsInvalidTransactionException() {
        assertThrows(InvalidTransactionException.class, () -> {
            new Transaction(
                LocalDate.of(2026, 9, 25),
                TransactionType.EXPENSE,
                Category.FOOD,
                -10.0,
                "Invalid negative amount"
            );
        });
    }
    @Test
        void expenseExceedingBalanceIsRejected()
                throws InvalidTransactionException, InsufficientFundsException {

            // Arrange: start with a balance of 100.
            Ledger ledger = new Ledger();

            ledger.addTransaction(new Transaction(
                LocalDate.of(2026, 9, 25),
                TransactionType.INCOME,
                Category.FOOD,
                100.0,
                "Food refund"
            ));

            Transaction expense = new Transaction(
                LocalDate.of(2026, 9, 25),
                TransactionType.EXPENSE,
                Category.FOOD,
                150.0,
                "Groceries"
            );

            // Act and assert: attempting the expense must throw.
            assertThrows(
                InsufficientFundsException.class,
                () -> ledger.addTransaction(expense)
            );

            // The rejected expense must not change the ledger.
            assertEquals(100.0, ledger.getBalance(), 0.001);
            assertEquals(1, ledger.getAll().size());
        }
}

