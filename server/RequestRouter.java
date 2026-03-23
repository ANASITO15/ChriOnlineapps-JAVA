package server;

import common.Message;
import common.JsonUtil;

import models.Commande;
import models.Paiement;

import services.AuthService;
import services.CommandeService;
import services.PaiementService;

import java.util.List;
import java.util.Map;

public class RequestRouter {

    // 🔥 SERVICE PAIEMENT
    private static PaiementService paiementService = new PaiementService();

    public static Message route(Message message) {

        // 🔒 Vérification message null
        if (message == null) {
            return new Message("ERROR", "0", "ERROR", "", "NULL_MESSAGE");
        }

        try {

            // 🔥 sécurisation du type
            String rawType = message.getType();
            if (rawType == null || rawType.trim().isEmpty()) {
                return new Message("UNKNOWN", message.getRequestId(), "ERROR", "", "EMPTY_TYPE");
            }

            String type = rawType.trim().toUpperCase();

            switch (type) {

                // =========================
                // 🔹 TEST
                // =========================
                case "PING":
                    return new Message(
                            type,
                            message.getRequestId(),
                            "SUCCESS",
                            "PONG",
                            ""
                    );

                // =========================
                // 🔹 AUTH
                // =========================
                case "LOGIN":
                    return AuthService.login(message);

                case "REGISTER":
                    return AuthService.register(message);

                // =========================
                // 🔹 CREATE COMMANDE
                // =========================
                case "CREATE_COMMANDE":

                    try {
                        String payload = message.getPayload();

                        if (payload == null || payload.trim().isEmpty()) {
                            return error(type, message, "EMPTY_PAYLOAD");
                        }

                        Map<String, String> data = JsonUtil.toMap(payload);

                        if (data == null || data.isEmpty()) {
                            return error(type, message, "INVALID_JSON");
                        }

                        // 🔥 VERSION AVEC PRODUITS
                        if (data.containsKey("produits")) {

                            if (!data.containsKey("userId")) {
                                return error(type, message, "MISSING_USERID");
                            }

                            int userId = Integer.parseInt(data.get("userId"));
                            String produits = data.get("produits");

                            Commande cmd = CommandeService.createCommandeAvecProduits(userId, produits);

                            return success(type, message, cmd.toJson());
                        }

                        // 🔥 VERSION SIMPLE
                        if (!data.containsKey("userId") || !data.containsKey("total")) {
                            return error(type, message, "MISSING_FIELDS");
                        }

                        int userId = Integer.parseInt(data.get("userId"));
                        double total = Double.parseDouble(data.get("total"));

                        Commande cmd = CommandeService.createCommande(userId, total);

                        return success(type, message, cmd.toJson());

                    } catch (Exception e) {
                        return error(type, message, "INVALID_JSON");
                    }

                // =========================
                // 🔹 VALIDER COMMANDE
                // =========================
                case "VALIDER_COMMANDE":

                    try {
                        String payload = message.getPayload();

                        if (payload == null || payload.trim().isEmpty()) {
                            return error(type, message, "EMPTY_PAYLOAD");
                        }

                        int cmdId = Integer.parseInt(payload);

                        boolean ok = CommandeService.validerCommande(cmdId);

                        return new Message(
                                type,
                                message.getRequestId(),
                                ok ? "SUCCESS" : "ERROR",
                                ok ? "COMMANDE_VALIDEE" : "",
                                ok ? "" : "NOT_FOUND"
                        );

                    } catch (Exception e) {
                        return error(type, message, "INVALID_PAYLOAD");
                    }

                // =========================
                // 🔹 GET COMMANDES
                // =========================
                case "GET_COMMANDES":

                    try {
                        String payload = message.getPayload();

                        if (payload == null || payload.trim().isEmpty()) {
                            return error(type, message, "EMPTY_PAYLOAD");
                        }

                        int userId = Integer.parseInt(payload);

                        List<Commande> commandes = CommandeService.getCommandesByUser(userId);

                        StringBuilder json = new StringBuilder("[");
                        for (int i = 0; i < commandes.size(); i++) {
                            json.append(commandes.get(i).toJson());
                            if (i < commandes.size() - 1) json.append(",");
                        }
                        json.append("]");

                        return success(type, message, json.toString());

                    } catch (Exception e) {
                        return error(type, message, "INVALID_PAYLOAD");
                    }

                // =========================
                // 🔹 ANNULER COMMANDE
                // =========================
                case "ANNULER_COMMANDE":

                    try {
                        String payload = message.getPayload();

                        if (payload == null || payload.trim().isEmpty()) {
                            return error(type, message, "EMPTY_PAYLOAD");
                        }

                        int cmdId = Integer.parseInt(payload);

                        boolean ok = CommandeService.annulerCommande(cmdId);

                        return new Message(
                                type,
                                message.getRequestId(),
                                ok ? "SUCCESS" : "ERROR",
                                ok ? "COMMANDE_ANNULEE" : "",
                                ok ? "" : "IMPOSSIBLE"
                        );

                    } catch (Exception e) {
                        return error(type, message, "INVALID_PAYLOAD");
                    }

                // =========================
                // 🔥 PAYMENT
                // =========================
                case "PAYMENT":

                    try {
                        String payload = message.getPayload();

                        if (payload == null || payload.trim().isEmpty()) {
                            return error(type, message, "EMPTY_PAYLOAD");
                        }

                        Map<String, String> data = JsonUtil.toMap(payload);

                        if (data == null || data.isEmpty()) {
                            return error(type, message, "INVALID_JSON");
                        }

                        if (!data.containsKey("userId") ||
                            !data.containsKey("commandeId") ||
                            !data.containsKey("montant")) {
                            return error(type, message, "MISSING_FIELDS");
                        }

                        int userId = Integer.parseInt(data.get("userId"));
                        int commandeId = Integer.parseInt(data.get("commandeId"));
                        double montant = Double.parseDouble(data.get("montant"));

                        Paiement paiement = paiementService.processPayment(userId, commandeId, montant);

                        if (paiement != null) {
                            return success(type, message, paiement.toString());
                        } else {
                            return error(type, message, "PAYMENT_FAILED");
                        }

                    } catch (Exception e) {
                        return error(type, message, "INVALID_JSON");
                    }

                // =========================
                // 🔹 DEFAULT
                // =========================
                default:
                    return error(type, message, "UNKNOWN_REQUEST");
            }

        } catch (Exception e) {
            return error("SERVER_ERROR", message, "SERVER_ERROR");
        }
    }

    // =========================
    // 🔧 HELPERS
    // =========================
    private static Message error(String type, Message msg, String code) {
        return new Message(type, msg.getRequestId(), "ERROR", "", code);
    }

    private static Message success(String type, Message msg, String payload) {
        return new Message(type, msg.getRequestId(), "SUCCESS", payload, "");
    }
}
