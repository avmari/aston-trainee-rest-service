package servlet.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.impl.UserService;
import servlet.dto.IncomingUserDto;
import servlet.mapper.UserDtoMapper;
import util.ServletUtil;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();
    private final UserDtoMapper userDtoMapper = UserDtoMapper.getInstance();

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        UUID id = UUID.fromString(req.getParameter("id"));
        Optional<User> user = userService.findById(id);

        if (user.isPresent()) {
            String userJson = new Gson().toJson(userDtoMapper.toDto(user.get()));
            ServletUtil.writeJsonToResponse(userJson, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonObject jsonData = new Gson().fromJson(req.getReader(), JsonObject.class);
        Set<String> jsonKeys = jsonData.keySet();

        IncomingUserDto userDto = new IncomingUserDto();
        userDto.setUsername(jsonData.get("username").getAsString());
        if (jsonKeys.contains("firstName"))
            userDto.setFirstName(jsonData.get("firstName").getAsString());
        if (jsonKeys.contains("lastName"))
            userDto.setFirstName(jsonData.get("lastName").getAsString());

        User user = userService.save(userDtoMapper.toEntity(userDto));

        ServletUtil.writeJsonToResponse(new Gson().toJson(userDtoMapper.toDto(user)), resp);
    }

    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonObject jsonData = new Gson().fromJson(req.getReader(), JsonObject.class);
        Set<String> jsonKeys = jsonData.keySet();

        IncomingUserDto userDto = new IncomingUserDto();
        userDto.setId(UUID.fromString(jsonData.get("id").getAsString()));
        if (jsonKeys.contains("username"))
            userDto.setUsername(jsonData.get("username").getAsString());
        if (jsonKeys.contains("firstName"))
            userDto.setFirstName(jsonData.get("firstName").getAsString());
        if (jsonKeys.contains("lastName"))
            userDto.setFirstName(jsonData.get("lastName").getAsString());

        userService.update(userDtoMapper.toEntity(userDto));
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        UUID id = UUID.fromString(req.getParameter("id"));
        Optional<User> user = userService.findById(id);

        if (user.isPresent()) {
            userService.deleteById(id);
        }
    }
}
