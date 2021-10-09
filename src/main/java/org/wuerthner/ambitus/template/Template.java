package org.wuerthner.ambitus.template;

import org.wuerthner.ambitus.model.*;
import org.wuerthner.cwn.api.TimeSignature;
import org.wuerthner.sport.api.ModelElementFactory;

public interface Template {
    public final static String[] TEMPLATES = { SingleViolin.NAME, SingleBass.NAME, Piano.NAME, SATB.NAME, ViolinSATB.NAME, SATBPiano.NAME, SSAATTBB.NAME };
    public Arrangement apply(int key, int tempo, TimeSignature timeSignature, ModelElementFactory factory);

    default void initTrack(MidiTrack track, Event... events) {
        for (Event event: events) {
            track.performTransientAddChildOperation(event);
//            if (event instanceof TimeSignatureEvent) {
//                track.performTransientSetAttributeValueOperation(MidiTrack.timeSignature, ((TimeSignatureEvent)event).getTimeSignature());
//            } else if (event instanceof KeyEvent) {
//                track.performTransientSetAttributeValueOperation(MidiTrack.key, ((KeyEvent)event).getKey()+7);
//            } else if (event instanceof ClefEvent) {
//                track.performTransientSetAttributeValueOperation(MidiTrack.clef, ((ClefEvent)event).getClef());
//            } else if (event instanceof TempoEvent) {
//                track.performTransientSetAttributeValueOperation(MidiTrack.tempo, ((TempoEvent)event).getTempo());
//            }
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

    static Integer getTemplateNo(String templateName) {
        Integer no = null;
        for (int i=0; i<TEMPLATES.length; i++) {
            if (TEMPLATES[i].equals(templateName)) {
                no = i;
                break;
            }
        }
        return no;
    }
    static Template createTemplate(String templateName) {
        Template template = null;
        switch (templateName) {
            case SingleViolin.NAME: template = new SingleViolin(); break;
            case SingleBass.NAME: template = new SingleBass(); break;
            case Piano.NAME: template = new Piano(); break;
            case SATB.NAME: template = new SATB(); break;
            case ViolinSATB.NAME: template = new ViolinSATB(); break;
            case SATBPiano.NAME: template = new SATBPiano(); break;
            case SSAATTBB.NAME: template = new SSAATTBB(); break;
            default: template = new SingleViolin();
        }
        return template;
    }
}
