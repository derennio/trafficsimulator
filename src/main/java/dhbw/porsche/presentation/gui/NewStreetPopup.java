package dhbw.porsche.presentation.gui;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import dhbw.porsche.common.Point2D;
import dhbw.porsche.domain.Street;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class NewStreetPopup extends JDialog {
    private JTextField maxVelField;
    private JTextField endXField;
    private JTextField endYField;
    private MainWindow mw;
  
    private JOptionPane optionPane;
    private JComboBox<String> startSelector;
  
    private String btnString1 = "Create";
    private String btnString2 = "Close Window";

    NewStreetPopup(JFrame frame, MainWindow mw) {
        super(frame);
        this.mw = mw;
    
        //this.setLayout(null);
    
        this.setBounds(500, 300, 400, 400);

        String maxVelString = "Max velocity [m/s]";
        maxVelField = new JTextField(10);
        maxVelField.setText("100.0");

        String startSelectorText = "Select a street to start";
        String[] startChoices = new String[mw.getSim().streetService.getStreetAmount()];
        for (int i = 0; i < mw.getSim().streetService.getStreetAmount(); i++) {
            Street s = mw.getSim().streetService.getStreetById(i);
            startChoices[i] = Float.toString(s.end().getX()) + " | " + Float.toString(s.end().getY());
        }
        startSelector = new JComboBox<String>(startChoices);

        String endXString = "End Position X:";
        endXField = new JTextField(10);
        endXField.setText("1000.0");
        
        String endYString = "End Position Y:";
        endYField = new JTextField(10);
        endYField.setText("1000.0");

        Object[] array = { maxVelString, maxVelField, startSelectorText, startSelector, endXString, endXField, endYString, endYField};

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
                } else if (that.isVisible() && (e.getSource() == optionPane) && optionPane.getValue() == "Create") {
                    createStreet();
                    that.setVisible(false);
                }
            }
        });
        
        // // Make this dialog display it.
        setContentPane(optionPane);

        // Handle window closing correctly.
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);   
    }

    void createStreet() {
        float maxVel = 0;
        Point2D start = new Point2D(0, 0), end = new Point2D(0, 0);
        try {
            maxVel = Float.parseFloat(maxVelField.getText());
            end = new Point2D(Float.parseFloat(endXField.getText()), Float.parseFloat(endYField.getText()));
            start = mw.getSim().streetService.getStreetById(startSelector.getSelectedIndex()).end();
        } catch (Exception e) {
            System.out.println("Did not enter numbers!");
        }
        Street s = new Street(maxVel, start, end);
        mw.getSim().streetService.addStreet(s);
    }
}