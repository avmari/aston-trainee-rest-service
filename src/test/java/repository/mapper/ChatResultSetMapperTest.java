package repository.mapper;

import entity.Chat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChatResultSetMapperTest {

    private final ChatResultSetMapper chatResultSetMapper = ChatResultSetMapper.getInstance();

    @Mock
    private ResultSet resultSet;

    @Test
    void map() throws SQLException {
        Chat expectedResult = new Chat();
        expectedResult.setId(UUID.randomUUID());
        expectedResult.setName("test_name");

        when(resultSet.getObject("id")).thenReturn(expectedResult.getId());
        when(resultSet.getString("name")).thenReturn(expectedResult.getName());

        Chat actualResult = chatResultSetMapper.map(resultSet);

        assertEquals(expectedResult, actualResult);
    }

}
