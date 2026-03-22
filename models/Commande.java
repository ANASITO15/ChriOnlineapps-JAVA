package models;

import common.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public class Commande {

    private int id;
    private int userId;
    private String status;
    private List<LigneCommande> lignes = new ArrayList<>();

    public Commande() {
    }

    public Commande(int id, int userId, String status) {
        this.id = id;
        this.userId = userId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LigneCommande> getLignes() {
        return lignes;
    }

    public void setLignes(List<LigneCommande> lignes) {
        this.lignes = lignes;
    }

    public double calculerTotal() {
        return lignes.stream().mapToDouble(LigneCommande::getTotal).sum();
    }

    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"id\":").append(id).append(",");
        json.append("\"userId\":").append(userId).append(",");
        json.append("\"status\":").append(JsonUtil.toJson(status)).append(",");
        json.append("\"total\":").append(calculerTotal()).append(",");

        json.append("\"lignes\":[");
        for (int i = 0; i < lignes.size(); i++) {
            json.append(lignes.get(i).toJson());
            if (i < lignes.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");

        json.append("}");
        return json.toString();
    }

    @Override
    public String toString() {
        return "Commande{" +
                "id=" + id +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                ", total=" + calculerTotal() +
                ", lignes=" + lignes +
                '}';
    }
}
