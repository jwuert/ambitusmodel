package org.wuerthner.ambitus.service;

import org.wuerthner.ambitus.model.Arrangement;
import org.wuerthner.ambitus.model.Event;
import org.wuerthner.ambitus.model.MidiTrack;
import org.wuerthner.ambitus.model.NoteEvent;
import org.wuerthner.cwn.score.Score;
import org.wuerthner.sport.api.Operation;
import org.wuerthner.sport.operation.SetAttributeValueOperation;
import org.wuerthner.sport.operation.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Velocity {
    private static final List<Integer> pitchList = Arrays.stream(Score.freqTab).boxed().collect(Collectors.toList());

    public static void run(Arrangement arrangement, int velocityStart, int velocityEnd, Scope scope) {
        List<Operation> opList = new ArrayList<>();
        switch (scope) {
            case ARRANGEMENT:
                for (MidiTrack track : arrangement.getActiveMidiTrackList()) {
                    velocityTrack(opList, track, velocityStart, velocityEnd);
                }
                break;
            case TRACK:
                velocityTrack(opList, arrangement.getSelectedMidiTrack(), velocityStart, velocityEnd);
                break;
            case SELECTION:
                velocityList(opList, arrangement.getSelection().getSelection()
                                .stream().filter(e -> e instanceof NoteEvent).map( e -> (NoteEvent) e)
                                .collect(Collectors.toList()),
                                velocityStart, velocityEnd);
                break;
            default:
                throw new RuntimeException("Scope '" + scope.name() + "' undefined!");
        }
        if (!opList.isEmpty()) {
            Transaction transaction = new Transaction("Velocity", opList);
            arrangement.performTransaction(transaction);
        }
    }

    private static void velocityTrack(List<Operation> opList, MidiTrack track, int velocityStart, int velocityEnd) {
        velocityList(opList, track.getChildrenByClass(NoteEvent.class), velocityStart, velocityEnd);
    }

    private static void velocityList(List<Operation> opList, List<NoteEvent> list, int velocityStart, int velocityEnd) {
        if (!list.isEmpty()) {
            long start = list.get(0).getPosition();
            long end = list.get(list.size()-1).getPosition();
            double ratio = (velocityEnd-velocityStart)*1.0/(end-start);
            System.out.println("ra: " + ratio);
            for (Event event : list) {
                if (event instanceof NoteEvent) {
                    long pos = event.getPosition();
                    int velocity = (int) (velocityStart + ratio*1.0*(pos-start));
                    System.out.println("vel: " + velocity);
                    opList.add(new SetAttributeValueOperation(event, NoteEvent.velocity, velocity));
                }
            }
        }
    }
}