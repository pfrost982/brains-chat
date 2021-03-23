package com.geekbrains.server;

import java.sql.*;

public class DBservice {
    private static DBservice dbService;

    private Connection connection = null;
    private Statement statement = null;

    private DBservice() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:auth_list.db");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            statement = connection.createStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static DBservice getDBservice() {
        if (dbService == null) {
            dbService = new DBservice();
        }
        return dbService;
    }

    public String getNicknameByLoginAndPassword(String login, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM auth_list WHERE login = ? AND password = ?");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String nickname = resultSet.getString("nickname");
                return nickname;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void changeNickname(String oldNickname, String newNickname) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE auth_list SET nickname = ? WHERE nickname = ?");
            preparedStatement.setString(1, newNickname);
            preparedStatement.setString(2, oldNickname);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void printTable() {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery("SELECT * FROM auth_list");
            while (resultSet.next()) {
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                String nickname = resultSet.getString("nickname");

                System.out.println(login + " " + password + " " + nickname);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
