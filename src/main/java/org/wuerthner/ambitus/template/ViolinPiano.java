package org.wuerthner.ambitus.template;

import org.wuerthner.ambitus.model.*;
import org.wuerthner.cwn.api.TimeSignature;
import org.wuerthner.sport.api.ModelElementFactory;

public class ViolinPiano implements Template {
    public static final String NAME = "Violin + Piano";
    public static final String VIOLIN = "Violin";
    public static final String PIANO = "Piano";

    @Override
    public Arrangement apply(int key, int tempo, TimeSignature timeSignature, ModelElementFactory factory) {
        Arrangement arrangement = factory.createElement(Arrangement.TYPE);

        MidiTrack sopran = factory.createElement(MidiTrack.TYPE);
        sopran.performTransientSetAttributeValueOperation(MidiTrack.name, VIOLIN);
        sopran.performTransientSetAttributeValueOperation(MidiTrack.channel, 0);
        init(arrangement, sopran, 0, key, tempo, timeSignature, factory);

        MidiTrack rightHand = factory.createElement(MidiTrack.TYPE);
        rightHand.performTransientSetAttributeValueOperation(MidiTrack.name, PIANO);
        rightHand.performTransientSetAttributeValueOperation(MidiTrack.channel, 1);
        rightHand.performTransientSetAttributeValueOperation(MidiTrack.piano, true);
        init(arrangement, rightHand, 0, key, timeSignature, factory);

        MidiTrack leftHand = factory.createElement(MidiTrack.TYPE);
        leftHand.performTransientSetAttributeValueOperation(MidiTrack.name, PIANO + "2");
        leftHand.performTransientSetAttributeValueOperation(MidiTrack.channel, 2);
        leftHand.performTransientSetAttributeValueOperation(MidiTrack.piano, true);
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
