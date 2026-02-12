package ru.suveren.task3;

public class BlockingQueue {

    Runnable[] runnableArray = new Runnable[10];
    int index = 0;

    public synchronized void enqueue(Runnable runnable) throws InterruptedException {
        if (index == 10) {
            wait();
        } else {
            runnableArray[index] = runnable;
            index++;
        }
    }

    public synchronized Runnable dequeue() throws InterruptedException {
        if (index == 0) {
            wait();
        }
        return runnableArray[index--];
    }

    public int size() {
        return runnableArray.length;
    }
}
