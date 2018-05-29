package application.repository;

import application.entity.Order;
import application.entity.OrderItem;
import application.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderItemRepository extends CrudRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
}