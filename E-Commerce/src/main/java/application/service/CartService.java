package application.service;

import application.entity.Cart;
import application.entity.Product;
import application.entity.User;
import application.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CartService implements ICartService {

    private CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public List<Cart> findAll() {
        ArrayList<Cart> carts = (ArrayList<Cart>) cartRepository.findAll();
        return carts;
    }

    @Override
    public Cart findByUser(User user) {
        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        Cart cart;
        try {
            cart = optionalCart.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return cart;
    }

    @Override
    public Cart create(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public Cart update(Cart cart) {
        if (!cartRepository.existsById(cart.getId())) {
            return null;
        }
        return cartRepository.save(cart);
    }

    @Override
    public boolean delete(Cart cart) {
        if (!cartRepository.existsById(cart.getId())) {
            return false;
        }
        cartRepository.delete(cart);
        return true;
    }
}
