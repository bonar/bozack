
package bozack.midi.receiver;

import bozack.midi.receiver.CustomReceiver;
import bozack.Note;
import javax.sound.midi.*;

public final class DumpRelay
    extends CustomReceiver {

    public DumpRelay() {
        super();
    }

    public void handleShortMessage(ShortMessage sm, long timeStamp) {
        if (sm.getCommand() == ShortMessage.NOTE_ON
            && sm.getData2() > 0) {
            Note note = new Note(sm.getData1());
            System.out.println(note.toString());
            this.defaultChannel.noteOn(
                sm.getData1(), sm.getData2());
            return;
        }
        else if ((sm.getCommand() == ShortMessage.NOTE_ON
            && sm.getData2() == 0) ||
            sm.getCommand() == ShortMessage.NOTE_OFF
            ) {
            this.defaultChannel.noteOff(
                sm.getData1(), sm.getData2());
            return;
        }
    }
}

