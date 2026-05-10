package view;

import controller.MenuController;
import model.Plats;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

public class GestionMenuView extends JFrame {
    private final MenuController menuController = new MenuController();

    // Menu panel
    private final DefaultTableModel menuModel = new DefaultTableModel(new String[]{"ID", "Nom", "Description"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable menuTable = new JTable(menuModel);

    // Plats panel
    private final DefaultTableModel platModel = new DefaultTableModel(
            new String[]{"ID", "Nom", "Description", "Prix (DT)", "Menu ID"}, 0) {
        public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable platTable = new JTable(platModel);

    public GestionMenuView() {
        setTitle("Gestion des Menus et Plats");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        buildUI();
        loadMenus();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Menus",  buildMenuPanel());
        tabs.addTab("Plats",  buildPlatsPanel());
        add(tabs);
    }

    // ── Menus tab ───────────────────────────────────────────────────────────────
    private JPanel buildMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(menuTable), BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd  = new JButton("Ajouter");
        JButton btnEdit = new JButton("Modifier");
        JButton btnDel  = new JButton("Supprimer");
        JButton btnSave = new JButton("Enregistrer");
        JButton btnClose= new JButton("Fermer");
        btns.add(btnAdd); btns.add(btnEdit); btns.add(btnDel); btns.add(btnSave); btns.add(btnClose);
        panel.add(btns, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            String nom  = JOptionPane.showInputDialog(this, "Nom du menu :");
            String desc = JOptionPane.showInputDialog(this, "Description :");
            if (nom != null && !nom.isBlank()) {
                model.Menu m = new model.Menu(0, nom, desc);
                menuController.ajouterMenu(m);
                loadMenus();
            }
        });

        btnEdit.addActionListener(e -> {
            int row = menuTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez un menu."); return; }
            int    id   = (int)    menuModel.getValueAt(row, 0);
            String nom  = JOptionPane.showInputDialog(this, "Nouveau nom :", menuModel.getValueAt(row, 1));
            String desc = JOptionPane.showInputDialog(this, "Nouvelle description :", menuModel.getValueAt(row, 2));
            if (nom != null && !nom.isBlank()) {
                menuController.modifierMenu(new model.Menu(id, nom, desc));
                loadMenus();
            }
        });

        btnDel.addActionListener(e -> {
            int row = menuTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez un menu."); return; }
            int id = (int) menuModel.getValueAt(row, 0);
            if (JOptionPane.showConfirmDialog(this, "Supprimer ce menu ?") == JOptionPane.YES_OPTION) {
                menuController.supprimerMenu(id);
                loadMenus();
            }
        });

        btnSave.addActionListener(e -> {
            loadMenus();
            JOptionPane.showMessageDialog(this, "Liste sauvegardée en base de données.");
        });

        btnClose.addActionListener(e -> dispose());
        return panel;
    }

    // ── Plats tab ────────────────────────────────────────────────────────────────
    private JPanel buildPlatsPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JScrollPane(platTable), BorderLayout.CENTER);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd  = new JButton("Ajouter");
        JButton btnEdit = new JButton("Modifier");
        JButton btnDel  = new JButton("Supprimer");
        JButton btnSave = new JButton("Enregistrer");
        JButton btnClose= new JButton("Fermer");
        btns.add(btnAdd); btns.add(btnEdit); btns.add(btnDel); btns.add(btnSave); btns.add(btnClose);
        panel.add(btns, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            List<model.Menu> menus = menuController.getAllMenus();
            if (menus.isEmpty()) { JOptionPane.showMessageDialog(this, "Créez d'abord un menu."); return; }
            model.Menu selectedMenu = (model.Menu) JOptionPane.showInputDialog(this, "Choisir le menu :", "Menu",
                    JOptionPane.QUESTION_MESSAGE, null, menus.toArray(), menus.get(0));
            if (selectedMenu == null) return;
            String nom  = JOptionPane.showInputDialog(this, "Nom du plat :");
            String desc = JOptionPane.showInputDialog(this, "Description :");
            String prix = JOptionPane.showInputDialog(this, "Prix (DT) :");
            if (nom != null && !nom.isBlank() && prix != null) {
                try {
                    Plats p = new Plats(0, nom, desc, Double.parseDouble(prix), selectedMenu.getId());
                    menuController.ajouterPlat(p);
                    loadPlats();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Prix invalide.");
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = platTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez un plat."); return; }
            int    id   = (int)    platModel.getValueAt(row, 0);
            String nom  = JOptionPane.showInputDialog(this, "Nouveau nom :", platModel.getValueAt(row, 1));
            String desc = JOptionPane.showInputDialog(this, "Nouvelle description :", platModel.getValueAt(row, 2));
            String prix = JOptionPane.showInputDialog(this, "Nouveau prix :", platModel.getValueAt(row, 3));
            if (nom != null && prix != null) {
                try {
                    menuController.modifierPlat(new Plats(id, nom, desc,
                            Double.parseDouble(prix.toString()), (int) platModel.getValueAt(row, 4)));
                    loadPlats();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Prix invalide.");
                }
            }
        });

        btnDel.addActionListener(e -> {
            int row = platTable.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Sélectionnez un plat."); return; }
            int id = (int) platModel.getValueAt(row, 0);
            if (JOptionPane.showConfirmDialog(this, "Supprimer ce plat ?") == JOptionPane.YES_OPTION) {
                menuController.supprimerPlat(id);
                loadPlats();
            }
        });

        btnSave.addActionListener(e -> {
            loadPlats();
            JOptionPane.showMessageDialog(this, "Liste sauvegardée en base de données.");
        });

        btnClose.addActionListener(e -> dispose());
        loadPlats();
        return panel;
    }

    private void loadMenus() {
        menuModel.setRowCount(0);
        for (model.Menu m : menuController.getAllMenus())
            menuModel.addRow(new Object[]{m.getId(), m.getNom(), m.getDescription()});
    }

    private void loadPlats() {
        platModel.setRowCount(0);
        for (Plats p : menuController.getAllPlats())
            platModel.addRow(new Object[]{p.getId(), p.getNom(), p.getDescription(),
                    String.format("%.2f", p.getPrix()), p.getMenuId()});
    }
}
