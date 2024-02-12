import java.util.Random;

public class DiceRack {
    private Dice[] rack;     // array of Dice objects

    public DiceRack()   // default constructor
    {
        rack = new Dice[5];  // array of 5 default Dice

        for (int i = 0; i < 5; i++)
            rack[i] = new Dice();
    }

    public void rollDice()    // roll dice to get new values
    {
        Random r = new Random();

        for (int i = 0; i < 5; i++) {
            if (!(rack[i].getStatus()))
                rack[i].setValue(r.nextInt(6) + 1);     // range of 1-6
        }
    }

    public Dice getDice(int position) {    // access specific Dice
        return rack[position-1];
    }
}