
package bozack.midi;
import javax.sound.midi.*;

public class RecvDumpRelay
    extends CustomReceiver {

    public void send(MidiMessage message, long timeStamp) {
        if (this.debug) {
            dumpMessage(message);
        }

        MidiChannel target_ch = this.getDefaultChannel();
    }
}

