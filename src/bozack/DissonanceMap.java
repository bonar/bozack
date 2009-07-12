
package bozack;

import java.util.HashMap;
import bozack.Note;

public class DissonanceMap extends HashMap<String, Double> {

    private static String makeKey(int a, int b) {
        return new String("" + a + "," + b);
    }
    public void put (int a, int b) {
        Note noteA = new Note(a);
        Note noteB = new Note(b);

        double des = noteA.getDessonance(noteB);
        this.put(makeKey(a, b), new Double(des));
    }

    public boolean hasKey (int a, int b) {
        return this.containsKey(makeKey(a, b));
    }

    public double getValue (int a, int b) {
        return this.get(makeKey(a, b)).doubleValue();
    }

    public double getValue(Note a, Note b) {
        return this.getValue(a.getNote(), b.getNote());
    }
}


