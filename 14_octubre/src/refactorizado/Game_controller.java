package refactorizado;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Game_controller implements KeyListener, Serializable {
    private Game_Panel game_panel;
    private static final byte LEFT = 37;
    private static final byte RIGHT = 39;
    private static final byte UP = 38;
    private static final byte DOWN = 40;

    public Game_controller(Game_Panel game_panel) {
        this.game_panel=game_panel;
    }//dummy methods

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Game_Panel.is_moved = false;
        byte key = (byte) e.getKeyCode();
        if (key == LEFT) {
            Game_Panel.is_moved = horizontal_pressed(Game_Panel.real_end, Game_Panel.left_increment);
        } else if (key == RIGHT) {
            Game_Panel.is_moved = horizontal_pressed(Game_Panel.fake_end, Game_Panel.right_increment);
        } else if (key == UP) {
            Game_Panel.panel = rotateRight(Game_Panel.panel);
            Game_Panel.is_moved = horizontal_pressed(Game_Panel.fake_end, Game_Panel.right_increment);
            Game_Panel.panel = rotateLeft(Game_Panel.panel);
        } else if (key == DOWN) {
            Game_Panel.panel = rotateRight(Game_Panel.panel);
            Game_Panel.is_moved = horizontal_pressed(Game_Panel.real_end, Game_Panel.left_increment);
            Game_Panel.panel = rotateLeft(Game_Panel.panel);
        }
        Generate(Game_Panel.is_moved);
        game_panel.repaint();
    }

    public boolean horizontal_pressed(byte left_or_right, byte increment) {
        byte compare = (byte) (increment + left_or_right);
        byte which_end = (byte) (Game_Panel.real_end - left_or_right);

        for (byte row = 0; row < Game_Panel.ROWS_COLS; row++) {
            shift_row(row, which_end, compare, increment);
        }

        //merge_row
        for (byte y = 0; y < Game_Panel.ROWS_COLS; y++) {
            byte x = which_end;
            while (x != compare && x != left_or_right) {
                if (Game_Panel.panel[y][x] != null && Game_Panel.panel[y][x + increment] != null && Game_Panel.panel[y][x].value == Game_Panel.panel[y][x + increment].value) {
                    Game_Panel.panel[y][x].doubleValue();
                    game_panel.remove(Game_Panel.panel[y][x + increment].LABEL);
                    Game_Panel.panel[y][x + increment] = null;
                    Game_Panel.current_tiles--;
                    Game_Panel.is_moved = true;
                    x = (byte) (x + (increment + increment));
                } else {
                    x = (byte) (x + increment);
                }
            }
            shift_row(y, which_end, compare, increment);
        }

        return Game_Panel.is_moved;

    }

    public void shift_row(byte row, byte which_end, byte compare, byte increment) {
        ArrayList<TILE> temp_row = new ArrayList<TILE>();
        byte col;
        for (col = which_end; col != compare; col = (byte) (col + increment)) {
            if (Game_Panel.panel[row][col] != null) {
                temp_row.add(Game_Panel.panel[row][col]);
            }
        }

        byte next = 0;
        for (col = which_end; col != compare; col = (byte) (col + increment)) {
            try {
                if (temp_row.get(next) != Game_Panel.panel[row][col]) {
                    Game_Panel.is_moved = true;
                    Game_Panel.panel[row][col] = temp_row.get(next);
                }
            } catch (IndexOutOfBoundsException E) {
                Game_Panel.panel[row][col] = null;
            }

            next++;
        }
    }

    public void Generate(boolean is_moved) {
        if (is_moved) {
            Random row_col = new Random();
            byte row = (byte) row_col.nextInt(Game_Panel.ROWS_COLS);
            byte col = (byte) row_col.nextInt(Game_Panel.ROWS_COLS);
            int two_four = row_col.nextInt(Game_Panel.RANDOM);

            if (two_four % 2 == 0) {
                if (Game_Panel.panel[row][col] == null) {
                    Game_Panel.panel[row][col] = new TILE(null, Game_Panel.default_start_A);
                    Game_Panel.current_tiles++;
                } else {
                    Generate(is_moved);
                }
            } else {
                if (Game_Panel.panel[row][col] == null) {
                    Game_Panel.panel[row][col] = new TILE(null, Game_Panel.default_start_B);
                    Game_Panel.current_tiles++;
                } else {
                    Generate(is_moved);
                }
            }
        }
    }

    public TILE[][] rotateLeft(TILE image[][]) {
        TILE new_image[][] = new TILE[Game_Panel.ROWS_COLS][Game_Panel.ROWS_COLS];

        for (int y = 0; y < Game_Panel.ROWS_COLS; y++) {
            for (int x = 0; x < Game_Panel.ROWS_COLS; x++) {
                new_image[x][y] = image[y][Game_Panel.real_end - x];
            }
        }

        return new_image;
    }

    public TILE[][] rotateRight(TILE image[][]) {
        TILE new_image[][] = new TILE[Game_Panel.ROWS_COLS][Game_Panel.ROWS_COLS];
        for (int y = 0; y < Game_Panel.ROWS_COLS; y++) {
            for (int x = 0; x < Game_Panel.ROWS_COLS; x++) {
                new_image[x][Game_Panel.real_end - y] = image[y][x];
            }
        }

        return new_image;
    }
}