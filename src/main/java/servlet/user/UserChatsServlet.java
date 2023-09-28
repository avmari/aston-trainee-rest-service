package servlet.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.impl.UserRepository;
import service.impl.UserService;
import servlet.dto.OutgoingChatDto;
import servlet.mapper.ChatDtoMapper;
import util.ServletUtil;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/userChats")
public class UserChatsServlet extends HttpServlet {

    private final UserService userService;
    private final ChatDtoMapper chatDtoMapper;
    private final ServletUtil servletUtil;
    private final Gson gson;

    public UserChatsServlet(UserService userService, ChatDtoMapper chatDtoMapper,
                           ServletUtil servletUtil, Gson gson) {
        this.userService = userService;
        this.chatDtoMapper = chatDtoMapper;
        this.servletUtil = servletUtil;
        this.gson = gson;
    }

    public UserChatsServlet() {
        this.userService = new UserService(UserRepository.getInstance());
        this.chatDtoMapper = ChatDtoMapper.getInstance();
        this.servletUtil = new ServletUtil();
        this.gson = new Gson();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        UUID id = UUID.fromString(req.getParameter("id"));
        List<OutgoingChatDto> chats = userService.getUserChatsById(id).stream().map(chatDtoMapper::toDto).toList();
        String chatsJson = gson.toJson(chats);

        servletUtil.writeJsonToResponse(chatsJson, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonObject jsonData = gson.fromJson(req.getReader(), JsonObject.class);

        UUID userId = UUID.fromString(jsonData.get("user").getAsJsonObject().get("id").getAsString());
        UUID chatId = UUID.fromString(jsonData.get("chat").getAsJsonObject().get("id").getAsString());

        userService.saveUserChat(userId, chatId);
    }

}
