
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
        System.out.println("FramePainter: ON " + onNote.toString());
    
    }
    
    public void  performOffNote(Note offNote, NoteSet pressedNotes) {
        System.out.println("FramePainter: OFF " + offNote.toString());

    }
}

