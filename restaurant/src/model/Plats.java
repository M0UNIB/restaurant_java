package model;

public class Plats {
    private int id;
    private String nom;
    private String description;
    private double prix;
    private int menuId;

    public Plats() {}

    public Plats(int id, String nom, String description, double prix, int menuId) {
        this.id          = id;
        this.nom         = nom;
        this.description = description;
        this.prix        = prix;
        this.menuId      = menuId;
    }

    public int getId()                      { return id; }
    public void setId(int id)               { this.id = id; }
    public String getNom()                  { return nom; }
    public void setNom(String nom)          { this.nom = nom; }
    public String getDescription()          { return description; }
    public void setDescription(String d)    { this.description = d; }
    public double getPrix()                 { return prix; }
    public void setPrix(double prix)        { this.prix = prix; }
    public int getMenuId()                  { return menuId; }
    public void setMenuId(int menuId)       { this.menuId = menuId; }

    @Override
    public String toString() { return nom + " — " + String.format("%.2f", prix) + " DT"; }
}
