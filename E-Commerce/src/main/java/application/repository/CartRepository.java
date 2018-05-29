package application.repository;

import application.entity.Cart;
import application.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CartRepository extends CrudRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}
