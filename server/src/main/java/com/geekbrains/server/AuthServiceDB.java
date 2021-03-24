package com.geekbrains.server;

import java.io.Closeable;
import java.sql.*;

public class AuthServiceDB implements AuthService, Closeable {
    private static AuthServiceDB dbService;

    private Connection connection = null;
    private PreparedStatement statementGetNick;
    private PreparedStatement statementUpdateNick;

    private AuthServiceDB() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:auth_list.db");
            statementGetNick = connection.prepareStatement("SELECT * FROM auth_list WHERE login = ? AND password = ?");
            statementUpdateNick = connection.prepareStatement("UPDATE auth_list SET nickname = ? WHERE nickname = ?");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static AuthServiceDB getDBservice() {
        if (dbService == null) {
            dbService = new AuthServiceDB();
        }
        return dbService;
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        try {
            statementGetNick.setString(1, login);
            statementGetNick.setString(2, password);
            ResultSet resultSet = statementGetNick.executeQuery();
            if (resultSet.next()) {
                String nickname = resultSet.getString("nickname");
                return nickname;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public void changeNickname(String oldNickname, String newNickname) {
        try {
            statementUpdateNick.setString(1, newNickname);
            statementUpdateNick.setString(2, oldNickname);
            statementUpdateNick.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void close(){
        try {
            statementGetNick.close();
            statementUpdateNick.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
