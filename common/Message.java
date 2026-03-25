package common;

import com.google.gson.Gson;

public class Message {

    private String type;
    private String requestId;
    private String status;
    private String payload;
    private String errorCode;

    private String token; // 🔥 AJOUT DU TOKEN

    private static final Gson gson = new Gson();

    // =========================
    // 🔧 CONSTRUCTEURS
    // =========================

    // constructeur vide
    public Message() {}

    // constructeur EXISTANT (compatibilité)
    public Message(String type, String requestId, String status, String payload, String errorCode) {
        this.type = type;
        this.requestId = requestId;
        this.status = status;
        this.payload = payload;
        this.errorCode = errorCode;
    }

    // 🔥 NOUVEAU constructeur avec token
    public Message(String type, String requestId, String status, String payload, String errorCode, String token) {
        this.type = type;
        this.requestId = requestId;
        this.status = status;
        this.payload = payload;
        this.errorCode = errorCode;
        this.token = token;
    }

    // =========================
    // 📌 GETTERS
    // =========================
    public String getType() { return type; }
    public String getRequestId() { return requestId; }
    public String getStatus() { return status; }
    public String getPayload() { return payload; }
    public String getErrorCode() { return errorCode; }
    public String getToken() { return token; } // 🔥

    // =========================
    // 🔥 SETTERS
    // =========================
    public void setType(String type) { this.type = type; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public void setStatus(String status) { this.status = status; }
    public void setPayload(String payload) { this.payload = payload; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }
    public void setToken(String token) { this.token = token; } // 🔥

    // =========================
    // 🚀 FACTORY METHODS
    // =========================

    public static Message request(String type, String requestId, String payload) {
        return new Message(type, requestId, "", payload, "");
    }

    public static Message success(String type, String requestId, String payload) {
        return new Message(type, requestId, "SUCCESS", payload, "");
    }

    public static Message error(String type, String requestId, String errorCode) {
        return new Message(type, requestId, "FAIL", "", errorCode);
    }

    // =========================
    // 🔥 JSON METHODS
    // =========================

    public String toJson() {
        return gson.toJson(this);
    }

    public static Message fromJson(String json) {
        return gson.fromJson(json, Message.class);
    }

    // =========================
    // 🔍 DEBUG
    // =========================

    @Override
    public String toString() {
        return "Message{" +
                "type='" + type + '\'' +
                ", requestId='" + requestId + '\'' +
                ", status='" + status + '\'' +
                ", payload='" + payload + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", token='" + token + '\'' + // 🔥 visible debug
                '}';
    }
}
