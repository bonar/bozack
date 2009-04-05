
package bozack.midi.receiver;
import bozack.Note;
import javax.sound.midi.*;
import java.util.HashSet;
import java.util.ArrayList;

final class NoteSet extends HashSet<Note> {}
final class NoteHistory extends ArrayList<Note> {}

public class CustomReceiver
    implements javax.sound.midi.Receiver {

    protected Synthesizer synth;
    protected MidiChannel defaultChannel;
    protected NoteSet onNote;

    public void setSynth(MidiDevice device) {
        if (!(device instanceof Synthesizer)) {
            throw new IllegalArgumentException(
                "device is not a Synthesizer");
        }
        this.synth = (Synthesizer)device;

        // pickup first channel
        MidiChannel[] channels = this.synth.getChannels();
        if (0 == channels.length) {
            throw new IllegalStateException("no channels available");
        }
        this.defaultChannel = channels[0];
    }

    public static void dumpMessage(MidiMessage message) {
        System.out.print("size:" + message.getLength() + " ");
        byte[] body = message.getMessage();
        for (int i = 0; i < message.getLength(); i++) {
            System.out.print("[" + body[i] + "]");
        }
        System.out.print(" status:" + message.getStatus() + " ");

        if (message instanceof ShortMessage) {
            ShortMessage sm = ((ShortMessage)message);
            StringBuilder sb = new StringBuilder();
            sb.append("(" + sm.getCommand() + ")");
            sb.append("(" + sm.getData1() + ")");
            sb.append("(" + sm.getData2() + ") ");
            System.out.print(sb.toString());
        }
        System.out.println("");
    }

    public void send(MidiMessage message, long timeStamp) {
        dumpMessage(message);
        if (message instanceof ShortMessage) {
            ShortMessage sm = ((ShortMessage)message);
            handleShortMessage(sm, timeStamp);
            return;
        }
        handleMessage(message, timeStamp);
    }

    private void updateOnNoteSet(MidiMessage message) {
    }

    protected void handleMessage(
        MidiMessage message, long timeStamp) { }
    protected void handleShortMessage(
        ShortMessage message, long timeStamp) { }
    public void close() { }
}


