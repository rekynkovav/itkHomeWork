package ru.suveren.task2;

public class FilterImpl implements Filter<Integer> {
    @Override
    public Integer apply(Integer o) {
        return o + 1;
    }
}
