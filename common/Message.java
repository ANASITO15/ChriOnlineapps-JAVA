package common;

public class Message {

    public String type;
    public String requestId;
    public String status;
    public String payload;
    public String errorCode;

    public Message() {
    }

    // Création d'une requête
    public static Message request(String type, String requestId, String payload) {
        Message m = new Message();
        m.type = type;
        m.requestId = requestId;
        m.payload = payload;
        return m;
    }

    // Réponse OK
    public static Message ok(String type, String requestId, String payload) {
        Message m = new Message();
        m.status = "OK";
        m.type = type;
        m.requestId = requestId;
        m.payload = payload;
        return m;
    }

    // Réponse ERROR
    public static Message error(String type, String requestId, String errorCode, String payload) {
        Message m = new Message();
        m.status = "ERROR";
        m.type = type;
        m.requestId = requestId;
        m.errorCode = errorCode;
        m.payload = payload;
        return m;
    }
}
