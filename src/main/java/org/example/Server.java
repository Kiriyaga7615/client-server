package org.example;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class Server {

    private static final int PORT = 12345;
    private static final Map<String, ClientHandler> activeClients = new ConcurrentHashMap<>();
    private static int clientCounter = 1;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("[SERVER] Сервер запущений, чекаємо на підключення клієнтів...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientName = "client-" + clientCounter++;
                ClientHandler clientHandler = new ClientHandler(clientSocket, clientName);
                activeClients.put(clientName, clientHandler);

                System.out.println("[SERVER] " + clientName + " успішно підключився");
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {

        private final Socket clientSocket;
        private final String clientName;
        private final PrintWriter out;
        private final BufferedReader in;

        public ClientHandler(Socket socket, String name) throws IOException {
            this.clientSocket = socket;
            this.clientName = name;
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                out.println("[SERVER] Ласкаво просимо, " + clientName);

                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println("[SERVER] Отримано від " + clientName + ": " + message);

                    if ("exit".equalsIgnoreCase(message)) {
                        out.println("[SERVER] Ви успішно відключилися");
                        System.out.println("[SERVER] " + clientName + " відключився");
                        activeClients.remove(clientName);  // Видаляємо клієнта зі списку активних
                        break;
                    } else {
                        out.println("[SERVER] Ви надіслали: " + message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
