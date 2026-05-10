package model;

public class Menu {
    private int id;
    private String nom;
    private String description;

    public Menu() {}

    public Menu(int id, String nom, String description) {
        this.id          = id;
        this.nom         = nom;
        this.description = description;
    }

    public int getId()                        { return id; }
    public void setId(int id)                 { this.id = id; }
    public String getNom()                    { return nom; }
    public void setNom(String nom)            { this.nom = nom; }
    public String getDescription()            { return description; }
    public void setDescription(String desc)   { this.description = desc; }

    @Override
    public String toString() { return nom; }
}
