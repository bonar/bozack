
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

    private static final double MIN_DIS_DEFAULT = 100.0d;
    private static final double MIN_DIS_PLAY    = 3.0d;
    private static final int MIN_INTERVAL = 3;

    private static final int SCAN_RANGE = 6;
    private static final double DIRECT_RETURN_BORDER = 0.2d;
    private static final double NEIBOUR_BONUS_RATE   = 0.10d;
    private static final double CHORD_BONUS_RATE     = 0.20d;

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
        System.out.println("---------------------------");
        if (!this.chordAssist()
            && 0 == this.assistedOnNote.size()) {
            return note;
        }
        int cursorMove = -1;
        if (null == this.lastNote
            || note.getNote() > this.lastNote.getNote()) {
            cursorMove = 1;
        }

        int scanRange = SCAN_RANGE;
        int tmpNote = note.getNote();
        double minDes = MIN_DIS_DEFAULT;
        int minDesNote = note.getNote();

        SEARCH_NOTE:
        while (0 != scanRange--) {
            Note cursorNote = new Note(tmpNote);

            // minimum interval check
            NoteSet tmpNoteSet = (NoteSet)this.assistedOnNote.clone();
            for (Note n : tmpNoteSet) {
                int diff = Math.abs(cursorNote.getNote() - n.getNote());
                if (diff < MIN_INTERVAL) {
                    System.out.println("[" + tmpNote + "] too small interval");
                    tmpNote += cursorMove;
                    continue SEARCH_NOTE;
                }
            }

            tmpNoteSet.add(cursorNote);
            double desTotal = tmpNoteSet.getDessonance(this.dissonance);
            double des      = (desTotal / tmpNoteSet.size());

            // chord assist bonus
            double chord_bonus = 0.0d;
            if (this.chordAssist() && des < MIN_DIS_PLAY) {
                Chord c = this.chord;
                ChromaSet chromaSet = c.getChromaSet();
                if (chromaSet.contains(
                    new Integer(cursorNote.getChroma()))) {
                    chord_bonus = CHORD_BONUS_RATE * scanRange;
                }
            }

            double neibour_bonus = NEIBOUR_BONUS_RATE * scanRange;
            double total = des - neibour_bonus - chord_bonus;
            System.out.println("[" + tmpNote + "] score = basedis:" + des
                + " neibour:" + neibour_bonus
                + " chord:" + chord_bonus
                + " total:" + total);

            if (!this.assistedOnNote.contains(cursorNote) &&
                des < DIRECT_RETURN_BORDER) {
                return new Note(tmpNote);
            }

            if (!this.assistedOnNote.contains(cursorNote) &&
                total < minDes) {
                minDesNote = tmpNote;
                minDes = total;
            }
            tmpNote += cursorMove;
        }

        if (MIN_DIS_DEFAULT == minDes) {
            return null; // No note was selected
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

