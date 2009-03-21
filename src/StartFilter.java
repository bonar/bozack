
import java.io.*;
import bozack.midi.*;

class StartFilter {
    public static void main (String[] args) {
        bozack.midi.Bridge bridge = new bozack.midi.Bridge();
        bridge.dump_device_info();

        // select IN/OUT MIDI devices
        int device_in = read_int_from_stdin("select input device: ");
        if (0 > bridge.set_device_in(device_in)) {
            System.err.println("invalid index: " + device_in);
            System.exit(0);
        }
        int device_out = read_int_from_stdin("select output device: ");
        if (0 > bridge.set_device_out(device_out)) {
            System.err.println("invalid index: " + device_out);
            System.exit(0);
        }

        // bridge.echo_connect();
        RecvDumpRelay recv = new RecvDumpRelay();
        bridge.custom_connect(recv);


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

    public static int read_int_from_stdin(String label) {
        BufferedReader inbuf = new BufferedReader(
            new InputStreamReader(System.in));
        System.out.println(label);
        String selected = "";
        try {
            selected = inbuf.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }

        int in_digit = 0;
        try {
            in_digit = Integer.parseInt(selected);
        } catch (NumberFormatException e) {
            System.err.println(selected + " is not digit.");
            System.exit(0);
        }
        return in_digit;
    }
}

