
package bozack;

import java.text.DecimalFormat;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Container;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.util.ArrayList;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

import bozack.Note;
import bozack.NoteSet;
import bozack.midi.Bridge;
import bozack.midi.receiver.CustomReceiver;
import bozack.midi.receiver.DumpRelay;
import bozack.midi.receiver.Stabilizer;
import bozack.midi.event.FramePainter;

public final class BridgeFrame extends JFrame {

    private static final int POS_X  = 60;
    private static final int POS_Y  = 10;
    private static final int WIDTH  = 1200;
    private static final int HEIGHT = 600;

    private static NoteSet noteSet;

    private static int IDX_COMP_KEYPANEL = 0;

    public BridgeFrame() {
        this.setBounds(POS_X, POS_Y, WIDTH, HEIGHT);

        ArrayList devices = Bridge.getDevices();
        bozack.midi.Bridge bridge = new bozack.midi.Bridge();
        CustomReceiver recv = new Stabilizer();
        recv.addNoteEventListener(new FramePainter(this));
        try {
            bridge.connect(
                (MidiDevice)(devices.get(0)),
                MidiSystem.getSynthesizer(),
                recv
            );
        } catch (MidiUnavailableException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

        this.noteSet = new NoteSet();
        this.paintKeyPanel(this.noteSet);

        // register Software Keyboard emulation
        this.addKeyListener(new PianoKeyEmulator(recv));

        this.setVisible(true);
    }
    
    public void paintKeyPanel (NoteSet onNote) {
        this.paintKeyPanel(onNote, new NoteSet());
    }

    public void paintKeyPanel (NoteSet onNote, NoteSet assistedNote) {
        Container contentPane = this.getContentPane();
        this.noteSet = onNote;
        KeyPanel kp = new KeyPanel(this.noteSet, assistedNote);
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

    private static final int KEY_WIDTH  = 42;
    private static final int KEY_HEIGHT = 120;

    private static int NOTE_START = 12;
    private static int NOTE_END   = 108;

    private static final Font FONT_KB_LETTER
        = new Font("Serif", Font.PLAIN, 12);

    private final NoteSet noteSet;
    private final NoteSet assistedNote;

    public KeyPanel(NoteSet onNote, NoteSet assistedNote) {
        this.noteSet = onNote;
        this.assistedNote = assistedNote;
        this. setBounds(0, 0, WIDTH, HEIGHT);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(FONT_KB_LETTER);

        int x = 10 + (-1 * KEY_WIDTH);
        int y_row_height = 0;
        int x_col_width  = 0;
        for (int note = NOTE_START; note < NOTE_END; note++) {
            if (note >= 60) {
                y_row_height = 250;
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
            double des = tmpNoteSet.getDessonance();
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
            else if (this.assistedNote.contains(cursorNote)) {
                g.setColor(Color.magenta);
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

