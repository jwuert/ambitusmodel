package org.wuerthner.ambitus.model;

import org.junit.jupiter.api.Test;
import org.wuerthner.cwn.api.CwnTrack;
import org.wuerthner.sport.core.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrangementTest {

    @Test
    public void testTrackList() {
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

        // System.out.println(Model.makeString(arrangement));

        List<CwnTrack> trackList = arrangement.getTrackList();

        assertEquals(1, trackList.size());
    }
}
