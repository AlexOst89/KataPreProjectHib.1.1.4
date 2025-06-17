package jm.task.core.jdbc;


import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        final UserService userService = new UserServiceImpl();

        userService.createUsersTable();
        userService.saveUser("Григорий", "Петров", (byte) 20);
        userService.saveUser("Александр", "Сидоров", (byte) 43);
        userService.saveUser("Анастасия", "Макаренко", (byte) 32);
        userService.saveUser("Ярослав", "Жилин", (byte) 57);
        userService.removeUserById(2);
        for (User user: userService.getAllUsers()) {
            System.out.println(user);
        }
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
