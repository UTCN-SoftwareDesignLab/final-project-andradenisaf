package application.service;

import application.entity.Order;
import application.entity.OrderItem;
import application.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemService implements IOrderItemService {

    private OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public List<OrderItem> findAll() {
        ArrayList<OrderItem> orderItems = (ArrayList<OrderItem>) orderItemRepository.findAll();
        return orderItems;
    }

    @Override
    public List<OrderItem> findByOrder(Order order) {
        ArrayList<OrderItem> orderItems = (ArrayList<OrderItem>) orderItemRepository.findByOrder(order);
        return orderItems;
    }

    @Override
    public OrderItem create(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItem update(OrderItem orderItem) {
        if (!orderItemRepository.existsById(orderItem.getId())) {
            return null;
        }
        return orderItemRepository.save(orderItem);
    }

    @Override
    public boolean delete(OrderItem orderItem) {
        if (!orderItemRepository.existsById(orderItem.getId())) {
            return false;
        }
        orderItemRepository.delete(orderItem);
        return true;
    }
}
