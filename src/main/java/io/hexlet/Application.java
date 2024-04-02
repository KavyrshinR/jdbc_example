package io.hexlet;

import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Базовая работа с JDBC
public class Application {
    // Нужно указывать базовое исключение,
    // потому что выполнение запросов может привести к исключениям
    public static void main(String[] args) throws SQLException {
        // Создаем соединение с базой в памяти
        // База создается прямо во время выполнения этой строчки
        // Здесь hexlet_test — это имя базы данных
        var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test");

        var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
        // Чтобы выполнить запрос, создадим объект statement
        try(var statement = conn.createStatement()) {
            statement.execute(sql);
        }

        var sql2 = "INSERT INTO users (username, phone) VALUES ('tommy', '123456789')";
        try(var statement2 = conn.createStatement()) {
            statement2.executeUpdate(sql2);
        }

        var sqlInsertData2 = "INSERT INTO users (username, phone) VALUES (?, ?);";
        try (var preparedStatement = conn.prepareStatement(sqlInsertData2)) {
            preparedStatement.setString(1, "Ivan");
            preparedStatement.setString(2, "+7111111");
            preparedStatement.executeUpdate();

            preparedStatement.setString(1, "Viktor");
            preparedStatement.setString(2, "+7222222");
            preparedStatement.executeUpdate();
        }

        var sqlDeleteUserByNameSql = "DELETE FROM users WHERE username = ?;";
        try (var preparedStatement = conn.prepareStatement(sqlDeleteUserByNameSql)) {
            preparedStatement.setString(1, "Viktor");
            preparedStatement.executeUpdate();
        }

        var sqlSelectAll = "SELECT * FROM users";
        try(var statement3 = conn.createStatement()) {
            // Здесь вы видите указатель на набор данных в памяти СУБД
            var resultSet = statement3.executeQuery(sqlSelectAll);
            // Набор данных — это итератор
            // Мы перемещаемся по нему с помощью next() и каждый раз получаем новые значения
            while (resultSet.next()) {
                System.out.println(resultSet.getString("username") + " " + resultSet.getString("phone"));
            }
        }

        // Закрываем соединение
        conn.close();
    }
}
