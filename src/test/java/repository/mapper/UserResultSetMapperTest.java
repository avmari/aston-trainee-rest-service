package repository.mapper;

import entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserResultSetMapperTest {

    private final UserResultSetMapper userResultSetMapper = UserResultSetMapper.getInstance();

    @Mock
    private ResultSet resultSet;

    @Test
    void map() throws SQLException {
        User expectedResult = new User();
        expectedResult.setId(UUID.randomUUID());
        expectedResult.setUsername("test_username");
        expectedResult.setFirstName("test_first_name");
        expectedResult.setLastName("test_last_name");

        when(resultSet.getObject("id")).thenReturn(expectedResult.getId());
        when(resultSet.getString("username")).thenReturn(expectedResult.getUsername());
        when(resultSet.getString("first_name")).thenReturn(expectedResult.getFirstName());
        when(resultSet.getString("last_name")).thenReturn(expectedResult.getLastName());

        User actualResult = userResultSetMapper.map(resultSet);

        assertEquals(expectedResult, actualResult);
    }
}
