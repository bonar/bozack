
package bozack.midi.receiver;

import java.util.ArrayList;
import java.util.Iterator;
import javax.sound.midi.*;
import bozack.Note;
import bozack.NoteSet;
import bozack.NoteList;
import bozack.NoteHashMap;
import bozack.midi.event.NoteEventListener;

public class CustomReceiver
    implements javax.sound.midi.Receiver {

    protected Synthesizer synth;
    protected MidiChannel defaultChannel;
    protected NoteSet onNote;
    protected NoteSet assistedOnNote;
    protected NoteHashMap pickupRelation;
    protected Note lastNote;

    private ArrayList<NoteEventListener> noteEventListeners
        = new ArrayList<NoteEventListener>();
    private enum NOTE_EVENT {
        ON,
        OFF
    };

    public CustomReceiver() {
        this.onNote         = new NoteSet();
        this.assistedOnNote = new NoteSet();
        this.pickupRelation = new NoteHashMap();
        this.lastNote = null;
    }

    public NoteSet getOnNote() {
        return (NoteSet)this.onNote.clone();
    }

    public NoteSet getAssistedOnNote() {
        return (NoteSet)this.assistedOnNote.clone();
    }

    public NoteHashMap getPickupRelation() {
        return (NoteHashMap)this.pickupRelation.clone();
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

            switch(sm.getCommand()) {
                case ShortMessage.NOTE_ON:
                    Note n = new Note(sm.getData1());
                    this.lastNote = n;
                    this.performNoteEvent(n, NOTE_EVENT.ON);
                    break;
                case ShortMessage.NOTE_OFF:
                    this.performNoteEvent(new Note(sm.getData1())
                        , NOTE_EVENT.OFF);
                    break;
            }
            return;
        }
        this.handleMessage(message, timeStamp);
    }

    private void updateOnNoteSet(ShortMessage sm) {
        if ((sm.getCommand() != ShortMessage.NOTE_ON)
            && (sm.getCommand() != ShortMessage.NOTE_OFF)) {
            return;
        }
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

    public void handleMessage(
        MidiMessage message, long timeStamp) { }
    public void handleShortMessage(
        ShortMessage message, long timeStamp) { 
        System.out.println("parent method"); }
    public void close() { }

    public void addNoteEventListener(NoteEventListener listener) {
        this.noteEventListeners.add(listener);
    }

    private void performNoteEvent(Note lastNote, NOTE_EVENT type) {
        if (0 == this.noteEventListeners.size()) {
            return;
        }
        Iterator iter = this.noteEventListeners.iterator();
        while (iter.hasNext()) {
            NoteEventListener listener
                = (NoteEventListener)iter.next();
            if (type == NOTE_EVENT.ON) {
                listener.performOnNote(this, lastNote);
            }
            else {
                listener.performOffNote(this, lastNote);
            }

        }
    }
}


