
package bozack;

import bozack.Note;
import java.util.LinkedHashSet;
import java.util.Iterator;

public final class NoteSet extends LinkedHashSet<Note> 
    implements Cloneable {

    public double getDessonance() {
        double des = 0.0d;

        Iterator iter_outer = this.iterator();
        while (iter_outer.hasNext()) {
            Note noteA = (Note)iter_outer.next();
            Iterator iter_inner = this.iterator();
            while (iter_inner.hasNext()) {
                Note noteB = (Note)iter_inner.next();
                des += noteA.getDessonance(noteB);
            }
        }
        return des;
    }

    public Object clone() {
        return super.clone();
    }
}

