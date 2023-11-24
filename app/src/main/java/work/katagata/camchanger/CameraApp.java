package work.katagata.camchanger;

import java.io.ByteArrayInputStream;

import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import org.opencv.core.*;
import org.opencv.videoio.*;
import org.opencv.imgcodecs.Imgcodecs;

import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_HEIGHT;
import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_WIDTH;

public class CameraApp extends JFrame {
    private JLabel videoLabel;
    private CameraDevice cameraDevice;

    public CameraApp() {
        setTitle("Camera App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);

        videoLabel = new JLabel();
        add(videoLabel);

        cameraDevice = new CameraDevice();
        cameraDevice.start();

        Timer timer = new Timer(1000 / 30, e -> {
            BufferedImage frame = null;
            try{
                frame = cameraDevice.getFrame();
            }catch(Exception ex){
                frame = null;
            }
            if (frame != null) {
                ImageIcon icon = new ImageIcon(frame);
                videoLabel.setIcon(icon);
            }
        });
        timer.start();
    }
}

class CameraDevice {
    private VideoCapture videoCapture;

    public CameraDevice() {
        int width = 640;
        int height = 480;

        videoCapture = new VideoCapture();
        videoCapture.set(CAP_PROP_FRAME_WIDTH, width);
        videoCapture.set(CAP_PROP_FRAME_HEIGHT, height);
    }

    public void start() {
        videoCapture.open(0);
    }

    public BufferedImage getFrame() throws Exception {
        Mat capturedImage = new Mat();
        this.videoCapture.read(capturedImage);
        return convertMat2BufferedImage(capturedImage);
    }

    static BufferedImage convertMat2BufferedImage(Mat matrix)throws Exception {        
        MatOfByte mob=new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        byte ba[]=mob.toArray();

        BufferedImage bi=ImageIO.read(new ByteArrayInputStream(ba));
        return bi;
    }    
}
