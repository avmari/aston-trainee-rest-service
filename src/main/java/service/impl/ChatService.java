package service.impl;

import entity.Chat;
import repository.impl.ChatRepository;
import service.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ChatService implements Service<Chat> {

    private static final ChatRepository chatRepository = ChatRepository.getInstance();
    private static final ChatService INSTANCE = new ChatService();

    private ChatService() {}

    public static ChatService getInstance() {
        return INSTANCE;
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

    }
}
