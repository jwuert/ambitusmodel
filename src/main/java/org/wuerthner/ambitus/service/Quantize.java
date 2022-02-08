package org.wuerthner.ambitus.service;

import org.wuerthner.ambitus.model.Arrangement;
import org.wuerthner.ambitus.model.Event;
import org.wuerthner.ambitus.model.MidiTrack;
import org.wuerthner.ambitus.model.NoteEvent;
import org.wuerthner.cwn.score.QuantizedPosition;
import org.wuerthner.cwn.score.Score;
import org.wuerthner.sport.api.Operation;
import org.wuerthner.sport.operation.RemoveChildOperation;
import org.wuerthner.sport.operation.SetAttributeValueOperation;
import org.wuerthner.sport.operation.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Quantize {
    public static void run(Arrangement arrangement, int ticks, Scope scope) {
        List<Operation> opList = new ArrayList<>();
        switch (scope) {
            case ARRANGEMENT:
                for (MidiTrack track : arrangement.getActiveMidiTrackList()) {
                    quantizeTrack(ticks, opList, track);
                }
                break;
            case TRACK:
                quantizeTrack(ticks, opList, arrangement.getSelectedMidiTrack());
                break;
            case SELECTION:
                quantizeList(ticks, opList, arrangement.getSelection().getSelection());
                break;
            default:
                throw new RuntimeException("Scope '" + scope.name() + "' undefined!");
        }
        if (!opList.isEmpty()) {
            Transaction transaction = new Transaction("Quantize", opList);
            arrangement.performTransaction(transaction);
        }
    }

    private static void quantizeTrack(int ticks, List<Operation> opList, MidiTrack track) {
        quantizeList(ticks, opList, track.getChildrenByClass(Event.class));
    }

    private static void quantizeList(int ticks, List<Operation> opList, List<Event> list) {
        for (Event event : list) {
            long position = event.getPosition();
            long duration = event.getDuration();
            // pos = 2 | 2 % 240 = 0 | pos = pos
            long newPosition = position + (long) (ticks * 0.49);
            newPosition = newPosition - (newPosition % ticks);
            if (position != newPosition) {
                // event.performTransientSetAttributeValueOperation(Event.position, position);
                opList.add(new SetAttributeValueOperation<>(event, Event.position, newPosition));
            }
            if (event instanceof NoteEvent) {
                long newDuration = duration + (long) (ticks * 0.49);
                newDuration = newDuration - (newDuration % ticks);
                if (newDuration==0) { newDuration = ticks; }
                if (duration != newDuration) {
                    // event.performTransientSetAttributeValueOperation(NoteEvent.duration, duration);
                    opList.add(new SetAttributeValueOperation<>(event, NoteEvent.duration, newDuration));
                }
            }
        }
    }
}
