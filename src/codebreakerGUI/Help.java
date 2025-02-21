package codebreakerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Help {



    public static void showHelp(){

        Outfit.useOutfit();

        ImageIcon image = new ImageIcon(Help.class.getResource("/hints.jpg"));


        JFrame hintsFrame = new JFrame("Help");
        hintsFrame.setSize(540, 400);
        Dimension minimumDimension = new Dimension(500,400);
        hintsFrame.setMinimumSize(minimumDimension);
        hintsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        hintsFrame.setLocationRelativeTo(null);
        hintsFrame.setLayout(new BorderLayout());

        JPanel imagePanel = new JPanel();
        JLabel imageLabel = new JLabel(image);
        imageLabel.setOpaque(true);
        imageLabel.setBackground(Color.darkGray);
        JTextArea hintsArea = new JTextArea();
        hintsArea.setFont(new Font("Verdana",Font.BOLD,13));
        hintsArea.setOpaque(true);
        hintsArea.setBackground(Color.lightGray);
        hintsArea.setForeground(Color.darkGray);
        hintsArea.setEditable(false);
        hintsArea.setLineWrap(true);
        hintsArea.append("    Try to guess the 4-digit code to open the safe door.\n");
        hintsArea.append("    \"o\" - one of the digits is in the code but in a different position\n");
        hintsArea.append("    \"X\" - one of the digits is already in the correct position\n");
        hintsArea.append("    \"-\" - missed digits\n\n");
        hintsArea.append("    Note! In the above example, “X” does not mean that the 2 is in the\n    correct position.\n");
        hintsArea.append("    It could be any of the four entered numbers!\n");

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Verdana",Font.BOLD,14));
        closeButton.setForeground(Color.darkGray);
        closeButton.requestFocus();

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hintsFrame.setVisible(false);
            }
        });

        imagePanel.add(imageLabel);

        hintsFrame.add(imagePanel, BorderLayout.NORTH);
        hintsFrame.add(hintsArea, BorderLayout.CENTER);
        hintsFrame.add(closeButton, BorderLayout.SOUTH);

        hintsFrame.getRootPane().setDefaultButton(closeButton);

        hintsFrame.setVisible(true);








    }
}
