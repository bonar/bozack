
package bozack.midi;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.InvalidMidiDataException;
import bozack.midi.receiver.CustomReceiver;
import bozack.ui.BridgeFrame;
import bozack.Note;
import bozack.NoteSet;

public final class PianoKeyEmulator
    implements KeyListener {

    private static final int VELOCITY = 90;
    private final CustomReceiver receiver;
    private int octav = 4;
    private static final int MIN_OCTAV = 1;
    private static final int MAX_OCTAV = 8;
    private NoteSet onNote;
    private BridgeFrame frame;

    public PianoKeyEmulator(BridgeFrame frame, CustomReceiver recv) {
        this.frame = frame;
        this.receiver = recv;
        this.onNote = new NoteSet();
    }

    public void keyTyped(KeyEvent e) { }

    public void incrOctav() {
        if (this.octav < MAX_OCTAV) { this.octav++; }
    }

    public void dectOctav() {
        if (this.octav > MIN_OCTAV) { this.octav--; }
    }

    public void keyPressed(KeyEvent e) {
        Note inputNote = getNoteByKeyCode(e.getKeyCode(), this.octav);
        if (this.onNote.contains(inputNote)) {
            return;
        }
        if (null != inputNote) {
            this.onNote.add(inputNote);
            this.sendToReciever(inputNote, ShortMessage.NOTE_ON);
        }
    }

    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT:  this.dectOctav(); break;
            case KeyEvent.VK_RIGHT: this.incrOctav(); break;
        }

        {
            int chordIndex = 0;
            switch (e.getKeyCode()) {
                case KeyEvent.VK_F1: chordIndex = 1; break;
                case KeyEvent.VK_F2: chordIndex = 2; break;
                case KeyEvent.VK_F3: chordIndex = 3; break;
                case KeyEvent.VK_F4: chordIndex = 4; break;
                case KeyEvent.VK_F5: chordIndex = 5; break;
                case KeyEvent.VK_F6: chordIndex = 6; break;
                case KeyEvent.VK_F7: chordIndex = 7; break;
                case KeyEvent.VK_F8: chordIndex = 8; break;
                case KeyEvent.VK_F9: chordIndex = 9; break;
                case KeyEvent.VK_1: this.frame.chordVariate(); break;
            }
            if (chordIndex > 0) {
                this.frame.chordNext(chordIndex - 1);
                return;
            }
        }

        Note inputNote = getNoteByKeyCode(e.getKeyCode(), this.octav);
        if (null != inputNote) {
            this.onNote.remove(inputNote);
            this.sendToReciever(inputNote, ShortMessage.NOTE_OFF);
        }
    }

    private void sendToReciever(Note note, int type) {
        ShortMessage sm = new ShortMessage();
        try {
            sm.setMessage(type, note.getNote(), VELOCITY);
            this.receiver.send(sm, 0l);
        } catch (InvalidMidiDataException ie) {
            System.out.println(ie.getMessage());
            return;
        }
    }

    public static Note getNoteByKeyCode(int key, int octav) {
        int chroma;
        int octav_tmp = octav;

        switch (key) {
            case KeyEvent.VK_Q: chroma = 0; break;
            case KeyEvent.VK_2: chroma = 1; break;
            case KeyEvent.VK_W: chroma = 2; break;
            case KeyEvent.VK_3: chroma = 3; break;
            case KeyEvent.VK_E: chroma = 4; break;
            case KeyEvent.VK_R: chroma = 5; break;
            case KeyEvent.VK_5: chroma = 6; break;
            case KeyEvent.VK_T: chroma = 7; break;
            case KeyEvent.VK_6: chroma = 8; break;
            case KeyEvent.VK_Y: chroma = 9; break;
            case KeyEvent.VK_7: chroma = 10; break;
            case KeyEvent.VK_U: chroma = 11; break;
            case KeyEvent.VK_I: octav_tmp++; chroma = 0; break;
            case KeyEvent.VK_9: octav_tmp++; chroma = 1; break;
            case KeyEvent.VK_O: octav_tmp++; chroma = 2; break;
            case KeyEvent.VK_0: octav_tmp++; chroma = 3; break;
            case KeyEvent.VK_P: octav_tmp++; chroma = 4; break;

            case KeyEvent.VK_Z: octav_tmp++; chroma = 0; break;
            case KeyEvent.VK_S: octav_tmp++; chroma = 1; break;
            case KeyEvent.VK_X: octav_tmp++; chroma = 2; break;
            case KeyEvent.VK_D: octav_tmp++; chroma = 3; break;
            case KeyEvent.VK_C: octav_tmp++; chroma = 4; break;
            case KeyEvent.VK_V: octav_tmp++; chroma = 5; break;
            case KeyEvent.VK_G: octav_tmp++; chroma = 6; break;
            case KeyEvent.VK_B: octav_tmp++; chroma = 7; break;
            case KeyEvent.VK_H: octav_tmp++; chroma = 8; break;
            case KeyEvent.VK_N: octav_tmp++; chroma = 9; break;
            case KeyEvent.VK_J: octav_tmp++; chroma = 10; break;
            case KeyEvent.VK_M: octav_tmp++; chroma = 11; break;
            case KeyEvent.VK_COMMA: octav_tmp += 2; chroma = 0; break;

            default: return null;
        }

        return new Note((octav_tmp * 12) + chroma);
    }
}

