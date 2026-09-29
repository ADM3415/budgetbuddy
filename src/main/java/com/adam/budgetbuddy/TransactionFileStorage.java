package com.adam.budgetbuddy;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class TransactionFileStorage {

    public void save(List<Transaction> transactions, String filepath)
            throws IOException {

        List<String> lines = new ArrayList<>();

        for (Transaction transaction : transactions) {
            lines.add(
                transaction.getDate() + "," +
                transaction.getType() + "," +
                transaction.getCategory() + "," +
                transaction.getAmount() + "," +
                transaction.getDescription()
            );
        }

        Files.write(Paths.get(filepath), lines);
    }

    public List<Transaction> load(String filepath) throws IOException {
        Path path = Paths.get(filepath);
        List<Transaction> transactions = new ArrayList<>();

        if (Files.notExists(path)) {
            return transactions;
        }

        List<String> readBack = Files.readAllLines(path);
        int lineNumber = 0;

        for (String line : readBack) {
            lineNumber++;

            try {
                String[] parts = line.split(",", 5);

                if (parts.length != 5) {
                    throw new IllegalArgumentException(
                        "Expected five fields."
                    );
                }

                LocalDate date = LocalDate.parse(parts[0].trim());
                TransactionType type =
                    TransactionType.valueOf(parts[1].trim());
                Category category =
                    Category.valueOf(parts[2].trim());
                double amount = Double.parseDouble(parts[3].trim());
                String description = parts[4];

                Transaction transaction = new Transaction(
                    date, type, category, amount, description
                );

                transactions.add(transaction);

            } catch (InvalidTransactionException
                    | java.time.format.DateTimeParseException
                    | IllegalArgumentException e) {

                System.err.println(
                    "Skipping invalid line " + lineNumber
                    + ": " + e.getMessage()
                );
            }
        }

        return transactions;
    }
}