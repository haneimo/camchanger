package work.katagata.camchanger;

import javax.swing.*;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CameraApp app = new CameraApp();
            app.setVisible(true);
        });
    }
}

