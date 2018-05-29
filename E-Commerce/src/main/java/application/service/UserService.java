package application.service;

import application.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import application.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private static UserService instance = null;

    private UserRepository userRepository;

    public static UserService getInstance() {
        return instance;
    }

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        instance = this;
    }

    @Override
    public List<User> findAll() {
        Iterable<User> iterable = userRepository.findAll();
        ArrayList<User> users = new ArrayList<>();
        iterable.forEach(users::add);
        return users;
    }

    @Override
    public User findById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user;
        try {
            user = optionalUser.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();;
            return null;
        }
        return user;
    }

    @Override
    public User findByUsername(String username) {
        System.out.println(username);
        Optional<User> optionalUser = userRepository.findByUsername(username);
        System.out.println(optionalUser);
        User user;
        try {
            user = optionalUser.get();
        } catch (NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        if (userRepository.existsById(user.getId())) {
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public boolean delete(User user) {
        if (userRepository.existsById(user.getId())) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }
}

