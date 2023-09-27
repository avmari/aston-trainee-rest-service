package servlet.mapper;

import entity.User;
import org.junit.jupiter.api.Test;
import servlet.dto.IncomingUserDto;
import servlet.dto.OutgoingUserDto;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserDtoMapperTest {

    private final UserDtoMapper userDtoMapper = UserDtoMapper.getInstance();

    @Test
    void toDto() {
        User user = getUser();
        OutgoingUserDto expectedResult = new OutgoingUserDto(user.getUsername(), user.getFirstName(), user.getLastName());

        OutgoingUserDto actualResult = userDtoMapper.toDto(user);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void toEntity() {
        IncomingUserDto userDto = getUserDto();
        User expectedResult = new User();
        expectedResult.setId(userDto.getId());
        expectedResult.setUsername(userDto.getUsername());
        expectedResult.setFirstName(userDto.getFirstName());
        expectedResult.setLastName(userDto.getLastName());

        User actualResult = userDtoMapper.toEntity(userDto);

        assertEquals(expectedResult, actualResult);
    }

    private User getUser() {
        User user = new User();
        user.setUsername("test_username");
        user.setFirstName("test_first_name");
        user.setLastName("test_last_name");
        return user;
    }

    private IncomingUserDto getUserDto() {
        IncomingUserDto userDto = new IncomingUserDto();
        userDto.setId(UUID.randomUUID());
        userDto.setUsername("test_username");
        userDto.setFirstName("test_first_name");
        userDto.setLastName("test_last_name");
        return userDto;
    }
}