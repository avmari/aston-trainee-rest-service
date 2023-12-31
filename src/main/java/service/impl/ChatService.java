package service.impl;

import entity.Chat;
import entity.User;
import repository.impl.ChatRepository;
import service.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ChatService implements Service<Chat> {

    private final ChatRepository chatRepository;

    public ChatService(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat save(Chat chat) {
        return chatRepository.save(chat);
    }

    @Override
    public Optional<Chat> findById(UUID id) {
        return chatRepository.findById(id);
    }

    @Override
    public List<Chat> findAll() {
        return chatRepository.findAll();
    }

    @Override
    public boolean deleteById(UUID id) {
        return chatRepository.deleteById(id);
    }

    @Override
    public void update(Chat chat) {
        chatRepository.update(chat);
    }

    public List<User> getChatUsersById(UUID id) { return chatRepository.getChatUsersById(id); }
}
