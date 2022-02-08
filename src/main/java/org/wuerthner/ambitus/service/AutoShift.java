package org.wuerthner.ambitus.service;

import org.wuerthner.ambitus.model.Arrangement;
import org.wuerthner.ambitus.model.Event;
import org.wuerthner.ambitus.model.MidiTrack;
import org.wuerthner.ambitus.model.NoteEvent;
import org.wuerthner.cwn.score.Score;
import org.wuerthner.sport.api.Operation;
import org.wuerthner.sport.operation.RemoveChildOperation;
import org.wuerthner.sport.operation.SetAttributeValueOperation;
import org.wuerthner.sport.operation.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AutoShift {
    private static final List<Integer> pitchList = Arrays.stream(Score.freqTab).boxed().collect(Collectors.toList());

    public static void run(Arrangement arrangement, Scope scope) {
        List<Operation> opList = new ArrayList<>();
        switch (scope) {
            case ARRANGEMENT:
                for (MidiTrack track : arrangement.getActiveMidiTrackList()) {
                    shiftTrack(opList, track);
                }
                break;
            case TRACK:
                shiftTrack(opList, arrangement.getSelectedMidiTrack());
                break;
            case SELECTION:
                Integer newEnhShift = (arrangement.getKey() < 0 ? -1 : 0);
                shiftList(opList, arrangement.getSelection().getSelection(), newEnhShift);
                break;
            default:
                throw new RuntimeException("Scope '" + scope.name() + "' undefined!");
        }
        if (!opList.isEmpty()) {
            Transaction transaction = new Transaction("AutoShift", opList);
            arrangement.performTransaction(transaction);
        }
    }

    private static void shiftTrack(List<Operation> opList, MidiTrack track) {
        Integer newEnhShift = (track.getKey() < 0 ? -1 : 0);
        shiftList(opList, track.getChildrenByClass(Event.class), newEnhShift);
    }

    private static void shiftList(List<Operation> opList, List<Event> list, Integer newEnhShift) {
        for (Event event : list) {
            if (event instanceof NoteEvent) {
                int pitch = ((NoteEvent) event).getPitch();
                boolean evenPitch = pitchList.contains(pitch);

                int enharmonicShift = ((NoteEvent) event).getEnharmonicShift();
                //System.out.println("-> " + pitch + " (" + ((NoteEvent) event).getCPitch() + "), even:" + evenPitch + ", enh: " + enharmonicShift);
                if (!evenPitch) {
                    if (enharmonicShift != newEnhShift) {
                        // System.out.println("set enh = " + newEnhShift + " for " + pitch + " (" + ((NoteEvent) event).getCPitch() + ")");
                        opList.add(new SetAttributeValueOperation(event, NoteEvent.shift, newEnhShift));
                    }
                } else {
                    if (enharmonicShift != 0) {
                        // System.out.println("set enh = " + 0 + " for " + pitch + " (" + ((NoteEvent) event).getCPitch() + ")");
                        opList.add(new SetAttributeValueOperation(event, NoteEvent.shift, 0));
                    }
                }
            }
        }
    }
}