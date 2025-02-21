package codebreakerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javazoom.jl.player.Player;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class CodebreakerGUI {
    private static StringBuilder secretCode = new StringBuilder();
    private static StringBuilder input = new StringBuilder();
    private static int attempts = 0;
    private static int MAX_ATTEMPTS = 10 ;

    public static void main(String[] args) {

        generateSecretCode();

        Outfit.useOutfit();

        JFrame frame = new JFrame("Codebreaker");
        frame.setSize(400, 300);
        Dimension minimumDimension = new Dimension(400,300);
        frame.setMinimumSize(minimumDimension);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu settingsMenu = new JMenu("Settings");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem newItem = new JMenuItem("New game");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem easyItem = new JMenuItem("Easy");
        JMenuItem mediumItem = new JMenuItem("Medium");
        JMenuItem hardItem = new JMenuItem("Hard");
        JMenuItem helpItem = new JMenuItem("Hints");

        fileMenu.add(newItem);
        fileMenu.add(exitItem);
        settingsMenu.add(easyItem);
        settingsMenu.add(mediumItem);
        settingsMenu.add(hardItem);
        helpMenu.add(helpItem);

        menuBar.add(fileMenu);
        menuBar.add(settingsMenu);
        menuBar.add(helpMenu);

        frame.setJMenuBar(menuBar);

        JLabel display = new JLabel("----");
        display.setFont(new Font("Verdana", Font.BOLD, 14));
        display.setBorder(BorderFactory.createLineBorder(Color.white, 1, true));
        display.setOpaque(true);
        display.setBackground(Color.white);
        display.setForeground(Color.darkGray);
        display.setHorizontalAlignment(SwingConstants.CENTER);
        display.setVerticalAlignment(SwingConstants.CENTER);
        Font currentFont = display.getFont();
        Font newFont = currentFont.deriveFont(24f);
        display.setFont(newFont);

        JPanel displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(display, BorderLayout.CENTER);

        JPanel buttonPanel1 = new JPanel();
        buttonPanel1.setLayout(new GridLayout(4, 3));

        for (int i = 1; i <= 9; i++) {
            addButton(buttonPanel1, display, String.valueOf(i));
        }

        JLabel attemptsLabel = new JLabel("Attempts: " + (MAX_ATTEMPTS - attempts));
        attemptsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        attemptsLabel.setVerticalAlignment(SwingConstants.CENTER);
        attemptsLabel.setOpaque(true);
        attemptsLabel.setBackground(Color.white);
        attemptsLabel.setBorder(BorderFactory.createLineBorder(Color.white, 1, true));
        attemptsLabel.setFont(new Font("Verdana", Font.BOLD,14));
        attemptsLabel.setForeground(Color.darkGray);
        buttonPanel1.add(attemptsLabel);
        addButton(buttonPanel1, display, "0");


        JButton backspaceButton = new JButton("<---");
        backspaceButton.setFont(new Font("Verdana", Font.BOLD, 14));
        backspaceButton.setForeground(Color.darkGray);
        //backspaceButton.setBackground(Color.yellow);
        backspaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (input.length() > 0) {
                    input.deleteCharAt(input.length() - 1);
                    display.setText(input.toString());
                    if (input.length() == 0) display.setText("----");
                }
            }
        });
        buttonPanel1.add(backspaceButton);

        JButton confirm = new JButton("Confirm");
        confirm.setFont(new Font("Verdana", Font.BOLD, 14));
        confirm.setForeground(Color.darkGray);
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (input.length() == 4) {
                    attempts++;
                    String feedback = Code_compare.getFeedback(secretCode.toString(), input.toString());
                    display.setText(input + " " + feedback);
                    attemptsLabel.setText("Attempts: " + (MAX_ATTEMPTS - attempts));
                    if (MAX_ATTEMPTS- attempts <= 3){
                        attemptsLabel.setOpaque(true);
                        attemptsLabel.setForeground(Color.red);
                    }

                    if (feedback.equals("XXXX")) {
                        display.setOpaque(true);
                        display.setForeground(new Color(63,193,0));
                        try {
                            InputStream inputStream = CodebreakerGUI.class.getResourceAsStream("/door.mp3");
                            if (inputStream == null) {
                                throw new FileNotFoundException("MP3 file not found in resources.");
                            }
                            Player player = new Player(inputStream);
                            player.play();
                            JOptionPane.showMessageDialog(frame, "Door opened...", "Information", JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception w) {
                            w.printStackTrace();
                        }
                        resetGame(attemptsLabel, display);
                    } else if (attempts >= MAX_ATTEMPTS) {
                        display.setOpaque(true);
                        display.setForeground(Color.red);
                        try {
                            InputStream inputStream = CodebreakerGUI.class.getResourceAsStream("/alert.mp3");
                            if (inputStream == null) {
                                throw new FileNotFoundException("MP3 file not found in resources.");
                            }
                            Player player = new Player(inputStream);
                            player.play();
                            JOptionPane.showMessageDialog(frame, "Wrong code! Alarm triggered.", "Alert", JOptionPane.WARNING_MESSAGE);
                        } catch (Exception w) {
                            w.printStackTrace();
                        }
                        resetGame(attemptsLabel, display);
                    }
                    input.setLength(0);
                } else {
                    JOptionPane.showMessageDialog(frame, "Please enter exactly 4 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        newItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(frame,"Are you sure you want to abort the current game?", "New game",0);
                if (choice == JOptionPane.YES_OPTION){
                    resetGame(attemptsLabel,display);
                }

            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(frame,"Are you sure you want to exit the game?","Exit game",0);
                if (choice == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }
        });

        easyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(frame,"Are you sure you want to abort the current game?", "New game",0);
                if (choice == JOptionPane.YES_OPTION){
                    resetGame(attemptsLabel,display);
                    MAX_ATTEMPTS = 10;
                    attemptsLabel.setText("Attempts: " + (MAX_ATTEMPTS - attempts));
                }

            }
        });

        mediumItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(frame,"Are you sure you want to abort the current game?", "Abort game",0);
                if (choice == JOptionPane.YES_OPTION){
                    resetGame(attemptsLabel,display);
                    MAX_ATTEMPTS = 8;
                    attemptsLabel.setText("Attempts: " + (MAX_ATTEMPTS - attempts));
                }
            }
        });

        hardItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(frame,"Are you sure you want to abort the current game?", "New game",0);
                if (choice == JOptionPane.YES_OPTION){
                    resetGame(attemptsLabel,display);
                    MAX_ATTEMPTS = 6;
                    attemptsLabel.setText("Attempts: " + (MAX_ATTEMPTS - attempts));
                }
            }
        });

        helpItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Help.showHelp();
            }
        });

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                int choice = JOptionPane.showConfirmDialog(frame,"Are you sure you want to exit the game?","Exit game",0);
                if (choice == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        frame.getRootPane().setDefaultButton(confirm);

        frame.add(displayPanel, BorderLayout.NORTH);
        frame.add(buttonPanel1, BorderLayout.CENTER);
        frame.add(confirm, BorderLayout.SOUTH);
        frame.setVisible(true);

    }

    private static void addButton(JPanel panel, JLabel display, String text) {
        JButton button = new JButton(text);
        button.setForeground(Color.darkGray);
        button.setFont(new Font("Verdana", Font.BOLD,14));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (input.length() < 4) {
                    input.append(text);
                    display.setText(input.toString());
                }
            }
        });
        panel.add(button);
    }

    private static void generateSecretCode() {
        Random random = new Random();
        secretCode.setLength(0);
        for (int i = 0; i < 4; i++) {
            secretCode.append(random.nextInt(10));
        }
    }

    private static void resetGame(JLabel attemptsLabel, JLabel display) {
        generateSecretCode();
        input.setLength(0);
        attempts = 0;
        display.setOpaque(true);
        display.setForeground(Color.darkGray);
        display.setText("----");
        attemptsLabel.setOpaque(true);
        attemptsLabel.setForeground(Color.darkGray);
        attemptsLabel.setText("Attempts: " + (MAX_ATTEMPTS - attempts));
    }
}

class Code_compare {
    public static String getFeedback(String secretCode, String userAttempt) {
        StringBuilder feedback = new StringBuilder();
        boolean[] secretUsed = new boolean[4];
        boolean[] attemptUsed = new boolean[4];

        for (int i = 0; i < 4; i++) {
            if (userAttempt.charAt(i) == secretCode.charAt(i)) {
                feedback.append('X');
                secretUsed[i] = true;
                attemptUsed[i] = true;
            }
        }

        for (int i = 0; i < 4; i++) {
            if (!attemptUsed[i]) {
                for (int j = 0; j < 4; j++) {
                    if (!secretUsed[j] && userAttempt.charAt(i) == secretCode.charAt(j)) {
                        feedback.append('o');
                        secretUsed[j] = true;
                        break;
                    }
                }
            }
        }

        while (feedback.length() < 4) {
            feedback.append('-');
        }

        return feedback.toString();
    }
}
