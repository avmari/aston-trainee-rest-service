package repository.impl;

import db.ConnectionManager;
import entity.Chat;
import entity.Payment;
import entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.AbstractTest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserRepositoryTest extends AbstractTest {

    private final UserRepository userRepository = UserRepository.getInstance();
    private final ChatRepository chatRepository = ChatRepository.getInstance();
    private final PaymentRepository paymentRepository = PaymentRepository.getInstance();

    @BeforeEach
    void clean() {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users; " +
                     "DELETE FROM chat")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findById() {
        User expectedResult = userRepository.save(getUser("test_user1"));

        Optional<User> actualResult = userRepository.findById(expectedResult.getId());

        assertTrue(actualResult.isPresent());
        assertEquals(expectedResult, actualResult.get());
    }

    @Test
    void save() {
        User user = getUser("test_user1");

        User actualResult = userRepository.save(user);

        assertNotNull(actualResult.getId());
    }

    @Test
    void findAll() {
        User user1 = userRepository.save(getUser("test_user1"));
        User user2 = userRepository.save(getUser("test_user2"));
        User user3 = userRepository.save(getUser("test_user3"));

        List<User> actualResults = userRepository.findAll();

        assertEquals(3, actualResults.size());

        List<UUID> actualResultIds = actualResults.stream().map(User::getId).toList();
        assertTrue(actualResultIds.containsAll(List.of(user1.getId(), user2.getId(), user3.getId())));
    }

    @Test
    void deleteById() {
        User user = userRepository.save(getUser("test_user1"));

        boolean actualResult = userRepository.deleteById(user.getId());

        assertTrue(actualResult);
    }

    @Test
    void deleteByIdIfNotExists() {
        User user = userRepository.save(getUser("test_user1"));

        boolean actualResult = userRepository.deleteById(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        assertFalse(actualResult);
    }

    @ParameterizedTest
    @MethodSource("getUserAttributesToUpdate")
    void update(String username, String firstName, String lastName) {
        User user = userRepository.save(getUser("test_user1"));
        if (username != null)
            user.setUsername(username);
        if (firstName != null)
            user.setFirstName(firstName);
        if (lastName != null)
            user.setLastName(lastName);

        userRepository.update(user);

        User actualResult = userRepository.findById(user.getId()).get();
        assertEquals(user, actualResult);
    }


    @Test
    void saveUserChat() {
        User user = userRepository.save(getUser("test_user1"));
        Chat chat = chatRepository.save(getChat("test_chat1"));

        userRepository.saveUserChat(user.getId(), chat.getId());

        List<Chat> actualResults = userRepository.getUserChatsById(user.getId());

        assertEquals(1, actualResults.size());
        assertEquals(chat.getId(), actualResults.get(0).getId());
    }

    @Test
    void getUserChatsById() {
        User user = userRepository.save(getUser("test_user1"));
        Chat chat1 = chatRepository.save(getChat("test_chat1"));
        Chat chat2 = chatRepository.save(getChat("test_chat2"));

        User anotherUser = userRepository.save(getUser("test_user2"));
        Chat chat3 = chatRepository.save(getChat("test_chat3"));

        userRepository.saveUserChat(user.getId(), chat1.getId());
        userRepository.saveUserChat(user.getId(), chat2.getId());
        userRepository.saveUserChat(anotherUser.getId(), chat3.getId());

        List<Chat> actualResults = userRepository.getUserChatsById(user.getId());

        assertEquals(2, actualResults.size());

        List<UUID> actualResultIds = actualResults.stream().map(Chat::getId).toList();
        assertTrue(actualResultIds.containsAll(List.of(chat1.getId(), chat2.getId())));
    }

    @Test
    void getUserPaymentsById() {
        User user = userRepository.save(getUser("test_user1"));
        Payment payment1 = paymentRepository.save(getPayment(user.getId()));
        Payment payment2 = paymentRepository.save(getPayment(user.getId()));

        User anotherUser = userRepository.save(getUser("test_user2"));
        Payment payment3 = paymentRepository.save(getPayment(anotherUser.getId()));

        List<Payment> actualResults = userRepository.getUserPaymentsById(user.getId());

        assertEquals(2, actualResults.size());

        List<UUID> actualResultIds = actualResults.stream().map(Payment::getId).toList();
        assertTrue(actualResultIds.containsAll(List.of(payment1.getId(), payment2.getId())));
    }

    private User getUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName("test_first_name");
        user.setLastName("test_last_name");
        return user;
    }

    private Chat getChat(String name) {
        Chat chat = new Chat();
        chat.setName(name);
        return chat;
    }

    private Payment getPayment(UUID userId) {
        Payment payment = new Payment();
        payment.setAmount(1000);

        User user = new User();
        user.setId(userId);
        payment.setUser(user);

        return payment;
    }

    private static Stream<Arguments> getUserAttributesToUpdate() {
        return Stream.of(
                Arguments.of("new_test_username", "new_test_first_name", "new_test_last_name"),
                Arguments.of("new_test_username", null, null),
                Arguments.of(null, "new_test_first_name", "new_test_last_name"),
                Arguments.of(null, null, "new_test_last_name"),
                Arguments.of(null, "new_test_first_name", null),
                Arguments.of("new_test_username", "new_test_first_name", null),
                Arguments.of("new_test_username", null, "new_test_last_name"),
                Arguments.of(null, "new_test_first_name", null)
        );
    }

}
