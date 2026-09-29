package com.adam.budgetbuddy;

public class InvalidTransactionException extends Exception {

    public InvalidTransactionException(String message) {
        super(message);
    }
}