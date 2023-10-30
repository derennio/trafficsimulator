
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
        private int xOffset = 1000;
        private int yOffset = 1000;
        private double scale = .1;

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
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

        void drawStreet(Graphics g, Street s) {
            drawLine(g, s.start().getX(), s.start().getY(), s.end().getX(), s.end().getY());

            if (s.isVertical()) {
                float middleY = (s.start().getY() + s.end().getY()) / 2;
                drawLine(g, s.start().getX(), middleY, s.start().getX() + 10, middleY + 10 * (s.start().getY() > s.end().getY() ? 1 : -1));
                drawLine(g, s.start().getX(), middleY, s.start().getX() - 10, middleY + 10 * (s.start().getY() > s.end().getY() ? 1 : -1));
            } else {
                float middleX = (s.start().getX() + s.end().getX()) / 2;
                drawLine(g, middleX, s.start().getY(), middleX + 10 * (s.start().getX() > s.end().getX() ? 1 : -1), s.start().getY() + 10);
                drawLine(g, middleX, s.start().getY(), middleX + 10 * (s.start().getX() > s.end().getX() ? 1 : -1), s.start().getY() - 10);
            }
        }

        void drawVehicle(Graphics g, IVehicle v) {
            Street s = sim.streetService.getStreetById(v.getStreetIdx() +3);

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

        void drawLine(Graphics g, float startX, float startY, float endX, float endY) {
            g.drawLine((int)((startX + xOffset) * scale), (int)((startY + yOffset) * scale), (int)((endX + xOffset) * scale), (int)((endY + yOffset) * scale));
        }
    }
}

