package repository.mapper;

import entity.Chat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ChatResultSetMapper implements ResultSetMapper {

    private static final ChatResultSetMapper INSTANCE = new ChatResultSetMapper();

    private ChatResultSetMapper() {}

    public static ChatResultSetMapper getInstance() {
        return INSTANCE;
    }
    @Override
    public Chat map(ResultSet resultSet) throws SQLException {
        Chat chat = new Chat();
        chat.setId((UUID) resultSet.getObject("id"));
        chat.setName(resultSet.getString("name"));
        return chat;
    }
}
