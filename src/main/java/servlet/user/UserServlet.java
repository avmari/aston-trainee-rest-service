package servlet.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.impl.UserService;
import servlet.dto.UserDto;
import servlet.mapper.UserDtoMapper;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/user")
public class UserServlet extends HttpServlet {

    private final UserService userService = UserService.getInstance();
    private final UserDtoMapper userDtoMapper = UserDtoMapper.getInstance();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonData = new Gson().fromJson(req.getReader(), JsonObject.class);
        UserDto userDto = new UserDto(jsonData.get("username").getAsString(),
                jsonData.get("firstName").getAsString(),
                jsonData.get("lastName").getAsString());

        User user = userService.save(userDtoMapper.toUser(userDto));

        try (PrintWriter out = resp.getWriter()) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            out.print(new Gson().toJson(userDtoMapper.toDto(user)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
