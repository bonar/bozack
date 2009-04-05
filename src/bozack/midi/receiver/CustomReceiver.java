
package bozack.midi.receiver;
import bozack.Note;
import javax.sound.midi.*;
import java.util.LinkedHashSet;
import java.util.ArrayList;

final class NoteSet extends LinkedHashSet<Note> {}
final class NoteHistory extends ArrayList<Note> {}

public class CustomReceiver
    implements javax.sound.midi.Receiver {

    protected Synthesizer synth;
    protected MidiChannel defaultChannel;
    protected NoteSet onNote;
    protected NoteHistory onNoteHistory;

    public CustomReceiver() {
        this.onNote        = new NoteSet();
        this.onNoteHistory = new NoteHistory();
    }

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
            this.updateOnNoteSet(sm);
            this.handleShortMessage(sm, timeStamp);
            return;
        }
        System.out.println("not short message");
        this.handleMessage(message, timeStamp);
    }

    private void updateOnNoteSet(ShortMessage sm) {
        Note note = new Note(sm.getData1());
        switch(sm.getCommand()) {
            case ShortMessage.NOTE_ON:
                this.onNote.add(note);
                break;
            case ShortMessage.NOTE_OFF:
                this.onNote.remove(note);
                break;
        }
    }

    private void appendHistory(ShortMessage sm) {
        this.onNoteHistory.add(new Note(sm.getData1()));
    }

    protected void handleMessage(
        MidiMessage message, long timeStamp) { }
    protected void handleShortMessage(
        ShortMessage message, long timeStamp) { 
        System.out.println("parent method"); }
    public void close() { }
}


