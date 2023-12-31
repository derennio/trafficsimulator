
package dhbw.porsche.presentation.gui; 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.MouseInputListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import dhbw.porsche.Simulator;
import dhbw.porsche.common.Point2D;
import dhbw.porsche.domain.IVehicle;
import dhbw.porsche.domain.Street;
import lombok.Getter;

public class MainWindow implements ActionListener {
    @Getter
    private Simulator sim;
    private BufferedImage image;
    private JMenuItem saveLogButton;
    private JMenuItem createCarButton;
    private JMenuItem createStreetButton;
    private JFrame frame;

    public MainWindow(Simulator sim) {
        this.sim = sim;
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {}

        try {
            image = ImageIO.read(new File("src/main/java/dhbw/porsche/presentation/gui/imgs/GT3R.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame = new JFrame("Traffic simulator gui");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setJMenuBar(addMenuBar(frame));
        TestPane testPane = new TestPane(frame, this);
        frame.add(testPane);
        frame.pack();
        frame.addMouseListener(testPane);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        service.scheduleAtFixedRate(() -> {
            try {
                frame.repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    JMenuBar addMenuBar(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();

        //Build the first menu.
        JMenu logMenu = new JMenu("Log");
        menuBar.add(logMenu);

        //Build the first menu.
        JMenu simMenu = new JMenu("Simulation");
        menuBar.add(simMenu);

        //a group of JMenuItems
        saveLogButton = new JMenuItem("Save log to file");
        saveLogButton.addActionListener(this);
        logMenu.add(saveLogButton);

        createCarButton = new JMenuItem("New car");
        createCarButton.addActionListener(this);
        simMenu.add(createCarButton);

        createStreetButton = new JMenuItem("New street");
        createStreetButton.addActionListener(this);
        simMenu.add(createStreetButton);
        return menuBar;
    }

    public class TestPane extends JPanel implements MouseInputListener {
        private int xOffset = 1000;
        private int yOffset = 1000;
        private double scale = .2;
        private JFrame parentFrame;
        private MainWindow mw;

        TestPane(JFrame pf, MainWindow mw) {
            parentFrame = pf;
            this.mw = mw;
            this.setLayout(new BorderLayout());
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(600, 600);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            for (int i = 0; i < sim.streetService.getStreetAmount(); i++) {
                drawStreet(g, sim.streetService.getStreetById(i));
            }

            for (int i = 0; i < sim.streetService.getVehicles().size(); i++) {
                drawVehicle(g, sim.streetService.getVehicles().get(i));
            }
        }

        public static BufferedImage rotatedImage(BufferedImage bimg, Double angle) {
            double sin = Math.abs(Math.sin(angle)),
                   cos = Math.abs(Math.cos(angle));
            int w = bimg.getWidth();
            int h = bimg.getHeight();
            int neww = (int) Math.floor(w*cos + h*sin),
                newh = (int) Math.floor(h*cos + w*sin);
            BufferedImage rotated = new BufferedImage(neww, newh, bimg.getType());
            Graphics2D graphic = rotated.createGraphics();
            graphic.translate((neww-w)/2, (newh-h)/2);
            graphic.scale(.02, .02);
            graphic.rotate(angle, w/2, h/2);
            graphic.drawRenderedImage(bimg, null);
            graphic.dispose();
            return rotated;
        }

        void drawStreet(Graphics g, Street s) {
            drawLine(g, s.start().getX(), s.start().getY(), s.end().getX(), s.end().getY());
            double angle = Math.atan2(s.end().getX() - s.start().getX(), s.end().getY() - s.start().getY());

            Point2D middlePos = new Point2D((s.start().getX() + s.end().getX()) / 2, (s.start().getY() + s.end().getY()) / 2);
            drawLine(g, middlePos.getX(), middlePos.getY(), middlePos.getX() + (float)Math.sin(angle + 1.25 * Math.PI) * 50, middlePos.getY() + (float)Math.cos(angle + 1.25 * Math.PI) * 50);
            drawLine(g, middlePos.getX(), middlePos.getY(), middlePos.getX() + (float)Math.sin(angle - 1.25 * Math.PI) * 50, middlePos.getY() + (float)Math.cos(angle - 1.25 * Math.PI) * 50);
        }

        void drawVehicle(Graphics g, IVehicle v) {
            Street s = sim.streetService.getStreetById(v.getStreetIdx());
            double angle = Math.atan2(s.end().getX() - s.start().getX(), s.end().getY() - s.start().getY());

            Point2D pos = new Point2D((float)((s.end().getX() - s.start().getX()) * v.getRelPosition() + s.start().getX()), (float)((s.end().getY() - s.start().getY()) * v.getRelPosition() + s.start().getY()));
            // Drawing the rotated image at the required drawing locations
            drawRotatedImage(g, image, pos.getX(), pos.getY(), 2.5*Math.PI - angle);

        }

        void drawRotatedImage(Graphics g, BufferedImage img, float x, float y, double angle) {
            Graphics2D g2d = (Graphics2D) g;

            // Set the rotation transformation
            AffineTransform transform = new AffineTransform();
            transform.translate((x + xOffset) * scale, (y + yOffset) * scale + parentFrame.getJMenuBar().getHeight());
            transform.rotate((angle));
            transform.scale(.04, .04);
            transform.translate(-image.getWidth() / 2, -image.getHeight() / 2);
    
            // Apply the transformation to the Graphics2D object
            g2d.setTransform(transform);
    
            // Draw the rotated image
            g2d.drawImage(img, 0, 0, this);
    
            // Reset the transformation for future drawing
            g2d.setTransform(new AffineTransform());
        }

        void drawLine(Graphics g, float startX, float startY, float endX, float endY) {
            g.drawLine((int)((startX + xOffset) * scale), (int)((startY + yOffset) * scale), (int)((endX + xOffset) * scale), (int)((endY + yOffset) * scale));
        }

        void drawRect(Graphics g, float x, float y, float width, float height) {
            g.drawRect((int)((x + xOffset) * scale), (int)((y + yOffset) * scale), (int)(width * scale), (int)(height * scale));
        }

        void drawImage(Graphics g, BufferedImage img, float x, float y) {
            g.drawImage(img, (int)((x + xOffset) * scale), (int)((y + yOffset) * scale), null);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            List<Float> distances = new ArrayList<>();
            for (IVehicle v : sim.streetService.getVehicles()) {
                Street s = sim.streetService.getStreetById(v.getStreetIdx());
                Point2D pos = new Point2D((float)(s.start().getX() + (s.end().getX() - s.start().getX()) * v.getRelPosition()), (float)(s.start().getY() + (s.end().getY() - s.start().getY()) * v.getRelPosition()));
                Point2D posOnScreen = new Point2D((float)((pos.getX() + xOffset) * scale), (float)((pos.getY() + yOffset) * scale + parentFrame.getJMenuBar().getHeight()));
                distances.add(posOnScreen.distanceTo(new Point2D(e.getX(), e.getY())));
            }
            int index = distances.indexOf(Collections.min(distances));

            if (distances.get(index) < 30) {
                CarDetailPopup carDetailPopup = new CarDetailPopup(frame, mw, index);
                carDetailPopup.setVisible(true);
            }
        }

        @Override
        public void mouseEntered(MouseEvent arg0) {}
        @Override
        public void mouseExited(MouseEvent arg0) {}
        @Override
        public void mousePressed(MouseEvent arg0) {}
        @Override
        public void mouseReleased(MouseEvent arg0) {}
        @Override
        public void mouseDragged(MouseEvent arg0) {}
        @Override
        public void mouseMoved(MouseEvent arg0) {}
    }

    void saveLog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");   
        
JFrame parentFrame = new JFrame();
        int userSelection = fileChooser.showSaveDialog(parentFrame);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
            try {
                sim.fileService.saveData(fileToSave.getAbsolutePath());   
            } catch (Exception e) {
                System.err.println(e);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == this.saveLogButton) {
            saveLog();
        } else if (ae.getSource() == this.createCarButton) {

            //preparation for popups
            NewCarPopup newCarPopup = new NewCarPopup(frame, this);
            newCarPopup.setVisible(true);
        } else if (ae.getSource() == this.createStreetButton) {
            NewStreetPopup newStreetPopup = new NewStreetPopup(frame, this);
            newStreetPopup.setVisible(true);
        }
    }
}