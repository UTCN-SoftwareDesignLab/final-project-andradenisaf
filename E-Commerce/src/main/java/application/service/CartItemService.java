package application.service;

import application.entity.Cart;
import application.entity.CartItem;
import application.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemService implements ICartItemService {

    private CartItemRepository cartItemRepository;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public List<CartItem> findAll() {
        ArrayList<CartItem> cartItems = (ArrayList<CartItem>) cartItemRepository.findAll();
        return cartItems;
    }

    @Override
    public CartItem findById(Long id) {
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(id);
        if (optionalCartItem.isPresent())
            return optionalCartItem.get();
        return null;
    }

    @Override
    public List<CartItem> findByCart(Cart cart) {
        ArrayList<CartItem> cartItems = (ArrayList<CartItem>) cartItemRepository.findByCart(cart);
        return cartItems;
    }

    @Override
    public CartItem create(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem update(CartItem cartItem) {
        if (!cartItemRepository.existsById(cartItem.getId())) {
            return null;
        }
        return cartItemRepository.save(cartItem);
    }

    @Override
    public boolean delete(CartItem cartItem) {
        if (!cartItemRepository.existsById(cartItem.getId())) {
            return false;
        }
        cartItemRepository.delete(cartItem);
        return true;
    }
}
