
package bozack;

import java.util.HashMap;
import bozack.ChromaName;

/**
 * pitch String to note Interger
 * @see bozack.Note
 */
final class ChromaNameHash extends HashMap<ChromaName, Integer> {}
final class ChromaNumHash  extends HashMap<Integer, ChromaName> {}

public final class Types {
    // not to create instance
    private Types() {}

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


