package servlet.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Chat;
import entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.impl.UserService;
import servlet.dto.OutgoingChatDto;
import servlet.mapper.ChatDtoMapper;
import util.ServletUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserChatsServletTest {

    @Mock
    private UserService userService;
    @Mock
    private ChatDtoMapper chatDtoMapper;
    @Mock
    private ServletUtil servletUtil;
    @Mock
    private Gson gson;
    @InjectMocks
    private UserChatsServlet userChatsServlet;

    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;

    @Test
    void doGet() {
        Chat chat1 = getChat("test_chat1");
        Chat chat2 = getChat("test_chat2");
        when(httpServletRequest.getParameter("id")).thenReturn(UUID.randomUUID().toString());
        when(userService.getUserChatsById(any(UUID.class))).thenReturn(List.of(chat1, chat2));

        List<OutgoingChatDto> chatDtos = List.of(new OutgoingChatDto(chat1.getName()),
                new OutgoingChatDto(chat2.getName()));
        when(chatDtoMapper.toDto(chat1)).thenReturn(chatDtos.get(0));
        when(chatDtoMapper.toDto(chat2)).thenReturn(chatDtos.get(1));
        when(gson.toJson(any(List.class))).thenReturn(new Gson().toJson(chatDtos));

        ArgumentCaptor<List<OutgoingChatDto>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        userChatsServlet.doGet(httpServletRequest, httpServletResponse);

        verify(userService, times(1)).getUserChatsById(any(UUID.class));
        verify(chatDtoMapper, times(2)).toDto(any(Chat.class));
        verify(gson, times(1)).toJson(argumentCaptor.capture());
        verify(servletUtil, times(1)).writeJsonToResponse(any(String.class), any(HttpServletResponse.class));

        assertEquals(chatDtos, argumentCaptor.getValue());
    }

    @Test
    void doPost() throws IOException {
        BufferedReader reader = mock(BufferedReader.class);
        when(httpServletRequest.getReader()).thenReturn(reader);

        User user = new User();
        user.setId(UUID.randomUUID());
        Chat chat = new Chat();
        chat.setId(UUID.randomUUID());

        JsonObject jsonData = new JsonObject();
        JsonObject jsonUser = new JsonObject();
        JsonObject jsonChat = new JsonObject();
        jsonUser.addProperty("id", user.getId().toString());
        jsonChat.addProperty("id", chat.getId().toString());
        jsonData.add("user", jsonUser);
        jsonData.add("chat", jsonChat);
        when(gson.fromJson(reader, JsonObject.class)).thenReturn(jsonData);

        ArgumentCaptor<UUID> argumentCaptor = ArgumentCaptor.forClass(UUID.class);

        userChatsServlet.doPost(httpServletRequest, httpServletResponse);

        verify(httpServletRequest, times(1)).getReader();
        verify(gson, times(1)).fromJson(reader, JsonObject.class);
        verify(userService, times(1)).saveUserChat(argumentCaptor.capture(), argumentCaptor.capture());

        assertEquals(List.of(user.getId(), chat.getId()), argumentCaptor.getAllValues());
    }

    private Chat getChat(String name) {
        Chat chat = new Chat();
        chat.setName(name);
        return chat;
    }
}