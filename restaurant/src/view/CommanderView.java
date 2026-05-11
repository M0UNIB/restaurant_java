package view;

import controller.CommandeController;
import controller.MenuController;
import model.Commande;
import model.EtatCommande;
import model.LigneCommande;
import model.Plats;
import model.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class CommanderView extends JFrame {
    private final Utilisateur         user;
    private final MenuController      menuController   = new MenuController();
    private final CommandeController  commandeController = new CommandeController();

    private final JComboBox<model.Menu>    menuCombo  = new JComboBox<>();
    private final DefaultTableModel  platModel  = new DefaultTableModel(
            new String[]{"ID", "Nom", "Prix (DT)"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable platTable = new JTable(platModel);

    private final DefaultTableModel  panierModel = new DefaultTableModel(
            new String[]{"Plat", "Qté", "Prix unit.", "Sous-total"}, 0) {
        public boolean isCellEditable(int r, int c) { return c == 1; }
    };
    private final JTable  panierTable = new JTable(panierModel);
    private final JLabel  totalLabel  = new JLabel("Total : 0.00 DT");

    private final List<LigneCommande> panier = new ArrayList<>();
    private final List<Plats>         platCache = new ArrayList<>();

    public CommanderView(Utilisateur user) {
        this.user = user;
        setTitle("Commander des plats — " + user.getPrenom());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 550);
        buildUI();
        loadMenus();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildUI() {
        setLayout(new BorderLayout(8, 8));
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel left = new JPanel(new BorderLayout(4, 4));
        left.setBorder(BorderFactory.createTitledBorder("Menu"));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Choisir menu :"));
        top.add(menuCombo);
        menuCombo.addActionListener(e -> loadPlats());
        left.add(top, BorderLayout.NORTH);
        left.add(new JScrollPane(platTable), BorderLayout.CENTER);

        JButton btnAjouter = new JButton("Ajouter au panier →");
        btnAjouter.addActionListener(e -> ajouterAuPanier());
        left.add(btnAjouter, BorderLayout.SOUTH);

        JPanel right = new JPanel(new BorderLayout(4, 4));
        right.setBorder(BorderFactory.createTitledBorder("Panier"));
        right.add(new JScrollPane(panierTable), BorderLayout.CENTER);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRetirerSelected = new JButton("Retirer");
        JButton btnCommander       = new JButton("Valider la commande");
        btnCommander.setFont(btnCommander.getFont().deriveFont(Font.BOLD));
        south.add(totalLabel);
        south.add(btnRetirerSelected);
        south.add(btnCommander);
        right.add(south, BorderLayout.SOUTH);

        btnRetirerSelected.addActionListener(e -> retirerDuPanier());
        btnCommander.addActionListener(e -> validerCommande());

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
        split.setDividerLocation(450);
        add(split, BorderLayout.CENTER);
    }

    private void loadMenus() {
        menuCombo.removeAllItems();
        menuController.getAllMenus().forEach(menuCombo::addItem);
        loadPlats();
    }

    private void loadPlats() {
        platModel.setRowCount(0);
        platCache.clear();
        model.Menu selected = (model.Menu) menuCombo.getSelectedItem();
        if (selected == null) return;
        List<Plats> plats = menuController.getPlatsByMenu(selected.getId());
        platCache.addAll(plats);
        for (Plats p : plats)
            platModel.addRow(new Object[]{p.getId(), p.getNom(), String.format("%.2f", p.getPrix())});
    }

    private void ajouterAuPanier() {
        int row = platTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez un plat."); return; }
        Plats plat = platCache.get(row);
        String qteStr = JOptionPane.showInputDialog(this, "Quantité :", "1");
        if (qteStr == null) return;
        try {
            int qte = Integer.parseInt(qteStr.trim());
            if (qte <= 0) throw new NumberFormatException();
            LigneCommande lc = new LigneCommande(0, 0, plat.getId(), qte, plat.getPrix());
            panier.add(lc);
            panierModel.addRow(new Object[]{
                plat.getNom(), qte,
                String.format("%.2f", plat.getPrix()),
                String.format("%.2f", lc.getSousTotal())
            });
            updateTotal();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantité invalide.");
        }
    }

    private void retirerDuPanier() {
        int row = panierTable.getSelectedRow();
        if (row < 0) return;
        panier.remove(row);
        panierModel.removeRow(row);
        updateTotal();
    }

    private void updateTotal() {
        double total = panier.stream().mapToDouble(LigneCommande::getSousTotal).sum();
        totalLabel.setText(String.format("Total : %.2f DT", total));
    }

    private void validerCommande() {
        if (panier.isEmpty()) { JOptionPane.showMessageDialog(this, "Le panier est vide."); return; }
        Commande c = commandeController.passerCommande(user.getId(), panier);
        JOptionPane.showMessageDialog(this, "Commande #" + c.getId() + " enregistrée !\nEtat : EN ATTENTE");
        panier.clear();
        panierModel.setRowCount(0);
        updateTotal();
        dispose();
    }
}
