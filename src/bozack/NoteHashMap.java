
package bozack;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import bozack.Note;

public class NoteHashMap extends HashMap<Note, Note> {

    public Note scanValue(Note valueNote) {
        if (0 == this.size()) {
            return null;
        }
        Iterator iter = this.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            if (valueNote.equals((Note)entry.getValue())) {
                return (Note)entry.getKey();
            }
        }
        return null;
    }
}

