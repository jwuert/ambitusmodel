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

    public static void run(Arrangement arrangement, int velocity, Scope scope) {
        List<Operation> opList = new ArrayList<>();
        switch (scope) {
            case ARRANGEMENT:
                for (MidiTrack track : arrangement.getActiveMidiTrackList()) {
                    velocityTrack(opList, track, velocity);
                }
                break;
            case TRACK:
                velocityTrack(opList, arrangement.getSelectedMidiTrack(), velocity);
                break;
            case SELECTION:
                velocityList(opList, arrangement.getSelection().getSelection()
                                .stream().filter(e -> e instanceof NoteEvent).map( e -> (NoteEvent) e)
                                .collect(Collectors.toList()),
                                velocity);
                break;
            default:
                throw new RuntimeException("Scope '" + scope.name() + "' undefined!");
        }
        if (!opList.isEmpty()) {
            Transaction transaction = new Transaction("Velocity", opList);
            arrangement.performTransaction(transaction);
        }
    }

    private static void velocityTrack(List<Operation> opList, MidiTrack track, int velocity) {
        velocityList(opList, track.getChildrenByClass(NoteEvent.class), velocity);
    }

    private static void velocityList(List<Operation> opList, List<NoteEvent> list, int velocity) {
        for (Event event : list) {
            if (event instanceof NoteEvent) {
                opList.add(new SetAttributeValueOperation(event, NoteEvent.velocity, velocity));
            }
        }
    }
}