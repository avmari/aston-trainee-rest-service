package service.impl;

import entity.Chat;
import entity.Payment;
import entity.User;
import repository.impl.UserRepository;
import service.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserService implements Service<User> {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteById(UUID id) {
        return userRepository.deleteById(id);
    }

    @Override
    public void update(User user) {
        userRepository.update(user);
    }

    public void saveUserChat(UUID userId, UUID chatId) {
        userRepository.saveUserChat(userId, chatId);
    }

    public List<Chat> getUserChatsById(UUID id) {
        return userRepository.getUserChatsById(id);
    }

    public List<Payment> getUserPaymentsById(UUID id) {
        return userRepository.getUserPaymentsById(id);
    }
}
