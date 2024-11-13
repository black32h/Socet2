package org.example;

import java.io.*;
import java.net.*;

public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;

    // Конструктор класу ReadThread, який ініціалізує з'єднання з сервером та зчитування повідомлень
    public ReadThread(Socket socket) {
        this.socket = socket;
        try {
            // Ініціалізація BufferedReader для зчитування повідомлень з сервера
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("IOException в конструкторі ReadThread: " + e.getMessage());
        }
    }

    // Метод run(), що виконується при запуску потоку
    @Override
    public void run() {
        try {
            String message;
            // Читаємо повідомлення від сервера, поки з'єднання відкрите
            while ((message = reader.readLine()) != null) {
                System.out.println("Отримано від сервера: " + message); // Виводимо отримане повідомлення
            }
        } catch (IOException e) {
            if (socket.isClosed()) {
                System.out.println("З'єднання закрито: " + e.getMessage());
            } else {
                System.err.println("IOException у ReadThread: " + e.getMessage());
            }
        } finally {
            try {
                System.out.println("Закриття сокета у ReadThread...");
                // Закриваємо сокет, якщо він ще не закритий
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                    System.out.println("Сокет закритий у ReadThread.");
                }
            } catch (IOException e) {
                System.err.println("Помилка при закритті сокета у ReadThread: " + e.getMessage());
            }
        }
    }
}
