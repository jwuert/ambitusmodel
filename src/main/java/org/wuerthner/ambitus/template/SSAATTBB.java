package org.wuerthner.ambitus.template;

import org.wuerthner.ambitus.model.*;
import org.wuerthner.cwn.api.TimeSignature;
import org.wuerthner.sport.api.ModelElementFactory;

public class SSAATTBB implements Template {
    public static final String SOPRAN1 = "Sopran1";
    public static final String SOPRAN2 = "Sopran2";
    public static final String ALT1 = "Alt1";
    public static final String ALT2 = "Alt2";
    public static final String TENOR1 = "Tenor1";
    public static final String TENOR2 = "Tenor2";
    public static final String BASS1 = "Bass1";
    public static final String BASS2 = "Bass2";

    @Override
    public Arrangement apply(int key, int tempo, TimeSignature timeSignature, ModelElementFactory factory) {
        Arrangement arrangement = factory.createElement(Arrangement.TYPE);

        MidiTrack sopran1 = factory.createElement(MidiTrack.TYPE);
        sopran1.performTransientSetAttributeValueOperation(MidiTrack.name, SOPRAN1);
        sopran1.performTransientSetAttributeValueOperation(MidiTrack.channel, 0);
        init(arrangement, sopran1, 0, key, tempo, timeSignature, factory);

        MidiTrack sopran2 = factory.createElement(MidiTrack.TYPE);
        sopran2.performTransientSetAttributeValueOperation(MidiTrack.name, SOPRAN1);
        sopran2.performTransientSetAttributeValueOperation(MidiTrack.channel, 1);
        init(arrangement, sopran2, 0, key, tempo, timeSignature, factory);

        MidiTrack alt1 = factory.createElement(MidiTrack.TYPE);
        alt1.performTransientSetAttributeValueOperation(MidiTrack.name, ALT1);
        alt1.performTransientSetAttributeValueOperation(MidiTrack.channel, 2);
        init(arrangement, alt1, 0, key, timeSignature, factory);

        MidiTrack alt2 = factory.createElement(MidiTrack.TYPE);
        alt2.performTransientSetAttributeValueOperation(MidiTrack.name, ALT2);
        alt2.performTransientSetAttributeValueOperation(MidiTrack.channel, 3);
        init(arrangement, alt2, 0, key, timeSignature, factory);

        MidiTrack tenor1 = factory.createElement(MidiTrack.TYPE);
        tenor1.performTransientSetAttributeValueOperation(MidiTrack.name, TENOR1);
        tenor1.performTransientSetAttributeValueOperation(MidiTrack.channel, 4);
        init(arrangement, tenor1, 4, key, timeSignature, factory);

        MidiTrack tenor2 = factory.createElement(MidiTrack.TYPE);
        tenor2.performTransientSetAttributeValueOperation(MidiTrack.name, TENOR2);
        tenor2.performTransientSetAttributeValueOperation(MidiTrack.channel, 5);
        init(arrangement, tenor2, 4, key, timeSignature, factory);

        MidiTrack bass1 = factory.createElement(MidiTrack.TYPE);
        bass1.performTransientSetAttributeValueOperation(MidiTrack.name, BASS1);
        bass1.performTransientSetAttributeValueOperation(MidiTrack.channel, 6);
        init(arrangement, bass1, 1, key, timeSignature, factory);

        MidiTrack bass2 = factory.createElement(MidiTrack.TYPE);
        bass2.performTransientSetAttributeValueOperation(MidiTrack.name, BASS2);
        bass2.performTransientSetAttributeValueOperation(MidiTrack.channel, 7);
        init(arrangement, bass2, 1, key, timeSignature, factory);

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
