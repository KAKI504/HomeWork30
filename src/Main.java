import homework.RestaurantOrders;
import homework.domain.Customer;
import homework.domain.Order;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        RestaurantOrders smallOrders = RestaurantOrders.read("orders_100.json");
        RestaurantOrders mediumOrders = RestaurantOrders.read("orders_1000.json");
        RestaurantOrders largeOrders = RestaurantOrders.read("orders_10_000.json");

        System.out.println("\n=== Working with 100 orders ===");
        processOrders(smallOrders);

        System.out.println("\n=== Working with 1000 orders ===");
        processOrders(mediumOrders);

        System.out.println("\n=== Working with 10000 orders ===");
        processOrders(largeOrders);
    }

    private static void processOrders(RestaurantOrders restaurantOrders) {
        System.out.println("\n=== All Orders Sample (first 5) ===");
        restaurantOrders.printOrders();

        System.out.println("\n=== Top 3 Orders by Total ===");
        List<Order> topOrders = restaurantOrders.getTopNOrdersByTotal(3);
        topOrders.forEach(order -> System.out.printf("Customer: %s, Total: %.2f%n",
                order.getCustomer().getFullName(), order.getTotal()));

        System.out.println("\n=== Bottom 3 Orders by Total ===");
        List<Order> bottomOrders = restaurantOrders.getBottomNOrdersByTotal(3);
        bottomOrders.forEach(order -> System.out.printf("Customer: %s, Total: %.2f%n",
                order.getCustomer().getFullName(), order.getTotal()));

        System.out.println("\n=== Home Delivery Orders Sample (first 5) ===");
        List<Order> homeDeliveryOrders = restaurantOrders.getHomeDeliveryOrders();
        homeDeliveryOrders.stream().limit(5).forEach(order -> System.out.printf("Customer: %s, Total: %.2f%n",
                order.getCustomer().getFullName(), order.getTotal()));

        System.out.println("\n=== Home Delivery Extremes ===");
        Map<String, Order> homeDeliveryExtremes = restaurantOrders.getHomeDeliveryExtremes();
        System.out.printf("Max Order - Customer: %s, Total: %.2f%n",
                homeDeliveryExtremes.get("MAX").getCustomer().getFullName(),
                homeDeliveryExtremes.get("MAX").getTotal());
        System.out.printf("Min Order - Customer: %s, Total: %.2f%n",
                homeDeliveryExtremes.get("MIN").getCustomer().getFullName(),
                homeDeliveryExtremes.get("MIN").getTotal());

        System.out.println("\n=== Orders in Range (50-100) Sample (first 5) ===");
        List<Order> ordersInRange = restaurantOrders.getOrdersByTotalRange(50.0, 100.0);
        ordersInRange.stream().limit(5).forEach(order -> System.out.printf("Customer: %s, Total: %.2f%n",
                order.getCustomer().getFullName(), order.getTotal()));

        System.out.println("\n=== Total Revenue ===");
        double totalRevenue = restaurantOrders.calculateTotalRevenue();
        System.out.printf("Total Revenue: %.2f%n", totalRevenue);

        System.out.println("\n=== Unique Customer Emails Sample (first 5) ===");
        List<String> uniqueEmails = restaurantOrders.getUniqueEmailsSorted();
        uniqueEmails.stream().limit(5).forEach(System.out::println);

        System.out.println("\n=== Orders by Customer Sample (first 5) ===");
        Map<String, List<Order>> ordersByCustomer = restaurantOrders.getOrdersByCustomerName();
        ordersByCustomer.entrySet().stream().limit(5).forEach((entry) -> {
            System.out.printf("%s: %d orders%n", entry.getKey(), entry.getValue().size());
        });

        System.out.println("\n=== Totals by Customer Sample (first 5) ===");
        Map<String, Double> totalsByCustomer = restaurantOrders.getTotalsByCustomerName();
        totalsByCustomer.entrySet().stream().limit(5).forEach((entry) -> {
            System.out.printf("%s: %.2f%n", entry.getKey(), entry.getValue());
        });

        System.out.println("\n=== Customer with Maximum Orders Total ===");
        Customer maxCustomer = restaurantOrders.getCustomerWithMaxOrdersTotal();
        System.out.printf("Name: %s, Email: %s%n",
                maxCustomer.getFullName(), maxCustomer.getEmail());

        System.out.println("\n=== Customer with Minimum Orders Total ===");
        Customer minCustomer = restaurantOrders.getCustomerWithMinOrdersTotal();
        System.out.printf("Name: %s, Email: %s%n",
                minCustomer.getFullName(), minCustomer.getEmail());

        System.out.println("\n=== Items Sold Quantities Sample (first 5) ===");
        Map<String, Integer> itemsSoldQuantities = restaurantOrders.getItemsSoldQuantities();
        itemsSoldQuantities.entrySet().stream().limit(5).forEach((entry) -> {
            System.out.printf("%s: %d%n", entry.getKey(), entry.getValue());
        });

        System.out.println("\n" + "=".repeat(50) + "\n");
    }
}