package ru.suveren.task3;

import java.util.ArrayDeque;
import java.util.LinkedList;

public class BlockingQueue {

    ArrayDeque<Runnable> runnableArrayDeque = new ArrayDeque(10);
    LinkedList

    public synchronized void enqueue(Runnable runnable) throws InterruptedException {
        if (size() == 10) {
            wait();
        } else {
            runnableArrayDeque.addLast(runnable);
        }
    }

    public synchronized void dequeue() throws InterruptedException {
        if (size() != 0) {
            runnableArrayDeque.getFirst();
        } else {
            wait();
        }
    }

    private int size() {
        return runnableArrayDeque.size();
    }
}
