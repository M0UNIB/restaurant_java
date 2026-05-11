package controller;

import dao.CommandeDAO;
import dao.LigneCommandeDAO;
import dao.FactureDAO;
import model.*;

import java.util.List;

public class CommandeController {
    private final CommandeDAO      commandeDAO      = new CommandeDAO();
    private final LigneCommandeDAO ligneCommandeDAO = new LigneCommandeDAO();
    private final FactureDAO       factureDAO       = new FactureDAO();

    public Commande passerCommande(int utilisateurId, List<LigneCommande> lignes) {
        Commande commande = new Commande();
        commande.setUtilisateurId(utilisateurId);
        commande.setEtat(EtatCommande.EN_ATTENTE);
        commandeDAO.insert(commande);
        for (LigneCommande lc : lignes) {
            lc.setCommandeId(commande.getId());
            ligneCommandeDAO.insert(lc);
        }
        return commande;
    }

    public void commencerTraitement(int commandeId) {
        commandeDAO.updateEtat(commandeId, EtatCommande.EN_COURS);
    }

    public void marquerPrete(int commandeId) {
        commandeDAO.updateEtat(commandeId, EtatCommande.PRETE);
        System.out.println("[Notification] Commande #" + commandeId + " est prête !");
    }

    public void annulerTraitement(int commandeId) {
        commandeDAO.updateEtat(commandeId, EtatCommande.ANNULEE);
    }

    public void marquerServie(int commandeId) {
        commandeDAO.updateEtat(commandeId, EtatCommande.SERVIE);
    }

    public Facture genererFacture(int commandeId) {
        List<LigneCommande> lignes = ligneCommandeDAO.findByCommande(commandeId);
        double total = lignes.stream().mapToDouble(LigneCommande::getSousTotal).sum();
        Facture facture = new Facture();
        facture.setCommandeId(commandeId);
        facture.setMontantTotal(total);
        factureDAO.insert(facture);
        return facture;
    }

    public List<Commande> getCommandesByEtat(String etat)       { return commandeDAO.findByEtat(etat); }
    public List<Commande> getAllCommandes()                      { return commandeDAO.findAll(); }
    public List<LigneCommande> getLignes(int commandeId)        { return ligneCommandeDAO.findByCommande(commandeId); }
    public Facture getFacture(int commandeId)                   { return factureDAO.findByCommande(commandeId); }
}
