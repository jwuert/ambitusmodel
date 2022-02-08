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

public class InsertSpace {
    public static void run(Arrangement arrangement, int numberOfBeats, Scope scope) {
        if (numberOfBeats>0) {
            List<Operation> opList = new ArrayList<>();
            int deltaTicks = arrangement.getPPQ() * numberOfBeats;
            switch (scope) {
                case ARRANGEMENT:
                    for (MidiTrack track : arrangement.getActiveMidiTrackList()) {
                        insertSpaceTrack(opList, track, deltaTicks);
                    }
                    break;
                case TRACK:
                    insertSpaceTrack(opList, arrangement.getSelectedMidiTrack(), deltaTicks);
                    break;
                case SELECTION:
                    insertSpaceList(opList, arrangement.getSelection().getSelection(), deltaTicks);
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

    private static void insertSpaceTrack(List<Operation> opList, MidiTrack track, int deltaTicks) {
        insertSpaceList(opList, track.getChildrenByClass(Event.class), deltaTicks);
    }

    private static void insertSpaceList(List<Operation> opList, List<Event> list, int deltaTicks) {
        for (Event event : list) {
            if (event instanceof NoteEvent || event.getPosition() != 0) {
                opList.add(new SetAttributeValueOperation<>(event, Event.position, event.getPosition() + deltaTicks));
            }
        }
    }
}