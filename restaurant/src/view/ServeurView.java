package view;

import controller.CommandeController;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ServeurView extends JFrame {
    private final Utilisateur        user;
    private final CommandeController commandeController = new CommandeController();

    public ServeurView(Utilisateur user) {
        this.user = user;
        setTitle("Espace Serveur — " + user.getPrenom() + " " + user.getNom());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 600);
        buildUI();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Menu & Commander",        buildCommanderTab());
        tabs.addTab("Commandes en cours",      buildCommandesTab(EtatCommande.EN_COURS));
        tabs.addTab("Commandes reçues (prêtes)", buildCommandesTab(EtatCommande.PRETE));
        tabs.addTab("Générer une facture",     buildFactureTab());

        JPanel panel = new JPanel(new BorderLayout());
        JLabel header = new JLabel("Bonjour " + user.getPrenom() + " !", SwingConstants.LEFT);
        header.setBorder(BorderFactory.createEmptyBorder(8, 12, 4, 0));
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        panel.add(header, BorderLayout.NORTH);
        panel.add(tabs, BorderLayout.CENTER);

        JButton btnLogout = new JButton("Déconnexion");
        btnLogout.addActionListener(e -> { dispose(); new LoginView(); });
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnLogout);
        panel.add(south, BorderLayout.SOUTH);
        add(panel);
    }

    private JPanel buildCommanderTab() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 20));
        JButton btn = new JButton("Ouvrir la fenêtre de commande");
        btn.addActionListener(e -> new CommanderView(user));
        p.add(btn);
        return p;
    }

    private JPanel buildCommandesTab(String etat) {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"#", "Utilisateur ID", "Date", "État"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);

        JButton btnRefresh = new JButton("Actualiser");
        JButton btnMarquerServie = new JButton("Marquer comme Servie");
        btnRefresh.addActionListener(e -> loadCommandes(model, etat));
        btnMarquerServie.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(null, "Sélectionnez une commande."); return; }
            int id = (int) model.getValueAt(row, 0);
            commandeController.marquerServie(id);
            loadCommandes(model, etat);
        });

        loadCommandes(model, etat);

        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btns.add(btnRefresh);
        if (EtatCommande.PRETE.equals(etat)) btns.add(btnMarquerServie);
        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildFactureTab() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"# Commande", "Utilisateur ID", "Date", "État"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        loadCommandes(model, EtatCommande.SERVIE);

        JTextArea factureArea = new JTextArea(10, 40);
        factureArea.setEditable(false);
        factureArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JButton btnGenerer = new JButton("Générer la facture");
        JButton btnRefresh = new JButton("Actualiser");

        btnRefresh.addActionListener(e -> loadCommandes(model, EtatCommande.SERVIE));
        btnGenerer.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(null, "Sélectionnez une commande."); return; }
            int commandeId = (int) model.getValueAt(row, 0);
            Facture f = commandeController.getFacture(commandeId);
            if (f == null) f = commandeController.genererFacture(commandeId);
            List<LigneCommande> lignes = commandeController.getLignes(commandeId);
            StringBuilder sb = new StringBuilder();
            sb.append("══════════════════════════════\n");
            sb.append("          FACTURE #").append(f.getId()).append("\n");
            sb.append("══════════════════════════════\n");
            sb.append(String.format("Commande : #%d%n", commandeId));
            sb.append(String.format("Date     : %s%n%n", f.getDateFacture()));
            for (LigneCommande lc : lignes)
                sb.append(String.format("  Plat ID %d  x%d  @ %.2f DT  = %.2f DT%n",
                        lc.getPlatId(), lc.getQuantite(), lc.getPrixUnitaire(), lc.getSousTotal()));
            sb.append("\n──────────────────────────────\n");
            sb.append(String.format("TOTAL    : %.2f DT%n", f.getMontantTotal()));
            sb.append("══════════════════════════════\n");
            factureArea.setText(sb.toString());
        });

        panel.add(new JScrollPane(table), BorderLayout.NORTH);
        panel.add(new JScrollPane(factureArea), BorderLayout.CENTER);
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btns.add(btnRefresh); btns.add(btnGenerer);
        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    private void loadCommandes(DefaultTableModel model, String etat) {
        model.setRowCount(0);
        commandeController.getCommandesByEtat(etat).forEach(c ->
            model.addRow(new Object[]{c.getId(), c.getUtilisateurId(), c.getDateCommande(), c.getEtat()}));
    }
}
