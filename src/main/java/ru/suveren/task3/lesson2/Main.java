package ru.suveren.task3.lesson2;

import java.util.concurrent.BrokenBarrierException;

public class Main {
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        ComplexTaskExecutor complexTaskExecutor = new ComplexTaskExecutor();
        complexTaskExecutor.executeTasks(4);
    }
}