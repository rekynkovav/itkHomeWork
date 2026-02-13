package ru.suveren.task3.lesson2;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ComplexTaskExecutor {
    CyclicBarrier cyclicBarrier;
    ExecutorService executorService;
    ComplexTask complexTask;

    public void executeTasks(int numberOfTasks) {
        if (numberOfTasks == 0) {
            numberOfTasks = 4;
        }
        complexTask = new ComplexTask();
        executorService = Executors.newFixedThreadPool(numberOfTasks);
        cyclicBarrier = new CyclicBarrier(numberOfTasks, () -> {
            System.out.println(ComplexTask.result);
        });

        for (int i = 0; i < numberOfTasks; i++) {
            executorService.submit(() -> {
                try {
                    complexTask.execute();
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        executorService.shutdown();
    }
}
