
package bozack.midi;
import javax.sound.midi.*;

public class RecvDumpRelay
    extends CustomReceiver {

    public void send(MidiMessage message, long timeStamp) {
        dump_message(message);
    }

    public void close() { }
}

