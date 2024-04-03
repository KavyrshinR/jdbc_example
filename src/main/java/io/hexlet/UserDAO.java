package io.hexlet;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(User user) throws SQLException {
        if (user.getId() == null) {
            var saveUserSql = "INSERT INTO users (username, phone) VALUES (?, ?);";
            try (var preparedStatement = connection.prepareStatement(saveUserSql, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPhone());
                preparedStatement.executeUpdate();

                var generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Can't generate id for user");
                }
            }
        } else {
            var updateUserSql = "UPDATE users SET username = ?, phone = ? WHERE id = ?;";
            try (var preparedStatement = connection.prepareStatement(updateUserSql)) {
                preparedStatement.setString(1, user.getUsername());
                preparedStatement.setString(2, user.getPhone());
                preparedStatement.setLong(3, user.getId());
                preparedStatement.executeUpdate();
            }
        }
    }

    public Optional<User> find(Long id) throws SQLException {
        var findUserSql = "SELECT * FROM users WHERE id = ?;";
        try (var preparedStatement = connection.prepareStatement(findUserSql)) {
            preparedStatement.setLong(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(
                        new User(
                                resultSet.getLong("id"),
                                resultSet.getString("username"),
                                resultSet.getString("phone")
                        )
                );
            } else {
                return Optional.empty();
            }
        }
    }

    public boolean deleteById(Long id) throws SQLException {
        var deleteByIdSql = "DELETE FROM users WHERE id = ?;";
        try (var preparedStatement = connection.prepareStatement(deleteByIdSql)) {
            preparedStatement.setLong(1, id);
            return preparedStatement.executeUpdate() != 0;
        }
    }

    public boolean deleteByUsername(String username) throws SQLException {
        var deleteByUsernameSql = "DELETE FROM users WHERE username = ?;";
        try (var preparedStatement = connection.prepareStatement(deleteByUsernameSql)) {
            preparedStatement.setString(1, username);
            return preparedStatement.executeUpdate() != 0;
        }
    }

    public List<User> getAllUsers() throws SQLException {
        ArrayList<User> result = new ArrayList<User>();
        var selectAllSql = "SELECT * FROM users";
        try (var statement = connection.createStatement()) {
            var resultSet = statement.executeQuery(selectAllSql);
            while (resultSet.next()) {
                result.add(
                    new User(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("phone")
                    )
                );
            }
        }

        return result;
    }
}
