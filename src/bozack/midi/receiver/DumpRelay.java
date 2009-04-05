
package bozack.midi.receiver;

import bozack.midi.receiver.CustomReceiver;
import bozack.Note;
import javax.sound.midi.*;

public final class DumpRelay
    extends CustomReceiver {

    public DumpRelay() {
        super();
    }

    protected void handleShortMessage(ShortMessage sm, long timeStamp) {
        switch(sm.getCommand()) {
            case ShortMessage.NOTE_ON:
                Note note = new Note(sm.getData1());
                System.out.println(note.toString());
                this.defaultChannel.noteOn(
                    sm.getData1(), sm.getData2());
                break;
            case ShortMessage.NOTE_OFF:
                this.defaultChannel.noteOff(
                    sm.getData1(), sm.getData2());
                break;
        }
    }
}

