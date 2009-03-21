
import java.io.*;
import java.util.ArrayList;
import javax.sound.midi.*;
import bozack.midi.*;

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

        // bridge.connectEcho();
        RecvDumpRelay recv = new RecvDumpRelay();
        recv.debug = true;
        try {
            bridge.connect(
                (MidiDevice)(devices.get(device_num_in)),
                (MidiDevice)(devices.get(device_num_out)),
                recv);
        } catch (MidiUnavailableException e) {
            System.err.println(e.getMessage());
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

