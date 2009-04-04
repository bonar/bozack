
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
class PitchMap extends  HashMap<PitchName, Integer> {}

enum SpanType {
    ST_PLAIN,
    ST_m,
    ST_M,
    ST_7,
    ST_m7,
}

/**
 * ScanType String to IntegerArray
 * @see bozack.Chord
 */
class SpanTypeMap extends HashMap<SpanType, int[]> {}

public class Types {
    // not to create instance
    private Types() {}

    public static PitchMap pitchNameNoteHash() {
        PitchMap m = new PitchMap();
        m.put(PitchName.C_FLAT,  12);
        m.put(PitchName.C,       0);
        m.put(PitchName.C_SHARP, 1);
        m.put(PitchName.D_FLAT,  1);
        m.put(PitchName.D,       2);
        m.put(PitchName.D_SHARP, 3);
        m.put(PitchName.E_FLAT,  3);
        m.put(PitchName.E,       4);
        m.put(PitchName.E_SHARP, 5);
        m.put(PitchName.F_FLAT,  5);
        m.put(PitchName.F,       6);
        m.put(PitchName.F_SHARP, 7);
        m.put(PitchName.G_FLAT,  7);
        m.put(PitchName.G,       8);
        m.put(PitchName.G_SHARP, 9);
        m.put(PitchName.A_FLAT,  9);
        m.put(PitchName.A,       10);
        m.put(PitchName.A_SHARP, 11);
        m.put(PitchName.B_FLAT,  11);
        m.put(PitchName.B,       12);
        m.put(PitchName.B_SHARP, 0);
        return m;
    }

 

}


