
package bozack;

import bozack.Note;
import bozack.NoteSet;
import javax.swing.*;
import java.awt.Container;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

public final class BridgeFrame extends JFrame {

    private static final int POS_X  = 60;
    private static final int POS_Y  = 10;
    private static final int WIDTH  = 1100;
    private static final int HEIGHT = 250;

    private static NoteSet noteSet = null;

    private static int IDX_COMP_KEYPANEL = 0;

    public BridgeFrame() {
        this.setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        this.setVisible(true);
    }

    public void paintKeyPanel (NoteSet ns) {
        Container contentPane = this.getContentPane();
        this.noteSet = ns;
        KeyPanel kp = new KeyPanel(this.noteSet);
        try {
            contentPane.remove(IDX_COMP_KEYPANEL);
        } catch (Exception e) { }
        contentPane.add(kp, IDX_COMP_KEYPANEL);
        contentPane.validate();
    }
}

class KeyPanel extends JPanel {

    private static final int WIDTH  = 950;
    private static final int HEIGHT = 220;

    private static final int KEY_WIDTH  = 24;
    private static final int KEY_HEIGHT = 70;

    private static int NOTE_START = 12;
    private static int NOTE_END   = 84;

    private static final Font FONT_KB_LETTER
        = new Font("Serif", Font.PLAIN, 12);

    private final NoteSet noteSet;

    public KeyPanel(NoteSet ns) {
        this.noteSet = ns;
        this. setBounds(0, 0, WIDTH, HEIGHT);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(FONT_KB_LETTER);

        int x = 5;
        for (int note = NOTE_START; note < NOTE_END; note++) {
            x += KEY_WIDTH;
            int y = 10 + KEY_HEIGHT;

            int chroma = note % 12;
            boolean isHalfTone = false;
            if (
                chroma == 1 || chroma == 3 ||
                chroma == 6 || chroma == 8 ||
                chroma == 10) {
                x -= (KEY_WIDTH / 2);
                y -= KEY_HEIGHT;
                isHalfTone = true;
            }

            Note cursorNote = new Note(note);
            if (this.noteSet.contains(cursorNote)) {
                g.setColor(Color.yellow);
                g.fillRect(x, y, KEY_WIDTH, KEY_HEIGHT);
            }
            else {
                NoteSet tmpNoteSet = (NoteSet)this.noteSet.clone();
                tmpNoteSet.add(cursorNote);
                double des = tmpNoteSet.getDessonance();
                int desIndex = 255 - (int)(255.0d 
                    * (des / (tmpNoteSet.size() * 5)));
                if (desIndex < 0) {
                    desIndex = 0;
                }
                if (desIndex > 255) {
                    desIndex = 255;
                }

                Color heatColor = new Color(255, desIndex, desIndex);
                g.setColor(heatColor);
                g.fillRect(x, y, KEY_WIDTH, KEY_HEIGHT);
            }
            g.setColor(Color.black);
            g.drawRect(x, y, KEY_WIDTH, KEY_HEIGHT);
            g.drawString(String.valueOf(note), (x + 6), (y + 15));
            if (0 == chroma) {
                g.drawString("C", (x + 7), (y + 35));
            }
            if (isHalfTone) {
                x -= (KEY_WIDTH / 2);
            }

        }



    }

}

