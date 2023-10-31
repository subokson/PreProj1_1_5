package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        UserServiceImpl userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Alexey", "Alexeev", (byte) 21);
        userService.saveUser("Boris", "Borisov", (byte) 22);
        userService.saveUser("Victor", "Victorov", (byte) 23);
        userService.saveUser("Georgiy", "Georgiev", (byte) 24);

        userService.removeUserById(3);
        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();
        Util.closeConnection(Util.getConnection());
    }
}
