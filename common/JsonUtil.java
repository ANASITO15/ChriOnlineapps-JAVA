package common;

public class JsonUtil {

    // Convertir un objet Message en texte JSON
    public static String toJson(Message m) {

        String json = "{"
                + "\"type\":\"" + m.type + "\","
                + "\"requestId\":\"" + m.requestId + "\","
                + "\"status\":\"" + (m.status == null ? "" : m.status) + "\","
                + "\"payload\":\"" + (m.payload == null ? "" : m.payload) + "\","
                + "\"errorCode\":\"" + (m.errorCode == null ? "" : m.errorCode) + "\""
                + "}";

        return json;
    }

    // Convertir JSON en objet Message
    public static Message fromJson(String json) {

        Message m = new Message();

        m.type = getValue(json, "type");
        m.requestId = getValue(json, "requestId");
        m.status = getValue(json, "status");
        m.payload = getValue(json, "payload");
        m.errorCode = getValue(json, "errorCode");

        return m;
    }

    // Fonction pour extraire une valeur JSON
    private static String getValue(String json, String key) {

        String search = "\"" + key + "\":\"";

        int start = json.indexOf(search);

        if (start == -1)
            return "";

        start = start + search.length();

        int end = json.indexOf("\"", start);

        if (end == -1)
            return "";

        return json.substring(start, end);
    }
}
