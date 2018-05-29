package application.service;

import application.Constants;
import application.entity.User;
import application.repository.CartRepository;
import application.repository.OrderRepository;
import application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class BootstrapService implements IBootstrapService {

    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private CartRepository cartRepository;

    public static BootstrapService instance = null;

    @Autowired
    public BootstrapService(UserRepository userRepository, OrderRepository orderRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        instance = this;
    }

    @Override
    public void performBootstrap() {
        createUser();
        System.out.println("DONE");
    }

    private void createUser() {
        User user = new User();
        PasswordEncoder passwordEncoder = new Pbkdf2PasswordEncoder();
        String password = "pass1234";
        String encodedPassword = passwordEncoder.encode(password);

        user.setUsername("andradenisaf");
        user.setPassword(encodedPassword);
        user.setFullname("Andrada Farcas");
        user.setEmail("andradenisaf@gmail.com");
        user.setAddress("str. Sovata nr. 52, Oradea");

        user.setRole(Constants.Roles.ADMINISTRATOR);

        userRepository.save(user);
    }
}
