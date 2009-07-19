
package bozack.midi.receiver;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Receiver;

import bozack.midi.receiver.CustomReceiver;

public class SequenceReceiver implements Receiver {
    private CustomReceiver receiver;
    public SequenceReceiver (CustomReceiver r) {
        this.receiver = r;
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
                return;
            case ShortMessage.NOTE_OFF:
                this.receiver.sequencerNoteOff(sm.getData1());
                return;
        }
    }
}

