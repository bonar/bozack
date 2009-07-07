
import java.io.InputStreamReader;
import java.io.IOException;
import bozack.ui.BridgeFrame;
import bozack.NoteSet;

class StartBozack {
    public static void main (String[] arg) {

        BridgeFrame frame = new BridgeFrame();

        // command line operation
        InputStreamReader cmdin = new InputStreamReader(System.in);
        try {
            while (true) {
                int input = cmdin.read();
                System.out.print("[" + input + "]");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}


