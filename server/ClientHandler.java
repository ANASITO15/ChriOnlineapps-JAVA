package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import common.Message;
import common.JsonUtil;

public class ClientHandler implements Runnable {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {

            BufferedReader input =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));

            PrintWriter output =
                    new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Handler started for : " + socket.getInetAddress());

            String request;

            while((request = input.readLine()) != null) {

                System.out.println("Request received : " + request);

                Message message = JsonUtil.fromJson(request);

                Message response = RequestRouter.route(message);

                String jsonResponse = JsonUtil.toJson(response);

                System.out.println("Response sent : " + jsonResponse);

                output.println(jsonResponse);
            }

        } catch(Exception e) {

            System.out.println("Client disconnected unexpectedly");

        } finally {

            try {
                socket.close();
                System.out.println("Connection closed : " + socket.getInetAddress());
            } catch(Exception e) {
                e.printStackTrace();
            }

        }
    }
}
