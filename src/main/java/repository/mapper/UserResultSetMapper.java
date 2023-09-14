package repository.mapper;

import entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserResultSetMapper implements ResultSetMapper {

    private static final UserResultSetMapper INSTANCE = new UserResultSetMapper();

    private UserResultSetMapper() {}

    public static UserResultSetMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public User map(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId((UUID)resultSet.getObject("id"));
        user.setUsername(resultSet.getString("username"));
        user.setFirstName(resultSet.getString("first_name"));
        user.setLastName(resultSet.getString("last_name"));

        return user;
    }
}
