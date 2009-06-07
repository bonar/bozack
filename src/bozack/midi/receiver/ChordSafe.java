
package bozack.midi.receiver;

import bozack.midi.receiver.CustomReceiver;
import bozack.Note;
import bozack.NoteSet;
import bozack.PitchSet;
import bozack.controller.ChordInterval;
import javax.sound.midi.*;
import java.util.Iterator;

public final class ChordSafe
    extends CustomReceiver {

    private PitchSet chroma = new PitchSet();
    private NoteSet innerOnNote = new NoteSet();
    private int chordCursor = 0;

    public ChordSafe() {
        System.out.println("constructor");
        this.chroma.add(new Integer(0));
        this.chroma.add(new Integer(4));
        this.chroma.add(new Integer(7));
    }

    protected void handleShortMessage(ShortMessage sm, long timeStamp) {
        switch(sm.getCommand()) {
            case ShortMessage.NOTE_ON:
                Note givenNote = new Note(sm.getData1());
                if (givenNote.getOctav() < 6) {
                    Note cNote = pickupChord(givenNote);
                    if (null != cNote) {
                        this.defaultChannel.noteOn(
                            cNote.getNote(), sm.getData2());
                    }
                    break;
                }
                else {
                    Note cNote = pickupNote(givenNote);
                    if (null != cNote) {
                        this.defaultChannel.noteOn(
                            cNote.getNote(), sm.getData2());
                    }
                    break;
                }
            case ShortMessage.NOTE_OFF:
                this.innerOnNote.clear();
                this.defaultChannel.noteOff(
                    sm.getData1(), sm.getData2());
                break;
            case ShortMessage.CONTROL_CHANGE:
                if (sm.getData1() == 67 && sm.getData2() == 127) {
                    this.moveChordCursor();
                }
                break;
        }
    }

    private void moveChordCursor() {
        this.chordCursor++;
        int pattern = this.chordCursor % 4;
        PitchSet newChroma = new PitchSet();
        switch (pattern) {
            case 0:
                newChroma.add(new Integer(5));
                newChroma.add(new Integer(9));
                newChroma.add(new Integer(0));
                break;
            case 1:
                newChroma.add(new Integer(7));
                newChroma.add(new Integer(11));
                newChroma.add(new Integer(2));
                break;
            case 2:
                newChroma.add(new Integer(4));
                newChroma.add(new Integer(8));
                newChroma.add(new Integer(11));
                break;
            case 3:
                newChroma.add(new Integer(9));
                newChroma.add(new Integer(1));
                newChroma.add(new Integer(4));
                break;
        }
        this.chroma = newChroma;
    }

    private Note pickupNote(Note note) {
        PitchSet largeChroma = new PitchSet();
        Iterator iter = this.chroma.iterator();
        while (iter.hasNext()) {
            int chroma = (Integer)iter.next();
            largeChroma.add(chroma);

            int extraChroma = chroma + 7;
            if (extraChroma >= 12) {
                extraChroma -= 12;
            }
            if (!largeChroma.contains(extraChroma)) {
                largeChroma.add(extraChroma);
            }
        }
        
        for (int i = note.getNote(); i > 12; i++) {
            Note targetNote = new Note(i);
            if (largeChroma.contains(targetNote.getPitch())
                && !this.innerOnNote.contains(targetNote)) {
                return targetNote;
            }
        }
        return null;
    }

    private Note pickupChord(Note note) {
        for (int i = note.getNote(); i > 12; i--) {
            Note targetNote = new Note(i);
            if (this.chroma.contains(targetNote.getPitch())
                && !this.innerOnNote.contains(targetNote)) {
                this.innerOnNote.add(targetNote);
                return targetNote;
            }
        }
        return null;
    }
}

