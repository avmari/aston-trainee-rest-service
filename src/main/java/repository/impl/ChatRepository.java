package repository.impl;

import db.ConnectionManager;
import entity.Chat;
import entity.User;
import repository.CrudRepository;
import repository.mapper.ChatResultSetMapper;
import repository.mapper.ResultSetMapper;
import repository.mapper.UserResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ChatRepository implements CrudRepository<UUID, Chat> {

    private static final ChatRepository INSTANCE = new ChatRepository();
    private static final ResultSetMapper chatResultSetMapper = ChatResultSetMapper.getInstance();

    private static final ResultSetMapper userResultSetMapper = UserResultSetMapper.getInstance();

    private ChatRepository() {
    }

    public static ChatRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public Optional<Chat> findById(UUID id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chat " +
                     "WHERE id=?")) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            Chat chat = null;
            if (resultSet.next())
                chat = (Chat) chatResultSetMapper.map(resultSet);
            return Optional.ofNullable(chat);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(UUID id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM chat " +
                     "WHERE id=?")) {
            preparedStatement.setObject(1, id);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Chat> findAll() {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM chat")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Chat> chats = new ArrayList<>();
            while (resultSet.next()) {
                chats.add((Chat) chatResultSetMapper.map(resultSet));
            }
            return chats;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Chat save(Chat chat) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO chat " +
                     "(name) " +
                     "VALUES(?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, chat.getName());
            preparedStatement.executeUpdate();

            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                chat.setId((UUID) generatedKeys.getObject("id"));
            }
            return chat;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Chat chat) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("UPDATE chat SET " +
                     "name=? WHERE id=?")) {
            preparedStatement.setString(1, chat.getName());
            preparedStatement.setObject(2, chat.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getChatUsersById(UUID id) {
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE " +
                     "id IN " +
                     "(SELECT user_id FROM user_chat " +
                     "WHERE chat_id=?)")) {
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add((User) userResultSetMapper.map(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
