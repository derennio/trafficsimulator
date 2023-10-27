
package dhbw.porsche.presentation.gui; 

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import dhbw.porsche.Simulator;
import dhbw.porsche.common.Point2D;
import dhbw.porsche.domain.IVehicle;
import dhbw.porsche.domain.Street;

public class MainWindow {
    private Simulator sim;

    public MainWindow(Simulator sim) {
        this.sim = sim;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame("Traffic simulator gui");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class TestPane extends JPanel {
        private int xOffset = 100;
        private int yOffset = 100;

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            // g.fillOval(100, 100, 30, 30);
            for (int i = 0; i < sim.streetService.getStreetAmount(); i++) {
                Street s = sim.streetService.getStreetById(i);
                g.drawLine((int)s.start().getX() + xOffset,(int)s.start().getY() + yOffset, (int)s.end().getX() + xOffset, (int)s.end().getY() + yOffset);
            }

            for (int i = 0; i < sim.streetService.getVehicles().size(); i++) {
                IVehicle v = sim.streetService.getVehicles().get(i);
                Street s = sim.streetService.getStreetById(v.getStreetIdx() +3);

                System.out.println(s.isInverted());

                g.setColor(Color.RED);
                int inverted = s.isInverted() ? -1 : 1;
                if (s.isVertical()) {
                    Point2D pos = new Point2D(s.start().getX(), (float)(s.start().getY() + v.getRelPosition() * s.getLength() * inverted));
                    g.drawRect((int)pos.getX()-10 + xOffset, (int)pos.getY() - v.getLength() / 2 + yOffset, 20, v.getLength());
                } else {
                    Point2D pos = new Point2D((float)(s.start().getX() + v.getRelPosition() * s.getLength() * inverted), (float)(s.start().getY()));
                    g.drawRect((int)pos.getX() - v.getLength()/ 2 + xOffset, (int)pos.getY()-10 + yOffset, v.getLength(), 20);
                }
            }
        }
    }
}

