package ru.suveren.task3.lesson3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BankAccount {
    int balance;

    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();

    public BankAccount(int balance) {
        if (balance > 0) {
            this.balance = balance;
        } else {
            throw new RuntimeException("сумма сумма должна быть больше 0");
        }
    }

    public void deposit(int sum) {
        try {
            lock1.lock();
            balance += sum;
        } finally {
            lock1.unlock();
        }
    }

    public void withdraw(int sum) {
        try {
            lock2.lock();
            if (balance - sum >= 0) {
                balance -= sum;
            } else {
                throw new RuntimeException("некорректная сумма");
            }
        } finally {
            lock2.unlock();
        }
    }

    public int getBalance() {
        return balance;
    }
}
