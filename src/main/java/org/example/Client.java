package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientHandler implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public String getUsername() {
        return username;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Запит імені користувача
            out.println("Введіть ваше ім'я:");
            username = in.readLine();
            Server.addClient(username, this);
            Server.broadcast("Користувач " + username + " приєднався до чату.", this);

            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("/pm")) { // Приватне повідомлення
                    String[] tokens = message.split(" ", 3);
                    if (tokens.length == 3) {
                        String recipient = tokens[1];
                        String privateMessage = tokens[2];
                        Server.sendPrivateMessage(privateMessage, recipient, this);
                    }
                } else { // Загальне повідомлення
                    Server.broadcast(username + ": " + message, this);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Видаляємо клієнта при відключенні
            Server.removeClient(username);
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
