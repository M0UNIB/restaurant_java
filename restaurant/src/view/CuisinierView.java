package view;

import controller.CommandeController;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CuisinierView extends JFrame {
    private final Utilisateur        user;
    private final CommandeController commandeController = new CommandeController();

    public CuisinierView(Utilisateur user) {
        this.user = user;
        setTitle("Espace Cuisinier — " + user.getPrenom() + " " + user.getNom());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 620);
        buildUI();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Commandes demandées",    buildTab(EtatCommande.EN_ATTENTE, true));
        tabs.addTab("En cours de traitement", buildTab(EtatCommande.EN_COURS,   false));
        tabs.addTab("Commandes servies",      buildTab(EtatCommande.SERVIE,     false));
        tabs.addTab("Gestion Menu / Plats",   buildMenuTab());

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

    private JPanel buildTab(String etat, boolean withActions) {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"#", "Utilisateur ID", "Date", "État"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        loadCommandes(model, etat);

        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Actualiser");
        btns.add(btnRefresh);
        btnRefresh.addActionListener(e -> loadCommandes(model, etat));

        if (withActions) {
            JButton btnDebuter  = new JButton("Commencer traitement");
            JButton btnAnnuler  = new JButton("Annuler");

            btnDebuter.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row < 0) { JOptionPane.showMessageDialog(null, "Sélectionnez une commande."); return; }
                commandeController.commencerTraitement((int) model.getValueAt(row, 0));
                loadCommandes(model, etat);
            });

            btnAnnuler.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row < 0) { JOptionPane.showMessageDialog(null, "Sélectionnez une commande."); return; }
                commandeController.annulerTraitement((int) model.getValueAt(row, 0));
                loadCommandes(model, etat);
            });

            btns.add(btnDebuter);
            btns.add(btnAnnuler);
        }

        if (EtatCommande.EN_COURS.equals(etat)) {
            JButton btnPrete = new JButton("Marquer comme Prête ✓");
            btnPrete.setForeground(new Color(0, 128, 0));
            btnPrete.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row < 0) { JOptionPane.showMessageDialog(null, "Sélectionnez une commande."); return; }
                int id = (int) model.getValueAt(row, 0);
                commandeController.marquerPrete(id);
                JOptionPane.showMessageDialog(null,
                        "Commande #" + id + " prête !\nNotification envoyée à la serveuse.");
                loadCommandes(model, etat);
            });
            btns.add(btnPrete);
        }

        panel.add(btns, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildMenuTab() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 20));
        JButton btn = new JButton("Ouvrir la gestion des menus et plats");
        btn.addActionListener(e -> new GestionMenuView());
        p.add(btn);
        return p;
    }

    private void loadCommandes(DefaultTableModel model, String etat) {
        model.setRowCount(0);
        commandeController.getCommandesByEtat(etat).forEach(c ->
            model.addRow(new Object[]{c.getId(), c.getUtilisateurId(), c.getDateCommande(), c.getEtat()}));
    }
}
