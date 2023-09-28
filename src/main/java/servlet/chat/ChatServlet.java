package servlet.chat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Chat;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.impl.ChatRepository;
import service.impl.ChatService;
import servlet.dto.IncomingChatDto;
import servlet.mapper.ChatDtoMapper;
import util.ServletUtil;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {

    private final ChatService chatService;
    private final ChatDtoMapper chatDtoMapper;
    private final ServletUtil servletUtil;
    private final Gson gson;


    public ChatServlet(ChatService chatService, ChatDtoMapper chatDtoMapper, ServletUtil servletUtil,
                       Gson gson) {
        this.chatService = chatService;
        this.chatDtoMapper = chatDtoMapper;
        this.servletUtil = servletUtil;
        this.gson = gson;
    }

    public ChatServlet() {
        this.chatService = new ChatService(ChatRepository.getInstance());
        this.chatDtoMapper = ChatDtoMapper.getInstance();
        this.servletUtil = new ServletUtil();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        UUID id = UUID.fromString(req.getParameter("id"));
        Optional<Chat> chat = chatService.findById(id);

        if (chat.isPresent()) {
            String chatJson = gson.toJson(chatDtoMapper.toDto(chat.get()));
            servletUtil.writeJsonToResponse(chatJson, resp);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonObject jsonData = gson.fromJson(req.getReader(), JsonObject.class);

        IncomingChatDto chatDto = new IncomingChatDto();
        chatDto.setName(jsonData.get("name").getAsString());

        Chat chat = chatService.save(chatDtoMapper.toEntity(chatDto));

        servletUtil.writeJsonToResponse(gson.toJson(chatDtoMapper.toDto(chat)), resp);
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonObject jsonData = gson.fromJson(req.getReader(), JsonObject.class);

        IncomingChatDto chatDto = new IncomingChatDto();
        chatDto.setId(UUID.fromString(jsonData.get("id").getAsString()));
        chatDto.setName(jsonData.get("name").getAsString());

        chatService.update(chatDtoMapper.toEntity(chatDto));
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        UUID id = UUID.fromString(req.getParameter("id"));
        chatService.deleteById(id);
    }
}
