
package bozack.midi.event;

import bozack.Note;
import bozack.NoteSet;
import bozack.midi.receiver.CustomReceiver;

public interface NoteEventListener {
    void performOnNote(CustomReceiver recv, Note onNote);
    void performOffNote(CustomReceiver recv, Note offNote);
}

