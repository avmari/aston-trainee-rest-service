package servlet.user;

import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.impl.UserRepository;
import service.impl.UserService;
import servlet.dto.OutgoingUserDto;
import servlet.mapper.UserDtoMapper;
import util.ServletUtil;

import java.util.List;

@WebServlet("/users")
public class AllUsersServlet extends HttpServlet {

    private final UserService userService;
    private final UserDtoMapper userDtoMapper;
    private final ServletUtil servletUtil;
    private final Gson gson;

    public AllUsersServlet(UserService userService, UserDtoMapper userDtoMapper,
                              ServletUtil servletUtil, Gson gson) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
        this.servletUtil = servletUtil;
        this.gson = gson;
    }

    public AllUsersServlet() {
        this.userService = new UserService(UserRepository.getInstance());
        this.userDtoMapper = UserDtoMapper.getInstance();
        this.servletUtil = new ServletUtil();
        this.gson = new Gson();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        List<OutgoingUserDto> users = userService.findAll().stream().map(userDtoMapper::toDto).toList();
        String usersJson = gson.toJson(users);

        servletUtil.writeJsonToResponse(usersJson, resp);
    }
}
