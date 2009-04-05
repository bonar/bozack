
package bozack;
import bozack.Note;
import bozack.NoteSet;

public interface HarmonyController {
    void add(Note note);
    void add(NoteSet noteset);
    NoteSet getAllNote();
    Note getLastNote();

}

