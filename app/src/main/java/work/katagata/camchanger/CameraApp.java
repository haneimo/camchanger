package work.katagata.camchanger;

import java.io.ByteArrayInputStream;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;

import org.opencv.core.*;
import org.opencv.videoio.*;
import org.opencv.imgcodecs.Imgcodecs;


import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_HEIGHT;
import static org.opencv.videoio.Videoio.CAP_PROP_FRAME_WIDTH;

public class CameraApp extends JFrame {
    
    private JLabel videoLabel;
    private CameraDevice cam0;
    private CameraDevice cam1;

    public CameraApp() {
        setTitle("Camera App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 480);

        videoLabel = new JLabel();
        add(videoLabel);

        cam0 = new CameraDevice(0);
        cam1 = new CameraDevice(1);

        cam0.start();
        cam1.start();

        Timer timer = new Timer(1000 / 16, e -> {
            BufferedImage frame0 = null;
            BufferedImage frame1 = null;
            BufferedImage frame3 = null;

            try{
                frame0 = cam0.getFrame();
                frame1 = cam1.getFrame();
                
                int width = frame0.getWidth() + frame1.getWidth();
                int height = Math.max(frame0.getHeight(), frame1.getHeight());
                
                frame3 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                
                Graphics2D g2d = frame3.createGraphics();
                g2d.drawImage(frame0, 0, 0, null);
                g2d.drawImage(frame1, frame0.getWidth(), 0, null);
                g2d.dispose();
            }catch(Exception ex){
                frame0 = null;
                frame1 = null;
            }
            if (frame3 != null) {
                ImageIcon icon = new ImageIcon(frame3);
                videoLabel.setIcon(icon);
            }
        });
        timer.start();
    }
}

class CameraDevice {
    private VideoCapture videoCapture;
    private int index;

    public CameraDevice(int index) {

        this.index = index;
        videoCapture = new VideoCapture();

    }

    public void start() {
        int cameraCount = (int) videoCapture.get(Videoio.CAP_PROP_FRAME_COUNT);
        System.out.println("Camera count: " + cameraCount);
        
        videoCapture.open(this.index);
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
