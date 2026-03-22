package models;

import common.JsonUtil;

public class LigneCommande {

    private int produitId;
    private String nomProduit;
    private int quantite;
    private double prixUnitaire;

    public LigneCommande() {
    }

    public LigneCommande(int produitId, String nomProduit, int quantite, double prixUnitaire) {
        this.produitId = produitId;
        this.nomProduit = nomProduit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public int getProduitId() {
        return produitId;
    }

    public void setProduitId(int produitId) {
        this.produitId = produitId;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public double getTotal() {
        return prixUnitaire * quantite;
    }

    public String toJson() {
        return "{"
                + "\"produitId\":" + produitId + ","
                + "\"nomProduit\":" + JsonUtil.toJson(nomProduit) + ","
                + "\"quantite\":" + quantite + ","
                + "\"prixUnitaire\":" + prixUnitaire
                + "}";
    }

    @Override
    public String toString() {
        return "LigneCommande{" +
                "produitId=" + produitId +
                ", nomProduit='" + nomProduit + '\'' +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                '}';
    }
}
