package service.impl;

import entity.Chat;
import entity.Payment;
import entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.impl.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void save() {
        User expectedResult = getUser("test_user");
        when(userRepository.save(any(User.class))).thenReturn(expectedResult);

        User actualResult = userService.save(expectedResult);

        assertEquals(expectedResult, actualResult);
        verify(userRepository, times(1)).save(expectedResult);
    }

    @Test
    void findById() {
        User expectedResult = getUser("test_user");
        UUID id = UUID.randomUUID();
        when(userRepository.findById(any(UUID.class))).thenReturn(Optional.of(expectedResult));

        Optional<User> actualResult = userService.findById(id);

        assertTrue(actualResult.isPresent());
        assertEquals(expectedResult, actualResult.get());
        verify(userRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void findAll() {
        List<User> expectedResult = List.of(getUser("test_user1"), getUser("test_user2"),
                getUser("test_user3"));
        when(userRepository.findAll()).thenReturn(expectedResult);

        List<User> actualResult = userService.findAll();

        assertEquals(expectedResult, actualResult);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void deleteById() {
        when(userRepository.deleteById(any(UUID.class))).thenReturn(true);

        boolean actualResult = userService.deleteById(UUID.randomUUID());

        assertTrue(actualResult);
    }

    @Test
    void update() {
        userService.update(getUser("test_user"));

        verify(userRepository, times(1)).update(any(User.class));
    }

    @Test
    void saveUserChat() {
        userService.saveUserChat(UUID.randomUUID(), UUID.randomUUID());

        verify(userRepository, times(1)).saveUserChat(any(UUID.class), any(UUID.class));
    }

    @Test
    void getUserChatsById() {
        List<Chat> expectedResult = List.of(getChat("test_chat1"), getChat("test_chat2"),
                getChat("test_chat3"));
        when(userRepository.getUserChatsById(any(UUID.class))).thenReturn(expectedResult);

        List<Chat> actualResult = userService.getUserChatsById(UUID.randomUUID());

        assertEquals(expectedResult, actualResult);
        verify(userRepository, times(1)).getUserChatsById(any(UUID.class));
    }

    @Test
    void getUserPaymentsById() {
        List<Payment> expectedResult = List.of(getPayment(UUID.randomUUID()), getPayment(UUID.randomUUID()),
                getPayment(UUID.randomUUID()));

        when(userRepository.getUserPaymentsById(any(UUID.class))).thenReturn(expectedResult);

        List<Payment> actualResult = userService.getUserPaymentsById(UUID.randomUUID());

        assertEquals(expectedResult, actualResult);
        verify(userRepository, times(1)).getUserPaymentsById(any(UUID.class));

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
}