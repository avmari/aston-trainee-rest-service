package repository.mapper;

import entity.Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetMapper {
    Entity map(ResultSet resultSet) throws SQLException;
}
