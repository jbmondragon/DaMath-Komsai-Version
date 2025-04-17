import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Chips extends JLabel {

    String valueOfChips;
    String operator;
    int i;
    int j;
    String color;

    public Chips(int i, int j, String operator[][], String valueOfChips[][]) {

        this.i = i;
        this.j = j;
        this.valueOfChips = valueOfChips[i][j];

        try {
            ImageIcon image = new ImageIcon(getClass().getResource("/resources/"+operator[i][j] + ".png"));
            setIcon(image);
        } catch (Exception e) {
            System.out.println("Picture not found");
            e.printStackTrace();
        }

    }

    public String getChipValue() {
        return valueOfChips;
    }

    public String getOperator() {
        return this.operator;
    }

    public String getC() {
        return this.color;
    }
}
