
package bozack.controller;
import bozack.Note;
import bozack.NoteSet;
import java.util.Iterator;

public final class ChordInterval
    implements bozack.HarmonyController {

    private static int SAFE_INTERVAL = 4;
    private NoteSet notes = new NoteSet();
    private NoteSet chordnotes = new NoteSet();

    public ChordInterval() {}

    public void add(Note note) {
        Note located_note = this.locate(note);
        if (null != located_note) {
            this.notes.add(located_note);
        }
    }

    public void add(NoteSet noteset) {
        Iterator iter = noteset.iterator();
        while (iter.hasNext()) {
            this.add((Note)iter.next());
        }
    }

    private Note locate(Note note) {
        for (int n = note.getNote(); n < Note.getMaxNote(); n++) {
            Note now_note = new Note(n);
            if (this.can_locate(now_note)) {
                return now_note;
            }
        }
        return null;
    }

    private boolean can_locate(Note note) {
        int start = (note.getNote() - SAFE_INTERVAL);
        if (start < 0) {
            start = 0;
        }
        int end = (note.getNote() + SAFE_INTERVAL);
        if (end > Note.getMaxNote()) {
            end = Note.getMaxNote();
        }

        for (int n = start; n < end; n++) {
            Note tmp_note = new Note(n);
            if (this.notes.contains(tmp_note)) {
                return false;
            }
        }
        return true;
    }

    public NoteSet getAllNote() {
        return (NoteSet)this.notes.clone();
    }

    public Note getLastNote() {
        if (0 == this.notes.size()) {
            return null;
        }
        Object[] n = this.notes.toArray();
        if (1 == n.length) {
            return (Note)n[0];
        }
        return (Note)n[(n.length - 1)];
    }

}
