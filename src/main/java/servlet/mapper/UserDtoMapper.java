package servlet.mapper;

import entity.User;
import servlet.dto.UserDto;

public class UserDtoMapper {

    private static final UserDtoMapper INSTANCE = new UserDtoMapper();

    private UserDtoMapper() {}

    public static UserDtoMapper getInstance() {
        return INSTANCE;
    }

    public UserDto toDto(User user) {
        return new UserDto(user.getUsername(), user.getFirstName(), user.getLastName());
    }

    public User toUser(UserDto userDto) {
        return new User(userDto.username(), userDto.firstName(), userDto.lastName());
    }
}
