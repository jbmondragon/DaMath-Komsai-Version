import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

public class Board1 extends JPanel {

    JPanel board_panel, mainBoard, rightSide_panel;
    JPanel gameOver, scoreBoard, firstPlayer, secondPlayer;
    GridBagConstraints gbc = new GridBagConstraints();

    JLabel board[][] = new JLabel[8][8];
    JLabel operatorLabel[][] = new JLabel[8][8];
    JLabel chipsLabel[][] = new JLabel[8][8];
    JPanel sidePanel, restartPanel;
    JLabel player0time, player1time;
    JLabel targetChip;
    JLabel newLocation;
    JLabel selectedyarn = null, selectedChip = null;

    Timer date = new Timer();
    JTable pTable;
    JScrollPane sp1, sp2;
    DefaultTableModel model1, model2;
    String a, b, c, d;
    int yellowDama = 12, redDama = 12, selectedChipValue, targetChipValue;

    float player0Score = 0, player1Score = 0;

    boolean player0 = true;
    boolean player1 = false;
    boolean canEatDama = false;
    boolean onlyEat = false;
    boolean stop = false;
    boolean[] surround = new boolean[4];
    boolean[] canStillEat = new boolean[4];
    boolean eat = false;

    private final Color HIGHLIGHT_MOVE_COLOR = new Color(173, 216, 230); // Light Blue for possible moves
    private final Color HIGHLIGHT_CAPTURE_COLOR = new Color(255, 99, 71); // Tomato color for possible captures

    int NONE = 0, Yellow = 1, YellowKing = 2, Red = 3, RedKing = 4;
    int damaRowBlock = 0, damaColBlock = 0;
    int previousRow = 0, previousCol = 0;
    int operatorIndexRow = 0, operatorIndexCol = 0;
    int chipsIndexRow = 0, chipsIndexCol = 0;
    int dec = -1, inc = 1;
    int it, check = 0;
    int playerCtr = 1, st = 1, nd = 2;

    int[][] playerData = new int[8][8];
    int[][] dec_inc_Eat = {
            { 0, 0 },
            { 0, 0 },
            { 0, 0 },
            { 0, 0 },
    };

    int[][] resetEat = {
            { 0, 0 },
            { 0, 0 },
            { 0, 0 },
            { 0, 0 },
    };

    String[][] operator = {
            { "x", "/", "-", "+" },
            { "/", "x", "+", "-" },
            { "-", "+", "x", "/" },
            { "+", "-", "/", "x" },
            { "x", "/", "-", "+" },
            { "/", "x", "+", "-" },
            { "-", "+", "x", "/" },
            { "+", "-", "/", "x" },
    };
    String[][] chips = {
            { "24", "23", "22", "21" },
            { "20", "19", "18", "17" },
            { "16", "15", "14", "13" },
            { "1", "2", "3", "4" },
            { "5", "6", "7", "8" },
            { "9", "10", "11", "12" }
    };
    String[][] valueOfChips = {
            { "12", "11", "10", "9" },
            { "8", "7", "4", "5" },
            { "4", "3", "2", "1" },
            { "1", "2", "3", "4" },
            { "5", "4", "7", "8" },
            { "9", "10", "11", "12" }
    };
    String o;

    public Board1() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        createContainerPanel();
        createBoard();
        displayChips();
        createT1();
        createT2();
        createTimePanel();
        createTimeLabel();
        addComponentsToPanel();
    }

    // creating panel for the board
    public void createContainerPanel() {
        board_panel = new JPanel();
        board_panel.setLayout(new GridLayout(8, 8));
        board_panel.setPreferredSize(new Dimension(800, 800));
    }

    // Creating board
    public void createBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JLabel label = new JLabel();
                label.setLayout(new GridBagLayout()); // Change to GridBagLayout
                label.setOpaque(true);
                label.setHorizontalAlignment(JLabel.CENTER);
                label.setVerticalAlignment(JLabel.CENTER);
                label.setBorder(new LineBorder(Color.BLACK));
                board[i][j] = label;

                // Mouse listener to handle clicks
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleMouseClick(e);
                    }
                });

                if ((i + j) % 2 == 0) {
                    board[i][j].setBackground(Color.WHITE);
                    board[i][j].setText(operator[operatorIndexRow][operatorIndexCol]);
                    operatorIndexCol++;
                } else {
                    board[i][j].setBackground(Color.BLACK);
                }
                board_panel.add(board[i][j]);
            }
            operatorIndexCol = 0;
            operatorIndexRow++;
        }
    }

    // Displaying chips
    private void displayChips() {
        int chipsIndexRow = 0, chipsIndexCol = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                playerData[i][j] = NONE;
                if ((i + j) % 2 == 0 && (i < 3 || i > 4)) {
                    if (i < 3)
                        playerData[i][j] = Yellow; // assign yellow chips from row 0 to 2
                    else
                        playerData[i][j] = Red; // assign yellow chips from row 5 to 7
                    chipsLabel[i][j] = new Chips(chipsIndexRow % 6, chipsIndexCol % 4, chips, valueOfChips);

                    // Add chips to the center of the JLabel
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    gbc.anchor = GridBagConstraints.CENTER;
                    board[i][j].add(chipsLabel[i][j], gbc);

                    chipsIndexCol++;
                }
            }
            chipsIndexCol = 0;
            if (i < 3 || i > 4) {
                chipsIndexRow++;
            }
        }
    }

    // creating table 1 for player 0 scoreboard
    public void createT1() {
        String[] columnNames = { "Player0" };
        model1 = new DefaultTableModel(columnNames, 0);
        pTable = new JTable(model1);
        pTable.setEnabled(false);

        // adding it to scrollane
        sp1 = new JScrollPane(pTable);
    }

    // creating table 1 for player 1 scoreboard
    public void createT2() {
        String[] columnNames = { "Player1" };
        model2 = new DefaultTableModel(columnNames, 0);
        pTable = new JTable(model2);
        pTable.setEnabled(false);

        // adding it to scrollane
        sp2 = new JScrollPane(pTable);
    }

    // method for adding rows for recording the data or score once the player eats a
    // chip
    public void addRow1(String data1) {
        model1.addRow(new Object[] { data1 });
    }

    public void addRow2(String data1) {
        model2.addRow(new Object[] { data1 });
    }

    // creating panel for timer
    public void createTimePanel() {
        sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(300, 800));
        sidePanel.setLayout(new GridLayout(4, 2));
        sidePanel.setBackground(new Color(0x5a8d03));
    }

    // labels for time label for player 1 and 2
    public void createTimeLabel() {
        player0time = new JLabel();
        player1time = new JLabel();
    }

    // adding all the created labels and panels to its correndponding mainpanels
    public void addComponentsToPanel() {
        sidePanel.add(sp1);
        sidePanel.add(player0time);
        sidePanel.add(sp2);
        sidePanel.add(player1time);
        sidePanel.add(player1time);

        add(board_panel);
        add(sidePanel);
    }

    // main method for handling the chips of the game
    private void handleMouseClick(MouseEvent e) {

        JLabel clickedLabel = (JLabel) e.getSource(); // what label has been clicked
        Component[] components = clickedLabel.getComponents(); // what component is in that label

        int clickedX = clickedLabel.getX();
        int clickedY = clickedLabel.getY();

        int boardPanelWidth = board_panel.getWidth();
        int columnWidth = boardPanelWidth / 8;

        int clickedCol = (clickedX / columnWidth); // calculating the row of the clicked chips
        int clickedRow = clickedY / (getHeight() / 8); // calculating the column of the clicked chips

        boolean isPlayer0Turn = player0;
        boolean isPlayer1Turn = player1;
        int currentCellData = playerData[clickedRow][clickedCol];

        // checks if the clicked chips is yellow
        if (playerData[clickedRow][clickedCol] == Yellow) {
            try {
                date.scheduleAtFixedRate(task, 0, 1000); // timer starts once confirmed that it's a yellow chip
            } catch (Exception ee) {
                System.out.println();
            }
        }

        if (onlyEat && playerData[clickedRow][clickedCol] == playerData[previousRow][previousCol]) {
            // if selected component is a chip then we will highlight possible moves
            if (clickedLabel.getComponentCount() > 0 && components[0] instanceof Chips) {
                selectedChip = (JLabel) clickedLabel.getComponent(0);
                highlightPossibleMoves(clickedRow, clickedCol);
                previousRow = clickedRow;
                previousCol = clickedCol;

                selectedChipValue = Integer.parseInt(((Chips) selectedChip).getChipValue());
            }
        }

        else if (selectedChip == null && !onlyEat
                && ((isPlayer0Turn && (currentCellData == Yellow || currentCellData == YellowKing)) ||
                        (isPlayer1Turn && (currentCellData == Red || currentCellData == RedKing)))) {
            if (clickedLabel.getComponentCount() > 0 && components[0] instanceof Chips) {
                System.out.println("Chip selected for movement.");
                selectedChip = (JLabel) clickedLabel.getComponent(0);
                previousRow = clickedRow;
                previousCol = clickedCol;

                selectedChipValue = Integer.parseInt(((Chips) selectedChip).getChipValue());
                highlightPossibleMoves(clickedRow, clickedCol);
            }
        }

        else if (selectedChip != null) {
            clearHighlights();

            if (moveChip(clickedLabel)) {
                clickedLabel.repaint();
                if (isPlayer0Turn && !onlyEat && isPLayerAvail()) {
                    player0 = false;
                    player1 = true;
                } else if (!onlyEat && isPLayerAvail()) {
                    player1 = false;
                    player0 = true;
                } else if (!isPLayerAvail()) {
                    stop = true;
                    System.out.println("Mastop it na dapat");
                }
            } else
                selectedChip = null;

            if (onlyEat)
                board[previousRow][previousCol].setBackground(HIGHLIGHT_MOVE_COLOR);

        }
    }

    private boolean isPLayerAvail() {

        int[] rowDirections = { -1, -1, 1, 1 };
        int[] colDirections = { -1, 1, -1, 1 };
        int noChipMove = 0;

        if (player0) {
            for (int rowLocation = 0; rowLocation < 8; rowLocation++) {
                for (int colLocation = 0; colLocation < 8; colLocation++) {
                    if ((rowLocation + colLocation) % 2 == 0 && (playerData[rowLocation][colLocation] == Red)) {
                        for (int direction = 0; direction < 4; direction++) {
                            int newRow = rowLocation + rowDirections[direction];
                            int newCol = colLocation + colDirections[direction];

                            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8
                                    && playerData[newRow][newCol] == NONE
                                    && playerData[rowLocation][colLocation] == Red && newRow < rowLocation
                                    && direction < 2) {
                                System.out.println("nagreturn ak true1");
                                return true;
                            }

                            int captureRow = rowLocation + 2 * rowDirections[direction];
                            int captureCol = colLocation + 2 * colDirections[direction];

                            if (captureRow >= 0 && captureRow < 8 && captureCol >= 0 && captureCol < 8 &&
                                    playerData[captureRow][captureCol] == NONE &&
                                    (playerData[newRow][newCol] == Yellow
                                            || playerData[newRow][newCol] == YellowKing)) {
                                System.out.println("nagreturn ak true2");
                                return true;
                            }
                        }
                        noChipMove++;
                        System.out.println("No chip move: " + noChipMove);
                    }

                    if ((rowLocation + colLocation) % 2 == 0 && (playerData[rowLocation][colLocation] == RedKing)) {
                        for (int direction = 0; direction < 4; direction++) {
                            for (int distance = 1; distance < 8; distance++) {
                                int newRow = rowLocation + distance * rowDirections[direction];
                                int newCol = colLocation + distance * colDirections[direction];

                                if (newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8)
                                    break;

                                if (playerData[newRow][newCol] == NONE) {
                                    return true;
                                } else if (playerData[newRow][newCol] == Yellow
                                        || playerData[newRow][newCol] == YellowKing) {
                                    int jumpRow = newRow + rowDirections[direction];
                                    int jumpCol = newCol + colDirections[direction];

                                    if (jumpRow >= 0 && jumpRow < 8 && jumpCol >= 0 && jumpCol < 8
                                            && playerData[jumpRow][jumpCol] == NONE) {
                                        return true;
                                    } else
                                        break;
                                } else {
                                    break;
                                }
                            }
                        }
                        noChipMove++;
                    }
                }
            }
        } else {
            for (int rowLocation = 0; rowLocation < 8; rowLocation++) {
                for (int colLocation = 0; colLocation < 8; colLocation++) {
                    if ((rowLocation + colLocation) % 2 == 0 && (playerData[rowLocation][colLocation] == Yellow)) {
                        for (int direction = 0; direction < 4; direction++) {
                            int newRow = rowLocation + rowDirections[direction];
                            int newCol = colLocation + colDirections[direction];

                            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8
                                    && playerData[newRow][newCol] == NONE
                                    && playerData[rowLocation][colLocation] == Yellow && newRow > rowLocation
                                    && direction > 1) {
                                return true;
                            }

                            int captureRow = rowLocation + 2 * rowDirections[direction];
                            int captureCol = colLocation + 2 * colDirections[direction];

                            if (captureRow >= 0 && captureRow < 8 && captureCol >= 0 && captureCol < 8 &&
                                    playerData[captureRow][captureCol] == NONE &&
                                    (playerData[newRow][newCol] == Red
                                            || playerData[newRow][newCol] == RedKing)) {
                                System.out.println("nagreturn ak true3");
                                return true;
                            }
                        }
                        noChipMove++;
                    }
                    if ((rowLocation + colLocation) % 2 == 0 && (playerData[rowLocation][colLocation] == YellowKing)) {
                        for (int direction = 0; direction < 4; direction++) {
                            for (int distance = 1; distance < 8; distance++) {
                                int newRow = rowLocation + distance * rowDirections[direction];
                                int newCol = colLocation + distance * colDirections[direction];

                                if (newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8)
                                    break;

                                if (playerData[newRow][newCol] == NONE) {
                                    return true;
                                } else if (playerData[newRow][newCol] == Red || playerData[newRow][newCol] == RedKing) {
                                    int jumpRow = newRow + rowDirections[direction];
                                    int jumpCol = newCol + colDirections[direction];

                                    if (jumpRow >= 0 && jumpRow < 8 && jumpCol >= 0 && jumpCol < 8
                                            && playerData[jumpRow][jumpCol] == NONE) {
                                        return true;
                                    } else
                                        break;
                                } else {
                                    break;
                                }
                            }
                        }
                        noChipMove++;
                    }
                }
            }
        }

        if (player0 && noChipMove == redDama)
            return false;
        else if (player1 && noChipMove == yellowDama)
            return false;
        else
            return true;

    }

    private void highlightPossibleMoves(int startRow, int startCol) {
        clearHighlights();

        int[] rowDirections = { -1, -1, 1, 1 };
        int[] colDirections = { -1, 1, -1, 1 };
        int chips;

        if (playerData[startRow][startCol] == YellowKing || playerData[startRow][startCol] == RedKing) {
            for (int direction = 0; direction < 4; direction++) {
                chips = 0;
                for (int distance = 1; distance < 8; distance++) {
                    int newRow = startRow + distance * rowDirections[direction];
                    int newCol = startCol + distance * colDirections[direction];

                    if (newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8)
                        break;

                    if (playerData[newRow][newCol] == NONE && !onlyEat && chips == 0) {
                        board[newRow][newCol].setBackground(HIGHLIGHT_MOVE_COLOR);
                    } else if (playerData[newRow][newCol] == NONE && onlyEat && chips == 0) {
                        continue;
                    } else if (playerData[newRow][newCol] != NONE && chips == 1) {
                        break;
                    } else if (playerData[newRow][newCol] == NONE && chips == 1) {
                        board[newRow][newCol].setBackground(HIGHLIGHT_CAPTURE_COLOR);
                    } else if ((player0 && (playerData[newRow][newCol] == Red || playerData[newRow][newCol] == RedKing))
                            ||
                            (player1 && (playerData[newRow][newCol] == Yellow
                                    || playerData[newRow][newCol] == YellowKing)) && chips < 1) {
                        int jumpRow = newRow + rowDirections[direction];
                        int jumpCol = newCol + colDirections[direction];

                        if (jumpRow >= 0 && jumpRow < 8 && jumpCol >= 0 && jumpCol < 8
                                && playerData[jumpRow][jumpCol] == NONE) {
                            board[jumpRow][jumpCol].setBackground(HIGHLIGHT_CAPTURE_COLOR);
                            chips++;
                        } else
                            break;
                    } else {
                        break;
                    }
                }
            }
        } else {
            for (int direction = 0; direction < 4; direction++) {
                int newRow = startRow + rowDirections[direction];
                int newCol = startCol + colDirections[direction];

                if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8 && playerData[newRow][newCol] == NONE
                        && playerData[startRow][startCol] == Yellow && newRow > startRow && !onlyEat) {
                    board[newRow][newCol].setBackground(HIGHLIGHT_MOVE_COLOR);
                } else if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8 && playerData[newRow][newCol] == NONE
                        && playerData[startRow][startCol] == Red && newRow < startRow && !onlyEat) {
                    board[newRow][newCol].setBackground(HIGHLIGHT_MOVE_COLOR);
                }

                int captureRow = startRow + 2 * rowDirections[direction];
                int captureCol = startCol + 2 * colDirections[direction];

                if (captureRow >= 0 && captureRow < 8 && captureCol >= 0 && captureCol < 8 &&
                        playerData[captureRow][captureCol] == NONE &&
                        ((player0 && (playerData[newRow][newCol] == Red || playerData[newRow][newCol] == RedKing)) ||
                                (player1 && (playerData[newRow][newCol] == Yellow
                                        || playerData[newRow][newCol] == YellowKing)))) {
                    board[captureRow][captureCol].setBackground(HIGHLIGHT_CAPTURE_COLOR);
                }
            }
        }
    }

    private void clearHighlights() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    board[i][j].setBackground(Color.WHITE);
                } else {
                    board[i][j].setBackground(Color.BLACK);
                }
            }
        }
    }

    private boolean moveChip(JLabel destinationLabel) {
        if (selectedChip != null && destinationLabel.getComponentCount() == 0) {

            int clickedX = destinationLabel.getX();
            int clickedY = destinationLabel.getY();

            int boardPanelWidth = board_panel.getWidth();
            int columnWidth = boardPanelWidth / 8;

            // Calculate column by considering the side panel
            int col = (clickedX / columnWidth);
            int row = clickedY / (getHeight() / 8);

            if (isValidMove(previousRow, previousCol, row, col)) {
                System.out.println("Valid move");
                board[previousRow][previousCol].remove(selectedChip); // Remove chip from previous position
                board[previousRow][previousCol].revalidate();
                board[previousRow][previousCol].repaint();

                playMoveSound();

                playerData[row][col] = playerData[previousRow][previousCol];
                playerData[previousRow][previousCol] = NONE;

                destinationLabel.add(selectedChip); // Add chip to the new position
                o = destinationLabel.getText(); // get the operation of the destination label
                destinationLabel.revalidate();
                destinationLabel.repaint();

                int rowDirection = (row - previousRow) > 0 ? 1 : -1;
                int colDirection = (col - previousCol) > 0 ? 1 : -1;

                isPathClear(previousRow, previousCol, row, col);

                // If it's a capturing move, remove the captured chip
                if (Math.abs(previousRow - row) == 2 && Math.abs(previousCol - col) == 2
                        && (playerData[row][col] != YellowKing && playerData[row][col] != RedKing)) {
                    int capturedRow = (previousRow + row) / 2;
                    int capturedCol = (previousCol + col) / 2;

                    playCaptureSound();
                    System.out.println(yellowDama);
                    System.out.println(redDama);

                    if (player0) {
                        redDama--;
                    } else {
                        yellowDama--;
                    }

                    System.out.println("Ginkakaon ba 1");

                    // Integer.parseInt(((Chips) selectedChip).getChipValue())
                    targetChip = (JLabel) board[capturedRow][capturedCol].getComponent(0);

                    targetChipValue = Integer.parseInt(((Chips) targetChip).getChipValue());

                    if (playerData[capturedRow][capturedCol] == YellowKing
                            || playerData[capturedRow][capturedCol] == RedKing) {
                        calculateDamaEat(selectedChipValue, o, targetChipValue);
                    } else
                        calculate(selectedChipValue, o, targetChipValue);

                    board[capturedRow][capturedCol].removeAll(); // Remove captured chip
                    board[capturedRow][capturedCol].revalidate();
                    board[capturedRow][capturedCol].repaint();
                    playerData[capturedRow][capturedCol] = NONE;

                    for (int i = 0; i < 4; i++) {
                        surround[i] = false;
                    }

                    onlyEat = isSurrounded(row, col);

                    // checks for double eating can still eat after eating one chips
                    if (onlyEat) {
                        System.out.println("Eat");
                        previousCol = col;
                        previousRow = row;
                        board[previousRow][previousCol].setBackground(HIGHLIGHT_MOVE_COLOR);
                        return true;
                    }

                    System.out.println("Capturing move");
                } else if (isPathClear(previousRow, previousCol, row, col)
                        && ((playerData[row][col] == YellowKing) || (playerData[row][col] == RedKing))) {
                    // Handle long-range capture for king pieces

                    System.out.println("Long-range king move");
                    int capturedRow = previousRow + rowDirection;
                    int capturedCol = previousCol + colDirection;
                    int chipCount = 0;
                    int chipRow = -1;
                    int chipCol = -1;

                    while (capturedRow != row && capturedCol != col) {
                        if (playerData[capturedRow][capturedCol] != NONE) {
                            chipCount++;
                            chipRow = capturedRow;
                            chipCol = capturedCol;
                        }
                        capturedRow += rowDirection;
                        capturedCol += colDirection;
                    }

                    if (chipCount == 1) {

                        targetChip = (JLabel) board[chipRow][chipCol].getComponent(0);

                        targetChipValue = Integer.parseInt(((Chips) targetChip).getChipValue());

                        if ((playerData[chipRow][chipCol] == YellowKing || playerData[chipRow][chipCol] == RedKing)
                                && (playerData[row][col] == RedKing || playerData[row][col] == YellowKing)) {
                            calculate2DamaEat(selectedChipValue, o, targetChipValue);
                        } else {
                            calculateDamaEat(selectedChipValue, o, targetChipValue);
                        }

                        board[chipRow][chipCol].removeAll(); // Remove captured chip
                        board[chipRow][chipCol].revalidate();
                        board[chipRow][chipCol].repaint();
                        playerData[chipRow][chipCol] = NONE;

                        if (player0) {
                            redDama--;
                        } else {
                            yellowDama--;
                        }

                        System.out.println("Ginkakaon ba 3");
                        System.out.println(yellowDama);
                        System.out.println(redDama);

                        playCaptureSound();

                        for (int i = 0; i < 4; i++) {
                            surround[i] = false;
                        }

                        isAvailable(row, col);

                        if (onlyEat) {
                            System.out.println("Eat");
                            previousCol = col;
                            previousRow = row;
                            board[previousRow][previousCol].setBackground(HIGHLIGHT_MOVE_COLOR);
                        } else
                            onlyEat = false;

                    } else if (chipCount > 1) {
                        playerData[previousRow][previousCol] = playerData[row][col];
                        playerData[row][col] = NONE;
                        destinationLabel.remove(selectedChip); // Remove chip from the new position
                        board[previousRow][previousCol].add(selectedChip); // Re-add chip to the previous position
                        selectedChip = null;
                        board[previousRow][previousCol].revalidate();
                        board[previousRow][previousCol].repaint();
                        return false;
                    }

                }

                if (playerData[row][col] == Yellow && row == 7)
                    playerData[row][col] = YellowKing;
                else if (playerData[row][col] == Red && row == 0)
                    playerData[row][col] = RedKing;

                selectedChip = null;

                return true;
            }
        }
        return false;
    }

    private boolean isValidMove(int startRow, int startCol, int endRow, int endCol) {

        if (playerData[startRow][startCol] == Yellow || playerData[startRow][startCol] == YellowKing) {
            // Check if the move is within the bounds of the board
            if (endRow < 0 || endRow >= 8 || endCol < 0 || endCol >= 8) {
                return false;
            }

            // Simple move
            int rowDelta = endRow - startRow;
            int colDelta = endCol - startCol;

            if (Math.abs(rowDelta) == 1 && Math.abs(colDelta) == 1 && endRow > startRow && !onlyEat) {
                return true;
            } else if (Math.abs(rowDelta) == Math.abs(colDelta) && isPathClear(startRow, startCol, endRow, endCol)
                    && playerData[startRow][startCol] == YellowKing && !onlyEat) {
                return true;
            }

            // Capturing move
            if (Math.abs(rowDelta) == 2 && Math.abs(colDelta) == 2) {
                int capturedRow = (startRow + endRow) / 2;
                int capturedCol = (startCol + endCol) / 2;

                if (board[capturedRow][capturedCol].getComponentCount() > 0
                        && (playerData[capturedRow][capturedCol] == Red
                                || playerData[capturedRow][capturedCol] == RedKing)) {
                    Component capturedComponent = board[capturedRow][capturedCol].getComponent(0);
                    if (capturedComponent instanceof Chips) {
                        return true;
                    }
                }
            }

            int rowDirection = (endRow - startRow) > 0 ? 1 : -1;
            int colDirection = (endCol - startCol) > 0 ? 1 : -1;
            int capturedRow = startRow + rowDirection;
            int capturedCol = startCol + colDirection;
            int chipCount = 0;
            int chipRow = -1;
            int chipCol = -1;

            while (capturedRow != endRow && capturedCol != endCol) {
                if (playerData[capturedRow][capturedCol] != NONE) {
                    chipCount++;
                    chipRow = capturedRow;
                    chipCol = capturedCol;
                }
                capturedRow += rowDirection;
                capturedCol += colDirection;
            }

            if (chipCount == 1) {
                capturedRow = chipRow;
                capturedCol = chipCol;
            } else if (chipCount > 1) {
                return false;
            }

            if (Math.abs(rowDelta) == Math.abs(colDelta) && chipCount == 1
                    && playerData[startRow][startCol] == YellowKing) {
                // capture for dama
                if (board[capturedRow][capturedCol].getComponentCount() > 0
                        && ((player0 && (playerData[capturedRow][capturedCol] == Red
                                || playerData[capturedRow][capturedCol] == RedKing)))) {
                    Component capturedComponent = board[capturedRow][capturedCol].getComponent(0);
                    if (capturedComponent instanceof Chips) {
                        return true;
                    }
                }

                return false;
            }

            return false;
        } else if (playerData[startRow][startCol] == Red || playerData[startRow][startCol] == RedKing) {
            // Check if the move is within the bounds of the board
            if (endRow < 0 || endRow >= 8 || endCol < 0 || endCol >= 8) {
                return false;
            }

            // Simple move
            int rowDelta = endRow - startRow;
            int colDelta = endCol - startCol;

            if (Math.abs(rowDelta) == 1 && Math.abs(colDelta) == 1 && endRow < startRow && !onlyEat) {
                return true;
            } else if (Math.abs(rowDelta) == Math.abs(colDelta) && isPathClear(startRow, startCol, endRow, endCol)
                    && playerData[startRow][startCol] == RedKing && !onlyEat) {
                return true;
            }

            // Capturing move
            if (Math.abs(rowDelta) == 2 && Math.abs(colDelta) == 2) {
                int capturedRow = (startRow + endRow) / 2;
                int capturedCol = (startCol + endCol) / 2;

                if (board[capturedRow][capturedCol].getComponentCount() > 0
                        && (playerData[capturedRow][capturedCol] == Yellow
                                || playerData[capturedRow][capturedCol] == YellowKing)) {
                    Component capturedComponent = board[capturedRow][capturedCol].getComponent(0);
                    if (capturedComponent instanceof Chips) {
                        return true;
                    }
                }
            }

            int rowDirection = (endRow - startRow) > 0 ? 1 : -1;
            int colDirection = (endCol - startCol) > 0 ? 1 : -1;
            int capturedRow = startRow + rowDirection;
            int capturedCol = startCol + colDirection;
            int chipCount = 0;
            int chipRow = -1;
            int chipCol = -1;

            while (capturedRow != endRow && capturedCol != endCol) {
                if (playerData[capturedRow][capturedCol] != NONE) {
                    chipCount++;
                    chipRow = capturedRow;
                    chipCol = capturedCol;
                }
                capturedRow += rowDirection;
                capturedCol += colDirection;
            }

            if (chipCount == 1) {
                capturedRow = chipRow;
                capturedCol = chipCol;
            } else if (chipCount > 1) {
                return false;
            }

            if (Math.abs(rowDelta) == Math.abs(colDelta) && chipCount == 1
                    && playerData[startRow][startCol] == RedKing) {

                if (board[capturedRow][capturedCol].getComponentCount() > 0
                        && (player1 && (playerData[capturedRow][capturedCol] == Yellow
                                || playerData[capturedRow][capturedCol] == YellowKing))) {
                    Component capturedComponent = board[capturedRow][capturedCol].getComponent(0);
                    if (capturedComponent instanceof Chips) {
                        return true;
                    }
                }

                return false;
            }

            // capture if dama

            return false;
        } else
            return false;
    }

    private boolean isPathClear(int startRow, int startCol, int endRow, int endCol) {

        int rowDirection = (endRow - startRow) > 0 ? 1 : -1;
        int colDirection = (endCol - startCol) > 0 ? 1 : -1;

        int row = startRow + rowDirection;
        int col = startCol + colDirection;

        while (row != endRow && col != endCol && row >= 0 && row <= 7 && col >= 0 && col <= 0) {
            if (playerData[row][col] != NONE) {
                return false;
            }
            row += rowDirection;
            col += colDirection;
        }

        return true;
    }

    private boolean isSurrounded(int endRow, int endCol) {
        inc = 1;
        dec = -1;
        for (int i = 0; i < 4; i++) {
            if (player0 && (playerData[endRow][endCol] == Yellow || playerData[endRow][endCol] == YellowKing)) {
                if (i == 0 && endRow + dec != -1 && endCol + dec != -1) {
                    if (playerData[endRow + dec][endCol + dec] == Red
                            || playerData[endRow + dec][endCol + dec] == RedKing) {
                        surround[i] = true;
                    }
                } else if (i == 1 && endRow + dec != -1 && endCol + inc != 8) {
                    if (playerData[endRow + dec][endCol + inc] == Red
                            || playerData[endRow + dec][endCol + inc] == RedKing) {
                        surround[i] = true;
                    }
                } else if (i == 2 && endRow + inc != 8 && endCol + dec != -1) {
                    if (playerData[endRow + inc][endCol + dec] == Red
                            || playerData[endRow + inc][endCol + dec] == RedKing) {
                        surround[i] = true;
                    }
                } else if (i == 3 && endRow + inc != 8 && endCol + inc != 8) {
                    if (playerData[endRow + inc][endCol + inc] == Red
                            || playerData[endRow + inc][endCol + inc] == RedKing) {
                        surround[i] = true;
                    }
                } else
                    surround[i] = false;
            } else if (!player0 && (playerData[endRow][endCol] == Red || playerData[endRow][endCol] == RedKing)) {
                if (i == 0 && endRow + dec != -1 && endCol + dec != -1) {
                    if (playerData[endRow + dec][endCol + dec] == Yellow
                            || playerData[endRow + dec][endCol + dec] == YellowKing) {
                        surround[i] = true;
                    }
                } else if (i == 1 && endRow + dec != -1 && endCol + inc != 8) {
                    if (playerData[endRow + dec][endCol + inc] == Yellow
                            || playerData[endRow + dec][endCol + inc] == YellowKing) {
                        surround[i] = true;
                    }
                } else if (i == 2 && endRow + inc != 8 && endCol + dec != -1) {
                    if (playerData[endRow + inc][endCol + dec] == Yellow
                            || playerData[endRow + inc][endCol + dec] == YellowKing) {
                        surround[i] = true;
                    }
                } else if (i == 3 && endRow + inc != 8 && endCol + inc != 8) {
                    if (playerData[endRow + inc][endCol + inc] == Yellow
                            || playerData[endRow + inc][endCol + inc] == YellowKing) {
                        surround[i] = true;
                    }
                } else
                    surround[i] = false;
            }
        }
        return canEat(endRow, endCol);
    }

    private void isAvailable(int endRow, int endCol) {
        int[] rowDirections = { -1, -1, 1, 1 };
        int[] colDirections = { -1, 1, -1, 1 };
        int counter = 0;

        for (int direction = 0; direction < 4; direction++) {
            for (int distance = 1; distance < 8; distance++) {
                int newRow = endRow + distance * rowDirections[direction];
                int newCol = endCol + distance * colDirections[direction];
                if (newRow >= 0 && newRow <= 7 && newCol >= 0 && newCol <= 7) {
                    if (playerData[newRow][newCol] != NONE && (((playerData[newRow][newCol] == Red
                            || playerData[newRow][newCol] == RedKing) && (playerData[endRow][endCol] == YellowKing))
                            || ((playerData[newRow][newCol] == Yellow || playerData[newRow][newCol] == YellowKing)
                                    && (playerData[endRow][endCol] == RedKing)))) {
                        if (newRow + rowDirections[direction] >= 0 && newRow + rowDirections[direction] <= 7
                                && newCol + colDirections[direction] >= 0 && newCol + colDirections[direction] <= 7) {
                            if (playerData[newRow + rowDirections[direction]][newCol
                                    + colDirections[direction]] == NONE) {
                                counter++;
                                onlyEat = true;
                            }
                        }
                    }
                }
            }
        }

        if (onlyEat == true && counter == 0)
            onlyEat = false;

    }

    private boolean canEat(int endRow, int endCol) {
        inc++;
        dec--;
        boolean posEat = false;

        for (int i = 0; i < 4; i++) {
            if (player0 && playerData[endRow][endCol] == Yellow || playerData[endRow][endCol] == YellowKing) {
                if (surround[i] && endRow + dec >= 0 && endCol + dec >= 0 && i == 0) {
                    if (playerData[endRow + dec][endCol + dec] == NONE) {
                        posEat = true;
                    }
                } else if (surround[i] && endRow + dec >= 0 && endCol + inc <= 7 && i == 1) {
                    if (playerData[endRow + dec][endCol + inc] == NONE) {
                        posEat = true;
                    }
                } else if (surround[i] && endRow + inc <= 7 && endCol + dec >= 0 && i == 2) {
                    if (playerData[endRow + inc][endCol + dec] == NONE) {
                        posEat = true;
                    }
                } else if (surround[i] && endRow + inc <= 7 && endCol + inc <= 7 && i == 3) {
                    if (playerData[endRow + inc][endCol + inc] == NONE) {
                        posEat = true;
                    }
                } else
                    canStillEat[i] = false;
            } else if (!player0 && playerData[endRow][endCol] == Red || playerData[endRow][endCol] == RedKing) {
                if (surround[i] && endRow + dec >= 0 && endCol + dec >= 0 && i == 0) {
                    if (playerData[endRow + dec][endCol + dec] == NONE) {
                        posEat = true;
                    }
                } else if (surround[i] && endRow + dec >= 0 && endCol + inc <= 7 && i == 1) {
                    if (playerData[endRow + dec][endCol + inc] == NONE) {
                        posEat = true;
                    }
                } else if (surround[i] && endRow + inc <= 7 && endCol + dec >= 0 && i == 2) {
                    if (playerData[endRow + inc][endCol + dec] == NONE) {
                        posEat = true;
                    }
                } else if (surround[i] && endRow + inc <= 7 && endCol + inc <= 7 && i == 3) {
                    if (playerData[endRow + inc][endCol + inc] == NONE) {
                        posEat = true;
                    }
                }
            }
        }

        return posEat;
    }

    // for showing the score on the table once the player eats the oponents chips
    public void calculate(float selectedValue, String operator, float targetValue) {
        float result;

        switch (operator) {
            case "+":
                result = selectedValue + targetValue;
                if (player0) {
                    addRow1(" " + selectedValue + " " + operator + " " + targetValue + " =  " + result);
                    player0Score += result;
                } else {
                    addRow2(" " + selectedValue + " " + operator + " " + targetValue + " =  " + result);
                    player1Score += result;
                }

                System.out.println(result);
                break;
            case "-":
                result = selectedValue - targetValue;
                if (player0) {
                    addRow1(" " + selectedValue + " " + operator + " " + targetValue + " =  " + result);
                    player0Score += result;
                } else {
                    addRow2(" " + selectedValue + " " + operator + " " + targetValue + " =  " + result);
                    player1Score += result;
                }
                System.out.println(result);
                break;
            case "x":
                result = selectedValue * targetValue;
                if (player0) {
                    addRow1(" " + selectedValue + " " + operator + " " + targetValue + " =  " + result);
                    player0Score += result;
                } else {
                    addRow2(" " + selectedValue + " " + operator + " " + targetValue + " =  " + result);
                    player1Score += result;
                }
                System.out.println(result);
                break;
            case "/":
                result = selectedValue / targetValue;
                if (player0) {
                    addRow1(" " + selectedValue + " " + operator + " " + targetValue + " =  " + result);
                    player0Score += result;
                } else {
                    addRow2(" " + selectedValue + " " + operator + " " + targetValue + " =  " + result);
                    player1Score += result;
                }
                System.out.println(result);
                break;
            default:
        }
    }

    public void calculateDamaEat(float selectedValue, String operator, float targetValue) {
        float result;

        switch (operator) {
            case "+":
                result = selectedValue + targetValue;
                result *= 2;
                if (player0) {
                    addRow1("2*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player0Score += result;
                } else {
                    addRow2("2*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player1Score += result;
                }

                System.out.println(result);
                break;
            case "-":
                result = selectedValue - targetValue;
                result *= 2;
                if (player0) {
                    addRow1("2*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player0Score += result;
                } else {
                    addRow2("2*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player1Score += result;
                }
                System.out.println(result);
                break;
            case "x":
                result = selectedValue * targetValue;
                result *= 2;
                if (player0) {
                    addRow1("2*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player0Score += result;
                } else {
                    addRow2("2*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player1Score += result;
                }
                System.out.println(result);
                break;
            case "/":
                result = selectedValue / targetValue;
                result *= 2;
                if (player0) {
                    addRow1("2*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player0Score += result;
                } else {
                    addRow2("2*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player1Score += result;
                }
                System.out.println(result);
                break;
            default:
        }
    }

    public void calculate2DamaEat(float selectedValue, String operator, float targetValue) {
        float result;

        switch (operator) {
            case "+":
                result = selectedValue + targetValue;
                result *= 4;
                if (player0) {
                    addRow1("4*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player0Score += result;
                } else {
                    addRow2("4*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player1Score += result;
                }

                System.out.println(result);
                break;
            case "-":
                result = selectedValue - targetValue;
                result *= 4;
                if (player0) {
                    addRow1("4*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player0Score += result;
                } else {
                    addRow2("4*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player1Score += result;
                }
                System.out.println(result);
                break;
            case "x":
                result = selectedValue * targetValue;
                result *= 4;
                if (player0) {
                    addRow1("4*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player0Score += result;
                } else {
                    addRow2("4*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player1Score += result;
                }
                System.out.println(result);
                break;
            case "/":
                result = selectedValue / targetValue;
                result *= 4;
                if (player0) {
                    addRow1("4*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player0Score += result;
                } else {
                    addRow2("4*( " + selectedValue + " " + operator + " " + targetValue + ") =  " + result);
                    player1Score += result;
                }
                System.out.println(result);
                break;
            default:
        }
    }

    TimerTask task = new TimerTask() {

        int s1 = 400;
        int s2 = 400;

        @Override
        public void run() {

            if (s1 < 0 || s2 < 0 || yellowDama == 0 || redDama == 0 || stop) {
                if (player0Score > player1Score) {
                    player0time.setForeground(Color.GREEN);
                    player0time.setBackground(Color.RED);
                    player0time.setOpaque(true);
                    player0time.setText("       Player 0 wins!! Score: " + String.format("%.2f", player0Score));
                    player0time.setFont(new Font("Serif", Font.BOLD, 20));

                    player1time.setBackground(new Color(0x5a8d03));
                    player1time.setOpaque(true);
                    player1time.setForeground(Color.RED);
                    player1time.setText("       Player 1 Lose!! Score: " + String.format("%.2f", player1Score));
                    player1time.setFont(new Font("Serif", Font.BOLD, 20));

                    if (player0) {
                        player0 = false;
                    } else {
                        player1 = false;
                    }
                    cancel();
                    try {
                        Thread.sleep(60000);
                    } catch (Exception ee) {
                        System.out.println();
                    }
                } else if (player0Score < player1Score) {
                    player1time.setBackground(Color.RED);
                    player1time.setOpaque(true);
                    player1time.setForeground(Color.GREEN);
                    player1time.setText("       Player 1 wins!! Score: " + String.format("%.2f", player1Score));
                    player1time.setFont(new Font("Serif", Font.BOLD, 20));

                    player0time.setForeground(Color.YELLOW);
                    player0time.setBackground(new Color(0x5a8d03));
                    player0time.setOpaque(true);
                    player0time.setText("       Player 0 Lose!! Score: " + String.format("%.2f", player0Score));
                    player0time.setFont(new Font("Serif", Font.BOLD, 20));

                    if (player0) {
                        player0 = false;
                    } else {
                        player1 = false;
                    }

                    cancel();

                    try {
                        Thread.sleep(60000);
                    } catch (Exception ee) {
                        System.out.println();
                    }

                }

                else if (player0Score == player1Score) {
                    player1time.setBackground(new Color(0x5a8d03));
                    player1time.setOpaque(true);
                    player1time.setForeground(Color.RED);
                    player1time.setText("                Draw!! Score: " + player1Score);
                    player1time.setFont(new Font("Serif", Font.BOLD, 20));

                    player0time.setBackground(new Color(0x5a8d03));
                    player0time.setOpaque(true);
                    player0time.setForeground(Color.YELLOW);
                    player0time.setText("                Draw!! Score: " + player0Score);
                    player0time.setFont(new Font("Serif", Font.BOLD, 20));

                    if (player0) {
                        player0 = false;
                    } else {
                        player1 = false;
                    }

                    cancel();

                    try {
                        Thread.sleep(60000);
                    } catch (Exception ee) {
                        System.out.println();
                    }

                }

                // System.exit(0);
            }

            if (s1 >= 0) {
                if (player0) {
                    player0time.setForeground(Color.YELLOW);
                    player0time.setBackground(new Color(0x5a8d03));
                    player0time.setOpaque(true);
                    player0time.setText("               " + Integer.toString(s1));
                    player0time.setFont(new Font("Serif", Font.BOLD, 35));

                } else if (s2 >= 0) {
                    player1time.setBackground(new Color(0x5a8d03));
                    player1time.setOpaque(true);
                    player1time.setForeground(Color.RED);
                    player1time.setText("               " + Integer.toString(s2));
                    player1time.setFont(new Font("Serif", Font.BOLD, 35));
                }

                // s--;

                if (player0) {
                    s1--;
                } else {
                    s2--;
                }

            }

        }

    };

    // method for music
    public void playMoveSound() {
        try {
            File soundFile = new File("C:\\Users\\Geralyn\\Desktop\\DaMath53\\DaMath-Komsai-Version-main\\resources\\move.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void playCaptureSound() {
        try {
            File soundFile = new File("C:\\Users\\Geralyn\\Desktop\\DaMath53\\DaMath-Komsai-Version-main\\resources\\capture.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

}
