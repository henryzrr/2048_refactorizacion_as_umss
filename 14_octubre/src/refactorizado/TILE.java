package refactorizado;

import javax.swing.*;
import java.awt.*;

class TILE
{
    private Game_Panel game_panel;
    public int value;
    public JLabel LABEL;

    public final Font font = new Font("Lithograph", Font.BOLD, 50);
    public final Font big_number = new Font("Lithograph", Font.BOLD, 35);
    public final Color DEFAULT = new Color(119,110,101);
    public final Color REMAINING = new Color(249,246,242);

    public TILE(Game_Panel game_panel, int value)
    {
        this.game_panel = game_panel;
        this.value = value;
        LABEL = new JLabel(Integer.toString(value));
        LABEL.setSize(40,40);
        LABEL.setFont(font);
        LABEL.setForeground(DEFAULT);
    }

    public void doubleValue()
    {
        value = value<<1;
        LABEL.setText(Integer.toString(value));
        if (value > 4)
        {
            LABEL.setForeground(REMAINING);
        }
        if (value == 2048)
        {
            game_panel.achieved_goal = true;
        }
    }
}
