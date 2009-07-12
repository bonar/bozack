package bozack.ui;

import java.net.URL;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.border.TitledBorder;

public class LayoutDialog extends JDialog {
    private static final String TITLE = "Keyboard layout";
    private static final String IMAGE_PATH = "piano_layout.png";

    public LayoutDialog(JFrame f) {
        super(f, TITLE, true);
        this.setBounds(300, 200, 400, 400);
        this.setResizable(false);

        JPanel imagePanel = new JPanel();
        URL imageUrl = getClass().getClassLoader()
            .getResource(IMAGE_PATH);
        ImageIcon icon = new ImageIcon(imageUrl);
        JLabel imageLabel = new JLabel(icon);
        imagePanel.add(imageLabel);

        TitledBorder border = new TitledBorder(
            "Keyboard-Piano Emulation layout");
        imagePanel.setBorder(border);
        this.getContentPane().add(imagePanel);

        this.pack();
    }
}

