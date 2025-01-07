package homework;

import com.google.gson.Gson;
import homework.domain.Customer;
import homework.domain.Order;
import homework.domain.Item;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class RestaurantOrders {
    private List<Order> orders;

    private RestaurantOrders(String fileName) {
        var filePath = Path.of("data", fileName);
        Gson gson = new Gson();
        try {
            orders = List.of(gson.fromJson(Files.readString(filePath), Order[].class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RestaurantOrders read(String fileName) {
        var ro = new RestaurantOrders(fileName);
        ro.getOrders().forEach(Order::calculateTotal);
        return ro;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void printOrders() {
        orders.forEach(order -> System.out.printf(
                "Customer: %s, Total: %.2f, Delivery: %s, Items: %d%n",
                order.getCustomer().getFullName(),
                order.getTotal(),
                order.isHomeDelivery() ? "Yes" : "No",
                order.getItems().size()
        ));
    }

    public List<Order> getTopNOrdersByTotal(int n) {
        return orders.stream()
                .sorted((o1, o2) -> Double.compare(o2.getTotal(), o1.getTotal()))
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<Order> getBottomNOrdersByTotal(int n) {
        return orders.stream()
                .sorted(Comparator.comparingDouble(Order::getTotal))
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<Order> getHomeDeliveryOrders() {
        return orders.stream()
                .filter(Order::isHomeDelivery)
                .collect(Collectors.toList());
    }

    public Map<String, Order> getHomeDeliveryExtremes() {
        Order maxOrder = getHomeDeliveryOrders().stream()
                .max(Comparator.comparingDouble(Order::getTotal))
                .orElse(null);
        Order minOrder = getHomeDeliveryOrders().stream()
                .min(Comparator.comparingDouble(Order::getTotal))
                .orElse(null);

        Map<String, Order> result = new HashMap<>();
        result.put("MAX", maxOrder);
        result.put("MIN", minOrder);
        return result;
    }

    public List<Order> getOrdersByTotalRange(double minOrderTotal, double maxOrderTotal) {
        return orders.stream()
                .filter(order -> order.getTotal() >= minOrderTotal && order.getTotal() <= maxOrderTotal)
                .collect(Collectors.toList());
    }

    public double calculateTotalRevenue() {
        return orders.stream()
                .mapToDouble(Order::getTotal)
                .sum();
    }

    public List<String> getUniqueEmailsSorted() {
        Set<String> uniqueEmails = new TreeSet<>();
        orders.forEach(order -> uniqueEmails.add(order.getCustomer().getEmail()));
        return new ArrayList<>(uniqueEmails);
    }

    public Map<String, List<Order>> getOrdersByCustomerName() {
        return orders.stream()
                .collect(Collectors.groupingBy(order -> order.getCustomer().getFullName()));
    }

    public Map<String, Double> getTotalsByCustomerName() {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getCustomer().getFullName(),
                        Collectors.summingDouble(Order::getTotal)
                ));
    }

    public Customer getCustomerWithMaxOrdersTotal() {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getCustomer().getFullName(),
                        Collectors.summingDouble(Order::getTotal)))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> orders.stream()
                        .filter(order -> order.getCustomer().getFullName().equals(entry.getKey()))
                        .findFirst()
                        .map(Order::getCustomer)
                        .orElse(null))
                .orElse(null);
    }

    public Customer getCustomerWithMinOrdersTotal() {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getCustomer().getFullName(),
                        Collectors.summingDouble(Order::getTotal)))
                .entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(entry -> orders.stream()
                        .filter(order -> order.getCustomer().getFullName().equals(entry.getKey()))
                        .findFirst()
                        .map(Order::getCustomer)
                        .orElse(null))
                .orElse(null);
    }

    public Map<String, Integer> getItemsSoldQuantities() {
        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .collect(Collectors.groupingBy(
                        Item::getName,
                        Collectors.summingInt(Item::getAmount)
                ));
    }

    public List<String> getEmailsByOrderedItem(String itemName) {
        return orders.stream()
                .filter(order -> order.getItems().stream()
                        .anyMatch(item -> item.getName().equals(itemName)))
                .map(order -> order.getCustomer().getEmail())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public Set<String> getAllItemNames() {
        return orders.stream()
                .flatMap(order -> order.getItems().stream())
                .map(Item::getName)
                .collect(Collectors.toSet());
    }
}