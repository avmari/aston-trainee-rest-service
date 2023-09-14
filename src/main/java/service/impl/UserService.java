package service.impl;

import entity.User;
import repository.impl.UserRepository;
import service.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService implements Service<User> {

    private static final UserRepository userRepository = UserRepository.getInstance();
    private static final UserService INSTANCE = new UserService();

    private UserService() {}

    public static UserService getInstance() {
        return INSTANCE;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean deleteById(UUID id) {
        return userRepository.deleteById(id);
    }
}
