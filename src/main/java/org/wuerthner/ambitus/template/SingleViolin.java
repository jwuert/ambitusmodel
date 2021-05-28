package org.wuerthner.ambitus.template;

import org.wuerthner.ambitus.model.*;
import org.wuerthner.cwn.api.TimeSignature;
import org.wuerthner.sport.api.ModelElementFactory;

public class SingleViolin implements Template {
    public static final String NAME = "Violin";

    @Override
    public Arrangement apply(int key, int tempo, TimeSignature timeSignature, ModelElementFactory factory) {
        Arrangement arrangement = factory.createElement(Arrangement.TYPE);
        MidiTrack track = factory.createElement(MidiTrack.TYPE);
        track.performTransientSetAttributeValueOperation(MidiTrack.name, NAME);
        track.performTransientSetAttributeValueOperation(MidiTrack.channel, 0);

        TimeSignatureEvent tsEvent = factory.createElement(TimeSignatureEvent.TYPE);
        tsEvent.performTransientSetAttributeValueOperation(TimeSignatureEvent.timeSignature, timeSignature);
        ClefEvent clefEvent = factory.createElement(ClefEvent.TYPE);
        clefEvent.performTransientSetAttributeValueOperation(ClefEvent.clef, 0);
        KeyEvent keyEvent = factory.createElement(KeyEvent.TYPE);
        keyEvent.performTransientSetAttributeValueOperation(KeyEvent.key, key-7);
        TempoEvent tempoEvent = factory.createElement(TempoEvent.TYPE);
        tempoEvent.performTransientSetAttributeValueOperation(TempoEvent.tempo, tempo);
        tempoEvent.performTransientSetAttributeValueOperation(TempoEvent.label, MidiTrack.getTempoLabel(tempo));
        initTrack(track, tsEvent, keyEvent, clefEvent, tempoEvent);
        arrangement.performTransientAddChildOperation(track);
        return arrangement;
    }
}
