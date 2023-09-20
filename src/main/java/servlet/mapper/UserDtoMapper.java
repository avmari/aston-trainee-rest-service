package servlet.mapper;

import entity.User;
import servlet.dto.IncomingUserDto;
import servlet.dto.OutgoingUserDto;

public class UserDtoMapper {

    private static final UserDtoMapper INSTANCE = new UserDtoMapper();

    private UserDtoMapper() {}

    public static UserDtoMapper getInstance() {
        return INSTANCE;
    }

    public OutgoingUserDto toDto(User user) {
        return new OutgoingUserDto(user.getUsername(), user.getFirstName(), user.getLastName());
    }

    public User toEntity(IncomingUserDto incomingUserDto) {
        return new User(incomingUserDto.getUsername(), incomingUserDto.getFirstName(), incomingUserDto.getLastName());
    }
}
