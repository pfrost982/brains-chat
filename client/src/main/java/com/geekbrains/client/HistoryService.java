package com.geekbrains.client;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class HistoryService implements Closeable {

    private PrintWriter out;
    private File file;
    private String login;
    private final static int LAST_STRINGS_COUNT = 10;

    public HistoryService(String login) {
        this.login = login;
        file = new File("client/History/history_" + login + ".txt");
        try {
            file.createNewFile();
            out = new PrintWriter(new FileWriter(file, true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveMessage(String message) {
        out.println(message);
        out.flush();
    }

    public LinkedList<String> readLastStrings() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        LinkedList<String> lastStrings = new LinkedList<>();
        while (scanner.hasNext()) {
            lastStrings.addLast(scanner.nextLine());
            if (lastStrings.size() > LAST_STRINGS_COUNT) {
                lastStrings.removeFirst();
            }
        }
        scanner.close();
        return lastStrings;
    }

    @Override
    public void close() {
        out.close();
    }
}
