
package bozack;

import bozack.Note;
import bozack.DissonanceMap;
import java.util.LinkedHashSet;
import java.util.Iterator;

public final class NoteSet extends LinkedHashSet<Note>
    implements Cloneable {

    private DissonanceMap dissonance = new DissonanceMap();

    public double getDessonance() {
        return this.getDessonance(new DissonanceMap());
    }

    public double getDessonance(DissonanceMap dis) {
        double des = 0.0d;

        Iterator iter_outer = this.iterator();
        while (iter_outer.hasNext()) {
            Note noteA = (Note)iter_outer.next();
            Iterator iter_inner = this.iterator();
            while (iter_inner.hasNext()) {
                Note noteB = (Note)iter_inner.next();

                double tmpDis = 0.0d;
                if (null != dis
                    && dis.hasKey(noteA.getNote(), noteB.getNote())) {
                    tmpDis = dis.getValue(noteA, noteB);
                }
                else {
                    tmpDis += noteA.getDessonance(noteB);
                }
                des += tmpDis;
            }
        }
        return des;
    }

    public Object clone() {
        return super.clone();
    }
}

