
package bozack.midi;

import bozack.midi.receiver.CustomReceiver;
import java.io.*;
import java.util.ArrayList;
import javax.sound.midi.*;

public class Bridge {
    protected int in_device_index;
    protected int out_device_index;
    protected MidiDevice deviceIn;
    protected MidiDevice deviceOut;

    public Bridge() { }

    public static ArrayList<MidiDevice> getDevices() {
        ArrayList<MidiDevice> devices = new ArrayList<MidiDevice>();

        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            MidiDevice.Info info = infos[i];
            MidiDevice dev = null;
            try {
                dev = MidiSystem.getMidiDevice(info);
                devices.add(dev);
            } catch (SecurityException e) {
                System.err.println(e.getMessage());
            } catch (MidiUnavailableException e) {
                System.err.println(e.getMessage());
            }
        }
        return devices;
    }

    public static void dumpDeviceInfo() {
        ArrayList<MidiDevice> devices = getDevices();

        for (int i = 0; i < devices.size(); i++) {
            MidiDevice device = devices.get(i);
            MidiDevice.Info info = device.getDeviceInfo();
            System.out.println("[" + i + "] devinfo: " + info.toString());
            System.out.println("  name:"        + info.getName());
            System.out.println("  vendor:"      + info.getVendor());
            System.out.println("  version:"     + info.getVersion());
            System.out.println("  description:" + info.getDescription());
            if (device instanceof Synthesizer) {
                System.out.println("  SYNTHESIZER");
            }
            if (device instanceof Sequencer) {
                System.out.println("  SEQUENCER");
            }
            System.out.println("");
        }
    }

    public void connect(MidiDevice device_in, MidiDevice device_out) 
        throws MidiUnavailableException {

        javax.sound.midi.Transmitter trans;
        javax.sound.midi.Receiver recv;

        if (!device_out.isOpen()) {
            device_out.open();
        }
        trans = device_in.getTransmitter();
        recv  = device_out.getReceiver();
        trans.setReceiver(recv);
    }

    public void connect(MidiDevice device_in, MidiDevice device_out
        , CustomReceiver recv) 
        throws MidiUnavailableException {

        if (!device_out.isOpen()) {
            device_out.open();
        }
        // tell receiver where he has to send messages
        recv.setSynth(device_out);

        Transmitter trans;
        trans = device_in.getTransmitter();
        trans.setReceiver(recv);
    }
}

