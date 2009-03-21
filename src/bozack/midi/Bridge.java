
package bozack.midi;

import java.io.*;
import java.util.ArrayList;
import javax.sound.midi.*;

public class Bridge {
    private ArrayList<MidiDevice> devices = new ArrayList<MidiDevice>();
    private int in_device_index;
    private int out_device_index;

    public Bridge() {
        this.scan_device_info();
    }

    protected int scan_device_info() {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        System.out.println("" + infos.length + " devices found.");

        for (int i = 0; i < infos.length; i++) {
            MidiDevice.Info info = infos[i];
            MidiDevice dev = null;
            try {
                dev = MidiSystem.getMidiDevice(info);
                this.devices.add(dev);
            } catch (SecurityException e) {
                System.err.println(e.getMessage());
            } catch (MidiUnavailableException e) {
                System.err.println(e.getMessage());
            }
        }
        return this.devices.size();
    }

    public boolean echo_connect() {
        MidiDevice in_device  = this.devices.get(this.in_device_index);
        MidiDevice out_device = this.devices.get(this.out_device_index);

        javax.sound.midi.Transmitter trans;
        javax.sound.midi.Receiver recv;
        try {
            if (!out_device.isOpen()) {
                out_device.open();
            }
            trans = in_device.getTransmitter();
            recv  = out_device.getReceiver();
            trans.setReceiver(recv);
        } catch(MidiUnavailableException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean custom_connect(CustomReceiver recv) {
        MidiDevice in_device  = this.devices.get(this.in_device_index);
        MidiDevice out_device = this.devices.get(this.out_device_index);

        recv.set_out_device(out_device);
        javax.sound.midi.Transmitter trans;
        try {
            trans = in_device.getTransmitter();
        } catch (MidiUnavailableException e) {
            System.err.println(e.getMessage());
            return false;
        }
        trans.setReceiver(recv);
        return true;
    }

    public void dump_device_info() {
        if (this.devices.size() == 0) {
            return;
        }
        for (int i = 0; i < this.devices.size(); i++) {
            MidiDevice device = this.devices.get(i);
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

    public int set_device_in(int index) {
        if (index < 0 || this.devices.size() <= index) {
            return -1;
        }
        this.in_device_index = index;
        return index;
    }
    public int set_device_out(int index) {
        if (index < 0 || this.devices.size() <= index) {
            return -1;
        }
        this.out_device_index = index;
        return index;
    }


}

