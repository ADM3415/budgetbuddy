package com.adam.budgetbuddy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

class Repository<T> {
    private ArrayList<T> items = new ArrayList<>();

    public void add(T item) {
        items.add(item);
    }

    public boolean remove(T item) {
        if (items.indexOf(item) != -1) {
            items.remove(items.indexOf(item));
            return true;
        }

        return false;
    }

    public List<T> getAll() {
        return new ArrayList<>(items);
    }

    public List<T> find(Predicate<T> condition) {
        List<T> items_ver_condition = new ArrayList<>();

        for (T item : items) {
            if (condition.test(item)) {
                items_ver_condition.add(item);
            }
        }

        return items_ver_condition;
    }
}