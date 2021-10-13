package org.wuerthner.ambitus.tool;

import org.wuerthner.ambitus.model.Event;
import org.wuerthner.cwn.api.CwnEvent;
import org.wuerthner.cwn.api.CwnPointer;
import org.wuerthner.cwn.api.CwnSelection;
import org.wuerthner.cwn.score.Location;
import org.wuerthner.sport.api.ModelElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractSelection implements CwnSelection<Event> {
    private List<Event> selectedElements = new ArrayList<>();
    private int staffIndex;
    private SelectionType type;
    private long mouseStartPosition;
    private long mouseEndPosition;
    private int mouseStaff;
    private final CwnPointer pointer = new AmbitusPointer();

    public void set(int staffIndex) {
        this.staffIndex = staffIndex;
    }

    public void set(int staffIndex, SelectionType type) {
        this.selectedElements = new ArrayList<>();
        this.staffIndex = staffIndex;
        this.type = type;
    }

    public void set(List<ModelElement> list, int staffIndex, SelectionType type) {
        this.selectedElements = list
                .stream()
                .filter(e -> e instanceof Event).map(e -> (Event) e)
                .collect(Collectors.toList());
        this.staffIndex = staffIndex;
        this.type = type;
    }

    public void set(List<ModelElement> list) {
        this.selectedElements = list
                .stream()
                .filter(e -> e instanceof Event).map(e -> (Event) e)
                .collect(Collectors.toList());
        this.staffIndex = -1;
        this.type = SelectionType.NOTE;
    }

    public void set(Event event, int staffIndex, SelectionType type) {
        this.selectedElements = new ArrayList<>();
        this.staffIndex = staffIndex;
        selectedElements.add(event);
        this.type = type;
    }

    public void clear() {
        selectedElements.clear();
    }

    @Override
    public boolean contains(Event event) {
        return (selectedElements.contains(event));
    }

    @Override
    public boolean hasStaffSelected(int index) {
        if (this.staffIndex < 0) {
            return false;
        } else {
            return staffIndex == index;
        }
    }

    @Override
    public int getSelectedStaff() {
        return staffIndex;
    }

    @Override
    public boolean isEmpty() {
        return selectedElements.isEmpty();
    }

//    @Override
//    public boolean hasCursor() {
//        return false; // TODO: MidiService.isRunning();
//    }
//
//    @Override
//    public long getCursorPosition() {
//        return 0; // TODO: MidiService.getPosition();
//    }

    @Override
    public boolean hasMouseDown() {
        return (mouseEndPosition > 0);
    }

    @Override
    public long getMouseLeftPosition() {
        return mouseStartPosition;
    }

    @Override
    public long getMouseRightPosition() {
        return mouseEndPosition;
    }

    @Override
    public int getMouseStaff() {
        return mouseStaff;
    }

    public void unsetMouseFrame() {
        mouseStartPosition = 0;
        mouseEndPosition = 0;
    }

    public void setMouseFrame(int staff, long mouseStartPosition, long mouseEndPosition) {
        mouseStaff = staff;
        this.mouseStartPosition = mouseStartPosition;
        this.mouseEndPosition = mouseEndPosition;
    }

    @Override
    public CwnSelection.SelectionType getSelectionType() {
        return type;
    }

    @Override
    public CwnPointer getPointer() {
        return pointer;
    }

    @Override
    public boolean hasSingleSelection() {
        return selectedElements.size()==1;
    }

    @Override
    public Event getSingleSelection() {
        return hasSingleSelection() ? selectedElements.get(0) : null;
    }

    public void setSingleSelection(Event event) {
        selectedElements.clear();
        selectedElements.add(event);
    }

    @Override
    public List<Event> getSelection() {
        return selectedElements;
    }

    public void setCursor(Location location) {
        if (location==null) {
            pointer.clear();
        } else {
            pointer.setPosition(location.position);
            pointer.setPitch(location.pitch);
            pointer.setStaffIndex(location.staffIndex);
            if (location.barConfig) {
                pointer.setRegion(CwnPointer.Region.CONFIG);
            } else if (location.position<0) {
                pointer.setRegion(CwnPointer.Region.NONE);
            } else {
                pointer.setRegion(CwnPointer.Region.SCORE);
            }
        }
    }
}
