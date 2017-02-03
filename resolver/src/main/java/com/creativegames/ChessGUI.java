package com.creativegames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.IllegalComponentStateException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Created by stephanotelolahy on 03/02/17.
 */
public class ChessGUI {

    private static final int GUI_BORDER = 5;
    private static final int CHESS_BORDER = 8;
    private static final Color BACKGROUND_COLOR = new Color(15, 128, 18);

    private static final String CARD_COLORS[] = {"♥", "♠", "♦", "♣"};
    private static final String CARD_NAMES[] = {"7", "8", "9", "10", "J", "Q", "K", "A"};
    private static final int CARD_WIDTH = 44;
    private static final int CARD_HEIGHT = 64;

    private static final String A_RANKING[] = {"J", "9", "A", "10", "K", "Q", "8", "7"};
    private static final String NA_RANKING[] = {"A", "10", "K", "Q", "J", "9", "8", "7"};

    private JPanel chessBoard;
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private final JButton[][] chessBoardButtons = new JButton[4][8];
    private final Image[][] chessPieceImages = new Image[4][8];
    private final JLabel[] chessBoardLabels = new JLabel[4];
    private final JLabel message = new JLabel("Belote is ready to play!");

    ChessGUI() {
        initializeGui();
    }

    public final void initializeGui() {
        // create the images for the chess pieces
        createImages();

        // set up the main GUI
        gui.setBorder(new EmptyBorder(GUI_BORDER, GUI_BORDER, GUI_BORDER, GUI_BORDER));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        Action newGameAction = new AbstractAction("New") {

            @Override
            public void actionPerformed(ActionEvent e) {

                String input = (String) JOptionPane.showInputDialog(null, "Choose color now...",
                        "The Choice are ", JOptionPane.QUESTION_MESSAGE, null, // Use
                        // default
                        // icon
                        CARD_COLORS, // Array of choices
                        CARD_COLORS[1]); // Initial choice

                //If a string was returned, say so.
                if ((input != null) && (input.length() > 0)) {
                    setupNewGame(input);
                    return;
                }
            }
        };
        tools.add(newGameAction);
        tools.addSeparator();
        tools.add(new JButton("Restore")); // TODO - add functionality!
        tools.addSeparator();
        tools.add(message);

        gui.add(new JLabel("?"), BorderLayout.LINE_START);

        chessBoard = new JPanel(new GridLayout(0, 9));
        chessBoard.setBorder(new CompoundBorder(
                new EmptyBorder(CHESS_BORDER, CHESS_BORDER, CHESS_BORDER, CHESS_BORDER),
                new LineBorder(Color.BLACK)
        ));

        chessBoard.setBackground(BACKGROUND_COLOR);
        JPanel boardConstrain = new JPanel(new GridBagLayout());
        boardConstrain.setBackground(BACKGROUND_COLOR);
        boardConstrain.add(chessBoard);
        gui.add(boardConstrain);

        // create the chess board squares
        Insets buttonMargin = new Insets(0, 0, 0, 0);
        for (int ii = 0; ii < chessBoardButtons.length; ii++) {
            for (int jj = 0; jj < chessBoardButtons[ii].length; jj++) {
                final JButton b = new JButton();
                b.setMargin(buttonMargin);
                // our chess pieces are 64x64 px in size, so we'll
                // 'fill this in' using a transparent icon..
                ImageIcon icon = new ImageIcon(new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_ARGB));
                b.setIcon(icon);
                b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        b.setEnabled(false);
                    }
                });
                chessBoardButtons[ii][jj] = b;
            }
        }

        // create the chess board titles
        for (int ii = 0; ii < chessBoardLabels.length; ii++) {
            chessBoardLabels[ii] = new JLabel("", SwingConstants.CENTER);
        }

        /*
         * fill the chess board
         */
        for (int ii = 0; ii < 4; ii++) {
            for (int jj = 0; jj < 8; jj++) {
                switch (jj) {
                    case 0:
                        chessBoard.add(chessBoardLabels[ii]);
                    default:
                        chessBoard.add(chessBoardButtons[ii][jj]);
                }
            }
        }
    }

    public final JComponent getGui() {
        return gui;
    }

    private final void createImages() {
        try {
            // Image from Storage in Firebase App friendlychat-cec4b
            URL url = new URL("https://firebasestorage.googleapis.com/v0/b/friendlychat-cec4b.appspot.com/o/cardTheme_alpha.png?alt=media&token=2fc255ed-a7ca-4a7a-b38f-e45b82d3e8af");
            BufferedImage bi = ImageIO.read(url);
            for (int ii = 0; ii < 4; ii++) {
                for (int jj = 0; jj < 8; jj++) {
                    chessPieceImages[ii][jj] = bi.getSubimage(jj * 44, ii * 64, 44, 64);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Initializes the icons of the initial chess board piece places
     */
    private final void setupNewGame(String color) {
        message.setText("Game is " + color + ". Play your card!");
        // set up pieces
        for (int ii = 0; ii < 4; ii++) {
            chessBoardLabels[ii].setText(CARD_COLORS[ii] + " x8");
            for (int jj = 0; jj < 8; jj++) {
                if (CARD_COLORS[ii].equals(color)) {
                    chessBoardButtons[ii][jj].setIcon(new ImageIcon((getImageForCard(CARD_COLORS[ii], A_RANKING[jj]))));
                } else {
                    chessBoardButtons[ii][jj].setIcon(new ImageIcon((getImageForCard(CARD_COLORS[ii], NA_RANKING[jj]))));
                }
                chessBoardButtons[ii][jj].setEnabled(true);
            }
        }
    }

    private Image getImageForCard(String color, String name) {
        for (int ii = 0; ii < 4; ii++) {
            for (int jj = 0; jj < 8; jj++) {
                if (CARD_COLORS[ii].equals(color) && CARD_NAMES[jj].equals(name)) {
                    return chessPieceImages[ii][jj];
                }
            }
        }
        throw new IllegalComponentStateException("Card not found color:" + color + "name:" + name);
    }

    public static void main(String[] args) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                ChessGUI cg = new ChessGUI();

                JFrame f = new JFrame("Belote Resolver");
                f.add(cg.getGui());
                // Ensures JVM closes after frame(s) closed and
                // all non-daemon threads are finished
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                // See http://stackoverflow.com/a/7143398/418556 for demo.
                f.setLocationByPlatform(true);

                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                f.pack();
                // ensures the minimum size is enforced.
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
            }
        };
        // Swing GUIs should be created and updated on the EDT
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency
        SwingUtilities.invokeLater(r);
    }
}