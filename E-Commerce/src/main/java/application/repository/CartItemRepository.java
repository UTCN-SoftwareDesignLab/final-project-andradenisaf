package application.repository;

import application.entity.Cart;
import application.entity.CartItem;
import application.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartItemRepository extends CrudRepository<CartItem, Long> {

    List<CartItem> findByCart(Cart cart);
}