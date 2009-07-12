
package bozack.ui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;
import java.util.ArrayList;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiUnavailableException;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
import bozack.ui.ConnectionPanel;
import bozack.ui.AboutDialog;
import bozack.ui.LayoutDialog;

public final class BridgeFrame extends JFrame {

    private static final int POS_X  = 60;
    private static final int POS_Y  = 10;
    private static final int WIDTH  = 1200;
    private static final int HEIGHT = 600;
    private static NoteSet noteSet;
    private static int IDX_COMP_CONPANEL = 0;
    private static int IDX_COMP_KEYPANEL = 1;

    private Bridge bridge;
    private MidiDevice deviceIn;
    private Synthesizer deviceOut;
    private CustomReceiver receiver;

    public BridgeFrame() {
        this.setBounds(POS_X, POS_Y, WIDTH, HEIGHT);
        this.appendMenu();
        
        CustomReceiver recv = new DumpRelay();
        this.receiver = recv;
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

        this.paintConnectionPanel();
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
        ButtonGroup groupProp  = new ButtonGroup();
        JMenuItem menuPropSimpe = new JRadioButtonMenuItem(
            "Simple Relay", true);
        menuPropSimpe.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                System.out.println("Simple Relay Clicked");
            }
        });
        JMenuItem menuPropDes = new JRadioButtonMenuItem(
            "Avoid Dessonance");
        groupProp.add(menuPropSimpe);
        groupProp.add(menuPropDes);
        menuProp.add(menuPropSimpe);
        menuProp.add(menuPropDes);

        JMenu menuDevice = new JMenu("MIDI Devices");
        JMenu menuDeviceController = new JMenu("Select Controller");
        JMenu menuDeviceSynth      = new JMenu("Select Synthesizer");

        // scan controller and synthesizer
        ButtonGroup groupController  = new ButtonGroup();
        ButtonGroup groupSynthesizer = new ButtonGroup();
        MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
        ArrayList<MidiDevice> synth = new ArrayList<MidiDevice>();
        for (int i = 0; i < info.length; i++) {
            MidiDevice device;
            try {
                device = MidiSystem.getMidiDevice(info[i]);
            } catch (MidiUnavailableException e) {
                continue;
            }
            JMenuItem menuItem = new JRadioButtonMenuItem(
                info[i].getVendor() + " - " + info[i].getName());

            if (device instanceof Synthesizer) {
                groupSynthesizer.add(menuItem);
                menuDeviceSynth.add(menuItem);
            }
            else {
                groupController.add(menuItem);
                menuDeviceController.add(menuItem);
            }
        }

        menuDevice.add(menuDeviceController);
        menuDevice.add(menuDeviceSynth);
        menuDevice.add("Select Foot Device");

        JMenu menuHelp = new JMenu("Help");
        JMenuItem menuHelpAbout = new JMenuItem("About bozack");
        menuHelpAbout.addMouseListener(new HelpAdapter(this));
        JMenuItem menuHelpKey   = new JMenuItem("Keyboard layout");
        menuHelpKey.addMouseListener(new KeyLayoutAdapter(this));
        menuHelp.add(menuHelpAbout);
        menuHelp.add(menuHelpKey);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuProp);
        menuBar.add(menuDevice);
        menuBar.add(menuHelp);
        this.setJMenuBar(menuBar);
    }

    protected class MenuAdapter extends MouseAdapter {
        protected JFrame frame;
        public MenuAdapter(JFrame f) { this.frame = f; }
    }
    private class HelpAdapter extends MenuAdapter {
        public HelpAdapter(JFrame f) { super(f); }
        public void mouseReleased(MouseEvent e) {
            AboutDialog about = new AboutDialog(this.frame);
            about.setVisible(true);
        }
    }
    private class KeyLayoutAdapter extends MenuAdapter {
        public KeyLayoutAdapter(JFrame f) { super(f); }
        public void mouseReleased(MouseEvent e) {
            LayoutDialog layout = new LayoutDialog(this.frame);
            layout.setVisible(true);
        }
    }

    public void paintConnectionPanel() {
        Container contentPane = this.getContentPane();
        ConnectionPanel cp = new ConnectionPanel(
            this.deviceIn, deviceOut
            , this.receiver.getClass().getSimpleName());
        contentPane.add(cp, IDX_COMP_CONPANEL);
        contentPane.validate();
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

