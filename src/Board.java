import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Board {

    // frame panel
    private JFrame frame; // frame to hold all game components
    private JPanel framePanel;  // hold inner panels
    private JPanel scorePanel;  // show scoring info
    private JPanel dicePanel;   // show dice
    private JPanel infoPanel;   // show game info

    // score panel

    // panels to store scoring info and options
    private JPanel topScorePanel;   // panel for top score descriptions
    private JPanel topButtonsPanel; // panel for top score values
    private JPanel bottomScorePanel;    // panel for bottom score descriptions
    private JPanel bottomButtonsPanel;  // panel for bottom score values

    // labels and buttons to store scoring info and options
    private JLabel[] topScoreLabels;    // label for top score descriptions
    private JButton[] topScoreButtons;  // buttons to input scores for top
    private JLabel[] bottomScoreLabels; // label for bottom score descriptions
    private JButton [] bottomScoreButtons;  // buttons to input scores for bottom

    // button styling
    private Border thickBorder;    // border for scoring labels & buttons

    // dice panel
    private JButton rollButton; // button to roll dice
    private DiceRack diceRack;  // possible pass in from main?

    // info panel

    private JLabel infoLabel;   // display game info
    private JLabel scoreTextLabel;  // txt?
    private JLabel scorePointsLabel;    // pts?


    int turnCount;  // track turns, increment every time score hit
    int rollCount;  // track rolls, increment every roll
    int score;  // user overall score
    boolean bonusActive;    // check if top bonus is hit or not
    boolean doubleYahtzee;  // check if yahtzee has been scored
    JButton resetButton;    // button that appears to reset game

    public Board()    // default constructor
    {
        frame = new JFrame("Yahtzee");
        initBoard();
    }   // end default constructor

    private void initBoard()
    {
        // initialize frame

        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setSize(800, 800);

        framePanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(framePanel, BoxLayout.Y_AXIS);
        framePanel.setLayout(boxLayout);

        // adding panels to framePanel

        framePanel.add(Box.createRigidArea(new Dimension(10, 10)));

        // init panels

        initScorePanel();
        initDicePanel();
        initInfoPanel();

        // adding play again button

        resetButton = new JButton("Play Again");
        resetButton.setVisible(false);
        resetButton.setEnabled(false);
        resetButton.setBackground(Color.GREEN);
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        framePanel.add(resetButton);    // was frame panel here n below
        framePanel.add(Box.createRigidArea(new Dimension(25, 25))); // need?

        turnCount = 0;
        rollCount = 0;
        score = 0;
        bonusActive = true;
        doubleYahtzee = false;

        ResetButtonHandler Resethandler = new ResetButtonHandler();
        resetButton.addActionListener(Resethandler);

        // add finished framePanel to frame

        frame.add(framePanel);

        // display frame after adding components

        frame.setVisible(true);
    }   // end method init

    private void initScorePanel()
    {
        scorePanel = new JPanel();

        // initialize score panel dimensions

        // initialize button border

        thickBorder = BorderFactory.createLineBorder(Color.BLACK, 2);

        // initialize elements for score panel

        GridLayout scoreLayout = new GridLayout(7,1);
        scoreLayout.setVgap(1);

        topScorePanel = new JPanel(scoreLayout);
        topButtonsPanel = new JPanel(scoreLayout);
        bottomScorePanel = new JPanel(scoreLayout);
        bottomButtonsPanel = new JPanel(scoreLayout);

        // calling init for main panels

        initTopScore();
        initBottomScore();

        // adding elements to score panel

        scorePanel.add(topScorePanel);
        scorePanel.add(topButtonsPanel);
        scorePanel.add(bottomScorePanel);
        scorePanel.add(bottomButtonsPanel);

        framePanel.add(scorePanel);
    }

    private void initTopScore()    // initialize top score labels and buttons
    {
        // initialize label & button elements

        topScoreLabels = new JLabel[7];
        topScoreButtons = new JButton[7];

        String labelText;

        // initialize top score labels

        for (int i = 0; i < 7; i++) {
            if (i != 6) {
                labelText = (i + 1) + "'s";
            }
            else {
                labelText = "Bonus";
            }

            topScoreLabels[i] = new JLabel(labelText, SwingConstants.CENTER);
            topScoreLabels[i].setBorder(thickBorder);
            topScoreLabels[i].setPreferredSize(new Dimension(100,50));
            topScorePanel.add(topScoreLabels[i]);
        }   // end for loop

        // initialize top score buttons

        for (int i = 0; i < 7; i++) {
            topScoreButtons[i] = new JButton("0");
            topScoreButtons[i].setBorder(thickBorder);
            topScoreButtons[i].setPreferredSize(new Dimension(100,50));  // fig dimensions all?
            topScoreButtons[i].setBackground(Color.WHITE);
            topScoreButtons[i].setEnabled(false);

            // adding mousehandler for border outline effect

            if (i != 6) {
                Board.BorderMouseHandler handler = new Board.BorderMouseHandler();    // no need Board.?
                topScoreButtons[i].addMouseListener(handler);
            }

            ScoreButtonHandler Scorehandler = new ScoreButtonHandler();
            topScoreButtons[i].addActionListener(Scorehandler);

            topButtonsPanel.add(topScoreButtons[i]);
        }   // end for loop
    }   // end method initTopScore

    private void initBottomScore()  // initialize bottom score labels and buttons
    {
        // initialize label and button elements

        bottomScoreLabels = new JLabel[7];
        bottomScoreButtons = new JButton[7];

        String labelText;

        // initialize bottom score labels

        for (int i = 0; i < 7; i++) {
            if (i == 0)
                labelText = "3X";
            else if (i == 1)
                labelText = "4X";
            else if (i == 2)
                labelText = "Full House";
            else if (i == 3)
                labelText = "Small Straight";
            else if (i == 4)
                labelText = "Large Straight";
            else if (i == 5)
                labelText = "Chance";
            else
                labelText = "Yahtzee";

            bottomScoreLabels[i] = new JLabel(labelText, SwingConstants.CENTER);
            bottomScoreLabels[i].setBorder(thickBorder);
            bottomScoreLabels[i].setPreferredSize(new Dimension(100, 50));
            bottomScorePanel.add(bottomScoreLabels[i]);
        }   // end for loop

        // initialize bottom score buttons

        for (int i = 0; i < 7; i++) {
            bottomScoreButtons[i] = new JButton("0");
            bottomScoreButtons[i].setBorder(thickBorder);
            bottomScoreButtons[i].setPreferredSize(new Dimension(100,50));
            bottomScoreButtons[i].setBackground(Color.WHITE);
            bottomScoreButtons[i].setEnabled(false);

            BorderMouseHandler handler = new BorderMouseHandler();  // this on outside?
            bottomScoreButtons[i].addMouseListener(handler);

            ScoreButtonHandler Scorehandler = new ScoreButtonHandler();
            bottomScoreButtons[i].addActionListener(Scorehandler);

            bottomButtonsPanel.add(bottomScoreButtons[i]);
        }   // end for loop

    } // end initBottomScore

    private void initDicePanel()
    {
        dicePanel = new JPanel();

        // add layout

        FlowLayout diceLayout = new FlowLayout();
        diceLayout.setHgap(10);     // can i do this w/o establish layout?
        dicePanel.setLayout(diceLayout);
        dicePanel.setPreferredSize(new Dimension(100,100));

        // setup dice rack

        Dice diceSlot;
        diceRack = new DiceRack();

        rollButton = new JButton("Roll");
        rollButton.setBackground(new Color(0,191, 255));
        rollButton.setPreferredSize(new Dimension(75,75));  // was 75

        RollButtonHandler Rollhandler = new RollButtonHandler();
        rollButton.addActionListener(Rollhandler);

        dicePanel.add(rollButton);

        for (int i = 1; i < 6; i++) {

            diceSlot = diceRack.getDice(i);

            diceSlot.setText(String.valueOf(diceRack.getDice(1).getValue()));
            // if adjust size do as new font
            diceSlot.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
            diceSlot.setBackground(Color.ORANGE);
            diceSlot.setPreferredSize(new Dimension(75, 75)); // was 75

            DiceButtonHandler buttonHandler = new DiceButtonHandler();
            diceSlot.addActionListener(buttonHandler);

            Board.BorderMouseHandler mouseHandler = new BorderMouseHandler();
            diceSlot.addMouseListener(mouseHandler);

            dicePanel.add(diceSlot);
        }   // end for loop

        framePanel.add(dicePanel);
    }

    private void initInfoPanel()
    {
        infoPanel = new JPanel();

        BoxLayout infoLayout = new BoxLayout(infoPanel, BoxLayout.Y_AXIS);
        infoPanel.setLayout(infoLayout);
        infoPanel.setPreferredSize(new Dimension(200,200));

        infoLabel = new JLabel("Roll dice to start game!");
        scoreTextLabel = new JLabel("Score");
        scorePointsLabel = new JLabel("0");

        Font infoFont = new Font(Font.SANS_SERIF, Font.PLAIN, 20);
        infoLabel.setFont(infoFont);
        Font scoreTextFont = new Font(Font.SANS_SERIF, Font.PLAIN, 30);
        scoreTextLabel.setFont(scoreTextFont);
        Font scorePointsFont = new Font(Font.SANS_SERIF, Font.PLAIN, 60);
        scorePointsLabel.setFont(scorePointsFont);

        infoPanel.add(Box.createRigidArea(new Dimension(5, 5)));
        infoPanel.add(infoLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(25, 25))); //creates space between elements
        infoPanel.add(scoreTextLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(5,5)));
        infoPanel.add(scorePointsLabel);

        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scorePointsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        framePanel.add(infoPanel);
    }

    private class RollButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent event)      // maybe dice array in main here? eh
        {
            Dice tempDice;

            rollCount++;

            if (rollCount == 1) {
                for (int i = 0; i < 6; i++) {
                    if (topScoreButtons[i].getBackground() != Color.GREEN)
                        topScoreButtons[i].setEnabled(true);

                    if (bottomScoreButtons[i].getBackground() != Color.GREEN)
                        bottomScoreButtons[i].setEnabled(true);
                }

                if (bottomScoreButtons[6].getBackground() != Color.GREEN)
                    bottomScoreButtons[6].setEnabled(true);
            }

            diceRack.rollDice();

            for (int i = 1; i < 6; i++) {
                tempDice = diceRack.getDice(i);
                tempDice.setText(Integer.toString(tempDice.getValue()));
                tempDice.setEnabled(true);
            }

            updateScores();

            if (rollCount == 3) {
                rollButton.setBackground(Color.GRAY);
                rollButton.setEnabled(false);
                infoLabel.setText("Select a move to score!");
            }
            else
                infoLabel.setText("Roll the dice, or score a move!");
        }   // end method actionPerformed

        private void updateScores()
        {
            int onesValue = 0;
            int twosValue = 0;
            int threesValue = 0;
            int foursValue = 0;
            int fivesValue = 0;
            int sixesValue = 0;
            int totalValue = 0;

            for (int i = 0; i < 6; i++) {
                if (topScoreButtons[i].getBackground() != Color.GREEN)
                    topScoreButtons[i].setText("0");

                if (bottomScoreButtons[i].getBackground() != Color.GREEN)
                    bottomScoreButtons[i].setText("0");
            }

            if (bottomScoreButtons[6].getBackground() != Color.GREEN)
                bottomScoreButtons[6].setText("0");

            // calculate score values

            for (int i = 1; i < 6; i++) {

                if (diceRack.getDice(i).getValue() == 1) {
                    onesValue += 1;
                    if (topScoreButtons[0].getBackground() != Color.GREEN)
                        topScoreButtons[0].setText(Integer.toString(onesValue));
                }
                else if (diceRack.getDice(i).getValue() == 2) {
                    twosValue += 2;
                    if (topScoreButtons[1].getBackground() != Color.GREEN)
                        topScoreButtons[1].setText(Integer.toString(twosValue));
                }
                else if (diceRack.getDice(i).getValue() == 3) {
                    threesValue += 3;
                    if (topScoreButtons[2].getBackground() != Color.GREEN)
                        topScoreButtons[2].setText(Integer.toString(threesValue));
                }
                else if (diceRack.getDice(i).getValue() == 4) {
                    foursValue += 4;
                    if (topScoreButtons[3].getBackground() != Color.GREEN)
                        topScoreButtons[3].setText(Integer.toString(foursValue));
                }
                else if (diceRack.getDice(i).getValue() == 5) {
                    fivesValue += 5;
                    if (topScoreButtons[4].getBackground() != Color.GREEN)
                        topScoreButtons[4].setText(Integer.toString(fivesValue));
                }
                else if (diceRack.getDice(i).getValue() == 6) {
                    sixesValue += 6;
                    if (topScoreButtons[5].getBackground() != Color.GREEN)
                        topScoreButtons[5].setText(Integer.toString(sixesValue));
                }
            }   // end for loop

            totalValue = (onesValue + twosValue + threesValue + foursValue + fivesValue + sixesValue);

            // 3 of a kind

            if (onesValue >= 3 || twosValue >= 6 || threesValue >= 9 || foursValue >= 12 || fivesValue >= 15 || sixesValue >= 18) {
                if (bottomScoreButtons[0].getBackground() != Color.GREEN)
                    bottomScoreButtons[0].setText(Integer.toString(totalValue));
            }

            // 4 of a kind

            if (onesValue >= 4 || twosValue >= 8 || threesValue >= 12 || foursValue >= 16 || fivesValue >= 20 || sixesValue >= 24) {
                if (bottomScoreButtons[1].getBackground() != Color.GREEN)
                    bottomScoreButtons[1].setText(Integer.toString(totalValue));
            }

            // Full House

            if (bottomScoreButtons[2].getBackground() != Color.GREEN) {
                if (onesValue == 3) {
                    if (twosValue == 4 || threesValue == 6 || foursValue == 8 || fivesValue == 10 || sixesValue == 12)
                        bottomScoreButtons[2].setText("25");
                } else if (twosValue == 6) {
                    if (onesValue == 2 || threesValue == 6 || foursValue == 8 || fivesValue == 10 || sixesValue == 12)
                        bottomScoreButtons[2].setText("25");
                } else if (threesValue == 9) {
                    if (onesValue == 2 || twosValue == 4 || foursValue == 8 || fivesValue == 10 || sixesValue == 12)
                        bottomScoreButtons[2].setText("25");
                } else if (foursValue == 12) {
                    if (onesValue == 2 || twosValue == 4 || threesValue == 6 || fivesValue == 10 || sixesValue == 12)
                        bottomScoreButtons[2].setText("25");
                } else if (fivesValue == 15) {
                    if (onesValue == 2 || twosValue == 4 || threesValue == 6 || foursValue == 8 || sixesValue == 12)
                        bottomScoreButtons[2].setText("25");
                } else if (sixesValue == 18) {
                    if (onesValue == 2 || twosValue == 4 || threesValue == 6 || foursValue == 8 || fivesValue == 10)
                        bottomScoreButtons[2].setText("25");
                }
            }   // end outer if

            // Small Straight

            if ((onesValue >= 1 && twosValue >= 2 && threesValue >=3 && foursValue >= 4) ||
                    (twosValue >= 2 && threesValue >= 3 && foursValue >= 4 && fivesValue >= 5) ||
                    (threesValue >= 3 && foursValue >= 4 && fivesValue >= 5 && sixesValue >= 6)) {
                if (bottomScoreButtons[3].getBackground() != Color.GREEN)
                    bottomScoreButtons[3].setText("30");
            }

            // Large Straight -- maybe thes ifs do diff order too? for all

            if ((onesValue == 1 && twosValue == 2 && threesValue == 3 && foursValue == 4 && fivesValue == 5) ||
                    (twosValue == 2 && threesValue == 3 && foursValue == 4 && fivesValue == 5 && sixesValue == 6)) {
                if (bottomScoreButtons[4].getBackground() != Color.GREEN)
                    bottomScoreButtons[4].setText("40");
            }

            // Chance

            if (bottomScoreButtons[5].getBackground() != Color.GREEN)
                bottomScoreButtons[5].setText(Integer.toString(totalValue));

            // Yahtzee

            if (onesValue == 5 || twosValue == 10 || threesValue == 15 || foursValue == 20 || fivesValue == 25 || sixesValue == 30) {
                if (bottomScoreButtons[6].getBackground() != Color.GREEN)
                    bottomScoreButtons[6].setText("50");

                // Double Yahtzee

                else {
                    doubleYahtzee = true;
                    bottomScoreButtons[6].setText(String.valueOf(Integer.valueOf(bottomScoreButtons[6].getText()) + 100));

                    int diceNum = diceRack.getDice(1).getValue();

                    // check if top spot is filled or not - display bottom values if is filled

                    if (topScoreButtons[diceNum-1].getBackground() == Color.GREEN) {
                        if (bottomScoreButtons[2].getBackground() != Color.GREEN)   // add in full house
                            bottomScoreButtons[2].setText("25");

                        if (bottomScoreButtons[3].getBackground() != Color.GREEN)   // add in small straight
                            bottomScoreButtons[3].setText("30");

                        if (bottomScoreButtons[4].getBackground() != Color.GREEN)   // add in large straight
                            bottomScoreButtons[4].setText("40");
                    }

                    // if top spot is not filled, take away rest of bottom value options

                    else {
                        if (bottomScoreButtons[0].getBackground() != Color.GREEN)   // add in full house
                            bottomScoreButtons[0].setText("0");

                        if (bottomScoreButtons[1].getBackground() != Color.GREEN)   // add in small straight
                            bottomScoreButtons[1].setText("0");

                        if (bottomScoreButtons[5].getBackground() != Color.GREEN)   // add in large straight
                            bottomScoreButtons[5].setText("0");

                        // lock rest of boxes of upper and lower -- must undo lock in score handler

                        for (int i = 0; i < 6; i++) {
                            if (topScoreButtons[i].getBackground() != Color.GREEN && i != diceNum-1)
                                topScoreButtons[i].setEnabled(false);

                            if (bottomScoreButtons[i].getBackground() != Color.GREEN)
                                bottomScoreButtons[i].setEnabled(false);
                        }
                    }
                }
            }
        }   // end method updateScores
    }   // end private inner class RollButtonHandler

    private class DiceButtonHandler implements ActionListener   // handle Dice buttons
    {
        public void actionPerformed(ActionEvent event)  // handle event when press Dice buttons
        {
            if (((JButton)event.getSource()).getBackground() == Color.ORANGE) {
                ((JButton)event.getSource()).setBackground(Color.RED);
                ((Dice)event.getSource()).setStatus(true);
            }   // end if
            else {
                ((JButton)event.getSource()).setBackground(Color.ORANGE);
                ((Dice)event.getSource()).setStatus(false);
            }   // end else
        }   // end method actionPerformed
    }   // end private inner class DiceButtonHandler

    private class ScoreButtonHandler implements ActionListener   // handle Score buttons
    {
        public void actionPerformed(ActionEvent event)  // handle event when press Score buttons
        {
            String scoreIn;
            String topVal;
            String botVal;
            int topTotal = 0;

            // add scoring to total and reset values

            ((JButton)event.getSource()).setEnabled(false);
            ((JButton)event.getSource()).setBackground(Color.GREEN);
            rollButton.setEnabled(true);
            rollButton.setBackground(new Color(0,191, 255));

            scoreIn = ((JButton)event.getSource()).getText();
            score += Integer.valueOf(scoreIn);

            if (doubleYahtzee) {
                score += 100;
                doubleYahtzee = false;

                for (int i = 0; i < 6; i++) {
                    if (topScoreButtons[i].getBackground() != Color.GREEN)
                        topScoreButtons[i].setEnabled(true);

                    if (bottomScoreButtons[i].getBackground() != Color.GREEN)
                        bottomScoreButtons[i].setEnabled(true);
                }
            }
            scorePointsLabel.setText(String.valueOf(score));

            rollCount = 0;
            turnCount++;
//
            for (int i = 0; i < 6; i++) {
                topScoreButtons[i].setEnabled(false);
                bottomScoreButtons[i].setEnabled(false);

                if (topScoreButtons[i].getBackground() == Color.GREEN) {    // need first line?
                    topVal = topScoreButtons[i].getText();
                    topTotal += Integer.valueOf(topVal);
                }

                if (i < 5) {
                    diceRack.getDice(i+1).setBackground(Color.ORANGE);
                    diceRack.getDice(i+1).setStatus(false);
                    diceRack.getDice(i+1).setEnabled(false);
                    diceRack.getDice(i+1).setText("0");
                }

                if (topScoreButtons[i].getBackground() != Color.GREEN)
                    topScoreButtons[i].setText("0");

                if (bottomScoreButtons[i].getBackground() != Color.GREEN)
                    bottomScoreButtons[i].setText("0");
            }   // end for loop

            bottomScoreButtons[6].setEnabled(false);

            if (bottomScoreButtons[6].getBackground() != Color.GREEN)
                bottomScoreButtons[6].setText("0");

            if (topTotal >= 63 && bonusActive) {
                topScoreButtons[6].setText("35");
                topScoreButtons[6].setOpaque(true);
                topScoreButtons[6].setBackground(Color.GREEN);
                score += 35;
                scorePointsLabel.setText(String.valueOf(score));
                bonusActive = false;
            }

            if (turnCount == 13) {
                rollButton.setEnabled(false);
                rollButton.setBackground(Color.GRAY);
                infoLabel.setText("Game Over!");
                resetButton.setVisible(true);
                resetButton.setEnabled(true);
            }
            else
                infoLabel.setText("Roll the dice!");

        }   // end method actionPerformed
    }   // end private inner class ScoreButtonHandler

    private class ResetButtonHandler implements ActionListener   // handle Reset Button
    {
        public void actionPerformed(ActionEvent event)  // handle event when press Reset Button
        {
            frame.remove(scorePanel);
            frame.remove(infoPanel);
            frame.remove(dicePanel);
            frame.remove(framePanel);
            initBoard();
        }   // end method actionPerformed
    }   // end private inner class ResetButtonHandler

    private class BorderMouseHandler implements MouseListener    // for hover highlight effect
    {
        @Override
        public void mouseClicked(MouseEvent e) {
            // keep empty
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // keep empty
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // keep empty
        }

        public void mouseEntered(MouseEvent event)
        {
            JButton button = (JButton)event.getSource();
            Border border = BorderFactory.createLineBorder(Color.RED, 3);
            button.setBorder(border);
        } // end method mouseEntered

        public void mouseExited(MouseEvent event)
        {
            JButton button = (JButton)event.getSource();
            Border border = UIManager.getBorder("Button.border"); // new

            if (button.getBackground() == Color.ORANGE || button.getBackground() == Color.RED)
                button.setBorder(border);
            else
                button.setBorder(thickBorder); // was just border in parenth
        } // end method mouseExited
    }   // end class MouseHandler
}