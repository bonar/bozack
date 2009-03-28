
import java.io.*;
import java.util.ArrayList;
import javax.sound.midi.*;
import bozack.midi.*;
import bozack.midi.receiver.*;

class StartFilter {
    public static void main (String[] args) {
        bozack.midi.Bridge bridge = new bozack.midi.Bridge();
        Bridge.dumpDeviceInfo();
        ArrayList devices = Bridge.getDevices();

        // select IN/OUT MIDI devices
        int device_num_in = readIntFromStdin("select input device: ");
        if (0 > device_num_in || device_num_in >= devices.size()) {
            System.err.println("invalid index: " + device_num_in);
            System.exit(0);
        }
        int device_num_out = readIntFromStdin("select output device: ");
        if (0 > device_num_out || device_num_out >= devices.size()) {
            System.err.println("invalid index: " + device_num_out);
            System.exit(0);
        }

        // mode select
        System.out.println("1: simple relay");
        System.out.println("2: dump and relay");
        System.out.println("3: (filter) scale minorize");
        int mode = readIntFromStdin("which mode: ");

        switch (mode) {
            case 1:
                try {
                    bridge.connect(
                        (MidiDevice)(devices.get(device_num_in)),
                        (MidiDevice)(devices.get(device_num_out)));
                } catch (MidiUnavailableException e) {
                    System.err.println(e.getMessage());
                    System.exit(0);
                }
                break;
            case 2:
                DumpRelay recv1 = new DumpRelay();
                recv1.debug = true;
                try {
                    bridge.connect(
                        (MidiDevice)(devices.get(device_num_in)),
                        (MidiDevice)(devices.get(device_num_out)),
                        recv1);
                } catch (MidiUnavailableException e) {
                    System.err.println(e.getMessage());
                    System.exit(0);
                }
                break;
            case 3:
                ScaleMinorize recv2 = new ScaleMinorize();
                recv2.debug = true;
                try {
                    bridge.connect(
                        (MidiDevice)(devices.get(device_num_in)),
                        (MidiDevice)(devices.get(device_num_out)),
                        recv2);
                } catch (MidiUnavailableException e) {
                    System.err.println(e.getMessage());
                    System.exit(0);
                }
                break;
            default:
                System.exit(0);
        }

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

    public static int readIntFromStdin(String label) {
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

