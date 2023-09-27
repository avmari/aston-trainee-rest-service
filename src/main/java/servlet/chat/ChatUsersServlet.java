package servlet.chat;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.impl.ChatRepository;
import service.impl.ChatService;
import servlet.dto.OutgoingUserDto;
import servlet.mapper.UserDtoMapper;
import util.ServletUtil;

import java.util.List;
import java.util.UUID;

@WebServlet("/chatUsers")
public class ChatUsersServlet extends HttpServlet {

    private final ChatService chatService;
    private final UserDtoMapper userDtoMapper;
    private final ServletUtil servletUtil;


    public ChatUsersServlet(ChatService chatService, UserDtoMapper userDtoMapper, ServletUtil servletUtil) {
        this.chatService = chatService;
        this.userDtoMapper = userDtoMapper;
        this.servletUtil = servletUtil;
    }

    public ChatUsersServlet() {
        this.chatService = new ChatService(ChatRepository.getInstance());
        this.userDtoMapper = UserDtoMapper.getInstance();
        this.servletUtil = new ServletUtil();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        UUID id = UUID.fromString(req.getParameter("id"));
        List<OutgoingUserDto> users = chatService.getChatUsersById(id).stream().map(userDtoMapper::toDto).toList();
        String chatsJson = new Gson().toJson(users);

        servletUtil.writeJsonToResponse(chatsJson, resp);
    }

}
