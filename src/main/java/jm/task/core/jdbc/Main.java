package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        List.of(
                new User("01_Name", "01_LastName", (byte) 10),
                new User("02_Name", "02_LastName", (byte) 20),
                new User("03_Name", "03_LastName", (byte) 30),
                new User("04_Name", "04_LastName", (byte) 40))
                .forEach(user -> {
                    userService.saveUser(user.getName(), user.getLastName(), user.getAge());
                    System.out.println("User с именем – " + user.getName() + " добавлен в базу данных");
                });
        userService.getAllUsers().forEach(System.out::println);
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
