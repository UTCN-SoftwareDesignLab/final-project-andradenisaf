package application.service;

import application.entity.Cart;
import application.entity.User;

import java.util.List;

public interface ICartService {

    List<Cart> findAll();
    Cart findByUser(User user);
    Cart create(Cart cart);
    Cart update(Cart cart);
    boolean delete(Cart cart);
}
