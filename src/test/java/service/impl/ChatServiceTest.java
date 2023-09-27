package service.impl;

import entity.Chat;
import entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.impl.ChatRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @InjectMocks
    private ChatService chatService;

    @Test
    void save() {
        Chat expectedResult = getChat("test_chat");
        when(chatRepository.save(any(Chat.class))).thenReturn(expectedResult);

        Chat actualResult = chatService.save(expectedResult);

        assertEquals(expectedResult, actualResult);
        verify(chatRepository, times(1)).save(expectedResult);
    }

    @Test
    void findById() {
        Chat expectedResult = getChat("test_chat");
        UUID id = UUID.randomUUID();
        when(chatRepository.findById(any(UUID.class))).thenReturn(Optional.of(expectedResult));

        Optional<Chat> actualResult = chatService.findById(id);

        assertTrue(actualResult.isPresent());
        assertEquals(expectedResult, actualResult.get());
        verify(chatRepository, times(1)).findById(any(UUID.class));
    }

    @Test
    void findAll() {
        List<Chat> expectedResult = List.of(getChat("test_chat1"), getChat("test_chat2"),
                getChat("test_chat3"));
        when(chatRepository.findAll()).thenReturn(expectedResult);

        List<Chat> actualResult = chatService.findAll();

        assertEquals(expectedResult, actualResult);
        verify(chatRepository, times(1)).findAll();
    }

    @Test
    void deleteById() {
        when(chatRepository.deleteById(any(UUID.class))).thenReturn(true);

        boolean actualResult = chatService.deleteById(UUID.randomUUID());

        assertTrue(actualResult);
    }

    @Test
    void update() {
        chatService.update(getChat("test_chat"));

        verify(chatRepository, times(1)).update(any(Chat.class));
    }

    @Test
    void getChatUsersById() {
        List<User> expectedResult = List.of(getUser("test_user1"), getUser("test_user2"),
                getUser("test_user3"));
        when(chatRepository.getChatUsersById(any(UUID.class))).thenReturn(expectedResult);

        List<User> actualResult = chatService.getChatUsersById(UUID.randomUUID());

        assertEquals(expectedResult, actualResult);
        verify(chatRepository, times(1)).getChatUsersById(any(UUID.class));
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