package org.wuerthner.ambitus.service;

import org.wuerthner.ambitus.model.Arrangement;
import org.wuerthner.ambitus.model.Event;
import org.wuerthner.ambitus.model.MidiTrack;
import org.wuerthner.ambitus.model.NoteEvent;
import org.wuerthner.sport.api.Operation;
import org.wuerthner.sport.operation.SetAttributeValueOperation;
import org.wuerthner.sport.operation.Transaction;

import java.util.ArrayList;
import java.util.List;

public class Scale {
    public static void run(Arrangement arrangement, double factor, Scope scope) {
        if (factor != 1.0) {
            List<Operation> opList = new ArrayList<>();
            switch (scope) {
                case ARRANGEMENT:
                    for (MidiTrack track : arrangement.getActiveMidiTrackList()) {
                        scaleTrack(opList, track, factor);
                    }
                    break;
                case TRACK:
                    scaleTrack(opList, arrangement.getSelectedMidiTrack(), factor);
                    break;
                case SELECTION:
                    scaleList(opList, arrangement.getSelection().getSelection(), factor);
                    break;
                default:
                    throw new RuntimeException("Scope '" + scope.name() + "' undefined!");
            }
            if (!opList.isEmpty()) {
                Transaction transaction = new Transaction("Transpose", opList);
                arrangement.performTransaction(transaction);
            }
        }
    }

    private static void scaleTrack(List<Operation> opList, MidiTrack track, double factor) {
        scaleList(opList, track.getChildrenByClass(Event.class), factor);
    }

    private static void scaleList(List<Operation> opList, List<Event> list, double factor) {
        for (Event event : list) {
            opList.add(new SetAttributeValueOperation<>(event, Event.position, (long)(event.getPosition() * factor)));
            if (event instanceof NoteEvent) {
                opList.add(new SetAttributeValueOperation<>(event, NoteEvent.duration, (long)(((NoteEvent)event).getDuration() * factor)));
            }
        }
    }
}