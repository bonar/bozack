
import java.util.ArrayList;
import javax.sound.midi.*;
import bozack.midi.Bridge;
import bozack.midi.receiver.*;

class JackInstrument {
    public static void main (String[] arg) {
        ArrayList devices = Bridge.getDevices();
        bozack.midi.Bridge bridge = new bozack.midi.Bridge();
        try {
            bridge.connect(
                (MidiDevice)(devices.get(0)),
                MidiSystem.getSynthesizer());
        } catch (MidiUnavailableException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }


    }
}


