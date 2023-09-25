package repository.mapper;

import entity.Payment;
import entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PaymentResultSetMapper implements ResultSetMapper {

    private static final PaymentResultSetMapper INSTANCE = new PaymentResultSetMapper();

    private PaymentResultSetMapper() {}

    public static PaymentResultSetMapper getInstance() {
        return INSTANCE;
    }
    @Override
    public Payment map(ResultSet resultSet) throws SQLException {
        Payment payment = new Payment();
        payment.setId((UUID) resultSet.getObject("id"));
        payment.setAmount(resultSet.getInt("amount"));

        User user = new User();
        user.setId(UUID.fromString(resultSet.getString("user_id")));
//        user.setUsername(resultSet.getString("username"));
//        user.setFirstName(resultSet.getString("first_name"));
//        user.setLastName(resultSet.getString("last_name"));
        payment.setUser(user);

        return payment;
    }
}
