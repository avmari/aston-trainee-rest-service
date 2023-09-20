package servlet.chat;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.Chat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.impl.ChatService;
import servlet.dto.ChatDto;
import servlet.mapper.ChatDtoMapper;
import util.ServletUtil;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {

    private final ChatService chatService = ChatService.getInstance();
    private final ChatDtoMapper chatDtoMapper = ChatDtoMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        UUID id = UUID.fromString(req.getParameter("id"));
        Optional<Chat> chat = chatService.findById(id);

        if (chat.isPresent()) {
            String chatJson = new Gson().toJson(chatDtoMapper.toDto(chat.get()));
            ServletUtil.writeJsonToResponse(chatJson, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonData = new Gson().fromJson(req.getReader(), JsonObject.class);
        ChatDto chatDto = new ChatDto(jsonData.get("name").getAsString());

        Chat chat = chatService.save(chatDtoMapper.toEntity(chatDto));

        ServletUtil.writeJsonToResponse(new Gson().toJson(chatDtoMapper.toDto(chat)), resp);
    }
}
