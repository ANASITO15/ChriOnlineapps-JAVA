package client;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

import common.Message;
import common.JsonUtil;

public class TestClient {

    public static void main(String[] args) {

        System.out.println("Starting client");

        try {

            // connexion au serveur
            Socket socket = new Socket("localhost", 9999);

            System.out.println("Connected to server");

            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

                // ping example
                sendMessage(output, input, Message.request("PING", "1", ""));

                // auth examples (just to show usage)
                sendMessage(output, input, Message.request("LOGIN", "2", ""));
                sendMessage(output, input, Message.request("REGISTER", "3", ""));

                // product examples
                sendMessage(output, input, Message.request("PRODUCT_LIST", "4", ""));
                sendMessage(output, input, Message.request("PRODUCT_DETAILS", "5", "001"));
                // update stock for product 001 to 75
                byte[] payloadData = JsonUtil.toBinary(new StockUpdatePayload("001", 75));
                String payload = Base64.getEncoder().encodeToString(payloadData);
                sendMessage(output, input, Message.request("STOCK_UPDATE", "6", payload));
                sendMessage(output, input, Message.request("PRODUCT_DETAILS", "7", "001"));

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendMessage(DataOutputStream output, DataInputStream input, Message msg) throws Exception {
        byte[] data = JsonUtil.toBinary(msg);
        output.writeInt(data.length);  // Send length first
        output.write(data);
        output.flush();

        int len = input.readInt();  // Read length
        byte[] respData = new byte[len];
        input.readFully(respData);
        Message resp = JsonUtil.fromBinary(respData, Message.class);
        System.out.println("Sent: " + msg + "  Received: " + resp);
    }

    // helper for stock update payload
    private static class StockUpdatePayload implements Serializable {
        public String id;
        public int stock;

        public StockUpdatePayload(String id, int stock) {
            this.id = id;
            this.stock = stock;
        }
    }
}
