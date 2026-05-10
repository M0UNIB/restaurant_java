package model;

import java.util.Date;

public class Facture {
    private int id;
    private int commandeId;
    private double montantTotal;
    private Date dateFacture;

    public Facture() {}

    public Facture(int id, int commandeId, double montantTotal, Date dateFacture) {
        this.id           = id;
        this.commandeId   = commandeId;
        this.montantTotal = montantTotal;
        this.dateFacture  = dateFacture;
    }

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }
    public int getCommandeId()                  { return commandeId; }
    public void setCommandeId(int cid)          { this.commandeId = cid; }
    public double getMontantTotal()             { return montantTotal; }
    public void setMontantTotal(double mt)      { this.montantTotal = mt; }
    public Date getDateFacture()                { return dateFacture; }
    public void setDateFacture(Date date)       { this.dateFacture = date; }

    @Override
    public String toString() {
        return "Facture #" + id + " — " + String.format("%.2f", montantTotal) + " DT";
    }
}
