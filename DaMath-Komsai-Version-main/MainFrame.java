import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.Border;

public class MainFrame extends JFrame implements ActionListener, MouseListener {

    JLabel welcomelbl, tolbl, damath53lbl;
    JButton playbtn, exitbtn;
    JTextArea instruction;
    GridBagConstraints gbc;
    JPanel mainPanel, boardPanel, instructionPanel, openingPanel;
    int currentPanel = 1;
    JButton restartButton, homeButton, instructionButton, nextbtn;

    JPanel game, buttonPanel;

    Clip clip;

    JPanel container, border1, border2, border3, border4;
    CardLayout cl = new CardLayout();

    public MainFrame() {
        setTitle("DaMath 53");
        setSize(1300, 800);
        setResizable(false);

        // Get the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Calculate the new location of the frame
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        // Set the location of the frame to the calculated coordinates
        setLocation(x, y);

        PanelTemplate();
        createOpening();
        container.add(openingPanel, "1");

        createInstruction();
        container.add(instructionPanel, "2");

        addComponentsToFrame();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        cl.show(container, "1");

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridBagLayout());

        createButtons();
        addToButtonPanel();
        // buttonPanel.add(restartButton);

        game = new JPanel();
        game.setSize(1400, 1000);
        game.setLayout(new BoxLayout(game, BoxLayout.X_AXIS));
        game.add(new Board1());
        game.add(buttonPanel);

        container.add(game, "3");
        playSound("C:\\Users\\Geralyn\\Desktop\\DaMath53\\DaMath-Komsai-Version-main\\resources\\intro.wav");
    }

    public void playSound(String filePath) {
        try {
            // Load audio file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Start playing
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PanelTemplate() {
        Border loweredbevelborder = BorderFactory.createRaisedBevelBorder();
        container = new JPanel();
        container.setSize(500, 500);
        container.setBackground(Color.GRAY);
        container.setLayout(cl);
        container.setBorder(loweredbevelborder);

        border1 = new JPanel();
        border1.setSize(500, 10);
        border1.setLayout(new BorderLayout());
        border1.setBorder(loweredbevelborder);

        border2 = new JPanel();
        border2.setSize(500, 10);
        border2.setLayout(new BorderLayout());
        border2.setBorder(loweredbevelborder);

        border3 = new JPanel();
        border3.setLayout(new BorderLayout());
        border3.setBorder(loweredbevelborder);

        border4 = new JPanel();
        border4.setSize(500, 10);
        border4.setLayout(new BorderLayout());
        border4.setBorder(loweredbevelborder);
    }

    private void addComponentsToFrame() {
        add(container, BorderLayout.CENTER);
        add(border1, BorderLayout.NORTH);
        add(border2, BorderLayout.SOUTH);
        add(border3, BorderLayout.EAST);
        add(border4, BorderLayout.WEST);
    }

    public void createButtons() {
        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Serif", Font.BOLD, 15));
        restartButton.setForeground(new Color(0xB7C9E2));
        restartButton.setPreferredSize(new Dimension(100, 40)); // Use setPreferredSize for consistent sizing
        restartButton.setBackground(new Color(0x5a8d03));
        restartButton.setOpaque(true);
        restartButton.setFocusPainted(false); // Remove focus outline
        restartButton.addMouseListener(this);

        homeButton = new JButton("Home");
        homeButton.setFont(new Font("Serif", Font.BOLD, 15));
        homeButton.setForeground(new Color(0xB7C9E2));
        homeButton.setPreferredSize(new Dimension(100, 40));
        homeButton.setBackground(new Color(0x5a8d03));
        homeButton.setFocusPainted(false); // Remove focus outline
        homeButton.setOpaque(true);
        homeButton.addMouseListener(this);
    }

    public void addToButtonPanel() {
        buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2); // Adding some padding

        gbc.gridx = 0;

        gbc.gridy = 0;
        buttonPanel.add(homeButton, gbc);

        gbc.gridy = 1;
        buttonPanel.add(restartButton, gbc);

        this.add(buttonPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        currentPanel++;
        cl.show(container, Integer.toString(currentPanel));
        clip.stop();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        try {
            if (e.getSource() instanceof JButton) {
                JButton clickedButton = (JButton) e.getComponent();
                if (clickedButton.getText().equals("Play")) {
                    currentPanel++;
                    cl.show(container, Integer.toString(currentPanel));
                }
                if (clickedButton.getText().equals("Next")) {
                    currentPanel++;
                    cl.show(container, Integer.toString(currentPanel));
                    clip.stop();
                }
                if (clickedButton.getText().equals("Exit")) {
                    clip.stop();
                    System.exit(0);
                }
                if ("Home".equals(clickedButton.getText())) {
                    initializeGamePanel();
                    currentPanel = 1;
                    cl.show(container, Integer.toString(currentPanel));
                    playSound("C:\\Users\\Geralyn\\Desktop\\DaMath53\\DaMath-Komsai-Version-main\\resources\\intro.wav");
                }
                if ("Restart".equals(clickedButton.getText())) {
                    initializeGamePanel();
                    currentPanel = 3;
                    cl.show(container, Integer.toString(currentPanel));
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void createOpening() {

        openingPanel = new JPanel();

        openingPanel.setLayout(new GridBagLayout());
        openingPanel.setBackground(Color.BLACK);

        welcomelbl = new JLabel("WELCOME");
        welcomelbl.setSize(500, 60);
        welcomelbl.setFont(new Font("Serif", Font.BOLD, 80));
        welcomelbl.setForeground(new Color(0x5a8d03));
        welcomelbl.setBackground(Color.BLACK);
        welcomelbl.setOpaque(true);

        tolbl = new JLabel("TO");
        tolbl.setSize(500, 30);
        tolbl.setFont(new Font("Serif", Font.BOLD, 50));
        tolbl.setForeground(new Color(0x5a8d03));
        tolbl.setBackground(Color.BLACK);
        tolbl.setOpaque(true);

        damath53lbl = new JLabel("DAMATH53");
        damath53lbl.setSize(500, 30);
        damath53lbl.setFont(new Font("Serif", Font.BOLD, 80));
        damath53lbl.setForeground(new Color(0x5a8d03));
        damath53lbl.setBackground(Color.BLACK);
        damath53lbl.setOpaque(true);

        playbtn = new JButton("Play");
        playbtn.setFont(new Font("Serif", Font.BOLD, 15));
        playbtn.setForeground(new Color(0xB7C9E2));
        playbtn.setPreferredSize(new Dimension(50, 40));
        playbtn.setBackground(new Color(0x5a8d03));
        playbtn.setOpaque(true);
        playbtn.setFocusPainted(false); // Remove focus outline
        playbtn.addMouseListener(this);

        // creating new Grid Bag Constraints
        gbc = new GridBagConstraints();

        // Creating insets to all components
        gbc.insets = new Insets(2, 2, 2, 2);

        // column 0
        gbc.gridx = 0;

        // row 0
        gbc.gridy = 0;

        // increases length by 15 pixels
        gbc.ipadx = 15;

        // increases width by 50 pixels
        gbc.ipadx = 10;

        openingPanel.add(welcomelbl, gbc);

        // column 0
        gbc.gridx = 0;

        // row 1
        gbc.gridy = 1;

        openingPanel.add(tolbl, gbc);

        // column 0
        gbc.gridx = 0;

        // row 2
        gbc.gridy = 2;

        openingPanel.add(damath53lbl, gbc);

        // column 0
        gbc.gridx = 0;

        // row 3
        gbc.gridy = 3;

        // increases length by 15 pixels
        gbc.ipadx = 200;

        // increases width by 50 pixels
        gbc.ipadx = 100;

        openingPanel.add(playbtn, gbc);
    }

    public void createInstruction() {
        instructionPanel = new JPanel();
        instructionPanel.setLayout(new GridBagLayout());
        instructionPanel.setBackground(Color.BLACK);

        // createInstruction
        instruction = new JTextArea(
                "\n\n\n                         \t\tWelcome to DaMath53! Embark yourself on a fun and strategic board game that will improve your mathematical skills."
                        +
                        "\n                         \t\tThe mechanics of this game will involve two players, each given 12 chips placed in the corresponding spots."
                        +

                        "\n                         \t\tTo win, a player must obtain the highest score by defeating the other player's chips"
                        +
                        "\n                         \t\tPlayers will earn points by defeating the other player's chips. Note that points may include negative values."
                        +
                        "\n                         \t\tNote that points may include negative. Good luck and Enjoy!" +
                        "\n                         \t\tPress the start button to begin your journey in Mathematical Operations!");
        // instruction.setFont(new Font(" Courier New", Font.BOLD, 15));
        instruction.setFont(new Font("Serif", Font.BOLD, 15));
        instruction.setForeground(new Color(0xB7C9E2));
        // instruction.setSize(200, 100);
        instruction.setBackground(new Color(0x5a8d03));
        instruction.setOpaque(true);
        instruction.setEditable(false);

        // create Next Button

        nextbtn = new JButton("Play");
        nextbtn.setFont(new Font("Serif", Font.BOLD, 15));
        nextbtn.setForeground(new Color(0xB7C9E2));
        // playbtn.setSize(200, 100);
        nextbtn.setBackground(new Color(0x5a8d03));
        nextbtn.setFocusPainted(false); // Remove focus outline
        nextbtn.setOpaque(true);
        nextbtn.addActionListener(this);

        exitbtn = new JButton("Exit");
        exitbtn.setFont(new Font("Serif", Font.BOLD, 15));
        exitbtn.setForeground(new Color(0xB7C9E2));
        // playbtn.setSize(200, 100);
        exitbtn.setFocusPainted(false); // Remove focus outline
        exitbtn.setBackground(new Color(0x5a8d03));
        exitbtn.setOpaque(true);
        exitbtn.addMouseListener(this);

        // add To Panel

        // creating new Grid Bag Constraints
        gbc = new GridBagConstraints();

        // Creating insets to all components
        gbc.insets = new Insets(4, 2, 2, 2);

        // column 0
        gbc.gridx = 0;

        // row 0
        gbc.gridy = 0;

        // increases length by 15 pixels
        gbc.ipadx = 200;

        // increases width by 50 pixels
        gbc.ipady = 100;

        instructionPanel.add(instruction, gbc);

        // column 0
        gbc.gridx = 0;

        // row 1
        gbc.gridy = 1;

        // increases length by 15 pixels
        gbc.ipadx = 0;

        // increases width by 50 pixels
        gbc.ipady = 2;

        instructionPanel.add(nextbtn, gbc);

        // column 0
        gbc.gridx = 0;

        // row 1
        gbc.gridy = 2;

        // increases length by 15 pixels
        gbc.ipadx = 0;

        // increases width by 50 pixels
        gbc.ipady = 2;

        instructionPanel.add(exitbtn, gbc);
    }

    private void initializeGamePanel() {
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridBagLayout());

        buttonPanel = new JPanel();
        buttonPanel.setSize(200, 1000);
        buttonPanel.setBackground(Color.cyan);
        buttonPanel.setLayout(new GridLayout(1, 1));

        createButtons();
        addToButtonPanel();
        // buttonPanel.add(restartButton);

        game = new JPanel();
        game.setSize(1400, 1000);
        game.setLayout(new BoxLayout(game, BoxLayout.X_AXIS));
        game.add(new Board1());
        game.add(buttonPanel);

        container.add(game, "3");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());

    }
}
