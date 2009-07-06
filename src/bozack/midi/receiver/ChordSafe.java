
package bozack.midi.receiver;

import bozack.midi.receiver.CustomReceiver;
import bozack.Note;
import bozack.NoteSet;
import bozack.ChromaName;
import bozack.ChordType;
import bozack.Chord;
import bozack.ChordProgression;
import bozack.ChromaSet;
import javax.sound.midi.*;
import java.util.Iterator;

public final class ChordSafe
    extends CustomReceiver {

    private ChromaSet chroma;
    private ChordProgression chordProg;
    private int chordCursor = 0;

    public ChordSafe() {
        this.chroma    = this.chordProg.getChord().getChromaSet();
        this.chordProg = new ChordProgression();
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
                this.assistedOnNote.clear();
                this.defaultChannel.noteOff(
                    sm.getData1(), sm.getData2());
                break;
            case ShortMessage.CONTROL_CHANGE:
                if (sm.getData1() == 67 && sm.getData2() == 127) {
                    this.chordProg.next();
                    this.chroma = this.chordProg
                        .getChord().getChromaSet();
                }
                if (sm.getData1() == 68 && sm.getData2() == 127) {
                    this.chordProg.variate();
                    this.chroma = this.chordProg
                        .getChord().getChromaSet();
                }
                break;
        }
    }

    private Note pickupNote(Note note) {
        ChromaSet largeChroma = new ChromaSet();
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
            if (largeChroma.contains(targetNote.getChroma())
                && !this.assistedOnNote.contains(targetNote)) {
                return targetNote;
            }
        }
        return null;
    }

    private Note pickupChord(Note note) {
        for (int i = note.getNote(); i > 12; i--) {
            Note targetNote = new Note(i);
            if (this.chroma.contains(targetNote.getChroma())
                && !this.assistedOnNote.contains(targetNote)) {
                this.assistedOnNote.add(targetNote);
                return targetNote;
            }
        }
        return null;
    }
}

