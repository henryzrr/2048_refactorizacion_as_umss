package P2048;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Game_Panel extends JPanel implements KeyListener
{
    //instance variables
    public TILE panel[][];
    public byte current_tiles;
    public boolean achieved_goal;

    public static final int default_start_A = 2;
    public static final int default_start_B = 4;
    public static final int HW = 489;
    public static final int separation_length = 4;
    public static final int block_width = 119;
    public static final int block_center = 119>>1;
    public static final int RANDOM = 101;
    public static final byte ROWS_COLS = 4;
    public static final byte real_end = ROWS_COLS-1;
    public static final byte fake_end = 0;
    public static final byte left_increment = 1;
    public static final byte right_increment = -1;  

    //keyboard ascii numbers
    public static final byte LEFT = 37;
    public static final byte RIGHT = 39;
    public static final byte UP = 38;
    public static final byte DOWN = 40;

    //Colors of different numbers
    public static final Color BACKGROUND = new Color(187,173,160);
    public static final Color DEFAULT_TILE = new Color(204,192,179);
    public static final Color TWO = new Color(238,228,218);
    public static final Color FOUR = new Color(237,224,200);
    public static final Color EIGHT = new Color(242,177,121);
    public static final Color SIXTEEN = new Color(245,149,98);
    public static final Color THIRTYTWO = new Color(246,124,95);
    public static final Color SIXTYFOUR = new Color(246,94,59);
    public static final Color REMAINING = new Color(237,204,97);

    //x and y positions of the four possible places of a tile.
    public static final int JUMPS[] = {separation_length,
                                      (block_width+separation_length),
                                      ((block_width<<1)+separation_length),
                                      (((block_width<<1)+block_width)+separation_length)};

    public boolean is_moved = false;
    public final Font END = new Font("Lithograph", Font.BOLD, 50);
    public static final RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    public Game_Panel()
    {
        setBackground(BACKGROUND);
        setPreferredSize(new Dimension(HW,HW));
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);

        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);      
        panel = new TILE[ROWS_COLS][ROWS_COLS];
        achieved_goal = false;

        //same as generate method, but thought it'd be a waste
        //to call it when we're initializing.
        Random row_col = new Random();
        byte row = (byte) row_col.nextInt(ROWS_COLS);
        byte col = (byte) row_col.nextInt(ROWS_COLS);
        int two_four = row_col.nextInt(RANDOM);

        if (two_four % 2 == 0)
        {
            panel[row][col] = new TILE(default_start_A);
        }
        else
        {
            panel[row][col] = new TILE(default_start_B);
        }
        current_tiles++;
    }

    public void paintComponent(Graphics g_first)
    {
        super.paintComponent(g_first);
        Graphics2D g = (Graphics2D) g_first;
        g.setRenderingHints(rh);
        for (byte row=0; row<ROWS_COLS; row++)
        {
            int Y_jump = JUMPS[row];    
            for (byte col=0; col<ROWS_COLS; col++)
            {
                int X_jump = JUMPS[col];

                if (panel[row][col] == null)
                {
                    g.setColor(DEFAULT_TILE);
                    g.fillRoundRect(X_jump, Y_jump, block_width, block_width, 80, 80);
                }
                else
                {
                    int value = panel[row][col].value;
                    JLabel temp = panel[row][col].LABEL;

                    if (value == 2)
                    {
                        g.setColor(TWO);
                        temp.setLocation(X_jump+block_center-18, Y_jump+block_center-20);
                    }
                    else if (value == 4)
                    {
                        g.setColor(FOUR);
                        temp.setLocation(X_jump+block_center-18, Y_jump+block_center-20);
                    }
                    else if (value == 8)
                    {
                        g.setColor(EIGHT);
                        temp.setLocation(X_jump+block_center-18, Y_jump+block_center-20);
                    }
                    else if (value == 16)
                    {
                        g.setColor(SIXTEEN);
                        temp.setLocation(X_jump+block_center-28, Y_jump+block_center-23);
                    }
                    else if (value == 32)
                    {
                        g.setColor(THIRTYTWO);
                        temp.setLocation(X_jump+block_center-28, Y_jump+block_center-23);
                    }
                    else if (value == 64)
                    {
                        g.setColor(SIXTYFOUR);
                        temp.setLocation(X_jump+block_center-30, Y_jump+block_center-23);
                    }
                    else if (value < 1024)
                    {
                        g.setColor(REMAINING);
                        temp.setLocation(X_jump+block_center-45, Y_jump+block_center-20);
                    }
                    else
                    {
                        g.setColor(REMAINING);
                        temp.setFont(panel[row][col].big_number);
                        temp.setLocation(X_jump+block_center-45, Y_jump+block_center-15);
                    }

                    g.fillRoundRect(X_jump, Y_jump, block_width, block_width, 80, 80);
                    add(temp);
                }

            }
        }

        if (!achieved_goal)
        {
            if (current_tiles == 16)
            {
                boolean check = false;
                for (byte x=0; x<ROWS_COLS; x++)
                {
                    try{
                        byte y=0;
                        while  (y!=ROWS_COLS)
                        {
                            if (y+1 <= real_end && x+1 <= real_end)
                            {
                                if (panel[x][y].value == panel[x][y+1].value || panel[x][y].value == panel[x+1][y].value)
                                {
                                    check = true;
                                    break;
                                }
                                else
                                {
                                    y++;
                                }
                            }
                            else if (y+1 <= real_end)
                            {
                                if (panel[x][y].value == panel[x][y+1].value)
                                {
                                    check = true;
                                    break;
                                }
                                else
                                {
                                    y++;
                                }
                            }
                            else
                            {
                                if (panel[x][y].value == panel[x+1][y].value)
                                {
                                    check = true;
                                    break;
                                }
                                else
                                {
                                    y++;
                                }
                            }
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException e){
                        break;
                    }
                    if (check)
                    {
                        break;
                    }
                }

                if (!check)
                {
                    System.out.println("YOU LOSE BAKA!!");
                    setEnabled(false);
                    try {
                        this.finalize();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else
        {
            System.out.println("YOU WIN!!");
            setEnabled(false);
            try {
                this.finalize();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

    }

    //dummy methods
    @Override
    public void keyReleased(KeyEvent e){}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e)
    {
        is_moved=false;
        byte key = (byte)e.getKeyCode();
        if (key == LEFT)
        {
            is_moved = horizontal_pressed(real_end, left_increment);
        }
        else if (key == RIGHT)
        {
            is_moved = horizontal_pressed(fake_end, right_increment);
        }
        else if (key == UP)
        {
            panel = rotateRight(panel);
            is_moved = horizontal_pressed(fake_end, right_increment);
            panel = rotateLeft(panel);
        }
        else if (key == DOWN)
        {
            panel = rotateRight(panel);
            is_moved = horizontal_pressed(real_end,left_increment);
            panel = rotateLeft(panel);
        }
        Generate(is_moved);
        repaint();
    }

    public boolean horizontal_pressed(byte left_or_right, byte increment)
    {
        byte compare = (byte)(increment+left_or_right);
        byte which_end = (byte)(real_end-left_or_right);

        for (byte row=0; row<ROWS_COLS; row++)
        {
            shift_row(row,which_end,compare,increment);
        }

        //merge_row
        for (byte y=0; y<ROWS_COLS; y++)
        {
            byte x = which_end;
            while (x != compare && x != left_or_right)
            {
                if (panel[y][x] != null && panel[y][x+increment] != null && panel[y][x].value == panel[y][x+increment].value)
                {
                    panel[y][x].doubleValue();
                    remove(panel[y][x+increment].LABEL);
                    panel[y][x+increment] = null;
                    current_tiles--;
                    is_moved = true;
                    x = (byte)(x+(increment+increment));
                }
                else
                {
                    x = (byte)(x+increment);
                }
            }
            shift_row(y,which_end,compare,increment);
        }

        return is_moved;

    }

    public void shift_row(byte row, byte which_end, byte compare, byte increment)
    {
        ArrayList<TILE> temp_row = new ArrayList<TILE>();
        byte col;
        for (col = which_end; col!=compare; col = (byte)(col+increment))
        {
            if (panel[row][col] != null)
            {
                temp_row.add(panel[row][col]);
            }
        }

        byte next = 0;
        for (col=which_end; col!=compare; col= (byte)(col+increment))
        {
            try {
                if (temp_row.get(next) != panel[row][col])
                {
                    is_moved = true;
                    panel[row][col] = temp_row.get(next);
                }
            }
            catch (IndexOutOfBoundsException E) {
                panel[row][col] = null;
            }

            next++;
        }
    }

    public void Generate(boolean is_moved)
    {
        if (is_moved)
        {
            Random row_col = new Random();
            byte row = (byte) row_col.nextInt(ROWS_COLS);
            byte col = (byte) row_col.nextInt(ROWS_COLS);
            int two_four = row_col.nextInt(RANDOM);

            if (two_four % 2 == 0)
            {
                if (panel[row][col] == null)
                {
                    panel[row][col] = new TILE(default_start_A);
                    current_tiles++;
                }
                else
                {
                    Generate(is_moved);
                }
            }
            else
            {
                if (panel[row][col] == null)
                {
                    panel[row][col] = new TILE(default_start_B);
                    current_tiles++;
                }
                else
                {
                    Generate(is_moved);
                }
            }
        }
    }

   public TILE[][] rotateLeft(TILE image[][])
   {
      TILE new_image[][] = new TILE[ROWS_COLS][ROWS_COLS];

      for (int y=0; y<ROWS_COLS; y++) 
      {
          for (int x=0; x<ROWS_COLS; x++) 
          {
              new_image[x][y] = image[y][real_end - x];
          }
      }

      return new_image;
   }

    public TILE[][] rotateRight(TILE image[][])
    {
       TILE new_image[][] = new TILE[ROWS_COLS][ROWS_COLS];
       for (int y=0; y<ROWS_COLS; y++)   
       {
           for (int x=0; x<ROWS_COLS; x++)    
           {
               new_image[x][real_end - y] = image[y][x];  
           }
       }

       return new_image;
    }    

    class TILE
    {
        public int value;
        public JLabel LABEL;

        public final Font font = new Font("Lithograph", Font.BOLD, 50);
        public final Font big_number = new Font("Lithograph", Font.BOLD, 35);
        public final Color DEFAULT = new Color(119,110,101);
        public final Color REMAINING = new Color(249,246,242);

        public TILE(int value)
        {
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
                Game_Panel.this.achieved_goal = true;
            }
        }
    }

    public static void main(String argc[])
    {
        System.setProperty("awt.useSystemAAFontSettings","on"); 
        System.setProperty("swing.aatext", "true"); 

        JFrame window = new JFrame();
        window.setSize(new Dimension(HW,HW));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setContentPane(new Game_Panel());
        window.pack();
        window.setVisible(true);
    }
}