
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

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.GREEN);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            g.setColor(Color.BLACK);
            // g.fillOval(100, 100, 30, 30);
            for (int i = 0; i < sim.streetService.getStreetAmount(); i++) {
                Street s = sim.streetService.getStreetById(i);
                g.drawLine((int)s.start().getX() + 100,(int)s.start().getY() + 100, (int)s.end().getX() + 100, (int)s.end().getY() + 100);
            }
        }
    }
}

