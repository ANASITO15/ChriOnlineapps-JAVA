package server;

import java.io.*;
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

            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            System.out.println("Handler started for : " + socket.getInetAddress());

            while(true) {

                int len = input.readInt();  // Read length
                byte[] data = new byte[len];
                input.readFully(data);

                System.out.println("Request received : " + data.length + " bytes");

                Message message = JsonUtil.fromBinary(data, Message.class);

                Message response = RequestRouter.route(message);

                byte[] respData = JsonUtil.toBinary(response);
                output.writeInt(respData.length);  // Send length first
                output.write(respData);
                output.flush();

                System.out.println("Response sent : " + respData.length + " bytes");
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
