package services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import common.Message;
import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthService {

    // =========================
    // LOGIN
    // =========================
    public static Message login(Message message) {
        try (Connection conn = DBConnection.getConnection()) {

            JsonObject data = JsonParser.parseString(message.getPayload()).getAsJsonObject();
            String email = data.get("email").getAsString();
            String password = data.get("password").getAsString();

            String sql = "SELECT id, nom, email FROM users WHERE email = ? AND password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                JsonObject responseData = new JsonObject();
                responseData.addProperty("userId", rs.getInt("id"));
                responseData.addProperty("nom", rs.getString("nom"));
                responseData.addProperty("email", rs.getString("email"));

                return new Message(
                        "LOGIN_RESPONSE",
                        message.getRequestId(),
                        "SUCCESS",
                        responseData.toString(),
                        ""
                );
            }

            return new Message(
                    "LOGIN_RESPONSE",
                    message.getRequestId(),
                    "FAIL",
                    "",
                    "INVALID_CREDENTIALS"
            );

        } catch (Exception e) {
            e.printStackTrace();
            return new Message(
                    "LOGIN_RESPONSE",
                    message.getRequestId(),
                    "FAIL",
                    "",
                    "SERVER_ERROR"
            );
        }
    }

    // =========================
    // REGISTER
    // =========================
    public static Message register(Message message) {
        try (Connection conn = DBConnection.getConnection()) {

            JsonObject data = JsonParser.parseString(message.getPayload()).getAsJsonObject();
            String nom = data.get("nom").getAsString();
            String email = data.get("email").getAsString();
            String password = data.get("password").getAsString();

            String checkSql = "SELECT id FROM users WHERE email = ?";
            PreparedStatement checkPs = conn.prepareStatement(checkSql);
            checkPs.setString(1, email);

            ResultSet rs = checkPs.executeQuery();
            if (rs.next()) {
                return new Message(
                        "REGISTER_RESPONSE",
                        message.getRequestId(),
                        "FAIL",
                        "",
                        "EMAIL_ALREADY_EXISTS"
                );
            }

            String insertSql = "INSERT INTO users(nom, email, password) VALUES (?, ?, ?)";
            PreparedStatement insertPs = conn.prepareStatement(insertSql);
            insertPs.setString(1, nom);
            insertPs.setString(2, email);
            insertPs.setString(3, password);

            int rows = insertPs.executeUpdate();

            if (rows > 0) {
                return new Message(
                        "REGISTER_RESPONSE",
                        message.getRequestId(),
                        "SUCCESS",
                        "REGISTER_OK",
                        ""
                );
            }

            return new Message(
                    "REGISTER_RESPONSE",
                    message.getRequestId(),
                    "FAIL",
                    "",
                    "REGISTER_FAILED"
            );

        } catch (Exception e) {
            e.printStackTrace();
            return new Message(
                    "REGISTER_RESPONSE",
                    message.getRequestId(),
                    "FAIL",
                    "",
                    "SERVER_ERROR"
            );
        }
    }
}
