package org.wuerthner.ambitus.service;

import org.wuerthner.ambitus.model.Arrangement;
import org.wuerthner.ambitus.model.Event;
import org.wuerthner.ambitus.model.MidiTrack;
import org.wuerthner.ambitus.model.NoteEvent;
import org.wuerthner.cwn.score.QuantizedPosition;
import org.wuerthner.sport.api.Operation;
import org.wuerthner.sport.operation.SetAttributeValueOperation;
import org.wuerthner.sport.operation.Transaction;

import java.util.ArrayList;
import java.util.List;

public class Quantize {
    public static void run(Arrangement arrangement, int ticks) {
        List<Operation> opList = new ArrayList<>();
        for (MidiTrack track : arrangement.getActiveMidiTrackList()) {
            for (Event event : track.getChildrenByClass(Event.class)) {
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
        if (!opList.isEmpty()) {
            Transaction transaction = new Transaction("Quantize", opList);
            arrangement.performTransaction(transaction);
        }
    }
}
