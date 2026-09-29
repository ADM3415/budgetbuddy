package com.adam.budgetbuddy;

class Budget {
    private Category category;
    private double monthlyLimit;

    public Budget(Category category, double monthlyLimit) {
        this.category = category;
        if(!Double.isFinite(monthlyLimit)){
            throw new IllegalArgumentException("Monthly limit has to be a finite positive number");
        }
        this.monthlyLimit = monthlyLimit;
    }

    public Category getCategory() {
        return category;
    }

    public double getMonthlyLimit() {
        return monthlyLimit;
    }
}