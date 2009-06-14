
import bozack.Note;

class NoteCalc {

    public static void main (String[] args) {
        System.out.println("note calc");

        Note C = new Note(60);
        for (int i = 60; i < 75; i++) {
            Note target = new Note(i);
            double des = C.getDessonance(target, 20);
            System.out.println(des + " C vs. " + target);

        }
    }
}

