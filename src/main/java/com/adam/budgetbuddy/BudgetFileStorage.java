package com.adam.budgetbuddy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class BudgetFileStorage {

    public void save(List<Budget> budgets, String filepath)
            throws IOException {

        List<String> lines = new ArrayList<>();

        for (Budget budget : budgets) {
            lines.add(
                budget.getCategory() + "," + budget.getMonthlyLimit()
            );
        }

        Files.write(Path.of(filepath), lines);
    }

    public List<Budget> load(String filepath) throws IOException {
        List<Budget> budgets = new ArrayList<>();
        Path path = Path.of(filepath);

        // First run: there may not be a saved file yet.
        if (Files.notExists(path)) {
            return budgets;
        }

        List<String> lines = Files.readAllLines(path);

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.isBlank()) {
                continue;
            }

            try {
                String[] parts = line.split(",", -1);

                if (parts.length != 2) {
                    throw new IllegalArgumentException("Expected category,limit");
                }

                Category category = Category.valueOf(parts[0].trim());
                double limit = Double.parseDouble(parts[1].trim());

                if (!Double.isFinite(limit) || limit <= 0) {
                    throw new IllegalArgumentException("Invalid monthly limit");
                }

                budgets.add(new Budget(category, limit));

            } catch (IllegalArgumentException e) {
                throw new IOException(
                    "Invalid budget on line " + (i + 1) + ": " + e.getMessage(),
                    e
                );
            }
        }

        return budgets;
    }
}