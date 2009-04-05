
package bozack;

import java.util.HashMap;

enum PitchName {
    C_FLAT, C, C_SHARP,
    D_FLAT, D, D_SHARP,
    E_FLAT, E, E_SHARP,
    F_FLAT, F, F_SHARP,
    G_FLAT, G, G_SHARP,
    A_FLAT, A, A_SHARP,
    B_FLAT, B, B_SHARP,
}

/**
 * pitch String to note Interger
 * @see bozack.Note
 */
final class PitchNameHash extends HashMap<PitchName, Integer> {}
final class PitchNumHash  extends HashMap<Integer, PitchName> {}

enum SpanType {
    ST_PLAIN,
    ST_m,
    ST_M,
    ST_7,
    ST_m7,
}

/**
 * NoteSpan is used to represent distance from the Note to 
 * another Note in some Chord
 */
final class NoteSpan {
    private final int[] span;

    NoteSpan(int[] span) {
        this.span = span;
    }

    public int[] getSpan() {
        return this.span;
    }
}

/**
 * ScanType String to IntegerArray
 * @see bozack.Chord
 */
final class SpanTypeMap extends HashMap<SpanType, NoteSpan> {}

public final class Types {
    // not to create instance
    private Types() {}

    public static PitchNameHash getPitchNameHash() {
        PitchNameHash m = new PitchNameHash();
        m.put(PitchName.B_SHARP, 0);
        m.put(PitchName.C,       0);
        m.put(PitchName.C_SHARP, 1);
        m.put(PitchName.D_FLAT,  1);
        m.put(PitchName.D,       2);
        m.put(PitchName.D_SHARP, 3);
        m.put(PitchName.E_FLAT,  3);
        m.put(PitchName.E,       4);
        m.put(PitchName.E_SHARP, 5);
        m.put(PitchName.F_FLAT,  4);
        m.put(PitchName.F,       5);
        m.put(PitchName.F_SHARP, 6);
        m.put(PitchName.G_FLAT,  6);
        m.put(PitchName.G,       7);
        m.put(PitchName.G_SHARP, 8);
        m.put(PitchName.A_FLAT,  8);
        m.put(PitchName.A,       9);
        m.put(PitchName.A_SHARP, 10);
        m.put(PitchName.B_FLAT,  10);
        m.put(PitchName.B,       11);
        m.put(PitchName.C_FLAT,  11);
        return m;
    }

    public static PitchNumHash getPitchNumHash() {
        PitchNumHash n = new PitchNumHash();
        n.put(0,  PitchName.C);
        n.put(1,  PitchName.C_SHARP);
        n.put(2,  PitchName.D);
        n.put(3,  PitchName.D_SHARP);
        n.put(4,  PitchName.E);
        n.put(5,  PitchName.F);
        n.put(6,  PitchName.F_SHARP);
        n.put(7,  PitchName.G);
        n.put(8,  PitchName.G_SHARP);
        n.put(9,  PitchName.A);
        n.put(10, PitchName.A_SHARP);
        n.put(11, PitchName.B);
        return n;
    }

    public static SpanTypeMap getSpanTypeMap() {
        SpanTypeMap s = new SpanTypeMap();
        s.put(SpanType.ST_PLAIN, new NoteSpan(new int[] {0, 4, 7}));
        s.put(SpanType.ST_M,     new NoteSpan(new int[] {0, 4, 7}));
        s.put(SpanType.ST_m,     new NoteSpan(new int[] {0, 3, 7}));
        s.put(SpanType.ST_m7,    new NoteSpan(new int[] {0, 3, 7, 10}));
        s.put(SpanType.ST_7,     new NoteSpan(new int[] {0, 4, 7, 10}));
        return s;
    }

 

}


