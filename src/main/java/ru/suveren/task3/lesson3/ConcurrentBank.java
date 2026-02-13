package ru.suveren.task3.lesson3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentBank {

    Lock lock1 = new ReentrantLock();
    Lock lock2 = new ReentrantLock();
    List<BankAccount> bankAccountList = new ArrayList<>();
    static int index = 0;

    public BankAccount createAccount(int balance) {
        try {
            lock1.lock();
            bankAccountList.add(index, new BankAccount(balance));
            return bankAccountList.get(index);
        } finally {
            index++;
            lock1.unlock();
        }
    }

    public void transfer(BankAccount begin, BankAccount end, int sum) {
        lock2.lock();
        try {
            begin.withdraw(sum);
            end.deposit(sum);
        } finally {
            lock2.unlock();
        }
    }

    public int getTotalBalance() {
        int temp = 0;
        for (BankAccount bankAccount : bankAccountList) {
            temp += bankAccount.getBalance();
        }
        return temp;
    }
}
