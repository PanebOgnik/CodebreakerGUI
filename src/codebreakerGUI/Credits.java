package codebreakerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Credits {

    public static void showCredits() {

        Outfit.useOutfit();

        ImageIcon creditsImage = new ImageIcon(Credits.class.getResource("/credits.jpg"));

        JFrame creditsFrame = new JFrame("Credits");
        creditsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        creditsFrame.setSize(540, 700);
        creditsFrame.setLocationRelativeTo(null);
        creditsFrame.setLayout(new BorderLayout(5, 5));

        JPanel creditsPanel = new JPanel();
        JLabel creditsLabel = new JLabel(creditsImage);
        creditsLabel.setOpaque(true);
        creditsLabel.setBackground(Color.darkGray);

        JTextArea creditsArea = new JTextArea();
        creditsArea.setFont(new Font("Verdana", Font.BOLD, 13));
        creditsArea.setOpaque(true);
        creditsArea.setBackground(Color.lightGray);
        creditsArea.setForeground(Color.darkGray);
        creditsArea.setEditable(false);
        creditsArea.setLineWrap(true);
        creditsArea.setWrapStyleWord(true);
        creditsArea.setText(
                "Developer:\n" +
                        "PanebOgnik (Adam S.)\n\n" +
                        "Idea & Concept:\n" +
                        "PanebOgnik, inspired by the board game Mastermind\n\n" +
                        "Graphics and Sound Effects:\n" +
                        "www.pixabay.com, www.freepik.com\n\n" +
                        "Special Thanks to:\n" +
                        "My coffee machine \n\n" +
                        "Test Player:\n" +
                        "My son - the brave hero\n "
        );

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Verdana", Font.BOLD, 14));
        closeButton.setForeground(Color.darkGray);
        closeButton.requestFocus();

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                creditsFrame.setVisible(false);
            }
        });

        creditsPanel.add(creditsLabel);

        creditsFrame.add(creditsPanel, BorderLayout.NORTH);
        creditsFrame.add(new JScrollPane(creditsArea), BorderLayout.CENTER);
        creditsFrame.add(closeButton, BorderLayout.SOUTH);

        creditsFrame.getRootPane().setDefaultButton(closeButton);

        creditsFrame.setVisible(true);
    }
}
