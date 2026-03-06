package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import common.Message;
import common.JsonUtil;

public class TestClient {

    public static void main(String[] args) {

        try {

            // connexion au serveur
            Socket socket = new Socket("localhost", 6000);

            System.out.println("Connected to server");

            BufferedReader input =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

            PrintWriter output =
                    new PrintWriter(socket.getOutputStream(), true);

            // création message PING
            Message message = Message.request("PING", "1", "");

            // convertir en JSON
            String json = JsonUtil.toJson(message);

            // envoyer au serveur
            output.println(json);

            // lire la réponse
            String response = input.readLine();

            System.out.println("Server response : " + response);

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
