import javax.swing.*;

public class Main {

    private static void createAndShowGUI() {

        //Creating the window with all its awesome snaky features
        Game f1 = new Game();

        //Setting up the window settings
        f1.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f1.setTitle("Snake");
        f1.setSize(1000, 1000);
        f1.setVisible(true);
    }


    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(Main::createAndShowGUI);
    }
}