package ru.suveren.task3.lesson1;

public class BlockingQueue {

    Runnable[] runnableArray = new Runnable[10];
    int index = 0;

    public synchronized void enqueue(Runnable runnable) throws InterruptedException {
        while (index == 10) {
            wait();
        }
        runnableArray[index++] = runnable;
        notifyAll();
    }

    public synchronized Runnable dequeue() throws InterruptedException {
        while (index == 0) {
            wait();
        }
        notifyAll();
        return runnableArray[index--];
    }

    public synchronized int size() {
        return runnableArray.length;
    }
}
