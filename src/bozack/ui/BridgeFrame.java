
package bozack.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JProgressBar;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ButtonGroup;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import java.util.ArrayList;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.sound.midi.Transmitter;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Sequence;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import bozack.Note;
import bozack.NoteSet;
import bozack.NoteHashMap;
import bozack.DissonanceMap;
import bozack.ChordProgression;
import bozack.midi.Bridge;
import bozack.midi.PianoKeyEmulator;
import bozack.midi.receiver.CustomReceiver;
import bozack.midi.receiver.DumpRelay;
import bozack.midi.receiver.Stabilizer;
import bozack.midi.receiver.SequenceReceiver;
import bozack.midi.event.FramePainter;
import bozack.midi.event.PedalListener;
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
    private static int IDX_COMP_CHPANEL  = 2;

    private Bridge bridge;
    private MidiDevice deviceIn;
    private Synthesizer deviceOut;
    private Sequencer sequencer;
    private CustomReceiver receiver;
    private PianoKeyEmulator pianoKey;

    private DissonanceMap dissonance;
    private static final int DISSONANCE_MAP_MIN = 1;
    private static final int DISSONANCE_MAP_MAX = 120;

    private ChordProgression chordProg;
    
    private static final String IMAGE_PATH = "splash.png";

    public BridgeFrame() {
        // create splash screen and make large dissonance mapping
        JDialog splash = new JDialog();
        splash.setBounds(300, 200, 10, 10);

        URL imageUrl = getClass().getClassLoader()
            .getResource(IMAGE_PATH);
        ImageIcon icon = new ImageIcon(imageUrl);
        JLabel imageLabel = new JLabel(icon);
        splash.setLayout(new BorderLayout());
        splash.getContentPane().add(imageLabel, BorderLayout.NORTH);

        JProgressBar prog = new JProgressBar(0, 5);
        prog.setStringPainted(true);
        splash.getContentPane().add(prog, BorderLayout.SOUTH);
        
        splash.pack();
        splash.setResizable(false);
        splash.setVisible(true);

        prog.setValue(0);
        prog.setString("creating DissonanceMap");
        // pre-calc all the dissonance patterns
        this.dissonance = new DissonanceMap();
        for (int a = DISSONANCE_MAP_MIN; a < DISSONANCE_MAP_MAX; a++) {
            for (int b = DISSONANCE_MAP_MIN
                ; b < DISSONANCE_MAP_MAX; b++) {
                this.dissonance.put(a, b);
            }
        }
        prog.setValue(1);
        prog.setString("Registering event listeners");

        this.setBounds(POS_X, POS_Y, WIDTH, HEIGHT);

        CustomReceiver recv = new DumpRelay();
        this.receiver = recv;
        this.receiver.setDissonanceMap(this.dissonance);
        recv.addNoteEventListener(new FramePainter(this));
        recv.addNoteEventListener(new PedalListener(this));

        prog.setValue(2);
        prog.setString("Investigating MIDI Devices");

        this.bridge = new Bridge();
        ArrayList devices = Bridge.getDevices();
        
        prog.setValue(3);
        prog.setString("Connecting MIDI Devices");
        try {
            this.sequencer = MidiSystem.getSequencer();
            this.sequencer.open();

            this.deviceIn  = MidiSystem.getSequencer();
            this.deviceOut = MidiSystem.getSynthesizer();

            this.connectDevice();
        } catch (MidiUnavailableException e) {
            System.err.println(e.getMessage());
            System.exit(0);
        }
        this.appendMenu();

        prog.setValue(5);
        prog.setString("Initializing software piano");

        this.chordProg = new ChordProgression();
        this.receiver.setChord(this.chordProg.getChord());
        this.paintConnectionPanel();
        this.noteSet = new NoteSet();
        this.paintKeyPanel(this.noteSet);

        // register Software Keyboard emulation
        this.pianoKey = new PianoKeyEmulator(this, recv);
        this.addKeyListener(this.pianoKey);

        splash.setVisible(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void chordNext(int index) {
        this.chordProg.next();
        this.chordProg.variate(index);
        this.receiver.setChord(this.chordProg.getChord());
        this.paintConnectionPanel();
    }
    public void chordVariate() {
        this.chordProg.variate();
        this.receiver.setChord(this.chordProg.getChord());
        this.paintConnectionPanel();
    }

    public void setDeviceIn(MidiDevice device) {
        this.deviceIn.close();
        this.deviceIn = device;
    }

    public void setDeviceOut(Synthesizer device) {
        this.deviceOut.close();
        this.deviceOut = device;
    }

    public void setReciever(CustomReceiver recv) {
        this.deviceIn.close();
        this.deviceOut.close();

        this.receiver.setDissonanceMap(this.dissonance);
        recv.addNoteEventListener(new FramePainter(this));
        recv.addNoteEventListener(new PedalListener(this));

        // replace key emulator instance
        this.removeKeyListener(this.pianoKey);
        this.pianoKey = new PianoKeyEmulator(this, recv);
        this.addKeyListener(this.pianoKey);

        this.receiver = recv;
        this.receiver.setChord(this.chordProg.getChord());
        this.receiver.setDissonanceMap(this.dissonance);
    }

    private void connectDevice()
        throws MidiUnavailableException {

        this.bridge.connect(this.deviceIn, this.deviceOut
            , this.receiver);
        this.paintConnectionPanel();

        System.out.println("Device reconnected");
        System.out.println(" - IN: " 
            + this.deviceIn.getDeviceInfo().getName());
        System.out.println(" - OUT: " 
            + this.deviceOut.getDeviceInfo().getName());
        System.out.println(" - RECV: " 
            + this.receiver.getClass().getName());
    }

    private void midiBeep(int note) {
        try {
            ShortMessage sm_on = new ShortMessage();
            sm_on.setMessage(ShortMessage.NOTE_ON, note, 100);
            ShortMessage sm_off = new ShortMessage();
            sm_off.setMessage(ShortMessage.NOTE_OFF, note, 100);

            this.receiver.handleShortMessage(sm_on, 0);
            this.receiver.handleShortMessage(sm_off, 0);
        } catch (InvalidMidiDataException ime) {
            System.out.println("beep failed" + ime.getMessage());
        }
    }

    private class MidiFileFilter extends FileFilter {
        public boolean accept(File f) {
            if (f.isDirectory()) { return true; }
            if (f.canRead() && (
                f.getName().toLowerCase().indexOf(".mid") > 0 ||
                f.getName().toLowerCase().indexOf(".smf") > 0 
            )) {
                return true;
            }
            return false;
        }
        public String getDescription() {
            return "Standard MIDI Files";
        }
    }

    private void appendMenu() {
        JMenu menuProp = new JMenu("Filter mode");
        ButtonGroup groupProp  = new ButtonGroup();
        JMenuItem menuPropSimple = new JRadioButtonMenuItem(
            "DumpRelay", true);
        menuPropSimple.addMouseListener(new ReconnectReceiverAdapter(
            this, new DumpRelay()));
        JMenuItem menuPropDes = new JRadioButtonMenuItem(
            "Stabilizer");
        menuPropDes.addMouseListener(new ReconnectReceiverAdapter(
            this, new Stabilizer(false)));
        JMenuItem menuPropDesCh = new JRadioButtonMenuItem(
            "Stabilizer (with Chord)");
        menuPropDesCh.addMouseListener(new ReconnectReceiverAdapter(
            this, new Stabilizer(true)));
        groupProp.add(menuPropSimple);
        groupProp.add(menuPropDes);
        groupProp.add(menuPropDesCh);
        menuProp.add(menuPropSimple);
        menuProp.add(menuPropDes);
        menuProp.add(menuPropDesCh);

        JMenu menuFile = new JMenu("File");
        JMenuItem menuFileLoad = new JMenuItem("Load and Play MIDI FIle");
        menuFileLoad.addMouseListener(new LoadMidiFileAdapter(this));
        JMenuItem menuFileStop = new JMenuItem("Stop Playing");
        menuFileStop.addMouseListener(new StopMidiFileAdapter(this));
        menuFile.add(menuFileLoad);
        menuFile.add(menuFileStop);

        JMenu menuDevice = new JMenu("MIDI Devices");
        JMenuItem menuDeviceController = new JMenu("Select Controller");
        JMenuItem menuDeviceSynth      = new JMenu("Select Synthesizer");
        JMenu menuDeviceFoot = new JMenu("Select Foot Control Device");

        // scan controller and synthesizer
        ButtonGroup groupController  = new ButtonGroup();
        ButtonGroup groupSynthesizer = new ButtonGroup();
        MidiDevice.Info infoDeviceIn  = this.deviceIn.getDeviceInfo();
        MidiDevice.Info infoDeviceOut = this.deviceOut.getDeviceInfo();
        MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
        ArrayList<MidiDevice> synth = new ArrayList<MidiDevice>();
        for (int i = 0; i < info.length; i++) {
            MidiDevice device;
            try {
                device = MidiSystem.getMidiDevice(info[i]);
            } catch (MidiUnavailableException e) {
                continue;
            }
            boolean checked = false;
            if (info[i].toString().equals(infoDeviceIn.toString())
                || info[i].toString().equals(infoDeviceOut.toString())) {
                checked = true;
            }
            JMenuItem menuItem = new JRadioButtonMenuItem(
                info[i].getName() + " / " + info[i].getVendor()
                , checked);

            if (device instanceof Synthesizer) {
                menuItem.addMouseListener(
                    new ReconnectDeviceOutAdapter(this, device));
                groupSynthesizer.add(menuItem);
                menuDeviceSynth.add(menuItem);
            }
            else {
                menuItem.addMouseListener(
                    new ReconnectDeviceInAdapter(this, device));
                groupController.add(menuItem);
                menuDeviceController.add(menuItem);

                // add Foot Controll option
                JMenuItem menuItemFoot = new JRadioButtonMenuItem(
                    info[i].getName() + " / " + info[i].getVendor());
                menuItemFoot.addMouseListener(
                    new DeviceFootAdapter(this, this.receiver, device));
                menuDeviceFoot.add(menuItemFoot);
            }
        }

        menuDevice.add(menuDeviceController);
        menuDevice.add(menuDeviceSynth);
        menuDevice.add(menuDeviceFoot);

        JMenu menuHelp = new JMenu("Help");
        JMenuItem menuHelpAbout = new JMenuItem("About bozack");
        menuHelpAbout.addMouseListener(new HelpAdapter(this));
        JMenuItem menuHelpKey   = new JMenuItem("Keyboard layout");
        menuHelpKey.addMouseListener(new KeyLayoutAdapter(this));
        menuHelp.add(menuHelpAbout);
        menuHelp.add(menuHelpKey);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuFile);
        menuBar.add(menuProp);
        menuBar.add(menuDevice);
        menuBar.add(menuHelp);
        this.setJMenuBar(menuBar);
    }

    // Mouse Event Listeners
    protected class MenuAdapter extends MouseAdapter {
        protected BridgeFrame frame;
        public MenuAdapter(BridgeFrame f) { this.frame = f; }
    }
    private class HelpAdapter extends MenuAdapter {
        public HelpAdapter(BridgeFrame f) { super(f); }
        public void mouseReleased(MouseEvent e) {
            AboutDialog about = new AboutDialog(this.frame);
            about.setVisible(true);
        }
    }
    private class KeyLayoutAdapter extends MenuAdapter {
        public KeyLayoutAdapter(BridgeFrame f) { super(f); }
        public void mouseReleased(MouseEvent e) {
            LayoutDialog layout = new LayoutDialog(this.frame);
            layout.setVisible(true);
        }
    }
    protected class ReconnectDeviceAdapter extends MouseAdapter {
        protected BridgeFrame frame;
        protected MidiDevice device;
        public ReconnectDeviceAdapter(BridgeFrame f, MidiDevice d) {
            this.frame  = f;
            this.device = d;
        }
    }
    private class ReconnectDeviceInAdapter
        extends ReconnectDeviceAdapter {
        ReconnectDeviceInAdapter(BridgeFrame f, MidiDevice d) {
            super(f, d);
        }
        public void mouseReleased(MouseEvent e) {
            System.out.println("ReconnectDeviceInAdapter called");
            try {
                this.frame.midiBeep(90);
                this.frame.setDeviceIn(this.device);
                this.frame.connectDevice();
                this.frame.midiBeep(95);
            } catch (MidiUnavailableException mue) {
                JOptionPane.showMessageDialog(this.frame
                   , "Device not available: " + mue.getMessage()
                   , "Device Error"
                   , JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    private class ReconnectDeviceOutAdapter
        extends ReconnectDeviceAdapter {
        ReconnectDeviceOutAdapter(BridgeFrame f, MidiDevice d) {
            super(f, d);
        }
        public void mouseReleased(MouseEvent e) {
            if (false == this.device instanceof Synthesizer) {
                JOptionPane.showMessageDialog(this.frame
                   , "This device is not a Synthesizer"
                   , "Device Error"
                   , JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                this.frame.midiBeep(90);
                this.frame.setDeviceOut((Synthesizer)this.device);
                this.frame.connectDevice();
                this.frame.midiBeep(95);
            } catch (MidiUnavailableException mue) {
                JOptionPane.showMessageDialog(this.frame
                   , "Device not available: " + mue.getMessage()
                   , "Device Error"
                   , JOptionPane.WARNING_MESSAGE);
            }
        }
    }
    private class DeviceFootAdapter
        extends MouseAdapter {
        private BridgeFrame frame;
        private CustomReceiver receiver;
        private MidiDevice device;
        DeviceFootAdapter(BridgeFrame f, CustomReceiver r, MidiDevice d) {
            this.frame    = f;
            this.receiver = r;
            this.device   = d;
        }
        public void mouseReleased(MouseEvent e) {
            try {
                this.frame.midiBeep(90);
                Transmitter trans = this.device.getTransmitter();
                trans.setReceiver(this.receiver);
                this.frame.midiBeep(95);
            } catch (MidiUnavailableException mue) {
                JOptionPane.showMessageDialog(this.frame
                   , "Device not available: " + mue.getMessage()
                   , "Device Error"
                   , JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private class StopMidiFileAdapter extends MouseAdapter {
        private BridgeFrame frame;
        StopMidiFileAdapter(BridgeFrame f) {
            this.frame = f;
        }
        public void mouseReleased(MouseEvent e) {
            this.frame.sequencer.stop();
            this.frame.clearKeyPanel();
        }
    }

    private class LoadMidiFileAdapter extends MouseAdapter {
        private BridgeFrame frame;
        LoadMidiFileAdapter(BridgeFrame f) {
            this.frame = f;
        }
        public void mouseReleased(MouseEvent e) {
            JFileChooser chooser = new JFileChooser();
            MidiFileFilter filter = new MidiFileFilter();
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(this.frame);
            if(returnVal != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File selected = chooser.getSelectedFile();
            try {
                Sequence seq = MidiSystem.getSequence(selected);
                this.frame.sequencer.setSequence(seq);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this.frame
                   , "Cannot Read File: "
                   + ioe.getMessage(), "File Permission Error"
                   , JOptionPane.WARNING_MESSAGE);
                return;
            } catch (InvalidMidiDataException ime) {
                JOptionPane.showMessageDialog(this.frame
                   , "Invalid MIDI data (broken?): "
                   + ime.getMessage(), "MIDI Sequence Error"
                   , JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Transmitter tran = this.frame.sequencer.getTransmitter();
                tran.setReceiver(new SequenceReceiver(
                    this.frame.receiver, this.frame));
            } catch (MidiUnavailableException mue) {
                JOptionPane.showMessageDialog(this.frame
                   , "Cannot get transmitter from sequencer: "
                   + mue.getMessage(), "Transmitter Error"
                   , JOptionPane.WARNING_MESSAGE);
                return;
            }

            this.frame.sequencer.start();
        }
    }

    protected class ReconnectReceiverAdapter extends MouseAdapter {
        private BridgeFrame frame;
        private CustomReceiver receiver;
        public ReconnectReceiverAdapter(BridgeFrame f, CustomReceiver r) {
            this.frame = f;
            this.receiver = r;
        }
        public void mouseReleased(MouseEvent e) {
            try {
                this.frame.setReciever(this.receiver);
                this.frame.connectDevice();
                this.frame.midiBeep(95);
            } catch (MidiUnavailableException mue) {
                JOptionPane.showMessageDialog(this.frame
                   , "This receiver doesn't match selected devices: "
                       + mue.getMessage()
                   , "Receiver Connect Error"
                   , JOptionPane.WARNING_MESSAGE);
            }

        }
    }

    public void paintConnectionPanel() {
        Container contentPane = this.getContentPane();
        ConnectionPanel cp = new ConnectionPanel(
            this.deviceIn, deviceOut
            , this.receiver.getClass().getSimpleName()
            , this.chordProg);
        try {
            contentPane.remove(IDX_COMP_CONPANEL);
        } catch (Exception e) { }

        contentPane.add(cp, IDX_COMP_CONPANEL);
        contentPane.validate();
    }

    public void clearKeyPanel () {
        this.paintKeyPanel(new NoteSet());
    }
    public void paintKeyPanel (NoteSet onNote) {
        this.paintKeyPanel(onNote, new NoteSet()
            , new NoteHashMap(), new NoteSet());
    }
    public void paintKeyPanel (
        NoteSet onNote,
        NoteSet assistedNote,
        NoteHashMap pickupRelation,
        NoteSet sequecerNote
    ) {
        Container contentPane = this.getContentPane();
        this.noteSet = onNote;
        KeyPanel kp = new KeyPanel(this.noteSet
            , assistedNote, pickupRelation, sequecerNote);
        if (null != this.dissonance) {
            kp.setDissonanceMap(this.dissonance);
        }

        try {
            contentPane.remove(IDX_COMP_KEYPANEL);
        } catch (Exception e) { }
        contentPane.add(kp, IDX_COMP_KEYPANEL);
        contentPane.validate();
    }
}

