
package bozack.ui;

import java.net.URL;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class AboutDialog extends JDialog {
    private static final String TITLE = "about bozack";
    private static final String IMAGE_PATH = "splash.png";

    public AboutDialog(JFrame f) {
        super(f, TITLE, true);
        this.setBounds(300, 200, 400, 200);
        this.setResizable(false);

        URL imageUrl = getClass().getClassLoader()
            .getResource(IMAGE_PATH);
        ImageIcon icon = new ImageIcon(imageUrl);
        JLabel imageLabel = new JLabel(icon);
        this.getContentPane().add(imageLabel);

        this.pack();
        this.setVisible(true);
    }

}



