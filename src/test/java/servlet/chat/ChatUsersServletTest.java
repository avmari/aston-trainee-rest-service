package servlet.chat;

import com.google.gson.Gson;
import entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.impl.ChatService;
import servlet.dto.OutgoingUserDto;
import servlet.mapper.UserDtoMapper;
import util.ServletUtil;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatUsersServletTest {

    @Mock
    private ChatService chatService;
    @Mock
    private UserDtoMapper userDtoMapper;
    @Mock
    private ServletUtil servletUtil;
    @InjectMocks
    private ChatUsersServlet chatUsersServlet;

    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;

    @Test
    void doGet() {
        User user1 = getUser("test_user1");
        User user2 = getUser("test_user2");
        when(chatService.getChatUsersById(any(UUID.class))).thenReturn(List.of(user1, user2));

        List<OutgoingUserDto> userDtos = List.of(new OutgoingUserDto(user1.getUsername(), user1.getFirstName(), user1.getLastName()),
                new OutgoingUserDto(user2.getUsername(), user2.getFirstName(), user2.getLastName()));
        when(userDtoMapper.toDto(user1)).thenReturn(userDtos.get(0));
        when(userDtoMapper.toDto(user2)).thenReturn(userDtos.get(1));
        when(httpServletRequest.getParameter("id")).thenReturn(UUID.randomUUID().toString());

        ArgumentCaptor<String> json = ArgumentCaptor.forClass(String.class);

        String expectedResult = new Gson().toJson(userDtos);

        chatUsersServlet.doGet(httpServletRequest, httpServletResponse);

        verify(httpServletRequest, times(1)).getParameter("id");
        verify(chatService, times(1)).getChatUsersById(any(UUID.class));
        verify(userDtoMapper, times(2)).toDto(any(User.class));
        verify(servletUtil, times(1)).writeJsonToResponse(json.capture(), any(HttpServletResponse.class));

        assertEquals(expectedResult, json.getValue());
    }

    private User getUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setFirstName("test_first_name");
        user.setLastName("test_last_name");
        return user;
    }
}