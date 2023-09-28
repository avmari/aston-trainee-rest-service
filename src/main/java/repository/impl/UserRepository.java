package repository.impl;

import db.ConnectionManager;
import entity.Chat;
import entity.Payment;
import entity.User;
import repository.CrudRepository;
import repository.mapper.ChatResultSetMapper;
import repository.mapper.PaymentResultSetMapper;
import repository.mapper.UserResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepository implements CrudRepository<UUID, User> {
    private static final UserResultSetMapper userResultSetMapper = UserResultSetMapper.getInstance();
    private static final ChatResultSetMapper chatResultSetMapper = ChatResultSetMapper.getInstance();

    private static final PaymentResultSetMapper paymentResultSetMapper = PaymentResultSetMapper.getInstance();
    private static final UserRepository INSTANCE = new UserRepository();

    private UserRepository() {
    }

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<User> findById(UUID id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users " +
                     "WHERE id=?")) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            User user = null;
            if (resultSet.next())
                user = userResultSetMapper.map(resultSet);
            return Optional.ofNullable(user);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(UUID id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users " +
                     "WHERE id=?")) {
            preparedStatement.setObject(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(userResultSetMapper.map(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User save(User user) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users " +
                     "(username, first_name, last_name) " +
                     "VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId((UUID) generatedKeys.getObject("id"));
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(User user) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(getUpdateSql(user))) {
            preparedStatement.setObject(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUserChat(UUID userId, UUID chatId) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user_chat " +
                     "(user_id, chat_id) " +
                     "VALUES(?,?)")) {
            preparedStatement.setObject(1, userId);
            preparedStatement.setObject(2, chatId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Chat> getUserChatsById(UUID id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name FROM chat " +
                     "WHERE id IN " +
                     "(SELECT chat_id FROM user_chat WHERE user_id=?)")) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Chat> chats = new ArrayList<>();
            while (resultSet.next()) {
                chats.add(chatResultSetMapper.map(resultSet));
            }
            return chats;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Payment> getUserPaymentsById(UUID id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " +
                     "payment " +
                     "WHERE user_id=?")) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Payment> payments = new ArrayList<>();
            while (resultSet.next()) {
                payments.add(paymentResultSetMapper.map(resultSet));
            }
            return payments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private String getUpdateSql(User user) {
        StringBuilder sql = new StringBuilder(260);
        sql.append("UPDATE users SET");
        if (user.getUsername() != null) {
            sql.append(" username='");
            sql.append(user.getUsername());
            sql.append("'");

            if (user.getFirstName() != null) {
                sql.append(", first_name='");
                sql.append(user.getFirstName());
                sql.append("'");
            }
            if (user.getLastName() != null) {
                sql.append(", last_name='");
                sql.append(user.getLastName());
                sql.append("'");
            }
        }
        else if (user.getFirstName() != null) {
            sql.append(" first_name='");
            sql.append(user.getFirstName());
            sql.append("'");

            if (user.getLastName() != null) {
                sql.append(", last_name='");
                sql.append(user.getLastName());
                sql.append("'");
            }
        }
        else if (user.getLastName() != null) {
            sql.append(" last_name='");
            sql.append(user.getLastName());
            sql.append("'");
        }
        sql.append(" WHERE id=?");

        return sql.toString();
    }
}
