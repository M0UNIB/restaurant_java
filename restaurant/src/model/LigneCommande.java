package model;

public class LigneCommande {
    private int id;
    private int commandeId;
    private int platId;
    private int quantite;
    private double prixUnitaire;

    public LigneCommande() {}

    public LigneCommande(int id, int commandeId, int platId, int quantite, double prixUnitaire) {
        this.id           = id;
        this.commandeId   = commandeId;
        this.platId       = platId;
        this.quantite     = quantite;
        this.prixUnitaire = prixUnitaire;
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }
    public int getCommandeId()                  { return commandeId; }
    public void setCommandeId(int cid)          { this.commandeId = cid; }
    public int getPlatId()                      { return platId; }
    public void setPlatId(int pid)              { this.platId = pid; }
    public int getQuantite()                    { return quantite; }
    public void setQuantite(int quantite)       { this.quantite = quantite; }
    public double getPrixUnitaire()             { return prixUnitaire; }
    public void setPrixUnitaire(double p)       { this.prixUnitaire = p; }

    public double getSousTotal()                { return quantite * prixUnitaire; }
}
