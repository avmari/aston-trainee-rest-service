package servlet.chat;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.impl.ChatService;
import servlet.dto.ChatDto;
import servlet.mapper.ChatDtoMapper;
import util.ServletUtil;

import java.util.List;

@WebServlet("/chats")
public class AllChatsServlet extends HttpServlet {

    private final ChatService chatService = ChatService.getInstance();
    private final ChatDtoMapper chatDtoMapper = ChatDtoMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<ChatDto> chats = chatService.findAll().stream().map(chatDtoMapper::toDto).toList();
        String chatsJson = new Gson().toJson(chats);

        ServletUtil.writeJsonToResponse(chatsJson, resp);
    }
}
