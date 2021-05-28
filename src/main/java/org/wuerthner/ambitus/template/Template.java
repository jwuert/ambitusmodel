package org.wuerthner.ambitus.template;

import org.wuerthner.ambitus.model.*;
import org.wuerthner.cwn.api.TimeSignature;
import org.wuerthner.sport.api.ModelElementFactory;

public interface Template {
    public Arrangement apply(int key, int tempo, TimeSignature timeSignature, ModelElementFactory factory);

    default void initTrack(MidiTrack track, Event... events) {
        for (Event event: events) {
            track.performTransientAddChildOperation(event);
            if (event instanceof TimeSignatureEvent) {
                track.performTransientSetAttributeValueOperation(MidiTrack.timeSignature, ((TimeSignatureEvent)event).getTimeSignature());
            } else if (event instanceof KeyEvent) {
                track.performTransientSetAttributeValueOperation(MidiTrack.key, ((KeyEvent)event).getKey()+7);
            } else if (event instanceof ClefEvent) {
                track.performTransientSetAttributeValueOperation(MidiTrack.clef, ((ClefEvent)event).getClef());
            } else if (event instanceof TempoEvent) {
                track.performTransientSetAttributeValueOperation(MidiTrack.tempo, ((TempoEvent)event).getTempo());
            }
        }
    }

    static Template createTemplate(int templateCode) {
        Template template = null;
        switch (templateCode) {
            case 0: template = new SingleViolin(); break;
            case 1: template = new SingleBass(); break;
            case 2: template = new Piano(); break;
            case 3: template = new SATB(); break;
            case 4: template = new ViolinSATB(); break;
            case 5: template = new SATBPiano(); break;
            case 6: template = new SSAATTBB(); break;
            default: template = new SingleViolin();
        }
        return template;
    }
}
