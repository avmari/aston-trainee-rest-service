package servlet.user;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.impl.UserService;
import servlet.dto.UserDto;
import servlet.mapper.UserDtoMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/users")
public class AllUsersServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();
    private final UserDtoMapper userDtoMapper = UserDtoMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<UserDto> users = userService.findAll().stream().map(userDtoMapper::toDto).toList();
        String usersJson = new Gson().toJson(users);

        try (PrintWriter out = resp.getWriter()) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(usersJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
