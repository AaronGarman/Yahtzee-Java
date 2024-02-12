import javax.swing.*;

public class Dice extends JButton {
    private int value;  // store Dice value
    private boolean locked; // track whether dice value can change or not when rolled

    public Dice()   // default constructor
    {
        value = 0;
        locked = false;
        setEnabled(false);
    }

    public void setValue(int newValue)     // set new value of Dice
    {
        value = newValue;
    }

    public void setStatus(boolean newStatus)   // set new status of Dice
    {
        locked = newStatus;
    }

    public int getValue()  // get value of Dice
    {
        return value;
    }

    public boolean getStatus() // get status of Dice
    {
        return locked;
    }
}