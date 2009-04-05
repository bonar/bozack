
package bozack;

import static org.testng.Assert.*;
import org.testng.annotations.Test;
import bozack.*;

public class TestSafeInterval {
    private static final bozack.controller.SafeInterval controller 
        = new bozack.controller.SafeInterval();

    @Test
    public void construct() {
        assertNotNull(this.controller);
    }
    @Test
    public void add_notes() {
        assertNull(this.controller.getLastNote());

        this.controller.add(new Note(50));
        assertEquals(this.controller.getLastNote(), new Note(50));
        assertEquals(this.controller.getAllNote().size(), 1);
    }
    @Test
    public void add_multi_notes() {
        bozack.controller.SafeInterval controller 
            = new bozack.controller.SafeInterval();

        controller.add(new Note(50));
        assertEquals(controller.getLastNote(), new Note(50));
        assertEquals(controller.getAllNote().size(), 1);

        controller.add(new Note(60));
        assertEquals(controller.getLastNote(), new Note(60));
        assertEquals(controller.getAllNote().size(), 2);

        controller.add(new Note(70));
        assertEquals(controller.getLastNote(), new Note(70));
        assertEquals(controller.getAllNote().size(), 3);
    }
    @Test
    public void add_multi_notes_setinterface() {
        bozack.controller.SafeInterval controller 
            = new bozack.controller.SafeInterval();

        NoteSet noteset = new NoteSet();
        noteset.add(new Note(50));
        noteset.add(new Note(60));
        noteset.add(new Note(70));

        controller.add(noteset);

        assertEquals(controller.getLastNote(), new Note(70));
        assertEquals(controller.getAllNote().size(), 3);
    }

    @Test
    public void safe_interval() {
        bozack.controller.SafeInterval controller 
            = new bozack.controller.SafeInterval();

        controller.add(new Note(50));
        assertEquals(controller.getLastNote(), new Note(50));
        assertEquals(controller.getAllNote().size(), 1);

        controller.add(new Note(50));
        assertEquals(controller.getLastNote(), new Note(54));
        assertEquals(controller.getAllNote().size(), 2);

        controller.add(new Note(50));
        assertEquals(controller.getLastNote(), new Note(58));
        assertEquals(controller.getAllNote().size(), 3);

        controller.add(new Note(58));
        assertEquals(controller.getLastNote(), new Note(62));
        assertEquals(controller.getAllNote().size(), 4);
    }
}

