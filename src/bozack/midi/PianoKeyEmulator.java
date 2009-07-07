
package bozack;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.InvalidMidiDataException;
import bozack.midi.receiver.CustomReceiver;
import bozack.Note;

public final class PianoKeyEmulator
    implements KeyListener {

    private static final int VELOCITY = 100;
    private final CustomReceiver receiver;
    private int octav = 4;

    public PianoKeyEmulator(CustomReceiver recv) {
        this.receiver = recv;
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        System.out.println("pressed: " + e);

        Note inputNote = getNoteByKeyCode(e.getKeyCode(), this.octav);
        if (null != inputNote) {
            ShortMessage sm = new ShortMessage();
            try {
            sm.setMessage(ShortMessage.NOTE_ON
                , inputNote.getNote(), VELOCITY);
            } catch (InvalidMidiDataException ie) {
                System.out.println(ie.getMessage());
                return;
            }
            System.out.println(inputNote);
        }

    }

    public void keyReleased(KeyEvent e) {

    }

    public static Note getNoteByKeyCode(int key, int octav) {
        int chroma;
        int octav_tmp = octav;

        switch (key) {
            case KeyEvent.VK_Q: octav_tmp++; chroma = 0; break;
            case KeyEvent.VK_2: octav_tmp++; chroma = 1; break;
            case KeyEvent.VK_W: octav_tmp++; chroma = 2; break;
            case KeyEvent.VK_3: octav_tmp++; chroma = 3; break;
            case KeyEvent.VK_E: octav_tmp++; chroma = 4; break;
            case KeyEvent.VK_R: octav_tmp++; chroma = 5; break;
            case KeyEvent.VK_5: octav_tmp++; chroma = 6; break;
            case KeyEvent.VK_T: octav_tmp++; chroma = 7; break;
            case KeyEvent.VK_6: octav_tmp++; chroma = 8; break;
            case KeyEvent.VK_Y: octav_tmp++; chroma = 9; break;
            case KeyEvent.VK_7: octav_tmp++; chroma = 10; break;
            case KeyEvent.VK_U: octav_tmp++; chroma = 11; break;
            case KeyEvent.VK_I: octav_tmp += 2; chroma = 0; break;

            case KeyEvent.VK_Z: chroma = 0; break;
            case KeyEvent.VK_S: chroma = 1; break;
            case KeyEvent.VK_X: chroma = 2; break;
            case KeyEvent.VK_D: chroma = 3; break;
            case KeyEvent.VK_C: chroma = 4; break;
            case KeyEvent.VK_V: chroma = 5; break;
            case KeyEvent.VK_G: chroma = 6; break;
            case KeyEvent.VK_B: chroma = 7; break;
            case KeyEvent.VK_H: chroma = 8; break;
            case KeyEvent.VK_N: chroma = 9; break;
            case KeyEvent.VK_J: chroma = 10; break;
            case KeyEvent.VK_M: chroma = 11; break;
            case KeyEvent.VK_COMMA: octav_tmp++; chroma = 0; break;

            default: return null;
        }

        return new Note((octav_tmp * 12) + chroma);
    }
}

