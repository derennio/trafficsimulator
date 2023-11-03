package dhbw.porsche.presentation.gui;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CarDetailPopup extends JDialog {
    private JLabel velocityText = new JLabel("");
    private JCheckBox brakeBox = new JCheckBox();
    private MainWindow mw;
    private int carIdx;
  
    private JOptionPane optionPane;
  
    private String btnString2 = "Close Window";

    CarDetailPopup(JFrame frame, MainWindow mw, int carIdx) {
        super(frame);
        this.mw = mw;
        this.carIdx = carIdx;
    
        //this.setLayout(null);
    
        this.setBounds(500, 300, 400, 400);

        Object[] array = { velocityText, new JLabel("Set tik to brake: "), brakeBox};

        // Create an array specifying the number of dialog buttons
        // and their text.
        Object[] options = { btnString2 };
        // Create the JOptionPane.
        optionPane = new JOptionPane(array, JOptionPane.PLAIN_MESSAGE,
            JOptionPane.CANCEL_OPTION, null, options, options[0]);
        var that = this;
        optionPane.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if (that.isVisible() && (e.getSource() == optionPane) && optionPane.getValue() == "Close Window") {
                    that.setVisible(false);
                }
            }
        });
        
        // // Make this dialog display it.
        setContentPane(optionPane);

        // Handle window closing correctly.
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
        service.scheduleAtFixedRate(() -> {
            try {
                that.repaint();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void repaint() {
        super.repaint();
        velocityText.setText("Current speed: " + mw.getSim().streetService.getVehicles().get(carIdx).getVelocity() + " m/s");

        if (brakeBox.isSelected()) {
            mw.getSim().streetService.getVehicles().get(carIdx).overrideController(true, -10);
        } else {
            mw.getSim().streetService.getVehicles().get(carIdx).overrideController(false);
        }
    }
}