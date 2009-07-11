
package bozack.ui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;
import java.util.ArrayList;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiUnavailableException;
import java.awt.Container;

import bozack.Note;
import bozack.NoteSet;
import bozack.NoteHashMap;
import bozack.midi.Bridge;
import bozack.midi.PianoKeyEmulator;
import bozack.midi.receiver.CustomReceiver;
import bozack.midi.receiver.DumpRelay;
import bozack.midi.receiver.Stabilizer;
import bozack.midi.event.FramePainter;
import bozack.ui.KeyPanel;

public final class BridgeFrame extends JFrame 
    implements MenuListener {

    private static final int POS_X  = 60;
    private static final int POS_Y  = 10;
    private static final int WIDTH  = 1200;
    private static final int HEIGHT = 600;
    private static NoteSet noteSet;
    private static int IDX_COMP_KEYPANEL = 0;

    private Bridge bridge;
    private MidiDevice deviceIn;
    private Synthesizer deviceOut;

    public BridgeFrame() {
        this.setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        this.appendMenu();
        
        CustomReceiver recv = new Stabilizer();
        recv.addNoteEventListener(new FramePainter(this));

        this.bridge = new Bridge();
        ArrayList devices = Bridge.getDevices();
        this.deviceIn  = (MidiDevice)(devices.get(0));
        try {
            this.deviceOut = MidiSystem.getSynthesizer();
            this.connectDevice(recv);
        } catch (MidiUnavailableException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }

        this.noteSet = new NoteSet();
        this.paintKeyPanel(this.noteSet);

        // register Software Keyboard emulation
        this.addKeyListener(new PianoKeyEmulator(recv));

        this.setVisible(true);
    }

    private void connectDevice(CustomReceiver recv) 
        throws MidiUnavailableException {
        this.bridge.connect(this.deviceIn, this.deviceOut, recv);
    }

    private void appendMenu() {
        JMenu menuProp = new JMenu("Filter mode");
        menuProp.add("Simple Relay");
        menuProp.add("Avoid Dessonance");

        JMenu menuDevice = new JMenu("MIDI Devices");
        JMenu menuDeviceController = new JMenu("Select Controller");
        JMenu menuDeviceSynth      = new JMenu("Select Synthesizer");

        // scan controller and synthesizer
        MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
        ArrayList<MidiDevice> synth = new ArrayList<MidiDevice>();
        for (int i = 0; i < info.length; i++) {
            MidiDevice device;
            try {
                device = MidiSystem.getMidiDevice(info[i]);
            } catch (MidiUnavailableException e) {
                continue;
            }
            JMenuItem menuItem = new JMenuItem();
            menuItem.setText(info[i].getVendor() 
                + " - " + info[i].getName());

            if (device instanceof Synthesizer) {
                menuDeviceSynth.add(menuItem);
            }
            else {
                menuDeviceController.add(menuItem);
            }
        }
        menuDevice.add(menuDeviceController);
        menuDevice.add(menuDeviceSynth);
        menuDevice.add("Select Foot Device");

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuProp);
        menuBar.add(menuDevice);
        this.setJMenuBar(menuBar);
    }

    public void menuCanceled(MenuEvent e)   { }
    public void menuDeselected(MenuEvent e) { }

    public void menuSelected(MenuEvent e) {

    }
    
    public void paintKeyPanel (NoteSet onNote) {
        this.paintKeyPanel(onNote, new NoteSet(), new NoteHashMap());
    }

    public void paintKeyPanel (
        NoteSet onNote,
        NoteSet assistedNote,
        NoteHashMap pickupRelation)
    {
        Container contentPane = this.getContentPane();
        this.noteSet = onNote;
        KeyPanel kp = new KeyPanel(this.noteSet
            , assistedNote, pickupRelation);
        try {
            contentPane.remove(IDX_COMP_KEYPANEL);
        } catch (Exception e) { }
        contentPane.add(kp, IDX_COMP_KEYPANEL);
        contentPane.validate();
    }
}

