package repository.mapper;

import entity.Payment;
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
public class PaymentResultSetMapperTest {

    private final PaymentResultSetMapper paymentResultSetMapper = PaymentResultSetMapper.getInstance();

    @Mock
    private ResultSet resultSet;

    @Test
    void map() throws SQLException {
        Payment expectedResult = new Payment();
        expectedResult.setId(UUID.randomUUID());
        expectedResult.setAmount(1000);
        User user = new User();
        user.setId(UUID.randomUUID());
        expectedResult.setUser(user);

        when(resultSet.getObject("id")).thenReturn(expectedResult.getId());
        when(resultSet.getInt("amount")).thenReturn(expectedResult.getAmount());
        when(resultSet.getObject("user_id")).thenReturn(expectedResult.getUser().getId());

        Payment actualResult = paymentResultSetMapper.map(resultSet);

        assertEquals(expectedResult, actualResult);
    }
}
