
package bozack.midi.receiver;

import java.util.HashMap;
import javax.sound.midi.ShortMessage;
import bozack.midi.receiver.CustomReceiver;
import bozack.Chord;
import bozack.ChromaSet;
import bozack.Note;
import bozack.NoteSet;

public final class Stabilizer
    extends CustomReceiver {

    public Stabilizer() { }
    public Stabilizer(boolean chordAssist) {
        this.chordAssist = chordAssist;
    }

    private static final int SCAN_RANGE = 9;
    private static final double DIRECT_RETURN_BORDER = 0.3d;
    private static final double NEIBOUR_BONUS_RATE   = 0.02d;
    private static final double CHORD_BONUS_RATE     = 1.50d;

    public void handleShortMessage(ShortMessage sm, long timeStamp) {
        if (sm.getCommand() == ShortMessage.NOTE_ON
            && sm.getData2() > 0) {
            this.noteOn(new Note(sm.getData1()), sm.getData2());
            return;
        }
        else if ((sm.getCommand() == ShortMessage.NOTE_ON 
            && sm.getData2() == 0) || 
            sm.getCommand() == ShortMessage.NOTE_OFF
            ) {
            this.noteOff(new Note(sm.getData1()), sm.getData2());
            return;
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
        if (0 == this.assistedOnNote.size()) {
            return note;
        }
        int cursorMove = 1;
        if (null == this.lastNote
            || note.getNote() > this.lastNote.getNote()) {
            cursorMove = 1;
        }

        int scanRange = SCAN_RANGE;
        int tmpNote = note.getNote();
        double minDes = 100.0d;
        int minDesNote = note.getNote();
        while (0 != scanRange--) {
            Note cursorNote = new Note(tmpNote);

            NoteSet tmpNoteSet = (NoteSet)this.assistedOnNote.clone();
            tmpNoteSet.add(cursorNote);
            double des = tmpNoteSet.getDessonance(this.dissonance);

            // chord assist bonus
            if (this.chordAssist()) {
                Chord c = this.chord;
                ChromaSet chromaSet = c.getChromaSet();
                if (chromaSet.contains(
                    new Integer(cursorNote.getChroma()))) {
                    System.out.println("match " + cursorNote.getChroma());
                    des -= CHORD_BONUS_RATE;
                }
            }

            double neibour_bonus = NEIBOUR_BONUS_RATE * scanRange;
            double total = (des / tmpNoteSet.size()) - neibour_bonus;

            if (!this.assistedOnNote.contains(cursorNote) &&
                total < DIRECT_RETURN_BORDER) {
                return new Note(tmpNote);
            }

            if (!this.assistedOnNote.contains(cursorNote) &&
                total < minDes) {
                minDesNote = tmpNote;
                minDes = total;
            }
            tmpNote += cursorMove;
        }
        return new Note(minDesNote);
    }

    private void noteOff(Note note, int velocity) {
        Note assistedNote = this.pickupRelation.get(note);
        this.assistedOnNote.remove(assistedNote);
        this.pickupRelation.remove(note);

        this.defaultChannel.noteOff(note.getNote(), velocity);
    }



}

