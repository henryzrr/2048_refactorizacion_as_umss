package refactorizado;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class Game_Starter implements Serializable {
    public static void main(String argc[]) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        JFrame window = new JFrame();
        window.setSize(new Dimension(Game_Panel.HW, Game_Panel.HW));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setContentPane(new Game_Panel());
        window.pack();
        window.setVisible(true);
    }
}