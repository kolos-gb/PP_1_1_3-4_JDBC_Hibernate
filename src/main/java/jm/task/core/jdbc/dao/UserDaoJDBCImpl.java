package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String sqlCreateTable = """
            CREATE TABLE IF NOT EXISTS users (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                lastName VARCHAR(50) NOT NULL,
                age TINYINT NOT NULL
            );
            """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sqlCreateTable);
        } catch (SQLException e) {
            System.err.println("Error creating table: " + e.getMessage());
        }
    }

    public void dropUsersTable() {
        String sqlDropTable = "DROP TABLE users";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlDropTable);
        } catch (SQLException e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String sqlSave = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlSave)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем — " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }

    public void removeUserById(long id) {
        String sqlRemove = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlRemove)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        String sqlAllUsers = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlAllUsers)) {
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastName"));
                user.setAge(resultSet.getByte("age"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(users);
        return users;
    }

    public void cleanUsersTable() {
        String sqlDelete = "DELETE from users";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlDelete);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
