package dhbw.porsche.presentation.gui;

import dhbw.porsche.Simulator;

class Gui {
    public static void main(String[] args) {
        Simulator sim = new Simulator();
        new MainWindow(sim);
    } 
}
