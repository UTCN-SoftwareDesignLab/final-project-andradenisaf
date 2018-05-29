package application.service;

import application.entity.Order;
import application.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import application.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class OrderService implements IOrderService {

    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> findAll() {
        ArrayList<Order> orders = (ArrayList<Order>) orderRepository.findAll();
        return orders;
    }

    @Override
    public Order findById(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        Order order;
        try {
            order = optionalOrder.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return order;
    }

    @Override
    public List<Order> findByUser(User user) {
        ArrayList<Order> orders = (ArrayList<Order>) orderRepository.findByUser(user);
        return orders;
    }

    @Override
    public Order create(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order update(Order order) {
        if (orderRepository.existsById(order.getId())) {
            orderRepository.save(order);
        }
        return null;
    }

    @Override
    public boolean delete(Order order) {
        if (!orderRepository.existsById(order.getId())) {
            return false;
        }
        orderRepository.delete(order);
        return true;
    }
}

