package ru.suveren.task2;

import java.util.HashMap;
import java.util.Map;

public class Collection {

    public void filter(Integer[] arrayInt, FilterImpl filter) {
        for (Integer i: arrayInt) {
            filter.apply(i);
        }
    }

    public Map<String, Integer> countOfElements(String[] arrayStr) {
        Map<String, Integer> map = new HashMap<>();
        for (String str: arrayStr) {
            if (map.containsKey(str)) {
                int value = map.get(str);
                value++;
                map.put(str, value);
            } else {
                map.put(str, 1);
            }
        }
        return map;
    }
}
