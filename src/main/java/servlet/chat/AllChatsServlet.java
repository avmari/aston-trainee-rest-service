package servlet.chat;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.impl.ChatRepository;
import service.impl.ChatService;
import servlet.dto.OutgoingChatDto;
import servlet.mapper.ChatDtoMapper;
import util.ServletUtil;

import java.util.List;

@WebServlet("/chats")
public class AllChatsServlet extends HttpServlet {

    private final ChatService chatService;
    private final ChatDtoMapper chatDtoMapper;
    private final ServletUtil servletUtil;

    public AllChatsServlet(ChatService chatService, ChatDtoMapper chatDtoMapper, ServletUtil servletUtil) {
        this.chatService = chatService;
        this.chatDtoMapper = chatDtoMapper;
        this.servletUtil = servletUtil;
    }

    public AllChatsServlet() {
        this.chatService = new ChatService(ChatRepository.getInstance());
        this.chatDtoMapper = ChatDtoMapper.getInstance();
        this.servletUtil = new ServletUtil();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<OutgoingChatDto> chats = chatService.findAll().stream().map(chatDtoMapper::toDto).toList();
        String chatsJson = new Gson().toJson(chats);

        servletUtil.writeJsonToResponse(chatsJson, resp);
    }
}
