package org.example;

import java.io.*;
import java.net.*;

public class WriteThread extends Thread {
    private PrintWriter writer;
    private String username;
    private Socket socket;

    // Конструктор класу WriteThread, який приймає сокет і ім'я користувача
    public WriteThread(Socket socket, String username) {
        this.socket = socket;
        try {
            // Ініціалізація PrintWriter для відправки повідомлень на сервер
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.username = username;
        } catch (IOException e) {
            System.err.println("IOException в конструкторі WriteThread: " + e.getMessage());
        }
    }

    // Метод run(), що виконується при запуску потоку
    @Override
    public void run() {
        try (BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            String message;
            System.out.println("Введіть повідомлення:"); // Виведення повідомлення для користувача
            // Читаємо повідомлення з консолі і надсилаємо на сервер
            while ((message = consoleReader.readLine()) != null) {
                writer.println(username + ": " + message); // Відправка повідомлення з ім'ям користувача
            }
        } catch (IOException e) {
            System.err.println("IOException у WriteThread: " + e.getMessage());
        } finally {
            try {
                System.out.println("Закриття сокета у WriteThread...");
                // Закриття сокета, якщо він ще не закритий
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println("Сокет закритий у WriteThread.");
                }
            } catch (IOException e) {
                System.err.println("Помилка при закритті сокета у WriteThread: " + e.getMessage());
            }
        }
    }
}
