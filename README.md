# BudgetBuddy

A Java console application for tracking income, expenses, and monthly budgets in MAD.

## Features

- Add income and expense transactions.
- View transaction history.
- Show income and expense totals by category.
- Set monthly budgets and check remaining amounts.
- Save and reload transactions and budgets using CSV files.
- Reject invalid transactions and expenses exceeding the available balance.

## Requirements

- JDK 25
- Apache Maven

## Build and test

From the project folder:

```bash
mvn test
```

## Run

After building:

```bash
java -cp target/classes com.adam.budgetbuddy.Main
```

## Usage

Choose an option from the console menu:

1. Add transaction
2. View all transactions
3. View category summary
4. Set a budget
5. Check budget status
6. Save & exit

Choose option 6 to save before closing the application.

## Data storage

Transactions are stored in `transactions.csv`, and budgets in `budgets.csv`.
Both files are loaded on startup and saved in the working directory.

Personal CSV files are excluded from Git using `.gitignore`.

## Technologies

Java, Maven, JUnit 5.