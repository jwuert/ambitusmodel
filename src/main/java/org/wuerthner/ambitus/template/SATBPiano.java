package org.wuerthner.ambitus.template;

import org.wuerthner.ambitus.model.*;
import org.wuerthner.cwn.api.TimeSignature;
import org.wuerthner.sport.api.ModelElementFactory;

public class SATBPiano implements Template {
    public static final String SOPRAN = "Sopran";
    public static final String ALT = "Alt";
    public static final String TENOR = "Tenor";
    public static final String BASS = "Bass";
    public static final String RIGHT = "Right";
    public static final String LEFT = "Left";

    @Override
    public Arrangement apply(int key, int tempo, TimeSignature timeSignature, ModelElementFactory factory) {
        Arrangement arrangement = factory.createElement(Arrangement.TYPE);

        MidiTrack sopran = factory.createElement(MidiTrack.TYPE);
        sopran.performTransientSetAttributeValueOperation(MidiTrack.name, SOPRAN);
        sopran.performTransientSetAttributeValueOperation(MidiTrack.channel, 0);
        init(arrangement, sopran, 0, key, tempo, timeSignature, factory);

        MidiTrack alt = factory.createElement(MidiTrack.TYPE);
        alt.performTransientSetAttributeValueOperation(MidiTrack.name, ALT);
        alt.performTransientSetAttributeValueOperation(MidiTrack.channel, 1);
        init(arrangement, alt, 0, key, timeSignature, factory);

        MidiTrack tenor = factory.createElement(MidiTrack.TYPE);
        tenor.performTransientSetAttributeValueOperation(MidiTrack.name, TENOR);
        tenor.performTransientSetAttributeValueOperation(MidiTrack.channel, 2);
        init(arrangement, tenor, 4, key, timeSignature, factory);

        MidiTrack bass = factory.createElement(MidiTrack.TYPE);
        bass.performTransientSetAttributeValueOperation(MidiTrack.name, BASS);
        bass.performTransientSetAttributeValueOperation(MidiTrack.channel, 3);
        init(arrangement, bass, 1, key, timeSignature, factory);

        MidiTrack rightHand = factory.createElement(MidiTrack.TYPE);
        rightHand.performTransientSetAttributeValueOperation(MidiTrack.name, RIGHT);
        rightHand.performTransientSetAttributeValueOperation(MidiTrack.channel, 4);
        init(arrangement, rightHand, 0, key, timeSignature, factory);

        MidiTrack leftHand = factory.createElement(MidiTrack.TYPE);
        leftHand.performTransientSetAttributeValueOperation(MidiTrack.name, LEFT);
        leftHand.performTransientSetAttributeValueOperation(MidiTrack.channel, 5);
        init(arrangement, leftHand, 1, key, timeSignature, factory);

        return arrangement;
    }

    private void init(Arrangement arrangement, MidiTrack track, int clef, int key, TimeSignature timeSignature, ModelElementFactory factory) {
        init(arrangement, track, clef, key, null, timeSignature, factory);
    }

    private void init(Arrangement arrangement, MidiTrack track, int clef, int key, Integer tempo, TimeSignature timeSignature, ModelElementFactory factory) {
        TimeSignatureEvent tsEvent = factory.createElement(TimeSignatureEvent.TYPE);
        tsEvent.performTransientSetAttributeValueOperation(TimeSignatureEvent.timeSignature, timeSignature);
        KeyEvent keyEvent = factory.createElement(KeyEvent.TYPE);
        keyEvent.performTransientSetAttributeValueOperation(KeyEvent.key, key-7);
        ClefEvent clefEvent = factory.createElement(ClefEvent.TYPE);
        clefEvent.performTransientSetAttributeValueOperation(ClefEvent.clef, clef);
        if (tempo!=null) {
            TempoEvent tempoEvent = factory.createElement(TempoEvent.TYPE);
            tempoEvent.performTransientSetAttributeValueOperation(TempoEvent.tempo, tempo);
            tempoEvent.performTransientSetAttributeValueOperation(TempoEvent.label, MidiTrack.getTempoLabel(tempo));
            initTrack(track, tsEvent, keyEvent, clefEvent, tempoEvent);
        } else {
            initTrack(track, tsEvent, keyEvent, clefEvent);
        }
        arrangement.performTransientAddChildOperation(track);
    }
}
