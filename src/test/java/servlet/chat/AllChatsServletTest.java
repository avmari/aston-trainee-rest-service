package servlet.chat;

import com.google.gson.Gson;
import entity.Chat;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.impl.ChatService;
import servlet.dto.OutgoingChatDto;
import servlet.mapper.ChatDtoMapper;
import util.ServletUtil;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AllChatsServletTest {

    @Mock
    private ChatService chatService;
    @Mock
    private ChatDtoMapper chatDtoMapper;
    @Mock
    private  ServletUtil servletUtil;
    @Mock
    private Gson gson;
    @InjectMocks
    private AllChatsServlet allChatsServlet;

    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;

    @Test
    void doGet() {
        Chat chat1 = getChat("test_chat1");
        Chat chat2 = getChat("test_chat2");
        when(chatService.findAll()).thenReturn(List.of(chat1, chat2));

        List<OutgoingChatDto> chatDtos = List.of(new OutgoingChatDto(chat1.getName()), new OutgoingChatDto(chat2.getName()));
        when(chatDtoMapper.toDto(chat1)).thenReturn(chatDtos.get(0));
        when(chatDtoMapper.toDto(chat2)).thenReturn(chatDtos.get(1));
        when(gson.toJson(any(List.class))).thenReturn(new Gson().toJson(chatDtos));

        ArgumentCaptor<List<OutgoingChatDto>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        allChatsServlet.doGet(httpServletRequest, httpServletResponse);

        verify(chatService, times(1)).findAll();
        verify(chatDtoMapper, times(2)).toDto(any(Chat.class));
        verify(gson, times(1)).toJson(argumentCaptor.capture());
        verify(servletUtil, times(1)).writeJsonToResponse(any(String.class), any(HttpServletResponse.class));

        assertEquals(chatDtos, argumentCaptor.getValue());
    }

    private Chat getChat(String name) {
        Chat chat = new Chat();
        chat.setId(UUID.randomUUID());
        chat.setName(name);
        return chat;
    }
}