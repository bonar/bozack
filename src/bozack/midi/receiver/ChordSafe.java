
package bozack.midi.receiver;

import bozack.midi.receiver.CustomReceiver;
import bozack.Note;
import bozack.NoteSet;
import bozack.ChromaName;
import bozack.ChordType;
import bozack.Chord;
import bozack.ChromaSet;
import bozack.controller.ChordInterval;
import javax.sound.midi.*;
import java.util.Iterator;

public final class ChordSafe
    extends CustomReceiver {

    private ChromaSet chroma = new ChromaSet();
    private NoteSet innerOnNote = new NoteSet();
    private int chordCursor = 0;

    public ChordSafe() {
        Chord C = new Chord(ChromaName.C, ChordType.MAJOR);
        this.chroma = C.getChromaSet();
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
        ChromaSet newChroma = null;
        switch (pattern) {
            case 0:
                Chord C = new Chord(ChromaName.C, ChordType.MAJOR);
                newChroma = C.getChromaSet();
                break;
            case 1:
                Chord Am = new Chord(ChromaName.A, ChordType.m);
                newChroma = Am.getChromaSet();
                break;
            case 2:
                Chord Dm = new Chord(ChromaName.D, ChordType.m);
                newChroma = Dm.getChromaSet();
                break;
            case 3:
                Chord G7 = new Chord(ChromaName.G, ChordType.SEVEN);
                newChroma = G7.getChromaSet();
                break;
        }
        this.chroma = newChroma;
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
                && !this.innerOnNote.contains(targetNote)) {
                return targetNote;
            }
        }
        return null;
    }

    private Note pickupChord(Note note) {
        for (int i = note.getNote(); i > 12; i--) {
            Note targetNote = new Note(i);
            if (this.chroma.contains(targetNote.getChroma())
                && !this.innerOnNote.contains(targetNote)) {
                this.innerOnNote.add(targetNote);
                return targetNote;
            }
        }
        return null;
    }
}

