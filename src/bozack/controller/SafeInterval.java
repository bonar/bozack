
package bozack.controller;
import bozack.Note;
import bozack.NoteSet;

public final class SafeInterval
    implements bozack.HarmonyController {

    private static int SAFE_INTERVAL = 3;
    private NoteSet notes = new NoteSet();

    public SafeInterval() {}

    public void add(Note note) {

    }

    public NoteSet getAllNote() {
        return this.notes;
    }

    public Note getLastNote() {
        Note[] n = (Note[])(this.notes.toArray());
        return n[(n.length - 1)];
    }

}
