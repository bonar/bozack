
package bozack;

import bozack.ChordRole;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class TestChordRole {

    @Test
    public void rotate() {
       assertEquals(ChordRole.rotate(ChordRole.TONIC)
           , ChordRole.SUB_DOMINANT);
       assertEquals(ChordRole.rotate(ChordRole.SUB_DOMINANT)
           , ChordRole.DOMINANT);
       assertEquals(ChordRole.rotate(ChordRole.DOMINANT)
           , ChordRole.TONIC);
    }
}


