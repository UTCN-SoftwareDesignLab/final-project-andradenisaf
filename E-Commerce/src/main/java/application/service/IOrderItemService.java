package application.service;

import application.entity.Order;
import application.entity.OrderItem;
import application.entity.Product;

import java.util.List;

public interface IOrderItemService {

    List<OrderItem> findAll();
    List<OrderItem> findByOrder(Order order);
    OrderItem create(OrderItem orderItem);
    OrderItem update(OrderItem orderItem);
    boolean delete(OrderItem orderItem);
}
