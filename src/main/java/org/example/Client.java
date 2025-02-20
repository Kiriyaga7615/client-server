package org.example;

import java.io.*;
import java.net.*;

public class Client {

    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println(in.readLine());

            String message;
            while (true) {
                System.out.print("Введіть команду: ");
                message = consoleInput.readLine();
                out.println(message);

                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("Ви відключилися від сервера");
                    break;
                }

                System.out.println(in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
