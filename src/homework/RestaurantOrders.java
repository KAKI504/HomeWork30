package homework;

import com.google.gson.Gson;
import homework.domain.Order;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class RestaurantOrders {
    // Этот блок кода менять нельзя! НАЧАЛО!
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
    // Этот блок кода менять нельзя! КОНЕЦ!

    //----------------------------------------------------------------------
    //------   Реализация ваших методов должна быть ниже этой линии   ------
    //----------------------------------------------------------------------

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
}