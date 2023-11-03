package dhbw.porsche.presentation.gui;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextField;

import dhbw.porsche.business.controller.PIController;
import dhbw.porsche.domain.Car;
import dhbw.porsche.domain.IVehicle;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NewCarPopup extends JDialog {
    private JTextField maxVelField;
    private JTextField maxAccField;
    private JTextField maxBrakeField;
    private JSlider relPosSlider;
    private MainWindow mw;
  
    private JOptionPane optionPane;
    private JComboBox<Integer> streetSelector;
  
    private String btnString1 = "Spawn";
    private String btnString2 = "Close Window";

    NewCarPopup(JFrame frame, MainWindow mw) {
        super(frame);
        this.mw = mw;
    
        //this.setLayout(null);
    
        this.setBounds(500, 300, 400, 400);

        String maxVelString = "Max velocity [m/s]";
        maxVelField = new JTextField(10);
        maxVelField.setText("100.0");
        String maxAccString = "Max Acceleration [m/s^2]";
        maxAccField = new JTextField(10);
        maxAccField.setText("10.0");
        String maxBrakeString = "Max brake force [m/s^2]";
        maxBrakeField = new JTextField(10);
        maxBrakeField.setText("5.0");

        String streetSelectorText = "Select a street to start";
        Integer[] streetChoices = new Integer[mw.getSim().streetService.getStreetAmount()];
        for (int i = 0; i < mw.getSim().streetService.getStreetAmount(); i++) {
            streetChoices[i] = i;
        }
        streetSelector = new JComboBox<Integer>(streetChoices);

        String relPosText = "Enter the relative Position to start: [0-1]";
        relPosSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);

        Object[] array = { maxVelString, maxVelField, maxAccString, maxAccField, maxBrakeString, maxBrakeField, streetSelectorText, streetSelector, relPosText, relPosSlider};

        // Create an array specifying the number of dialog buttons
        // and their text.
        Object[] options = { btnString1, btnString2 };
        // Create the JOptionPane.
        optionPane = new JOptionPane(array, JOptionPane.PLAIN_MESSAGE,
            JOptionPane.YES_NO_OPTION, null, options, options[0]);
        var that = this;
        optionPane.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if (that.isVisible() && (e.getSource() == optionPane) && optionPane.getValue() == "Close Window") {
                    that.setVisible(false);
                } else if (that.isVisible() && (e.getSource() == optionPane) && optionPane.getValue() == "Spawn") {
                    createCar();
                    that.setVisible(false);
                }
            }
        });
        
        // // Make this dialog display it.
        setContentPane(optionPane);

        // Handle window closing correctly.
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);   
    }

    void createCar() {
        float maxAccel = 0, maxBrake = 0, maxVelocity = 0, relPos = 0;
        int streetIndex = 0;
        try {
            streetIndex = (int)streetSelector.getSelectedItem();
            maxAccel = Float.parseFloat(maxAccField.getText());
            maxBrake = Float.parseFloat(maxBrakeField.getText());
            maxVelocity = Float.parseFloat(maxVelField.getText());
            relPos = relPosSlider.getValue() / 100.0f;
        } catch (Exception e) {
            System.out.println("Did not enter numbers!");
        }
        IVehicle v = new Car(mw.getSim().streetService, mw.getSim().fileService, new PIController(0.5f, 0.1f), maxAccel, maxBrake, maxVelocity, 63).translocate(relPos, streetIndex);
        mw.getSim().streetService.addVehicle(v);
    }
}