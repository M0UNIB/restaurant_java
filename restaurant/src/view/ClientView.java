package view;

import controller.MenuController;
import model.Plats;
import model.Utilisateur;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;

public class ClientView extends JFrame {
    private final Utilisateur    user;
    private final MenuController menuController = new MenuController();

    public ClientView(Utilisateur user) {
        this.user = user;
        setTitle("Espace Client — " + user.getPrenom() + " " + user.getNom());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 500);
        buildUI();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildUI() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        
        JLabel header = new JLabel("Bonjour " + user.getPrenom() + " ! Que souhaitez-vous faire ?",
                SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        panel.add(header, BorderLayout.NORTH);

        
        JPanel menuPanel = new JPanel(new BorderLayout(5, 5));
        menuPanel.setBorder(BorderFactory.createTitledBorder("Parcourir le Menu"));

        JComboBox<model.Menu> menuCombo = new JComboBox<>();
        menuController.getAllMenus().forEach(menuCombo::addItem);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Plat", "Description", "Prix (DT)"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);

        menuCombo.addActionListener(e -> {
            model.setRowCount(0);
            model.Menu m = (model.Menu) menuCombo.getSelectedItem();
            if (m != null)
                menuController.getPlatsByMenu(m.getId()).forEach(p ->
                    model.addRow(new Object[]{p.getNom(), p.getDescription(),
                            String.format("%.2f", p.getPrix())}));
        });

        if (menuCombo.getItemCount() > 0) menuCombo.setSelectedIndex(0);

        menuPanel.add(menuCombo, BorderLayout.NORTH);
        menuPanel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(menuPanel, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton btnCommander = new JButton("Commander des plats");
        JButton btnCoord     = new JButton("Mes coordonnées");
        JButton btnLogout    = new JButton("Déconnexion");

        btnCommander.addActionListener(e -> new CommanderView(user));
        btnCoord.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Nom     : " + user.getNom() + "\n" +
                "Prénom  : " + user.getPrenom() + "\n" +
                "Login   : " + user.getUsername() + "\n" +
                "Type    : " + user.getType()));
        btnLogout.addActionListener(e -> { dispose(); new LoginView(); });

        actions.add(btnCommander); actions.add(btnCoord); actions.add(btnLogout);
        panel.add(actions, BorderLayout.SOUTH);

        add(panel);
    }
}
