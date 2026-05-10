package controller;

import dao.MenuDAO;
import dao.PlatsDAO;
import model.Menu;
import model.Plats;

import java.util.List;

public class MenuController {
    private final MenuDAO  menuDAO  = new MenuDAO();
    private final PlatsDAO platsDAO = new PlatsDAO();

    public List<Menu>  getAllMenus()              { return menuDAO.findAll(); }
    public List<Plats> getPlatsByMenu(int menuId) { return platsDAO.findByMenu(menuId); }
    public List<Plats> getAllPlats()              { return platsDAO.findAll(); }

    public void ajouterMenu(Menu m)   { menuDAO.insert(m); }
    public void modifierMenu(Menu m)  { menuDAO.update(m); }
    public void supprimerMenu(int id) { menuDAO.delete(id); }

    public void ajouterPlat(Plats p)   { platsDAO.insert(p); }
    public void modifierPlat(Plats p)  { platsDAO.update(p); }
    public void supprimerPlat(int id)  { platsDAO.delete(id); }
}
