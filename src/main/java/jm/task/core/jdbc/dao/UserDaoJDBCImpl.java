package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
  private Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {
    }

    // создание таблицы юзеров
    @Override
    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS newdatabase (" +
                    "id BIGINT NOT NULL AUTO_INCREMENT," +
                    "name VARCHAR(45) NOT NULL," +
                    "last_name VARCHAR(45) NOT NULL," +
                    "age TINYINT NOT NULL," +
                    "PRIMARY KEY (id))");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблицы");
        }
    }

    // удаление таблицы юзеров
    @Override
    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS newdatabase");
        } catch (SQLException e) {
            System.out.println("Не удалось удалить таблицу");
        }
    }

    // добавление юзера
    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection
                .prepareStatement("INSERT INTO newdatabase (name, last_name, age) " +
                "VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Не удалось добавить пользователя");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    // достать юзера по айди
    @Override
    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM newdatabase WHERE id = ?")) {
            connection.setAutoCommit(true   );
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Не удалось удалить пользователя");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    // достать всех юзеров
    @Override
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery("SELECT * FROM newdatabase");

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("last_name");
                byte age = resultSet.getByte("age");

                User user = new User(name, lastName, age);
                userList.add(user);

                System.out.printf("%s с именем – %s добавлен в базу данных", user, name);
            }
        } catch (SQLException e) {
            System.out.println("Не удалось получить данные");
        }
        return userList;
    }

    // очистить таблицу
    @Override
    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM newdatabase");
        } catch (SQLException e) {
            System.out.println("Не удалось очистить таблицу");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
}
