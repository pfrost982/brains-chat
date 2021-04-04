package com.geekbrains.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.*;

public class Server {
    private Vector<ClientHandler> clients;
    private AuthService authService;
    private ExecutorService executorService;
    public static final Logger LOGGER = Logger.getLogger(Server.class.getName());

    public AuthService getAuthService() {
        return authService;
    }

    public Server() throws IOException {
        clients = new Vector<>();
        authService = AuthServiceDB.getDBservice();
        executorService = Executors.newCachedThreadPool();
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);
        Handler consoleHandler = new ConsoleHandler();
        Handler fileHandler = new FileHandler("log_file.txt", true);
        consoleHandler.setLevel(Level.ALL);
        fileHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new SimpleFormatter());
        fileHandler.setFormatter(new SimpleFormatter());
        LOGGER.addHandler(consoleHandler);
        LOGGER.addHandler(fileHandler);

        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            LOGGER.log(Level.INFO, "Сервер запущен на порту 8189");
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(this, socket, executorService);
                LOGGER.log(Level.INFO,"Подключился новый клиент");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Не удалось создать сокет", e);
        }
        LOGGER.log(Level.INFO, "Сервер завершил свою работу");
    }

    public void broadcastMsg(String msg) {
        LOGGER.log(Level.INFO, "Рассылка сообщения: " + msg);
        for (ClientHandler o : clients) {
            o.sendMsg(msg);
        }
    }

    public void privateMsg(ClientHandler sender, String receiverNick, String msg) {
        LOGGER.log(Level.INFO, "Отправка приватного сообщения для " + receiverNick + ": " + msg);
        if (sender.getNickname().equals(receiverNick)) {
            sender.sendMsg("заметка для себя: " + msg);
            return;
        }
        for (ClientHandler o : clients) {
            if (o.getNickname().equals(receiverNick)) {
                o.sendMsg("от " + sender.getNickname() + ": " + msg);
                sender.sendMsg("для " + receiverNick + ": " + msg);
                return;
            }
        }
        LOGGER.log(Level.WARNING, "Клиент приватного сообщения " + receiverNick + " не найден");
    }

    public void subscribe(ClientHandler clientHandler) {
        LOGGER.log(Level.FINE, "Добавлен обработчик клиента " + clientHandler.getNickname());
        clients.add(clientHandler);
        broadcastClientsList();
    }

    public void unsubscribe(ClientHandler clientHandler) {
        LOGGER.log(Level.FINE, "Удален обработчик клиента " + clientHandler.getNickname());
        clients.remove(clientHandler);
        broadcastClientsList();
    }

    public boolean isNickBusy(String nickname) {
        for (ClientHandler o : clients) {
            if (o.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }

    public void broadcastClientsList() {
        StringBuilder sb = new StringBuilder(15 * clients.size());
        sb.append("/clients ");
        // '/clients '
        for (ClientHandler o : clients) {
            sb.append(o.getNickname()).append(" ");
        }
        // '/clients nick1 nick2 nick3 '
        sb.setLength(sb.length() - 1);
        // '/clients nick1 nick2 nick3'
        String out = sb.toString();
        for (ClientHandler o : clients) {
            o.sendMsg(out);
        }
    }
}
