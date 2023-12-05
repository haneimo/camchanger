package work.katagata.camchanger;
import javax.swing.*;
import org.opencv.core.*;


public class App {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CameraApp app = new CameraApp();
            app.setVisible(true);
        });
    }
}

