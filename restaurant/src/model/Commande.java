package model;

import java.util.Date;

public class Commande {
    private int id;
    private int utilisateurId;
    private Date dateCommande;
    private String etat;

    public Commande() {}

    public Commande(int id, int utilisateurId, Date dateCommande, String etat) {
        this.id             = id;
        this.utilisateurId  = utilisateurId;
        this.dateCommande   = dateCommande;
        this.etat           = etat;
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }
    public int getUtilisateurId()               { return utilisateurId; }
    public void setUtilisateurId(int uid)       { this.utilisateurId = uid; }
    public Date getDateCommande()               { return dateCommande; }
    public void setDateCommande(Date date)      { this.dateCommande = date; }
    public String getEtat()                     { return etat; }
    public void setEtat(String etat)            { this.etat = etat; }

    @Override
    public String toString() {
        return "Commande #" + id + " [" + etat + "]";
    }
}
