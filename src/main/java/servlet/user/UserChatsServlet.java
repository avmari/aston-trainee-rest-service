package servlet.user;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.impl.UserService;
import servlet.dto.ChatDto;
import servlet.mapper.ChatDtoMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

@WebServlet("/userChats")
public class UserChatsServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();
    private final ChatDtoMapper chatDtoMapper = ChatDtoMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        UUID id = UUID.fromString(req.getParameter("id"));
        List<ChatDto> chats = userService.getUserChatsById(id).stream().map(chatDtoMapper::toDto).toList();
        String chatsJson = new Gson().toJson(chats);

        try (PrintWriter out = resp.getWriter()) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(chatsJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
