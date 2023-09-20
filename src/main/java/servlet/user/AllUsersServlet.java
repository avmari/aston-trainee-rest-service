package servlet.user;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.impl.UserService;
import servlet.dto.OutgoingUserDto;
import servlet.mapper.UserDtoMapper;
import util.ServletUtil;

import java.util.List;

@WebServlet("/users")
public class AllUsersServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();
    private final UserDtoMapper userDtoMapper = UserDtoMapper.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<OutgoingUserDto> users = userService.findAll().stream().map(userDtoMapper::toDto).toList();
        String usersJson = new Gson().toJson(users);

        ServletUtil.writeJsonToResponse(usersJson, resp);
    }
}
