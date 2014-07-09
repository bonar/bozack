
package bozack.ui;

import java.text.DecimalFormat;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;

import bozack.Note;
import bozack.NoteSet;
import bozack.NoteHashMap;
import bozack.DissonanceMap;

public final class KeyPanel extends JPanel {

    private static final int WIDTH  = 1200;
    private static final int HEIGHT = 600;

    private static final int KEY_WIDTH  = 42;
    private static final int KEY_HEIGHT = 120;

    private static int NOTE_START = 12;
    private static int NOTE_END   = 108;

    private static final Font FONT_KB_LETTER
        = new Font("Serif", Font.PLAIN, 12);

    private final NoteSet noteSet;
    private final NoteSet assistedNote;
    private final NoteHashMap pickupRelation;
    private final NoteSet sequencerNote;

    private DissonanceMap dissonance;

    public KeyPanel(
          NoteSet onNote
        , NoteSet assistedNote
        , NoteHashMap pickupRelation
        , NoteSet sequencerNote
    ) {
        this.noteSet = onNote;
        this.assistedNote   = assistedNote;
        this.pickupRelation = pickupRelation;
        this.sequencerNote  = sequencerNote;
        this. setBounds(0, 50, WIDTH, HEIGHT);
    }

    public void setDissonanceMap(DissonanceMap dis) {
        this.dissonance = dis;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(FONT_KB_LETTER);

        int x = 10 + (-1 * KEY_WIDTH);
        int y_row_height = 40;
        int x_col_width  = 0;
        for (int note = NOTE_START; note < NOTE_END; note++) {
            if (note >= 60) {
                y_row_height = 290;
                x_col_width  = 1175;
            }
            x += KEY_WIDTH;
            int y = 10 + KEY_HEIGHT + y_row_height;

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

            NoteSet tmpNoteSet = (NoteSet)this.noteSet.clone();
            tmpNoteSet.add(cursorNote);
            double des = tmpNoteSet.getDessonance(this.dissonance);
            int desIndex = 255 - (int)(255.0d
                * (des / (tmpNoteSet.size() * 5)));
            if (desIndex < 0) {
                desIndex = 0;
            }
            if (desIndex > 255) {
                desIndex = 255;
            }

            boolean isOnNote = false;
            if (this.noteSet.contains(cursorNote)) {
                g.setColor(Color.yellow);
                g.fillRect((x - x_col_width), y, KEY_WIDTH, KEY_HEIGHT);
                isOnNote = true;
            }
            else if (this.sequencerNote.contains(cursorNote)) {
                g.setColor(Color.magenta.brighter());
                g.fillRect((x - x_col_width), y, KEY_WIDTH, KEY_HEIGHT);
            }
            else {
                Color heatColor = new Color(255, desIndex, desIndex);
                g.setColor(heatColor);
                g.fillRect((x - x_col_width), y, KEY_WIDTH, KEY_HEIGHT);
            }

            if (des > 0.01d) {
                if (isOnNote) {
                    g.setColor(Color.black);
                }
                else if (desIndex < 80) {
                    g.setColor(Color.yellow);
                }
                else if (desIndex < 130) {
                    g.setColor(Color.orange);
                }
                else {
                    g.setColor(Color.red);
                }
                g.drawString(new DecimalFormat("00.000").format(des)
                    , ((x - x_col_width)+ 6), (y + 55));
                double average = des / this.noteSet.size();
                g.drawString(
                    new DecimalFormat("00.000").format(average)
                    , ((x - x_col_width)+ 6), (y + 70));
            }

            // assist marker
            if (this.assistedNote.contains(cursorNote)) {
                g.setColor(Color.green.darker());
                g.fillRect((x - x_col_width + 3), (y + 80)
                    , (KEY_WIDTH - 6), (KEY_HEIGHT - 82));
                g.setColor(Color.black);
                g.drawRect((x - x_col_width + 3), (y + 80)
                    , (KEY_WIDTH - 6), (KEY_HEIGHT - 82));

                // where the cursorNote was assisted from
                Note fromNote = pickupRelation.scanValue(cursorNote);
                if (null != fromNote) {
                    g.setColor(Color.white);
                    g.drawString(chromaToString(fromNote.getChroma())
                        + " " + (note / 12)
                        , ((x - x_col_width)+ 10), (y + 105));
                }
            }

            g.setColor(Color.black);
            g.drawRect((x - x_col_width), y, KEY_WIDTH, KEY_HEIGHT);
            g.drawString(String.valueOf(note)
                , ((x - x_col_width)+ 6), (y + 15));
            g.drawString(chromaToString(chroma) + " "
                + (note / 12), ((x - x_col_width)+ 7), (y + 35));
            if (isHalfTone) {
                x -= (KEY_WIDTH / 2);
            }

        }
    }

    private static String chromaToString(int chroma) {
        switch (chroma) {
            case 0: return "C";
            case 1: return "C#";
            case 2: return "D";
            case 3: return "D#";
            case 4: return "E";
            case 5: return "F";
            case 6: return "F#";
            case 7: return "G";
            case 8: return "G#";
            case 9: return "A";
            case 10: return "A#";
            case 11: return "B";
            default: return "-";
        }

    }
}
