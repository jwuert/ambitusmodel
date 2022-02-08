package org.wuerthner.ambitus.service;

import org.wuerthner.ambitus.model.Arrangement;
import org.wuerthner.ambitus.model.MidiTrack;
import org.wuerthner.ambitus.model.NoteEvent;
import org.wuerthner.sport.api.Operation;
import org.wuerthner.sport.operation.SetAttributeValueOperation;
import org.wuerthner.sport.operation.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Transpose {
    public static void run(Arrangement arrangement, int deltaPitch, Scope scope) {
        if (deltaPitch != 0) {
            List<Operation> opList = new ArrayList<>();
            switch (scope) {
                case ARRANGEMENT:
                    for (MidiTrack track : arrangement.getActiveMidiTrackList()) {
                        transposeTrack(opList, track, deltaPitch);
                    }
                    break;
                case TRACK:
                    transposeTrack(opList, arrangement.getSelectedMidiTrack(), deltaPitch);
                    break;
                case SELECTION:
                    transposeList(opList, arrangement.getSelection().getSelection()
                            .stream().filter(e -> e instanceof NoteEvent).map( e -> (NoteEvent) e)
                            .collect(Collectors.toList()),
                            deltaPitch);
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

    private static void transposeTrack(List<Operation> opList, MidiTrack track, int deltaPitch) {
        transposeList(opList, track.getChildrenByClass(NoteEvent.class), deltaPitch);
    }

    private static void transposeList(List<Operation> opList, List<NoteEvent> list, int deltaPitch) {
        for (NoteEvent event : list) {
            int pitch = event.getPitch();
            int newPitch = pitch + deltaPitch;
            opList.add(new SetAttributeValueOperation<>(event, NoteEvent.pitch, newPitch));
        }
    }
}