package ru.suveren.task3.lesson2;

public class ComplexTask {

    public static volatile int result = 0;

    public synchronized void execute() {
        result++;
    }
}
