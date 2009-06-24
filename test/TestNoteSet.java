
package bozack;

import static org.testng.Assert.*;
import org.testng.annotations.Test;
import bozack.*;

public class TestNoteSet {

    @Test
    public void basic() {
        NoteSet ns = new NoteSet();
        assertNotNull(ns);
        assertEquals(ns.size(), 0);
        ns.add(new Note(60));
        ns.add(new Note(72));
        assertEquals(ns.size(), 2);

        double des = ns.getDessonance();
        assertNotNull(des);


    }
}

