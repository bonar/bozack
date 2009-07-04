
package bozack.midi.event;

import bozack.Note;
import bozack.NoteSet;

public interface NoteEventListener {
    void performOnNote(Note onNote, NoteSet pressedNotes);
    void performOffNote(Note offNote, NoteSet pressedNotes);
}

