
package bozack.midi.event;

import bozack.BridgeFrame;
import bozack.midi.event.NoteEventListener;
import bozack.midi.receiver.CustomReceiver;
import bozack.Note;
import bozack.NoteSet;

public class FramePainter implements NoteEventListener {
    private final BridgeFrame frame;

    public FramePainter(BridgeFrame frame) {
        this.frame = frame;
    }

    public void performOnNote(CustomReceiver recv, Note onNote) {
        this.frame.paintKeyPanel(recv.getOnNote()
            , recv.getAssistedOnNote());
    }
    
    public void  performOffNote(CustomReceiver recv, Note offNote) {
        this.frame.paintKeyPanel(recv.getOnNote()
            , recv.getAssistedOnNote());
    }
}

