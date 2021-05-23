package org.wuerthner.ambitus.tool;

import org.wuerthner.ambitus.model.Arrangement;
import org.wuerthner.ambitus.model.MidiTrack;
import org.wuerthner.cwn.api.CwnTrack;
import org.wuerthner.cwn.midi.MidiWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class MidiFileWriter {
    private final Arrangement arrangement;
    public MidiFileWriter(Arrangement arrangement){
        this.arrangement = arrangement;
    }

    public void write(File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            long startPosition = 0;
            long endPosition = arrangement.findLastPosition();
            List<MidiTrack> trackList = arrangement.getActiveMidiTrackList();
            MidiWriter midiWriter = new MidiWriter(trackList);
            midiWriter.writer(fos, startPosition, endPosition);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
