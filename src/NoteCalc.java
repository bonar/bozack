
import bozack.Note;
import bozack.ChromaName;

class NoteCalc {

    public static void main (String[] args) {
        System.out.println("note calc");

        Note C = new Note(ChromaName.C, 5);
        int cNote = C.getNote();
        for (int i = cNote; i < (cNote + 1); i++) {
            System.out.println("------------");
            Note target = new Note(i);
            double des = C.getDessonance(target, 5, 0.88d);
            System.out.println(des + " : " + target);

        }
    }
}

