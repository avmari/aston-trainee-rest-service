package servlet.chat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import servlet.dto.IncomingChatDto;
import servlet.dto.OutgoingChatDto;
import servlet.mapper.ChatDtoMapper;
import util.ServletUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServletTest {

    @Mock
    private ChatService chatService;
    @Mock
    private ChatDtoMapper chatDtoMapper;
    @Mock
    private ServletUtil servletUtil;
    @Mock
    private Gson gson;
    @InjectMocks
    private ChatServlet chatServlet;

    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;

    @Test
    void doGet() {
        Chat chat = getChat("test_chat", UUID.randomUUID());

        when(httpServletRequest.getParameter("id")).thenReturn(chat.getId().toString());
        when(chatService.findById(any(UUID.class))).thenReturn(Optional.of(chat));

        OutgoingChatDto chatDto = new OutgoingChatDto(chat.getName());
        when(chatDtoMapper.toDto(chat)).thenReturn(chatDto);
        when(gson.toJson(any(OutgoingChatDto.class))).thenReturn(new Gson().toJson(chatDto));

        chatServlet.doGet(httpServletRequest, httpServletResponse);

        verify(httpServletRequest, times(1)).getParameter("id");
        verify(chatService, times(1)).findById(any(UUID.class));
        verify(chatDtoMapper, times(1)).toDto(any(Chat.class));
        verify(gson, times(1)).toJson(any(OutgoingChatDto.class));
        verify(servletUtil, times(1)).writeJsonToResponse(any(String.class), any(HttpServletResponse.class));
    }

    @Test
    void doPost() throws IOException {
        BufferedReader reader = mock(BufferedReader.class);
        when(httpServletRequest.getReader()).thenReturn(reader);

        Chat chat = getChat("test_chat_name", UUID.randomUUID());

        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("name", chat.getName());
        when(gson.fromJson(reader, JsonObject.class)).thenReturn(jsonData);

        IncomingChatDto incomingChatDto = getChatDto(chat.getName(), null);
        OutgoingChatDto outgoingChatDto = new OutgoingChatDto(chat.getName());

        when(chatDtoMapper.toEntity(any(IncomingChatDto.class))).thenReturn(chat);
        when(chatService.save(any(Chat.class))).thenReturn(chat);
        when(chatDtoMapper.toDto(any(Chat.class))).thenReturn(outgoingChatDto);
        when(gson.toJson(any(OutgoingChatDto.class))).thenReturn(new Gson().toJson(chat));

        ArgumentCaptor<IncomingChatDto> argumentCaptor = ArgumentCaptor.forClass(IncomingChatDto.class);

        chatServlet.doPost(httpServletRequest, httpServletResponse);

        verify(httpServletRequest, times(1)).getReader();
        verify(gson, times(1)).fromJson(reader, JsonObject.class);
        verify(chatService, times(1)).save(any(Chat.class));
        verify(chatDtoMapper, times(1)).toEntity(argumentCaptor.capture());
        verify(chatDtoMapper, times(1)).toDto(any(Chat.class));
        verify(gson, times(1)).toJson(any(OutgoingChatDto.class));
        verify(servletUtil, times(1)).writeJsonToResponse(any(String.class), any(HttpServletResponse.class));

        assertEquals(incomingChatDto, argumentCaptor.getValue());
    }

    @Test
    void doPut() throws IOException {
        BufferedReader reader = mock(BufferedReader.class);
        when(httpServletRequest.getReader()).thenReturn(reader);

        IncomingChatDto expectedArgument = getChatDto("test_chat_name", UUID.randomUUID());

        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("id", expectedArgument.getId().toString());
        jsonData.addProperty("name", expectedArgument.getName());
        when(gson.fromJson(reader, JsonObject.class)).thenReturn(jsonData);

        Chat chat = getChat(expectedArgument.getName(), expectedArgument.getId());

        when(chatDtoMapper.toEntity(any(IncomingChatDto.class))).thenReturn(chat);

        ArgumentCaptor<IncomingChatDto> argumentCaptor = ArgumentCaptor.forClass(IncomingChatDto.class);

        chatServlet.doPut(httpServletRequest, httpServletResponse);

        verify(httpServletRequest, times(1)).getReader();
        verify(gson, times(1)).fromJson(reader, JsonObject.class);
        verify(chatDtoMapper, times(1)).toEntity(argumentCaptor.capture());
        verify(chatService, times(1)).update(any(Chat.class));

        assertEquals(expectedArgument, argumentCaptor.getValue());
    }

    @Test
    void doDelete() {
        when(httpServletRequest.getParameter("id")).thenReturn(UUID.randomUUID().toString());

        chatServlet.doDelete(httpServletRequest, httpServletResponse);

        verify(httpServletRequest, times(1)).getParameter("id");
        verify(chatService, times(1)).deleteById(any(UUID.class));
    }

    private Chat getChat(String name, UUID id) {
        Chat chat = new Chat();
        chat.setId(id);
        chat.setName(name);
        return chat;
    }

    private IncomingChatDto getChatDto(String name, UUID id) {
        IncomingChatDto chatDto = new IncomingChatDto();
        chatDto.setId(id);
        chatDto.setName(name);
        return chatDto;
    }
}