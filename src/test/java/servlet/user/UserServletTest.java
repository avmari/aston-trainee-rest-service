package servlet.user;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.impl.UserService;
import servlet.dto.IncomingUserDto;
import servlet.dto.OutgoingUserDto;
import servlet.mapper.UserDtoMapper;
import util.ServletUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServletTest {

    @Mock
    private UserService userService;
    @Mock
    private UserDtoMapper userDtoMapper;
    @Mock
    private ServletUtil servletUtil;
    @Mock
    private Gson gson;
    @InjectMocks
    private UserServlet userServlet;

    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private HttpServletResponse httpServletResponse;

    @Test
    void doGet() {
        User user = getUser("test_user");

        when(httpServletRequest.getParameter("id")).thenReturn(user.getId().toString());
        when(userService.findById(any(UUID.class))).thenReturn(Optional.of(user));

        OutgoingUserDto userDto = new OutgoingUserDto(user.getUsername(), user.getFirstName(), user.getLastName());
        when(userDtoMapper.toDto(user)).thenReturn(userDto);
        when(gson.toJson(any(OutgoingUserDto.class))).thenReturn(new Gson().toJson(userDto));

        userServlet.doGet(httpServletRequest, httpServletResponse);

        verify(httpServletRequest, times(1)).getParameter("id");
        verify(userService, times(1)).findById(any(UUID.class));
        verify(userDtoMapper, times(1)).toDto(any(User.class));
        verify(gson, times(1)).toJson(any(OutgoingUserDto.class));
        verify(servletUtil, times(1)).writeJsonToResponse(any(String.class), any(HttpServletResponse.class));
    }

    @ParameterizedTest
    @MethodSource("getUserAttributesToSave")
    void doPost(String firstName, String lastName) throws IOException {
        BufferedReader reader = mock(BufferedReader.class);
        when(httpServletRequest.getReader()).thenReturn(reader);

        User user = getUser("test_user");

        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("username", user.getUsername());
        if (firstName != null) {
            user.setFirstName(firstName);
            jsonData.addProperty("firstName", firstName);
        }
        if (lastName != null) {
            user.setLastName(lastName);
            jsonData.addProperty("lastName", lastName);
        }
        when(gson.fromJson(reader, JsonObject.class)).thenReturn(jsonData);

        IncomingUserDto incomingUserDto = getUserDto(null, user);
        OutgoingUserDto outgoingUserDto = new OutgoingUserDto(user.getUsername(),
                user.getFirstName(), user.getLastName());

        when(userDtoMapper.toEntity(any(IncomingUserDto.class))).thenReturn(user);
        when(userService.save(any(User.class))).thenReturn(user);
        when(userDtoMapper.toDto(any(User.class))).thenReturn(outgoingUserDto);
        when(gson.toJson(any(OutgoingUserDto.class))).thenReturn(new Gson().toJson(user));

        ArgumentCaptor<IncomingUserDto> argumentCaptor = ArgumentCaptor.forClass(IncomingUserDto.class);

        userServlet.doPost(httpServletRequest, httpServletResponse);

        verify(httpServletRequest, times(1)).getReader();
        verify(gson, times(1)).fromJson(reader, JsonObject.class);
        verify(userService, times(1)).save(any(User.class));
        verify(userDtoMapper, times(1)).toEntity(argumentCaptor.capture());
        verify(userDtoMapper, times(1)).toDto(any(User.class));
        verify(gson, times(1)).toJson(any(OutgoingUserDto.class));
        verify(servletUtil, times(1)).writeJsonToResponse(any(String.class), any(HttpServletResponse.class));

        assertEquals(incomingUserDto, argumentCaptor.getValue());
    }

    @ParameterizedTest
    @MethodSource("getUserAttributesToUpdate")
    void doPut(String username, String firstName, String lastName) throws IOException {
        BufferedReader reader = mock(BufferedReader.class);
        when(httpServletRequest.getReader()).thenReturn(reader);

        User user = getUser(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        IncomingUserDto expectedArgument = getUserDto(user.getId(), user);

        JsonObject jsonData = new JsonObject();
        jsonData.addProperty("id", expectedArgument.getId().toString());
        if (username != null)
            jsonData.addProperty("username", expectedArgument.getUsername());
        if (firstName != null)
            jsonData.addProperty("firstName", expectedArgument.getFirstName());
        if (lastName != null)
            jsonData.addProperty("lastName", expectedArgument.getLastName());
        when(gson.fromJson(reader, JsonObject.class)).thenReturn(jsonData);
        when(userDtoMapper.toEntity(any(IncomingUserDto.class))).thenReturn(user);

        ArgumentCaptor<IncomingUserDto> argumentCaptor = ArgumentCaptor.forClass(IncomingUserDto.class);

        userServlet.doPut(httpServletRequest, httpServletResponse);

        verify(httpServletRequest, times(1)).getReader();
        verify(gson, times(1)).fromJson(reader, JsonObject.class);
        verify(userDtoMapper, times(1)).toEntity(argumentCaptor.capture());
        verify(userService, times(1)).update(any(User.class));

        assertEquals(expectedArgument, argumentCaptor.getValue());
    }

    @Test
    void doDelete() {
        when(httpServletRequest.getParameter("id")).thenReturn(UUID.randomUUID().toString());

        userServlet.doDelete(httpServletRequest, httpServletResponse);

        verify(httpServletRequest, times(1)).getParameter("id");
        verify(userService, times(1)).deleteById(any(UUID.class));
    }

    private User getUser(String username) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(username);
//        user.setFirstName("test_first_name");
//        user.setLastName("test_last_name");
        return user;
    }

    private IncomingUserDto getUserDto(UUID id, User user) {
        IncomingUserDto userDto = new IncomingUserDto();
        userDto.setId(id);
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        return userDto;
    }

    private static Stream<Arguments> getUserAttributesToUpdate() {
        return Stream.of(
                Arguments.of("test_username", "test_first_name", "test_last_name"),
                Arguments.of("test_username", "test_first_name", null),
                Arguments.of("test_username", null, "test_last_name"),
                Arguments.of(null, "test_first_name", "test_last_name"),
                Arguments.of(null, null, "test_last_name"),
                Arguments.of(null, "test_first_name", null),
                Arguments.of("test_username", null, null)
        );
    }

    private static Stream<Arguments> getUserAttributesToSave() {
        return Stream.of(
                Arguments.of("test_first_name", "test_last_name"),
                Arguments.of("test_first_name", null),
                Arguments.of(null, "test_last_name"),
                Arguments.of(null, null)
        );
    }
}