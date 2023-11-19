package org.wuerthner.ambitus.model;

import org.junit.jupiter.api.Test;
import org.wuerthner.cwn.api.CwnTrack;
import org.wuerthner.sport.api.Clipboard;
import org.wuerthner.sport.api.History;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.core.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArrangementTest {

    @Test
    public void testTrackList() {
        AmbitusFactory factory = new AmbitusFactory();
        Arrangement arrangement = factory.createElement(Arrangement.TYPE);
        MidiTrack track = factory.createElement(MidiTrack.TYPE);
        History history = arrangement.getHistory();
        arrangement.performAddChildOperation(track, history);

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

        Clipboard<Event> clipboard = arrangement.getClipboard();
        assertEquals(0, clipboard.size());
        assertTrue(arrangement.hasUndo());
        assertFalse(arrangement.hasRedo());

        arrangement.clearHistory();
        assertFalse(arrangement.hasUndo());

        arrangement.setTrackName(track, "Sopran");
        arrangement.setTrackMute(track, true);
        assertTrue(track.getMute());

        arrangement.setTrackMute(track.getId(), false);
        assertFalse(track.getMute());
    }

    @Test
    public void testDefaultValues() {
        AmbitusFactory factory = new AmbitusFactory();
        // KEY
        KeyEvent keyEvent = factory.createElement(KeyEvent.TYPE);
        int key = keyEvent.getKey();
        int genus = keyEvent.getGenus();
        String k = Arrangement.KEYS[key+7];
        String g = Arrangement.GENUS[genus];
        assertEquals("C", k);
        assertEquals("Major", g);
        // CLEF
        ClefEvent clefEvent = factory.createElement(ClefEvent.TYPE);
        int clef = clefEvent.getClef();
        String c = Arrangement.CLEFS[clef];
        assertEquals("Violin", c);
        // VALUES
        assertEquals(0L, Arrangement.DEFAULT_POSITION);
        assertEquals(100, Arrangement.DEFAULT_TEMPO);
        assertEquals(0, Arrangement.DEFAULT_TRANSPOSE);
        assertEquals(3, Arrangement.DEFAULT_SCORE_OBJECT_INDEX);
        assertEquals(0, Arrangement.DEFAULT_SHIFT);
        assertEquals(0, Arrangement.DEFAULT_TRANSPOSE);
        assertEquals(0, Arrangement.DEFAULT_VOICE_INDEX);
        assertEquals(0, Arrangement.DEFAULT_TUPLET_INDEX);
        assertEquals(0, Arrangement.DEFAULT_DOTS_INDEX);
    }
}
