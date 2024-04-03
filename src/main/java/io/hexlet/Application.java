package io.hexlet;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class Application {
    public static void main(String[] args) throws SQLException {
        var conn = DriverManager.getConnection("jdbc:h2:mem:hexlet_test");

        var sql = "CREATE TABLE users (id BIGINT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(255), phone VARCHAR(255))";
        try(var statement = conn.createStatement()) {
            statement.execute(sql);
        }

        var userDao = new UserDAO(conn);
        var tommy = new User("Tommy", "1111111");
        userDao.save(tommy);
        userDao.save(new User("Viktor", "+71111111"));
        userDao.save(new User("Ivan", "+7222222"));
        userDao.save(new User("Василий", "+73333322"));

        printUsers(userDao.getAllUsers());

        var newUser = new User("Геннадий", "+7555544");
        userDao.save(newUser);
        System.out.println("new user id = " + newUser.getId());

        tommy.setPhone(tommy.getPhone() + "322");
        userDao.save(tommy);

        printUsers(userDao.getAllUsers());

        var foundUser = userDao.find(2L);
        foundUser.ifPresent(Application::printUser);

        foundUser.ifPresent((it) -> {
            try {
                System.out.println("delete by id " + it.getId() + " = " + userDao.deleteById(it.getId()));
                System.out.println("delete by id " + it.getId() + " = " + userDao.deleteById(it.getId()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        printUsers(userDao.getAllUsers());

        System.out.println("delete by username " + "Василий" + " = " + userDao.deleteByUsername("Василий"));
        System.out.println("delete by username " + "Василий" + " = " + userDao.deleteByUsername("Василий"));

        printUsers(userDao.getAllUsers());

        conn.close();
    }

    public static void printUser(User user) {
        printUsers(List.of(user));
    }

    public static void printUsers(List<User> users) {
        System.out.println("-----------");
        for (User user: users) {
            System.out.println(user.getId() + " " + user.getUsername() + " " + user.getPhone());
        }
    }
}
