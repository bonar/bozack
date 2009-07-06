
package bozack.midi.event;

import bozack.Note;
import bozack.NoteSet;

public interface NoteEventListener {
    void performOnNote(Note onNote, NoteSet pressedNotes);
    void performOnNoteAssisted(
        Note onNote, NoteSet pressedNotes, NoteSet assistedNotes);
    void performOffNote(Note offNote, NoteSet pressedNotes);
}
