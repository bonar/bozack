
package bozack.midi;
import javax.sound.midi.*;

public class RecvDumpRelay
    extends CustomReceiver {

    public void send(MidiMessage message, long timeStamp) {
        if (this.debug) {
            dumpMessage(message);
        }

        if (message instanceof ShortMessage) {
            ShortMessage sm = ((ShortMessage)message);
            switch(sm.getCommand()) {
                case ShortMessage.NOTE_ON:
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
}

