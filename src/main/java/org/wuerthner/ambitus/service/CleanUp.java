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

public class CleanUp {
    public static void run(Arrangement arrangement, Scope scope) {
        List<Operation> opList = new ArrayList<>();
        switch (scope) {
            case ARRANGEMENT:
                for (MidiTrack track : arrangement.getActiveMidiTrackList()) {
                    cleanTrack(opList, track);
                }
                break;
            case TRACK:
                cleanTrack(opList, arrangement.getSelectedMidiTrack());
                break;
            case SELECTION:
                cleanList(opList, arrangement.getSelection().getSelection());
                break;
            default:
                throw new RuntimeException("Scope '" + scope.name() + "' undefined!");
        }
        if (!opList.isEmpty()) {
            Transaction transaction = new Transaction("Cleanup", opList);
            arrangement.performTransaction(transaction);
        }
        System.out.println("Clean Up: removed " + opList.size() + " events!");
    }

    private static void cleanTrack(List<Operation> opList, MidiTrack track) {
        cleanList(opList, track.getChildrenByClass(Event.class));
    }

    private static void cleanList(List<Operation> opList, List<Event> list) {
        Event oldEvent = null;
        for (Event event : list) {
            if (oldEvent != null) {
                if (event instanceof NoteEvent && oldEvent instanceof NoteEvent) {
                    if (event.getPosition() == oldEvent.getPosition()) {
                        if (((NoteEvent) event).getPitch() == ((NoteEvent) oldEvent).getPitch()) {
                            opList.add(new RemoveChildOperation(oldEvent));
                        }
                    }
                }
            }
            oldEvent = event;
        }
    }
}