package ru.suveren.task4.lesson1;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamCollectorsExample {
    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("Laptop", 1200.0),
                new Order("Smartphone", 800.0),
                new Order("Laptop", 1500.0),
                new Order("Tablet", 500.0),
                new Order("Smartphone", 900.0)
        );

        Map<String, List<Order>> mapListOrder = orders.stream()
                .collect(Collectors.groupingBy(Order::getProduct));

        Map<String, Double> mapOrderSumCoast = mapListOrder.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toMap(
                        Order::getProduct,
                        Order::getCost,
                        Double::sum
                ));
        System.out.println("Для каждого продукта найдите общую стоимость всех заказов.");
        print(mapOrderSumCoast);

        LinkedHashMap<String, Double> sortedMapOrder = mapOrderSumCoast.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
        System.out.println("Отсортируйте продукты по убыванию общей стоимости");
        print(sortedMapOrder);

        System.out.println("Выберите три самых дорогих продукта.");
        orders.stream().sorted((a,b) -> a.compareTo(b))
                .limit(3)
                .forEach(order -> System.out.println(order.getProduct() + " " + order.getCost()));

        System.out.println("их общая стоимость");
        double sum3ElementMaxCostOrder = orders.stream().sorted((a, b) -> a.compareTo(b))
                .limit(3).mapToDouble(Order::getCost).sum();
        System.out.println(sum3ElementMaxCostOrder);
    }

    private static void print(Map<String, Double> map) {
        for (Map.Entry<String, Double> stringDoubleEntry : map.entrySet()) {
            System.out.println(stringDoubleEntry.getKey() + " " + stringDoubleEntry.getValue());
        }
        System.out.println("-------------------------------------------------");
    }
}
