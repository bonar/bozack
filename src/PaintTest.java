
import bozack.graph.PianoPainter;
import bozack.Note;
import bozack.NoteSet;

class PaintTest {

    public static void main (String[] args) {
        PianoPainter pane = new PianoPainter();
        NoteSet ns = new NoteSet();
        ns.add(new Note(48));
        ns.add(new Note(53));
        pane.paintPiano(ns);
    }
}

