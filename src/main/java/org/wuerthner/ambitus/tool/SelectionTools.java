package org.wuerthner.ambitus.tool;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


import org.wuerthner.ambitus.model.Arrangement;
import org.wuerthner.ambitus.model.Event;
import org.wuerthner.ambitus.model.MidiTrack;
import org.wuerthner.ambitus.model.NoteEvent;
import org.wuerthner.cwn.api.CwnEvent;
import org.wuerthner.cwn.api.CwnSelection;
import org.wuerthner.cwn.position.PositionTools;
import org.wuerthner.sport.api.AttributeOperation;
import org.wuerthner.sport.api.Operation;
import org.wuerthner.sport.operation.SetAttributeValueOperation;
import org.wuerthner.sport.operation.Transaction;

/*
 * These keyboard shortcuts can be used on a selection:
 * CTRL+
 * LEFT/RIGHT/UP/DOWN: move cursor left, right, (staff) up, (staff) down
 * LEFT/RIGHT/UP/DOWN + SHIFT: move note left, right, up, down (left, right according to grid setting)
 * PAGE_UP/PAGE_DOWN: increase/decrease bar offset by 4
 * HOME: reset bar offset to 0
 * ESC: deselect all
 * #/b: increase/decrease enharmonic shift
 * DEL: delete
 * c: copy
 * v: paste
 */
public class SelectionTools {
    public long getStartPosition(AbstractSelection selection) {
        long startPosition = 0;
        long minPosition = Long.MAX_VALUE;
        List<Event> list = selection.getSelection();
        if (!selection.hasSingleSelection()) {
            for (int i = 0; i < list.size(); i++) {
                Event event = list.get(i);
                Long eventPosition = event.getPosition();
                if (eventPosition != null) {
                    minPosition = Math.min(minPosition, eventPosition);
                }
            }
        }
        if (minPosition < Long.MAX_VALUE) {
            startPosition = minPosition;
        }
        return startPosition;
    }

    public long getEndPosition(AbstractSelection selection) {
        long endPosition = 0;
        if (!selection.hasSingleSelection()) {
            List<Event> list = selection.getSelection();
            for (int i = 0; i < list.size(); i++) {
                Long eventPosition = list.get(i).getPosition();
                Long eventDuration = list.get(i).getDuration();
                if (eventPosition != null && eventDuration != null) {
                    endPosition = Math.max(endPosition, eventPosition + eventDuration);
                }
            }
        }
        return endPosition;
    }

    /*
     * MOVE CURSOR LEFT
     */
    public <E extends Event> E moveCursorLeft(MidiTrack selectedTrack, AbstractSelection selection, Class<E> clasz) {
        if (selection.hasSingleSelection()) {
            Event selectedElement = selection.getSingleSelection();
            // if (selectedElement instanceof Event) {
            if (clasz.isAssignableFrom(selectedElement.getClass())) {
                Event event = selectedElement;
                Optional<E> previousEvent = selectedTrack.findEventBefore(event.getPosition(), clasz);
                if (previousEvent.isPresent()) {
                    selection.setSingleSelection(previousEvent.get());
                } else {
                    selectedTrack.findLastEvent(clasz).ifPresent(ev -> {
                        selection.setSingleSelection(ev);
                    });
                }
            } else {
                selectedTrack.findLastEvent(clasz).ifPresent(event -> {
                    selection.setSingleSelection(event);
                });
            }
        } else {
            selectedTrack.findLastEvent(clasz).ifPresent(event -> {
                selection.setSingleSelection(event);
            });
        }
        @SuppressWarnings("unchecked")
        E event = (E) selection.getSingleSelection();
        return event;
    }

    /*
     * MOVE CURSOR RIGHT
     */
    public <E extends Event> E moveCursorRight(MidiTrack selectedTrack, AbstractSelection selection, Class<E> clasz) {
        if (selection.hasSingleSelection() && clasz.isAssignableFrom(selection.getSingleSelection().getClass())) {
            Event event = selection.getSingleSelection();
            E nextEvent = selectedTrack.findEventAfter((E) event, clasz);
            selection.setSingleSelection(nextEvent);
            // selectedTrack.findFirstNote().ifPresent(note -> {
            // selection.setSingleSelection(note);
            // });
        } else {
            long position = 0;
            Event event = selectedTrack.findEventFromPosition(position, clasz);
            selection.setSingleSelection(event);
        }
        @SuppressWarnings("unchecked")
        E event = (E) selection.getSingleSelection();
        // System.out.println("### TIME: " + (time - new Date().getTime()));
        return event;
    }

    /*
     * MOVE CURSOR UP
     */
    public <E extends Event> E moveCursorUp(MidiTrack selectedTrack, AbstractSelection selection) {
        E nextEvent = null;
        if (!selection.isEmpty()) {
            Event event = selection.getSelection().get(0);
            long position = event.getPosition();
            List<Event> eventList = selectedTrack.getChildrenByClass(Event.class);
            int index = eventList.indexOf(event);
            if (index >= 0) {
                if (index < eventList.size()-1) {
                    nextEvent = (E) eventList.get(index + 1);
                    selection.setSingleSelection(nextEvent);
                }
            }
        }
        return nextEvent;
    }

    /*
     * MOVE CURSOR DOWN
     */
    public <E extends Event> E moveCursorDown(MidiTrack selectedTrack, AbstractSelection selection) {
        E nextEvent = null;
        if (!selection.isEmpty()) {
            Event event = selection.getSelection().get(0);
            long position = event.getPosition();
            List<Event> eventList = selectedTrack.getChildrenByClass(Event.class);
            int index = eventList.indexOf(event);
            if (index >= 1) {
                nextEvent = (E) eventList.get(index - 1);
                selection.setSingleSelection(nextEvent);
            }
        }
        return nextEvent;
    }

    /*
     * MOVE NOTE LEFT
     */
    public void moveNoteLeft(Arrangement arrangement) {
        operateOnSelection(new OperationFactory<Long>() {
            public Optional<AttributeOperation<Long>> createOperation(Event element, int gridInTicks) {
                NoteEvent note = (NoteEvent) element;
                long newPosition = note.getPosition() - gridInTicks;
                if (newPosition >= 0) {
                    AttributeOperation<Long> operation = new SetAttributeValueOperation<Long>(note, NoteEvent.position, newPosition);
                    return Optional.of(operation);
                }
                return Optional.empty();
            }
        }, arrangement);
    }

    public void shrinkNote(Arrangement arrangement) {
        operateOnSelection(new OperationFactory<Long>() {
            public Optional<AttributeOperation<Long>> createOperation(Event element, int gridInTicks) {
                NoteEvent note = (NoteEvent) element;
                long newDuration = note.getDuration() - gridInTicks;
                if (newDuration > 0) {
                    AttributeOperation<Long> operation = new SetAttributeValueOperation<>(note, NoteEvent.duration, newDuration);
                    return Optional.of(operation);
                }
                return Optional.empty();
            }
        }, arrangement);
    }

    /*
     * MOVE NOTE RIGHT
     */
    public void moveNoteRight(Arrangement arrangement) {
        operateOnSelection(new OperationFactory<Long>() {
            public Optional<AttributeOperation<Long>> createOperation(Event element, int gridInTicks) {
                NoteEvent note = (NoteEvent) element;
                long newPosition = note.getPosition() + gridInTicks;
                if (newPosition >= 0) {
                    AttributeOperation<Long> operation = new SetAttributeValueOperation<Long>(note, NoteEvent.position, newPosition);
                    return Optional.of(operation);
                }
                return Optional.empty();
            }
        }, arrangement);
    }

    public void extendNote(Arrangement arrangement) {
        operateOnSelection(new OperationFactory<Long>() {
            public Optional<AttributeOperation<Long>> createOperation(Event element, int gridInTicks) {
                NoteEvent note = (NoteEvent) element;
                long newDuration = note.getDuration() + gridInTicks;
                if (newDuration > 0) {
                    AttributeOperation<Long> operation = new SetAttributeValueOperation<>(note, NoteEvent.duration, newDuration);
                    return Optional.of(operation);
                }
                return Optional.empty();
            }
        }, arrangement);
    }

    /*
     * HALF DURATION
     */
    public void halfDuration(Arrangement arrangement) {
        operateOnSelection(new OperationFactory<Long>() {
            public Optional<AttributeOperation<Long>> createOperation(Event element, int gridInTicks) {
                NoteEvent note = (NoteEvent) element;
                long newDuration = (long) (note.getDuration() * 0.5);
                if (newDuration >= gridInTicks) {
                    AttributeOperation<Long> operation = new SetAttributeValueOperation<Long>(note, NoteEvent.duration, newDuration);
                    return Optional.of(operation);
                }
                return Optional.empty();
            }
        }, arrangement);
    }

    /*
     * DOUBLE DURATION
     */
    public void doubleDuration(Arrangement arrangement) {
        operateOnSelection(new OperationFactory<Long>() {
            public Optional<AttributeOperation<Long>> createOperation(Event element, int gridInTicks) {
                NoteEvent note = (NoteEvent) element;
                long newDuration = note.getDuration() * 2;
                AttributeOperation<Long> operation = new SetAttributeValueOperation<Long>(note, NoteEvent.duration, newDuration);
                return Optional.of(operation);
            }
        }, arrangement);
    }

    /*
     * Sharp/Flat
     */
    public void sharp(Arrangement arrangement) {
        operateOnSelection(new OperationFactory<Integer>() {
            public Optional<AttributeOperation<Integer>> createOperation(Event element, int gridInTicks) {
                NoteEvent note = (NoteEvent) element;
                int enh = note.getEnharmonicShift();
                if (enh < 2) {
                    enh++;
                    AttributeOperation<Integer> operation = new SetAttributeValueOperation<>(note, NoteEvent.shift, enh);
                    return Optional.of(operation);
                }
                return Optional.empty();
            }
        }, arrangement);
    }
    public void flat(Arrangement arrangement) {
        operateOnSelection(new OperationFactory<Integer>() {
            public Optional<AttributeOperation<Integer>> createOperation(Event element, int gridInTicks) {
                NoteEvent note = (NoteEvent) element;
                int enh = note.getEnharmonicShift();
                if (enh > -2) {
                    enh--;
                    AttributeOperation<Integer> operation = new SetAttributeValueOperation<>(note, NoteEvent.shift, enh);
                    return Optional.of(operation);
                }
                return Optional.empty();
            }
        }, arrangement);
    }

    /*
     * ADD DURATION
     */
    public void addDuration(Arrangement arrangement) {
        operateOnSelection(new OperationFactory<Long>() {
            public Optional<AttributeOperation<Long>> createOperation(Event element, int gridInTicks) {
                NoteEvent note = (NoteEvent) element;
                long newDuration = note.getDuration() + gridInTicks;
                AttributeOperation<Long> operation = new SetAttributeValueOperation<Long>(note, NoteEvent.duration, newDuration);
                return Optional.of(operation);
            }
        }, arrangement);
    }

    /*
     * SUBTRACT DURATION
     */
    public void subtractDuration(Arrangement arrangement) {
        operateOnSelection(new OperationFactory<Long>() {
            public Optional<AttributeOperation<Long>> createOperation(Event element, int gridInTicks) {
                NoteEvent note = (NoteEvent) element;
                long newDuration = note.getDuration() - gridInTicks;
                if (newDuration >= gridInTicks) {
                    AttributeOperation<Long> operation = new SetAttributeValueOperation<Long>(note, NoteEvent.duration, newDuration);
                    return Optional.of(operation);
                }
                return Optional.empty();
            }
        } ,arrangement);
    }

    /*
     * MOVE STAFF CURSOR UP
     */
//    public void moveStaffCursorUp(GenericContext context) {
//        context.getSelection().getActiveDocument().ifPresent(document -> {
//            Arrangement arrangement = (Arrangement) document;
//            List<MidiTrack> trackList = arrangement.getActiveMidiTrackList();
//            MidiTrack newSelectedTrack = null;
//            if (trackList.size() > 0) {
//                AbstractSelection selection = context.getSelection();
//                MidiTrack selectedTrack = (MidiTrack) selection.getElement(MidiTrack.class.getName());
//                if (selectedTrack != null) {
//                    for (int i = 0; i < trackList.size(); i++) {
//                        MidiTrack track = trackList.get(i);
//                        if (track == selectedTrack) {
//                            if (i == 0) {
//                                newSelectedTrack = trackList.get(trackList.size() - 1);
//                            } else {
//                                newSelectedTrack = trackList.get(i - 1);
//                            }
//                            break;
//                        }
//                    }
//                } else {
//                    newSelectedTrack = trackList.get(trackList.size() - 1);
//                }
//                if (newSelectedTrack != null) {
//                    selection.putElement(MidiTrack.class.getName(), newSelectedTrack);
//                }
//            }
//        });
//    }

    /*
     * MOVE STAFF CURSOR DOWN
     */
//    public void moveStaffCursorDown(GenericContext context) {
//        context.getSelection().getActiveDocument().ifPresent(document -> {
//            Arrangement arrangement = (Arrangement) document;
//            List<MidiTrack> trackList = arrangement.getActiveMidiTrackList();
//            if (trackList.size() > 0) {
//                AbstractSelection selection = context.getSelection();
//                MidiTrack selectedTrack = (MidiTrack) selection.getElement(MidiTrack.class.getName());
//                MidiTrack newSelectedTrack = null;
//                if (selectedTrack != null) {
//                    for (int i = 0; i < trackList.size(); i++) {
//                        MidiTrack track = trackList.get(i);
//                        if (track == selectedTrack) {
//                            if (i == trackList.size() - 1) {
//                                newSelectedTrack = trackList.get(0);
//                            } else {
//                                newSelectedTrack = trackList.get(i + 1);
//                            }
//                            break;
//                        }
//                    }
//                } else {
//                    newSelectedTrack = trackList.get(0);
//                }
//                if (newSelectedTrack != null) {
//                    selection.putElement(MidiTrack.class.getName(), newSelectedTrack);
//                }
//            }
//        });
//    }

    /*
     * MOVE NOTE UP
     */
    public void moveNoteUp(Arrangement arrangement) {
        operateOnSelection(new OperationFactory<Integer>() {
            public Optional<AttributeOperation<Integer>> createOperation(Event element, int gridInTicks) {
                NoteEvent note = (NoteEvent) element;
                int newPitch = note.getPitch() + 1;
                if (newPitch < 127) {
                    AttributeOperation<Integer> operation = new SetAttributeValueOperation<Integer>(note, NoteEvent.pitch, newPitch);
                    return Optional.of(operation);
                }
                return Optional.empty();
            }
        }, arrangement);
    }

    /*
     * MOVE NOTE DOWN
     */
    public void moveNoteDown(Arrangement arrangement) {
        operateOnSelection(new OperationFactory<Integer>() {
            public Optional<AttributeOperation<Integer>> createOperation(Event element, int gridInTicks) {
                NoteEvent note = (NoteEvent) element;
                int newPitch = note.getPitch() - 1;
                if (newPitch >= 0) {
                    AttributeOperation<Integer> operation = new SetAttributeValueOperation<Integer>(note, NoteEvent.pitch, newPitch);
                    return Optional.of(operation);
                }
                return Optional.empty();
            }
        }, arrangement);
    }

    public void remove(Arrangement arrangement, AbstractSelection selection) {
        if (!selection.isEmpty()) {
            List<Event> list = selection.getSelection();
            arrangement.deleteElements(list);
        }
    }

    public void shiftNoteUp(Arrangement arrangement, AbstractSelection selection) {
        List<AttributeOperation<Integer>> operationList = new ArrayList<>();
        selection.getSelection().forEach(element -> {
            if (element instanceof NoteEvent) {
                NoteEvent note = (NoteEvent) element;
                int newShift = Math.min(2, note.getEnharmonicShift() + 1);
                AttributeOperation<Integer> operation = new SetAttributeValueOperation<Integer>(note, NoteEvent.shift, newShift);
                operationList.add(operation);
            }
        });
        Transaction transaction = new Transaction(operationList.toArray(new Operation[] {}));
        arrangement.performTransaction(transaction);
    }

    public void shiftNoteDown(Arrangement arrangement, AbstractSelection selection) {
        List<AttributeOperation<Integer>> operationList = new ArrayList<>();
        selection.getSelection().forEach(element -> {
            if (element instanceof NoteEvent) {
                NoteEvent note = (NoteEvent) element;
                int newShift = Math.max(-2, note.getEnharmonicShift() - 1);
                AttributeOperation<Integer> operation = new SetAttributeValueOperation<Integer>(note, NoteEvent.shift, newShift);
                operationList.add(operation);
            }
        });
        Transaction transaction = new Transaction(operationList.toArray(new Operation[] {}));
        arrangement.performTransaction(transaction);
    }

    // inclusive start, exclusive end: [start, end[
//    public List<GenericModelElement> getElementsBetweenPositions(GenericContext context, long start, long end) {
//        List<GenericModelElement> scoreRange = new ArrayList<>();
//        context.getSelection().getActiveDocument().ifPresent(document -> {
//            List<MidiTrack> activeMidiTrackList = ((Arrangement) document).getMidiTrackList();
//            for (MidiTrack track : activeMidiTrackList) {
//                List<NoteEvent> noteList = track.getList(NoteEvent.class);
//                List<NoteEvent> collect = noteList.stream().filter(n -> n.getPosition() >= start && n.getPosition() < end).collect(Collectors.toList());
//                scoreRange.addAll(collect);
//
//            }
//        });
//        return scoreRange;
//    }

//    public void selectRange(GenericContext context, NamedRange range) {
//        context.getSelection().getActiveDocument().ifPresent(document -> {
//            List<GenericModelElement> scoreRange = new ArrayList<>();
//            List<MidiTrack> activeMidiTrackList = ((Arrangement) document).getActiveMidiTrackList();
//            for (MidiTrack track : activeMidiTrackList) {
//                List<NoteEvent> noteList = track.getList(NoteEvent.class);
//                List<NoteEvent> collect = noteList.stream().filter(n -> n.getPosition() >= range.start && n.getPosition() <= range.end).collect(Collectors.toList());
//                scoreRange.addAll(collect);
//
//            }
//            context.getSelection().setSelectedModelElements(scoreRange);
//            if (!scoreRange.isEmpty()) {
//                long rangeStartPosition = scoreRange.stream().mapToLong(note -> ((NoteEvent) note).getPosition()).min().getAsLong();
//                int bar = PositionTools.getTrias(activeMidiTrackList.get(0), rangeStartPosition).bar;
//                ((Arrangement) document).setTransientBarOffset(context, bar);
//            }
//        });
//    }

    //
    // OPERATION FACTORY INTERFACE
    //
    private interface OperationFactory<T> {
        Optional<AttributeOperation<T>> createOperation(Event element, int gridInTicks);
    }

    private <T> void operateOnSelection(OperationFactory<T> operationFactory, Arrangement arrangement) {
            int gridInTicks = arrangement.getGridInTicks();
            List<AttributeOperation<T>> operationList = new ArrayList<>();
            arrangement.getSelection().getSelection().forEach(element -> {
                if (element instanceof NoteEvent) {
                    Optional<AttributeOperation<T>> operation = operationFactory.createOperation(element, gridInTicks);
                    if (operation.isPresent()) {
                        operationList.add(operation.get());
                    }
                }
            });
            // TODO: move execution into handler to allow for persistence, etc
            Transaction transaction = new Transaction(operationList.toArray(new Operation[] {}));
        ((Arrangement)arrangement).performTransaction(transaction);

    }

}