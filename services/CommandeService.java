package services;

import common.JsonUtil;
import common.Message;
import models.Commande;
import models.LigneCommande;

import java.util.ArrayList;
import java.util.List;

public class CommandeService {

    private static final List<Commande> COMMANDES = new ArrayList<>();
    private static int nextId = 1;

    public static Message createCommandeAvecProduits(Message request) {
        // payload attendu: JSON avec userId (int), produits (ex : "1:2;3:1")

        String payload = request.getPayload();
        if (payload == null || payload.isEmpty()) {
            return new Message("CREATE_COMMANDE", request.getRequestId(), "ERROR", "", "MISSING_PAYLOAD");
        }

        try {
            // parser JSON simple via JsonUtil
            java.util.Map<String, String> map = JsonUtil.toMap(payload);
            String userIdText = map.get("userId");
            String produitsData = map.get("produits");

            if (userIdText == null || userIdText.isBlank() || produitsData == null || produitsData.isBlank()) {
                return new Message("CREATE_COMMANDE", request.getRequestId(), "ERROR", "", "MISSING_FIELDS");
            }

            int userId = Integer.parseInt(userIdText.trim());
            Commande commande = new Commande(nextId++, userId, "CREATED");

            String[] produits = produitsData.split(";");

            for (String p : produits) {
                if (p == null || p.isBlank()) {
                    continue;
                }

                String[] parts = p.split(":", 2);
                if (parts.length != 2) {
                    return new Message("CREATE_COMMANDE", request.getRequestId(), "ERROR", "", "INVALID_PRODUCT_FORMAT");
                }

                int produitId = Integer.parseInt(parts[0].trim());
                int quantite = Integer.parseInt(parts[1].trim());

                if (quantite <= 0) {
                    return new Message("CREATE_COMMANDE", request.getRequestId(), "ERROR", "", "INVALID_QUANTITY");
                }

                // simulation produit
                String nomProduit = "Produit_" + produitId;
                double prix = produitId * 10.0;
                LigneCommande ligne = new LigneCommande(produitId, nomProduit, quantite, prix);
                commande.getLignes().add(ligne);
            }

            COMMANDES.add(commande);
            String resultPayload = commande.toJson();
            return new Message("CREATE_COMMANDE", request.getRequestId(), "SUCCESS", resultPayload, "");

        } catch (NumberFormatException e) {
            return new Message("CREATE_COMMANDE", request.getRequestId(), "ERROR", "", "INVALID_NUMBER");
        } catch (Exception e) {
            return new Message("CREATE_COMMANDE", request.getRequestId(), "ERROR", "", "SERVER_ERROR");
        }
    }

    public static Message listCommandes(Message request) {
        StringBuilder b = new StringBuilder();
        b.append("[");
        for (int i = 0; i < COMMANDES.size(); i++) {
            b.append(COMMANDES.get(i).toJson());
            if (i < COMMANDES.size() - 1) {
                b.append(",");
            }
        }
        b.append("]");
        return new Message("LIST_COMMANDES", request.getRequestId(), "SUCCESS", b.toString(), "");
    }
}
