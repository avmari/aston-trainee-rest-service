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
        payment.setUser(new User(resultSet.getString("username"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name")));
        return payment;
    }
}
