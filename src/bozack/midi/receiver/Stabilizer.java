
package bozack.midi.receiver;

import java.util.HashMap;
import javax.sound.midi.ShortMessage;
import bozack.midi.receiver.CustomReceiver;
import bozack.Note;
import bozack.NoteSet;

class NoteHashMap extends HashMap<Note, Note> {}

public final class Stabilizer
    extends CustomReceiver {

    private NoteHashMap pickupRelation;

    public Stabilizer() {
        this.pickupRelation = new NoteHashMap();
    }

    protected void handleShortMessage(ShortMessage sm, long timeStamp) {
        switch(sm.getCommand()) {
            case ShortMessage.NOTE_ON:
                this.noteOn(new Note(sm.getData1()), sm.getData2());
                break;
            case ShortMessage.NOTE_OFF:
                this.noteOff(new Note(sm.getData1()), sm.getData2());
                break;
        }
    }

    private void noteOn(Note note, int velocity) {
        Note pickupedNote = this.pickup(note);
        if (null == pickupedNote) {
            return;
        }
        this.assistedOnNote.add(pickupedNote);
        this.pickupRelation.put(note, pickupedNote);

        this.defaultChannel.noteOn(pickupedNote.getNote(), velocity);
    }

    private Note pickup(Note note) {
        return new Note(note.getNote() + 1);
    }

    private void noteOff(Note note, int velocity) {
        Note assistedNote = this.pickupRelation.get(note);
        this.assistedOnNote.remove(assistedNote);
        this.pickupRelation.remove(note);

        this.defaultChannel.noteOff(note.getNote(), velocity);
    }



}

