
package bozack;
import bozack.Note;
import bozack.NoteSet;

public interface HarmonyController {
    void add(Note note);
    NoteSet getAllNote();
    Note getLastNote();

}

