
package bozack.midi.event;

import bozack.BridgeFrame;
import bozack.midi.event.NoteEventListener;
import bozack.Note;
import bozack.NoteSet;

public class FramePainter implements NoteEventListener {
    private final BridgeFrame frame;

    public FramePainter(BridgeFrame frame) {
        this.frame = frame;
    }

    public void performOnNote(Note onNote, NoteSet pressedNotes) {
        this.frame.paintKeyPanel(pressedNotes);
        System.out.println("NoteEventListener ON");
    }
    
    public void  performOffNote(Note offNote, NoteSet pressedNotes) {
        this.frame.paintKeyPanel(pressedNotes);
        System.out.println("NoteEventListener OFF");

    }
}

