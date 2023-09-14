package repository.impl;

import db.ConnectionManager;
import entity.Payment;
import repository.CrudRepository;
import repository.mapper.PaymentResultSetMapper;
import repository.mapper.ResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PaymentRepository implements CrudRepository<UUID, Payment> {

    private static final PaymentRepository INSTANCE = new PaymentRepository();
    private static final ResultSetMapper resultSetMapper = PaymentResultSetMapper.getInstance();

    private PaymentRepository() {}

    public static PaymentRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Payment> findById(UUID id) {
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT FROM payment " +
                    "WHERE id=?");
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            Payment payment = null;
            if (resultSet.next())
                payment = (Payment) resultSetMapper.map(resultSet);
            return Optional.ofNullable(payment);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(UUID id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM payment " +
                     "WHERE id=?")) {
            preparedStatement.setObject(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Payment> findAll() {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM payment ")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Payment> payments = new ArrayList<>();
            while (resultSet.next()) {
                payments.add((Payment) resultSetMapper.map(resultSet));
            }
            return payments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Payment save(Payment payment) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO payment " +
                     "(amount, user_id) " +
                     "VALUES(?,?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, payment.getAmount());
            preparedStatement.setObject(2, payment.getUser().getId());
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            payment.setId((UUID) generatedKeys.getObject("id"));
            return payment;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
