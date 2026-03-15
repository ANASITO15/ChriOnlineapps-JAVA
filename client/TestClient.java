package client;

import java.io.*;
import java.net.Socket;
import java.util.Base64;
import java.util.Scanner;

import common.Message;
import common.JsonUtil;

public class TestClient {

    public static void main(String[] args) {

        System.out.println("Starting client");

        try (Socket socket = new Socket("localhost", 9999);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to server");

            int requestId = 1;

            while (true) {
                System.out.println("\nChoose a message to send:");
                System.out.println("1. Ping");
                System.out.println("2. Login");
                System.out.println("3. Register");
                System.out.println("4. Product List");
                System.out.println("5. Product Details");
                System.out.println("6. Stock Update");
                System.out.println("7. Exit");
                System.out.print("Enter choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                Message msg = null;

                switch (choice) {
                    case 1:
                        msg = Message.request("PING", String.valueOf(requestId++), "");
                        break;
                    case 2:
                        msg = Message.request("LOGIN", String.valueOf(requestId++), "");
                        break;
                    case 3:
                        msg = Message.request("REGISTER", String.valueOf(requestId++), "");
                        break;
                    case 4:
                        msg = Message.request("PRODUCT_LIST", String.valueOf(requestId++), "");
                        break;
                    case 5:
                        System.out.print("Enter product ID: ");
                        String id = scanner.nextLine();
                        msg = Message.request("PRODUCT_DETAILS", String.valueOf(requestId++), id);
                        break;
                    case 6:
                        System.out.print("Enter product ID: ");
                        String updateId = scanner.nextLine();
                        System.out.print("Enter new stock: ");
                        int stock = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        byte[] payloadData = JsonUtil.toBinary(new StockUpdatePayload(updateId, stock));
                        String payload = Base64.getEncoder().encodeToString(payloadData);
                        msg = Message.request("STOCK_UPDATE", String.valueOf(requestId++), payload);
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                        continue;
                }

                if (msg != null) {
                    Message response = sendMessage(output, input, msg);
                    String command = msg.getType();
                    String result = response.getStatus().equals("SUCCESS") ? "success" : "fail";
                    System.out.println(command + ": " + result);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Message sendMessage(DataOutputStream output, DataInputStream input, Message msg) throws Exception {
        byte[] data = JsonUtil.toBinary(msg);
        output.writeInt(data.length);  // Send length first
        output.write(data);
        output.flush();

        int len = input.readInt();  // Read length
        byte[] respData = new byte[len];
        input.readFully(respData);
        Message resp = JsonUtil.fromBinary(respData, Message.class);
        return resp;
    }

    // helper for stock update payload
    private static class StockUpdatePayload implements Serializable {
        public String id;
        public int stock;

        public StockUpdatePayload(String id, int stock) {
            this.id = id;
            this.stock = stock;
        }

        @Override
        public String toString() {
            return "StockUpdatePayload{id='" + id + "', stock=" + stock + '}';
        }
    }
}
