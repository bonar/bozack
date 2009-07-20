
package bozack.midi.receiver;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Receiver;

import bozack.midi.receiver.CustomReceiver;
import bozack.ui.BridgeFrame;

public class SequenceReceiver implements Receiver {
    private CustomReceiver receiver;
    private BridgeFrame frame;

    public SequenceReceiver (CustomReceiver r, BridgeFrame f) {
        this.receiver = r;
        this.frame    = f;
    }

    public void close() { }

    public void send(MidiMessage message, long timeStamp) {
        if (!(message instanceof ShortMessage)) {
            return;
        }
        ShortMessage sm = (ShortMessage)message;
        switch (sm.getCommand()) {
            case ShortMessage.NOTE_ON:
                this.receiver.sequencerNoteOn(sm.getData1());
                //this.repainFrame();
                return;
            case ShortMessage.NOTE_OFF:
                this.receiver.sequencerNoteOff(sm.getData1());
                //this.repainFrame();
                return;
        }
    }

    private void repainFrame() {
        this.frame.paintKeyPanel(
              this.receiver.getOnNote()
            , this.receiver.getAssistedOnNote()
            , this.receiver.getPickupRelation()
            , this.receiver.getSequencerOnNote()
            );
    }
}

