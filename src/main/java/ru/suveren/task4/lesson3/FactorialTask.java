package ru.suveren.task4.lesson3;

import java.util.concurrent.RecursiveTask;

public class FactorialTask extends RecursiveTask<Integer> {

    private static final int THRESHOLD = 2;
    private int start;
    private int end;

    FactorialTask(int n) {
        this.start = 1;
        this.end = n;
    }

    public FactorialTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start <= THRESHOLD) {
            return computeFactorial();
        } else {
            int mid = (start + end) / 2;
            FactorialTask leftTask = new FactorialTask(start, mid);
            FactorialTask rightTask = new FactorialTask(mid + 1, end);

            leftTask.fork();
            Integer rightResult = rightTask.compute();
            Integer leftResult = leftTask.join();

            return rightResult * leftResult;
        }
    }

    private int computeFactorial() {
        int result = 1;
        for (int i = start; i <= end; i++) {
            result *= i;
        }
        return result;
    }
}

