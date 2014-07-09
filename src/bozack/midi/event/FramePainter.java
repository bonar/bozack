
package bozack.midi.event;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import bozack.ui.BridgeFrame;
import bozack.midi.event.NoteEventListener;
import bozack.midi.receiver.CustomReceiver;
import bozack.Note;

public class FramePainter implements NoteEventListener {
    private final BridgeFrame frame;

    public FramePainter(BridgeFrame frame) {
        this.frame = frame;
    }

    public void performOnNote(CustomReceiver recv, Note onNote) {
        this.frame.paintKeyPanel(
              recv.getOnNote()
            , recv.getAssistedOnNote()
            , recv.getPickupRelation()
            , recv.getSequencerOnNote()
            );
    }

    public void  performOffNote(CustomReceiver recv, Note offNote) {
        this.frame.paintKeyPanel(
              recv.getOnNote()
            , recv.getAssistedOnNote()
            , recv.getPickupRelation()
            , recv.getSequencerOnNote()
            );
    }

    public void performControl(
        CustomReceiver recv, MidiMessage message) { }

}

