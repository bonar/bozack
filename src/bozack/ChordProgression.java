
package bozack;

import bozack.Chord;
import bozack.ChordRole;
import bozack.ChromaName;
import bozack.ChordType;
import java.util.ArrayList;
import java.util.HashMap;

final class ChordList    extends ArrayList<Chord> {}
final class ChordRoleMap extends HashMap<ChordRole, ChordList> {}

public final class ChordProgression {

    private ChordRole currentRole = ChordRole.TONIC;
    private int variateDepth      = 0;

    private static final ChordRoleMap roleMap = createRoleMap();

    private static ChordRoleMap createRoleMap () {
        ChordRoleMap map = new ChordRoleMap();

        ChordList tonic = new ChordList();
        tonic.add(new Chord(ChromaName.C, ChordType.MAJOR));
        tonic.add(new Chord(ChromaName.C, ChordType.SEVEN));
        tonic.add(new Chord(ChromaName.E, ChordType.m7));
        tonic.add(new Chord(ChromaName.F, ChordType.m7));
        map.put(ChordRole.TONIC, tonic);

        ChordList subdominant = new ChordList();
        subdominant.add(new Chord(ChromaName.F, ChordType.MAJOR));
        subdominant.add(new Chord(ChromaName.F, ChordType.SEVEN));
        subdominant.add(new Chord(ChromaName.D, ChordType.m7));
        map.put(ChordRole.SUB_DOMINANT, subdominant);

        ChordList dominat = new ChordList();
        dominat.add(new Chord(ChromaName.G, ChordType.SEVEN));
        dominat.add(new Chord(ChromaName.B, ChordType.m7));
        map.put(ChordRole.DOMINANT, dominat);

        return map;
    }

    public ChordProgression() { }

    private void clearVariateDepth() {
        this.variateDepth = 0;
    }
    private ChordList getChordList() {
        return roleMap.get(this.currentRole);
    }
    public Chord getChord() {
        ChordList list = this.getChordList();
        int chordIndex = this.variateDepth;
        chordIndex = chordIndex % list.size(); // rotate index
        return (Chord)list.get(chordIndex);
    }
    public void next() {
        this.clearVariateDepth();
        this.currentRole = ChordRole.rotate(this.currentRole);
    }
    public void variate() {
        this.variateDepth++;
    }

}

