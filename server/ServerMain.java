package server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

    // Port du serveur
    private static final int PORT = 6000;

    public static void main(String[] args) {

        System.out.println("Starting server...");

        try {

            // Création du serveur
            ServerSocket serverSocket = new ServerSocket(PORT);

            System.out.println("Server started on port " + PORT);

            // Boucle infinie pour accepter les clients
            while (true) {

                System.out.println("Waiting for client connection...");

                // accepter un client
                Socket clientSocket = serverSocket.accept();

                System.out.println("Client connected : " + clientSocket.getInetAddress());

                // créer un handler pour gérer ce client
                ClientHandler handler = new ClientHandler(clientSocket);

                // lancer le thread
                handler.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
