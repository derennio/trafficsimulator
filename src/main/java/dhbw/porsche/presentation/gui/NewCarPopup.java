package dhbw.porsche.presentation.gui;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewCarPopup extends JDialog {
    private JTextField maxVelField;
    private JTextField maxBrakeField;
    private JTextField relPosField;
  
    private JOptionPane optionPane;
    private JComboBox<Integer> streetSelector;
  
    private String btnString1 = "Enter";
    private String btnString2 = "Cancel";

    NewCarPopup(JFrame frame, MainWindow mw) {
        super(frame);
    
        //this.setLayout(null);
    
        this.setBounds(500, 300, 400, 400);

        String maxVelString = "Max velocity [m/s^2]";
        maxVelField = new JTextField(10);
        String maxBrakeString = "Max brake force [m/s^2]";
        maxBrakeField = new JTextField(10);

        String streetSelectorText = "Select a street to start";
        Integer[] streetChoices = new Integer[mw.getSim().streetService.getStreetAmount()];
        for (int i = 0; i < mw.getSim().streetService.getStreetAmount(); i++) {
            streetChoices[i] = i;
        }
        streetSelector = new JComboBox<>(streetChoices);

        String relPosText = "Enter the relative Position to start: [0-1]";
        relPosField = new JTextField(10);

        Object[] array = { maxVelString, maxVelField, maxBrakeString, maxBrakeField, streetSelectorText, streetSelector, relPosText, relPosField};

        // Create an array specifying the number of dialog buttons
        // and their text.
        Object[] options = { btnString1, btnString2 };

        // Create the JOptionPane.
        optionPane = new JOptionPane(array, JOptionPane.PLAIN_MESSAGE,
            JOptionPane.YES_NO_OPTION, null, options, options[0]);

        // // Make this dialog display it.
        setContentPane(optionPane);

        // Handle window closing correctly.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        
    }
}