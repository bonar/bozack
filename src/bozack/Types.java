
package bozack;

import java.util.HashMap;

enum ChromaName {
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
final class ChromaNameHash extends HashMap<ChromaName, Integer> {}
final class ChromaNumHash  extends HashMap<Integer, ChromaName> {}

enum SpanType {
    ST_MAJOR,
    ST_m,
    ST_7,
    ST_m7,
    ST_M7,
}

/**
 * NoteHop is used to represent distance from the Note to 
 * another Note in some Chord
 */
final class NoteHop {
    private int[] span = new int[4];

    NoteHop(int a, int b, int c) {
        this.span[0] = a;
        this.span[1] = b;
        this.span[2] = c;
    }

    NoteHop(int a, int b, int c, int d) {
        this.span[0] = a;
        this.span[1] = b;
        this.span[2] = c;
        this.span[3] = d;
    }

    public int[] getHop() {
        return this.span;
    }
}

/**
 * ScanType String to IntegerArray
 * @see bozack.Chord
 */
final class SpanTypeHash extends HashMap<SpanType, NoteHop> {}

public final class Types {
    // not to create instance
    private Types() {}

    public static SpanTypeHash getSpanTypeHash() {
        SpanTypeHash m = new SpanTypeHash();
        m.put(SpanType.ST_MAJOR, new NoteHop(0, 4, 7));
        m.put(SpanType.ST_m,     new NoteHop(0, 3, 7));
        m.put(SpanType.ST_7,     new NoteHop(0, 4, 7, 10));
        m.put(SpanType.ST_m7,    new NoteHop(0, 3, 7, 10));
        m.put(SpanType.ST_M7,    new NoteHop(0, 4, 7, 11));
        return m;
    }

    public static ChromaNameHash getChromaNameHash() {
        ChromaNameHash m = new ChromaNameHash();
        m.put(ChromaName.B_SHARP, 0);
        m.put(ChromaName.C,       0);
        m.put(ChromaName.C_SHARP, 1);
        m.put(ChromaName.D_FLAT,  1);
        m.put(ChromaName.D,       2);
        m.put(ChromaName.D_SHARP, 3);
        m.put(ChromaName.E_FLAT,  3);
        m.put(ChromaName.E,       4);
        m.put(ChromaName.E_SHARP, 5);
        m.put(ChromaName.F_FLAT,  4);
        m.put(ChromaName.F,       5);
        m.put(ChromaName.F_SHARP, 6);
        m.put(ChromaName.G_FLAT,  6);
        m.put(ChromaName.G,       7);
        m.put(ChromaName.G_SHARP, 8);
        m.put(ChromaName.A_FLAT,  8);
        m.put(ChromaName.A,       9);
        m.put(ChromaName.A_SHARP, 10);
        m.put(ChromaName.B_FLAT,  10);
        m.put(ChromaName.B,       11);
        m.put(ChromaName.C_FLAT,  11);
        return m;
    }

    public static ChromaNumHash getChromaNumHash() {
        ChromaNumHash n = new ChromaNumHash();
        n.put(0,  ChromaName.C);
        n.put(1,  ChromaName.C_SHARP);
        n.put(2,  ChromaName.D);
        n.put(3,  ChromaName.D_SHARP);
        n.put(4,  ChromaName.E);
        n.put(5,  ChromaName.F);
        n.put(6,  ChromaName.F_SHARP);
        n.put(7,  ChromaName.G);
        n.put(8,  ChromaName.G_SHARP);
        n.put(9,  ChromaName.A);
        n.put(10, ChromaName.A_SHARP);
        n.put(11, ChromaName.B);
        return n;
    }

}


