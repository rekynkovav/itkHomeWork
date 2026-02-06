package ru.suveren.task2;

public class Collection{

    public void filter(Object[] arrayObj, Filter filter){
        for (int i = 0; i < arrayObj.length; i++) {
            filter.apply(arrayObj[i]);
        }
    }
}
