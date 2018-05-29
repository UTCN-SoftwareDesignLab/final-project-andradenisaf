package application.service;

import application.entity.Cart;
import application.entity.CartItem;
import application.entity.Product;

import java.util.List;

public interface ICartItemService {

    List<CartItem> findAll();
    CartItem findById(Long id);
    List<CartItem> findByCart(Cart cart);
    CartItem create(CartItem cartItem);
    CartItem update(CartItem cartItem);
    boolean delete(CartItem cartItem);
}
