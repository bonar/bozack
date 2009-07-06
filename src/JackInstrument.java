
import java.io.*;
import java.util.ArrayList;
import javax.sound.midi.*;
import bozack.midi.Bridge;
import bozack.midi.receiver.CustomReceiver;
import bozack.midi.receiver.DumpRelay;
import bozack.midi.event.FramePainter;
import bozack.BridgeFrame;
import bozack.NoteSet;

class JackInstrument {
    public static void main (String[] arg) {

        BridgeFrame frame = new BridgeFrame();
        NoteSet ns = new NoteSet();
        frame.paintKeyPanel(ns);

        ArrayList devices = Bridge.getDevices();
        bozack.midi.Bridge bridge = new bozack.midi.Bridge();
        CustomReceiver recv = new DumpRelay();
        recv.addNoteEventListener(new FramePainter(frame));
        try {
            bridge.connect(
                (MidiDevice)(devices.get(0)),
                MidiSystem.getSynthesizer(),
                recv
            );
        } catch (MidiUnavailableException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }


        // command line operation
        InputStreamReader cmdin = new InputStreamReader(System.in);
        try {
            while (true) {
                int input = cmdin.read();
                System.out.print("[" + input + "]");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}


