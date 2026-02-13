package ru.suveren.task3.lesson3;

public class BankAccount {
    int balance;

    public BankAccount(int balance) {
        if (balance > 0) {
            this.balance = balance;
        } else {
            throw new RuntimeException("сумма сумма должна быть больше 0");
        }
    }

    public void deposit(int sum) {
        balance += sum;
    }

    public void withdraw(int sum) {
        if (balance - sum >= 0) {
            balance -= sum;
        } else {
            throw new RuntimeException("некорректная сумма");
        }
    }

    public int getBalance() {
        return balance;
    }
}
