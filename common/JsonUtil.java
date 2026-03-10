package common;

import java.io.*;

public class JsonUtil {

    // Serialize object to byte array (binary)
    public static byte[] toBinary(Object obj) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(obj);
        oos.close();
        return baos.toByteArray();
    }

    // Deserialize byte array to object (binary)
    @SuppressWarnings("unchecked")
    public static <T> T fromBinary(byte[] data, Class<T> clazz) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        T obj = (T) ois.readObject();
        ois.close();
        return obj;
    }

    // Legacy JSON methods (keep for compatibility if needed)
    public static String toJson(Message m) {

        String json = "{"
                + "\"type\":\"" + safe(m.getType()) + "\","
                + "\"requestId\":\"" + safe(m.getRequestId()) + "\","
                + "\"status\":\"" + safe(m.getStatus()) + "\","
                + "\"payload\":\"" + safe(m.getPayload()) + "\","
                + "\"errorCode\":\"" + safe(m.getErrorCode()) + "\""
                + "}";

        return json;
    }

    // Convertir JSON en objet Message
    public static Message fromJson(String json) {

        Message m = new Message();

        m.setType(getValue(json, "type"));
        m.setRequestId(getValue(json, "requestId"));
        m.setStatus(getValue(json, "status"));
        m.setPayload(getValue(json, "payload"));
        m.setErrorCode(getValue(json, "errorCode"));

        return m;
    }

    // Fonction pour extraire une valeur JSON
    private static String getValue(String json, String key) {

        String search = "\"" + key + "\":\"";

        int start = json.indexOf(search);

        if (start == -1) {
            return "";
        }

        start = start + search.length();

        int end = json.indexOf("\"", start);

        if (end == -1) {
            return "";
        }

        return json.substring(start, end);
    }

    // Évite les valeurs null
    private static String safe(String value) {
        return value == null ? "" : value;
    }

    public static String toJson(Object obj) {
        // Simple implementation for basic objects; extend for lists/POJOs if needed
        if (obj instanceof String) {
            return "\"" + obj + "\"";
        }
        // For now, assume obj is a POJO or list; use reflection or a lib like Gson
        // Placeholder: return a basic JSON string
        return "{}"; // Replace with proper serialization
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        // Placeholder: not implemented for binary switch
        return null;
    }
}
