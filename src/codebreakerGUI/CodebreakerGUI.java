package codebreakerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CodebreakerGUI {
    private static StringBuilder secretCode = new StringBuilder();
    private static StringBuilder input = new StringBuilder();
    private static int attempts = 0;
    private static final int MAX_ATTEMPTS = 10;

    public static void main(String[] args) {

        generateSecretCode();

        JFrame frame = new JFrame("Codebreaker");
        frame.setSize(400, 300);
        Dimension minimumDimension = new Dimension(300,200);
        frame.setMinimumSize(minimumDimension);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JLabel display = new JLabel("----");
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
        buttonPanel1.add(attemptsLabel);
        addButton(buttonPanel1, display, "0");


        JButton backspaceButton = new JButton("<---");
        backspaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (input.length() > 0) {
                    input.deleteCharAt(input.length() - 1);
                    display.setText(input.toString());
                }
            }
        });
        buttonPanel1.add(backspaceButton);

        JButton confirm = new JButton("Confirm");
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
                        attemptsLabel.setBackground(Color.red);
                    }

                    if (feedback.equals("XXXX")) {
                        display.setOpaque(true);
                        display.setBackground(Color.green);
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
                        display.setBackground(Color.red);
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

        frame.add(displayPanel, BorderLayout.NORTH);
        frame.add(buttonPanel1, BorderLayout.CENTER);
        frame.add(confirm, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private static void addButton(JPanel panel, JLabel display, String text) {
        JButton button = new JButton(text);
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
        display.setBackground(Color.white);
        attemptsLabel.setOpaque(true);
        attemptsLabel.setBackground(Color.white);
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
