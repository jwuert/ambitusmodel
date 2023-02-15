package org.wuerthner.ambitus.tool;

import org.wuerthner.ambitus.attribute.TimeSignatureAttribute;
import org.wuerthner.ambitus.model.*;
import org.wuerthner.cwn.api.TimeSignature;
import org.wuerthner.cwn.midi.MetaMessageType;
import org.wuerthner.cwn.timesignature.SimpleTimeSignature;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;

import javax.sound.midi.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class MidiImportService {
    private List<LyricEvent> lyricsList;

    public MidiImportService() {
    }

    public Arrangement readMidiFile(InputStream inputStream, ModelElementFactory factory) {
        Arrangement document = factory.createElement(Arrangement.TYPE);
        Sequence sequence = readMidi(inputStream);
        Track[] trackArray = sequence.getTracks();
        int channel = 0;
        for (Track track : trackArray) {
            if (hasMidiContent(track)) {
                MidiTrack midiTrack = factory.createElement(MidiTrack.TYPE);
                ModelElement timeSignatureEvent = factory.createElement(TimeSignatureEvent.TYPE);
                timeSignatureEvent.performTransientSetAttributeValueOperation(TimeSignatureEvent.timeSignature, document.getTimeSignature());
                midiTrack.performTransientAddChildOperation(timeSignatureEvent);
                ModelElement keyEvent = factory.createElement(KeyEvent.TYPE);
                midiTrack.performTransientAddChildOperation(keyEvent);
                ModelElement clefEvent = factory.createElement(ClefEvent.TYPE);
                midiTrack.performTransientAddChildOperation(clefEvent);
                if (channel==0) {
                    ModelElement tempoEvent = factory.createElement(TempoEvent.TYPE);
                    tempoEvent.performTransientSetAttributeValueOperation(TempoEvent.tempo, 100);
                    midiTrack.performTransientAddChildOperation(tempoEvent);
                }
                midiTrack.performTransientSetAttributeValueOperation(MidiTrack.channel, channel++);
                document.performTransientAddChildOperation(midiTrack);
                int size = track.size();
                lyricsList = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    MidiEvent midiEvent = track.get(i);
                    handleMidiEvent(track, midiTrack, i, midiEvent, factory);
                }
                addLyricsToTrack(midiTrack);
            }
        }
        return document;
    }

    private void addLyricsToTrack(MidiTrack midiTrack) {
        Iterator<LyricEvent> lyricsIterator = lyricsList.iterator();
        // we assume that there may be notes without lyrics, but no lyrics without notes!
        if (lyricsIterator.hasNext()) {
            LyricEvent lyrics = lyricsIterator.next();
            for (NoteEvent note : midiTrack.getList(NoteEvent.class)) {
                long noteStart = note.getPosition();
                while (lyricsIterator.hasNext() && lyrics.position < noteStart) {
                    lyrics = lyricsIterator.next();
                }
                if (!lyricsIterator.hasNext()) {
                    break;
                } else {
                    note.performTransientSetAttributeValueOperation(NoteEvent.lyrics, lyrics.lyrics);
                }
            }
        }
    }

    public void handleMidiEvent(Track track, MidiTrack midiTrack, int i, MidiEvent midiEvent, ModelElementFactory factory) {
        long start = midiEvent.getTick();
        MidiMessage midiMessage = midiEvent.getMessage();
        if (midiMessage instanceof MetaMessage) {
            handleMetaMessage(midiTrack, start, (MetaMessage) midiMessage);
        }
        // Add NoteMessage if this message describes a NoteOn event:
        // Since a NOTE_ON command with a velocity of zero functions
        // as a NoteOff event, this has to be taken care of!!!
        else if (midiMessage instanceof ShortMessage && ((ShortMessage) midiMessage).getCommand() == ShortMessage.NOTE_ON && ((ShortMessage) midiMessage).getData2() != 0) {
            long end = retrieveNoteOff(track, i, midiMessage.getMessage());
            int enharmonicShift = 0; // TODO: guess enharmonic_shift
            // nmg = new NoteMessage(smg.getData1(), smg.getData2(),
            // smg.getChannel(), tick_off-tick, enh, 0);
            // track.add(new MidiEvent(nmg, tick));

            // int channel = midiMessage.getMessage()[0] & 7;
            int pitch = midiMessage.getMessage()[1];
            int velocity = midiMessage.getMessage()[2];
            // if (masterInformationProvider != null) {
            // start = masterInformationProvider.snapToResolution(start);
            // end = masterInformationProvider.snapToResolution(end);
            // }
            NoteEvent noteEvent = factory.createElement(NoteEvent.TYPE);
            noteEvent.performTransientSetAttributeValueOperation(NoteEvent.position, start);
            noteEvent.performTransientSetAttributeValueOperation(NoteEvent.duration, end - start);
            noteEvent.performTransientSetAttributeValueOperation(NoteEvent.pitch, pitch);
            noteEvent.performTransientSetAttributeValueOperation(NoteEvent.shift, enharmonicShift);
            noteEvent.performTransientSetAttributeValueOperation(NoteEvent.velocity, velocity);
            midiTrack.performTransientAddChildOperation(noteEvent);
        } else if (midiMessage instanceof ShortMessage) {
            ShortMessage smg = (ShortMessage) midiMessage;
            if ((smg.getCommand() == ShortMessage.NOTE_OFF) || ((smg.getCommand() == ShortMessage.NOTE_ON) && (smg.getData2() == 0))) {
                // TODO:
                // midiTrack.getHandler().addChildWithoutNotification(new
                // MidiEvent(smg, start));
            } else {
                // TODO:
                // sgm = new SingleMessage((ShortMessage)msg);
                // track.add(new MidiEvent(sgm, tick));
            }
        } else {
            // track.add(midiEvent);
        }
    }

    private void handleMetaMessage(MidiTrack midiTrack, long start, MetaMessage metaMessage) {
        int type = metaMessage.getType();
        switch (type) {
            case MetaMessageType.TEXT_EVENT:
                System.out.println("text: " + makeString(metaMessage));
                break;
            case MetaMessageType.COPYRIGHT_EVENT:
                System.out.println("copyright: " + makeString(metaMessage));
                break;
            case MetaMessageType.TRACKNAME_EVENT:
                String name = makeString(metaMessage);
                midiTrack.performTransientSetAttributeValueOperation(MidiTrack.name, name);
                System.out.println("name: " + name);
                break;
            case MetaMessageType.INSTRUMENT_EVENT:
                System.out.println("instrument: " + makeString(metaMessage));
                break;
            case MetaMessageType.LYRIC_EVENT:
                System.out.println("lyric: " + makeString(metaMessage));
                String lyrics = makeString(metaMessage);
                lyricsList.add(new LyricEvent(start, lyrics));
                break;
            case MetaMessageType.TEMPO_EVENT:
                float tempo = getTempo(metaMessage);
                System.out.println("tempo: " + tempo);
                break;
            case MetaMessageType.METER_EVENT:
                byte[] data = metaMessage.getData();
                String metricString = (data[0] & 0xFF) + "/" + (1 << (data[1] & 0xFF));
                TimeSignature meter = new SimpleTimeSignature(metricString);
                // trackHandler.getParent().getHandler().setAttributeValueWithoutNotification(Arrangement.timeSignature, meter);
                System.out.println("meter: " + meter);
                break;
            case MetaMessageType.KEYSIG_EVENT:
                System.out.println("key: " + getKey(metaMessage));
                System.out.println("gender: " + getGender(metaMessage));
                break;
            default:
                break;
        }
    }

    private static String makeString(MetaMessage metaMessage) {
        byte[] msg = metaMessage.getData();
        return new String(msg);
    }

    /**
     * Returns the key signature (-7...7) in case of a key signature event and 0 otherwise
     **/
    private static int getKey(MetaMessage metaMessage) {
        byte[] msg = metaMessage.getData();
        return (msg[0] & 0xFF);
    }

    /**
     * Returns the gender (0: major, 1: minor) in case of a key signature event and 0 otherwise
     **/
    private static int getGender(MetaMessage metaMessage) {
        byte[] msg = metaMessage.getData();
        return (msg[1] & 0xFF);
    }

    private static float getTempo(MetaMessage metaMessage) {
        byte[] msg = metaMessage.getData();
        // tempo in microseconds per beat
        int nTempo = ((msg[0] & 0xFF) << 16) | ((msg[1] & 0xFF) << 8) | (msg[2] & 0xFF);
        float bpm = convertTempo(nTempo);
        // truncate it to 2 digits after point
        bpm = (float) (Math.round(nTempo * 100) / 100.0f);
        return bpm;
    }

    private static float convertTempo(float value) {
        if (value <= 0) {
            value = 0.1f;
        }
        return 60000000000.0f / value;
    }

    private Sequence readMidi(InputStream stream) {
        Sequence sequence = null;
        try {
            sequence = MidiSystem.getSequence(stream);
        } catch (InvalidMidiDataException | IOException e) {
            e.printStackTrace();
        }
        return sequence;
    }

    private static long retrieveNoteOff(Track track, int n, byte[] arr) {
        arr[0] = (byte) (arr[0] & 0x0f + ShortMessage.NOTE_OFF);
        long pos = -1;
        int sz = track.size();
        MidiEvent me;
        byte[] foo;
        for (int i = n + 1; i < sz && pos == -1; i++) {
            me = track.get(i);
            foo = me.getMessage().getMessage();
            if (foo.length > 1 && arr.length > 1 && (arr[0] & 0x0f) == (foo[0] & 0x0f) && arr[1] == foo[1])
                pos = me.getTick();
        }
        if (pos == -1)
            pos = 0;
        return pos;
    }

    private boolean hasMidiContent(Track track) {
        boolean midiContent = false;
        int size = track.size();
        for (int i = 0; i < size; i++) {
            MidiEvent midiEvent = track.get(i);
            MidiMessage midiMessage = midiEvent.getMessage();
            if (midiMessage instanceof ShortMessage) {
                midiContent = true;
                break;
            }
        }
        return midiContent;
    }

    private class LyricEvent {
        public final long position;
        public final String lyrics;

        public LyricEvent(long position, String lyrics) {
            this.position = position;
            this.lyrics = lyrics;
        }
    }
}
