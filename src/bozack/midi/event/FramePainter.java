
package bozack.midi.event;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import bozack.ui.BridgeFrame;
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
            , recv.getAssistedOnNote()
            , recv.getPickupRelation());
    }
    
    public void  performOffNote(CustomReceiver recv, Note offNote) {
        this.frame.paintKeyPanel(recv.getOnNote()
            , recv.getAssistedOnNote()
            , recv.getPickupRelation());
    }

    public void performControl(CustomReceiver recv, MidiMessage message) {
        if (message instanceof ShortMessage) {
            ShortMessage sm = (ShortMessage)message;
            if (sm.getStatus() == 176) {
                switch (sm.getData1()) {
                    case 1:
                        this.frame.chordNext(0);
                        break;
                    case 2:
                        this.frame.chordNext(1);
                        break;
                    case 3:
                        this.frame.chordNext(2);
                        break;
                    case 4:
                        this.frame.chordNext(3);
                        break;
                    case 5:
                        this.frame.chordNext(4);
                        break;
                }
            }
        }
    }
}

