import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;

// renders the game
public class BattleGui extends JFrame {

    private Warrior warrior = new Warrior("Valtor");
    private Mage mage = new Mage("Lyra");

    private Image bg, warriorImg, mageImg;
    private JTextArea log;
    private JProgressBar wBar, mBar;
    private JProgressBar wManaBar, mManaBar;
    private GamePanel panel;
    private boolean gameOver = false;

    public BattleGui() {
        setTitle("Battle of Ancient Lands");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loadImages();
        setupUI();

        log.append(" - Battle is Starting - \n");
        log.append(warrior.getName() + " vs " + mage.getName() + "\n\n");

    }
    // message to the log area

    public void appendToTerminal(String text) {
        log.append(text + "\n");
        SwingUtilities.invokeLater(() -> {
            log.setCaretPosition(log.getDocument().getLength());
        });
    }

    // ui components

    private void setupUI() {
        setLayout(new BorderLayout());
        panel = new GamePanel();
        panel.setPreferredSize(new Dimension(1000, 450));
        add(panel, BorderLayout.CENTER);

        JPanel top = new JPanel(new GridLayout(1, 2));
        wBar = createBar(100, Color.BLUE); // creating warrior's bars

        mBar = createBar(100, Color.RED); // creating mage's bars


        top.add(wrap(" Lyra the Mage", mBar));
        top.add(wrap(" Valtor the Warrior", wBar));
        add(top, BorderLayout.NORTH);

        log = new JTextArea(18, 40);
        log.setEditable(false);
        JScrollPane scroll = new JScrollPane(log);
        scroll.setPreferredSize(new Dimension(1000, 115));

        JButton next = new JButton("Fight!");
        next.addActionListener(e -> nextTurn());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(scroll, BorderLayout.CENTER);
        bottom.add(next, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);
    }

    // restarts the game by closing the window and creating new battlegui instance

    private void restartGame() {
        dispose();

        SwingUtilities.invokeLater(() -> {
            new BattleGui().setVisible(true);
        });
    }


    // handles a single game turn
    private void nextTurn() {
        if (gameOver) return;

        playTurn(warrior, mage);
        if (mage.getHealth() < 0) {
            mage.setHealth(0);  // hp can't be lower than 0

        }
        if (mage.getHealth() == 0) {
            updateBars();
            endGame(warrior.getName());
            return;
        }

        playTurn(mage, warrior);
        if (warrior.getHealth() < 0) {
          warrior.setHealth(0);
        }
        if (warrior.getHealth() == 0) {
            updateBars();
            endGame(mage.getName());
            return;
        }
    }
    // handles movement , attack and ui
    private void playTurn(Character a, Character d) {
        appendToTerminal("--- " + a.getName() +"---");

        int beforeHP = d.getHealth();
        int beforeMana = a.getMana();

        a.move();

        if (a.getMana() < 5) {
            appendToTerminal(a.getName() + " has no mana to attack!");
            updateBars();
            panel.repaint();
            return;
        }

        a.attack(d);

        int damage = beforeHP - d.getHealth();
        int manaUsed = beforeMana - a.getMana();

        if (damage > 0) {
            appendToTerminal(a.getName() + " dealt " + damage + " damage to " + d.getName());
            appendToTerminal("mana used: " + manaUsed + " | mana left: " + a.getMana());
        } else {
            if (manaUsed > 0) {
                appendToTerminal(a.getName() + " missed the attack!");
                appendToTerminal("mana used: " + manaUsed + " | mana left: " + a.getMana());
            } else {
                appendToTerminal(a.getName() + " couldn't attack (out of range or out of mana!)");
            }
        }
        updateBars();
        panel.repaint();
    }

    // handles restart or exit actions
    private void endGame(String winner) {
        gameOver = true;

        int choice = JOptionPane.showOptionDialog(
                this,
                winner + " WINS!\n\nWhat do you want to do?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"Play Again", "Exit"},
                "Play Again"
        );

        if (choice == 0) {
            restartGame();
        } else {
            dispose();
            System.exit(0);
        }
    }
    // loads all game images
    private void loadImages() {
        bg = load("/images/bg.png");
        warriorImg = load("/images/warrior.png");
        mageImg = load("/images/mage.png");
    }


    // loads images from the resource
    private Image load(String path) {
        try {
            return ImageIO.read(getClass().getResource(path));
        } catch (Exception e) {
            return null;
        }
    }
    // creates a health bar
    private JProgressBar createBar(int max, Color c) {
        JProgressBar b = new JProgressBar(0, max);
        b.setValue(max);
        b.setStringPainted(true);
        b.setForeground(c);
        return b;
    }
    private JPanel wrapWithMana(String title, JProgressBar hp, JProgressBar mana) {
        JPanel bars = new JPanel(new GridLayout(2, 1));
        bars.add(hp);
        bars.add(mana);

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(title, SwingConstants.CENTER), BorderLayout.NORTH);
        p.add(bars, BorderLayout.CENTER);

        return p;
    }

    // wraps a health bar together with its title
    private JPanel wrap(String title, JProgressBar bar) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(title, SwingConstants.CENTER), BorderLayout.NORTH);
        p.add(bar, BorderLayout.CENTER);
        return p;
    }
    //updates health bar based on current character
    private void updateBars() {
        wBar.setValue(warrior.getHealth());
        wBar.setString("HP: " + warrior.getHealth());
        mBar.setValue(mage.getHealth());
        mBar.setString("HP: " + mage.getHealth());

    }

    // game panel
    private class GamePanel extends JPanel {
        // draws the bg and character images
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // background
            if (bg != null)
                g2.drawImage(bg, 0, 0, getWidth(), getHeight(), this);

            int groundY = getHeight() - 180;

            draw(g2, mageImg, 50 + mage.getPosition() * 5, groundY -60, 300);
            draw(g2, warriorImg, 600 + warrior.getPosition() * 5, groundY - 10, 180);

        }
      // draws a character image at given position
        private void draw(Graphics2D g, Image img, int x, int y, int size) {
            if (img != null) {
                g.drawImage(img, x, y, size, size, this);
            }
        }

    }
    //main class
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BattleGui gui = new BattleGui();
            gui.setVisible(true);
            gui.appendToTerminal("Welcome to the Battle of Ancient Lands!");
            gui.appendToTerminal("You have to click to the button to play. Have fun !");
        });
    }
}

