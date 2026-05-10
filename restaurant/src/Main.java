import view.LoginView;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Use system look & feel for native appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(LoginView::new);
    }
}
