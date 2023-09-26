package repository.impl;

import db.ConnectionManager;
import entity.Chat;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.AbstractTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ChatRepositoryTest extends AbstractTest {

    private final ChatRepository chatRepository = ChatRepository.getInstance();
    private final UserRepository userRepository = UserRepository.getInstance();

    @BeforeEach
    void clean() {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM chat")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findById() {
        Chat expectedResult = chatRepository.save(getChat("test_chat1"));

        Optional<Chat> actualResult = chatRepository.findById(expectedResult.getId());

        assertTrue(actualResult.isPresent());
        assertEquals(expectedResult, actualResult.get());
    }

    @Test
    void save() {
        Chat chat = getChat("test_chat1");

        Chat actualResult = chatRepository.save(chat);

        assertNotNull(actualResult.getId());
    }

    @Test
    void findAll() {
        Chat chat1 = chatRepository.save(getChat("test_chat1"));
        Chat chat2 = chatRepository.save(getChat("test_chat2"));
        Chat chat3 = chatRepository.save(getChat("test_chat3"));

        List<Chat> actualResults = chatRepository.findAll();

        assertEquals(3, actualResults.size());

        List<UUID> actualResultIds = actualResults.stream().map(Chat::getId).toList();
        assertTrue(actualResultIds.containsAll(List.of(chat1.getId(), chat2.getId(), chat3.getId())));
    }

    @Test
    void deleteById() {
        Chat chat = chatRepository.save(getChat("test_chat1"));

        boolean actualResult = chatRepository.deleteById(chat.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteByIdIfNotExists() {
        Chat chat = chatRepository.save(getChat("test_chat1"));

        boolean actualResult = chatRepository.deleteById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        assertFalse(actualResult);
    }

    @Test
    void update() {
        Chat chat = chatRepository.save(getChat("test_chat1"));
        chat.setName("new_test_chat1");

        chatRepository.update(chat);

        Chat actualResult = chatRepository.findById(chat.getId()).get();
        assertEquals(chat, actualResult);
    }

    @Test
    void getChatUsersById() {
        Chat chat = chatRepository.save(getChat("test_chat1"));
        User user1 = userRepository.save(getUser("test_user1"));
        User user2 = userRepository.save(getUser("test_user2"));

        Chat anotherChat = chatRepository.save(getChat("test_chat2"));
        User user3 = userRepository.save(getUser("test_user3"));

        userRepository.saveUserChat(user1.getId(), chat.getId());
        userRepository.saveUserChat(user2.getId(), chat.getId());
        userRepository.saveUserChat(user3.getId(), anotherChat.getId());

        List<User> actualResults = chatRepository.getChatUsersById(chat.getId());

        assertEquals(2, actualResults.size());

        List<UUID> actualResultIds = actualResults.stream().map(User::getId).toList();
        assertTrue(actualResultIds.containsAll(List.of(user1.getId(), user2.getId())));
    }

    private Chat getChat(String name) {
        Chat chat = new Chat();
        chat.setName(name);
        return chat;
    }

    private User getUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName("test_first_name");
        user.setLastName("test_last_name");
        return user;
    }

}
