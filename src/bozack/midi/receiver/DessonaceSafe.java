package bozack.midi.receiver;

import bozack.midi.receiver.CustomReceiver;
import bozack.Note;
import javax.sound.midi.*;

public final class DessonaceSafe
    extends CustomReceiver {

    protected void handleShortMessage(ShortMessage sm, long timeStamp) {
        switch(sm.getCommand()) {
            case ShortMessage.NOTE_ON:
                bozack.HarmonyController controller
                    = new bozack.controller.SafeInterval();
                controller.add(this.onNote);
                Note note = controller.getLastNote();

                this.defaultChannel.noteOn(
                    note.getNote(), sm.getData2());
                dumpMessage(sm);
                break;
            case ShortMessage.NOTE_OFF:
                this.defaultChannel.noteOff(
                    sm.getData1(), sm.getData2());
                dumpMessage(sm);
                break;
            case ShortMessage.CONTROL_CHANGE:
                if (176 == sm.getStatus()) {
                    System.out.println("desonance limit to: "
                        + sm.getData2());
                }
                break;
        }
    }
}

