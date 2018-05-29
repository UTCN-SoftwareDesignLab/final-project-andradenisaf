package application.service;

import application.entity.Order;
import application.entity.User;

import java.util.List;

public interface IOrderService {

    List<Order> findAll();
    Order findById(Long id);
    List<Order> findByUser(User user);
    Order create(Order order);
    Order update(Order order);
    boolean delete(Order order);
}
