
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
        int device_num_ctl = readIntFromStdin("select control device: ");
        if (0 > device_num_ctl || device_num_ctl >= devices.size()) {
            System.err.println("invalid index: " + device_num_out);
            System.exit(0);
        }

        // mode select
        System.out.println("1: simple relay");
        System.out.println("2: dump and relay");
        System.out.println("3: (filter) scale minorize");
        System.out.println("4: (filter) safe interval");
        System.out.println("5: (filter) safe dessonance");
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
                try {
                    bridge.connect(
                        (MidiDevice)(devices.get(device_num_in)),
                        (MidiDevice)(devices.get(device_num_out)),
                        new DumpRelay());
                } catch (MidiUnavailableException e) {
                    System.err.println(e.getMessage());
                    System.exit(0);
                }
                break;
            case 3:
                try {
                    bridge.connect(
                        (MidiDevice)(devices.get(device_num_in)),
                        (MidiDevice)(devices.get(device_num_out)),
                        new ScaleMinorize());
                } catch (MidiUnavailableException e) {
                    System.err.println(e.getMessage());
                    System.exit(0);
                }
                break;
            case 4:
                try {
                    bridge.connect(
                        (MidiDevice)(devices.get(device_num_in)),
                        (MidiDevice)(devices.get(device_num_out)),
                        new HarmonySafe());
                } catch (MidiUnavailableException e) {
                    System.err.println(e.getMessage());
                    System.exit(0);
                }
                break;
            case 5:
                try {
                    DessonaceSafe recv = new DessonaceSafe();
                    MidiDevice ctrl = (MidiDevice)(
                        devices.get(device_num_ctl));
                    if (!ctrl.isOpen()) {
                        ctrl.open();
                    }
                    Transmitter trans = ctrl.getTransmitter();
                    trans.setReceiver(recv);

                    bridge.connect(
                        (MidiDevice)(devices.get(device_num_in)),
                        (MidiDevice)(devices.get(device_num_out)),
                        recv);
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

