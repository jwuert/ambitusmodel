package org.wuerthner.ambitus.model;

import org.junit.jupiter.api.Test;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.core.Model;

public class TrackTest {
    @Test
    public void testTrack() {
        AmbitusFactory factory = new AmbitusFactory();
        Arrangement arrangement = factory.createElement(Arrangement.TYPE);
        MidiTrack track = factory.createElement(MidiTrack.TYPE);
        arrangement.performTransientAddChildOperation(track);

        NoteEvent note1 = factory.createElement(NoteEvent.TYPE);
        note1.performTransientSetAttributeValueOperation(NoteEvent.position, 0L);
        note1.performTransientSetAttributeValueOperation(NoteEvent.pitch, 80);
        track.performTransientAddChildOperation(note1);

        NoteEvent note2 = factory.createElement(NoteEvent.TYPE);
        note2.performTransientSetAttributeValueOperation(NoteEvent.position, 3840L);
        note2.performTransientSetAttributeValueOperation(NoteEvent.pitch, 80);
        track.performTransientAddChildOperation(note2);

        NoteEvent note3 = factory.createElement(NoteEvent.TYPE);
        note3.performTransientSetAttributeValueOperation(NoteEvent.position, 960L);
        note3.performTransientSetAttributeValueOperation(NoteEvent.pitch, 80);
        track.performTransientAddChildOperation(note3);

        System.out.println(Model.makeString(arrangement));
    }

    @Test
    public void testNoteCopy() {
        AmbitusFactory factory = new AmbitusFactory();
        ModelElement element = factory.createElement(NoteEvent.TYPE);
        System.out.println(Model.makeString(element));
        ModelElement copy = factory.copyTree(element);
        System.out.println(Model.makeString(copy));

    }
}
