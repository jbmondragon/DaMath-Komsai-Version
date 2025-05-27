import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.Border;

public class MainFrame extends JFrame implements ActionListener, MouseListener {

    private JLabel welcomelbl, tolbl, damath53lbl;
    private JButton playbtn, exitbtn, restartButton, homeButton, nextbtn;
    private JTextArea instruction;
    private GridBagConstraints gbc;
    private JPanel mainPanel, boardPanel, instructionPanel, openingPanel;
    private JPanel container, border1, border2, border3, border4, game, buttonPanel;
    private CardLayout cl = new CardLayout();
    private Clip clip;
    private int currentPanel = 1;

    public MainFrame() {
        setTitle("DaMath 53");
        setSize(1300, 800);
        setResizable(false);
        centerFrame();

        initPanelTemplate();
        createOpening();
        container.add(openingPanel, "1");

        createInstruction();
        container.add(instructionPanel, "2");

        addFrameComponents();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        cl.show(container, "1");

        initializeGamePanel();
        playSound(getClass().getResource("/resources/intro.wav"));
    }

    private void centerFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2);
    }

    private void initPanelTemplate() {
        Border border = BorderFactory.createRaisedBevelBorder();
        container = createPanel(Color.GRAY, cl, border);
        border1 = createPanel(null, new BorderLayout(), border);
        border2 = createPanel(null, new BorderLayout(), border);
        border3 = createPanel(null, new BorderLayout(), border);
        border4 = createPanel(null, new BorderLayout(), border);
    }

    private JPanel createPanel(Color bg, LayoutManager layout, Border border) {
        JPanel panel = new JPanel();
        if (bg != null) panel.setBackground(bg);
        if (layout != null) panel.setLayout(layout);
        if (border != null) panel.setBorder(border);
        return panel;
    }

    private void addFrameComponents() {
        add(container, BorderLayout.CENTER);
        add(border1, BorderLayout.NORTH);
        add(border2, BorderLayout.SOUTH);
        add(border3, BorderLayout.EAST);
        add(border4, BorderLayout.WEST);
    }

    public void playSound(URL filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(filePath);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JButton createStyledButton(String text, boolean useListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.BOLD, 15));
        button.setForeground(new Color(0xB7C9E2));
        button.setBackground(new Color(0x5a8d03));
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(100, 40));
        if (useListener) button.addMouseListener(this);
        return button;
    }

    public void createButtons() {
        restartButton = createStyledButton("Restart", true);
        homeButton = createStyledButton("Home", true);
    }

    public void addToButtonPanel() {
        buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(Color.WHITE);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);

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
        if (!(e.getSource() instanceof JButton)) return;

        JButton clicked = (JButton) e.getComponent();
        String label = clicked.getText();

        switch (label) {
            case "Play":
                currentPanel++;
                cl.show(container, Integer.toString(currentPanel));
                break;
            case "Next":
                currentPanel++;
                cl.show(container, Integer.toString(currentPanel));
                clip.stop();
                break;
            case "Exit":
                clip.stop();
                System.exit(0);
                break;
            case "Home":
                initializeGamePanel();
                currentPanel = 1;
                cl.show(container, Integer.toString(currentPanel));
                playSound(getClass().getResource("resources/intro.wav"));
                break;
            case "Restart":
                initializeGamePanel();
                currentPanel = 3;
                cl.show(container, Integer.toString(currentPanel));
                break;
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    public void createOpening() {
        openingPanel = new JPanel(new GridBagLayout());
        openingPanel.setBackground(Color.BLACK);

        welcomelbl = createLabel("WELCOME", 80);
        tolbl = createLabel("TO", 50);
        damath53lbl = createLabel("DAMATH53", 80);
        playbtn = createStyledButton("Play", true);
        playbtn.setPreferredSize(new Dimension(300, 60));

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.gridx = 0;

        gbc.gridy = 0;
        openingPanel.add(welcomelbl, gbc);
        gbc.gridy = 1;
        openingPanel.add(tolbl, gbc);
        gbc.gridy = 2;
        openingPanel.add(damath53lbl, gbc);
        gbc.gridy = 3;
        openingPanel.add(playbtn, gbc);
    }

    private JLabel createLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Serif", Font.BOLD, fontSize));
        label.setForeground(new Color(0x5a8d03));
        label.setBackground(Color.BLACK);
        label.setOpaque(true);
        return label;
    }

    public void createInstruction() {
        instructionPanel = new JPanel(new GridBagLayout());
        instructionPanel.setBackground(Color.BLACK);

        instruction = new JTextArea(
                "\n\n\n                         \t\tWelcome to DaMath53! Embark yourself on a fun and strategic board game that will improve your mathematical skills."
                        + "\n                         \t\tThe mechanics of this game will involve two players, each given 12 chips placed in the corresponding spots."
                        + "\n                         \t\tTo win, a player must obtain the highest score by defeating the other player's chips"
                        + "\n                         \t\tPlayers will earn points by defeating the other player's chips. Note that points may include negative values."
                        + "\n                         \t\tNote that points may include negative. Good luck and Enjoy!"
                        + "\n                         \t\tPress the start button to begin your journey in Mathematical Operations!");
        instruction.setFont(new Font("Serif", Font.BOLD, 15));
        instruction.setForeground(new Color(0xB7C9E2));
        instruction.setBackground(new Color(0x5a8d03));
        instruction.setOpaque(true);
        instruction.setEditable(false);

        nextbtn = createStyledButton("Play", false);
        nextbtn.addActionListener(this);

        exitbtn = createStyledButton("Exit", true);

        gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 2, 2, 2);
        gbc.gridx = 0;

        gbc.gridy = 0;
        gbc.ipadx = 200;
        gbc.ipady = 100;
        instructionPanel.add(instruction, gbc);

        gbc.gridy = 1;
        gbc.ipadx = 0;
        gbc.ipady = 2;
        instructionPanel.add(nextbtn, gbc);

        gbc.gridy = 2;
        instructionPanel.add(exitbtn, gbc);
    }

    private void initializeGamePanel() {
        boardPanel = new JPanel(new GridBagLayout());

        buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(200, 1000));
        buttonPanel.setBackground(Color.cyan);
        buttonPanel.setLayout(new GridLayout(1, 1));

        createButtons();
        addToButtonPanel();

        game = new JPanel();
        game.setSize(1400, 1000);
        game.setLayout(new BoxLayout(game, BoxLayout.X_AXIS));
        game.add(new Board1());
        game.add(buttonPanel);

        container.add(game, "3");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
