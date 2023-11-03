package org.wuerthner.ambitus.model;

import org.wuerthner.cwn.api.*;
import org.wuerthner.cwn.api.exception.TimeSignatureException;
import org.wuerthner.cwn.position.PositionTools;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.attribute.StringAttribute;
import org.wuerthner.sport.core.AbstractModelElement;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class InfoTrack extends AbstractModelElement implements CwnTrack {
    public final static String TYPE = "InfoTrack";
    public final static StringAttribute name = new StringAttribute("name")
            .defaultValue("info")
            .required();

    public final static Comparator eventComparator = new Comparator<ModelElement>() {
        @Override
        public int compare(ModelElement element1, ModelElement element2) {
            int comp = 0;
            if (element1 instanceof Event && element2 instanceof Event) {
                comp = Long.compare(((Event) element1).getPosition(), ((Event) element2).getPosition());
            }
            return comp;
        }
    };

    public InfoTrack() {
        super(TYPE, Arrays.asList(InfoEvent.TYPE, TimeSignatureEvent.TYPE),
                Arrays.asList(name));
    }

    public String getId() {
        return getAttributeValue(name);
    }

    @Override
    public void addEvent(CwnEvent cwnEvent) {
        throw new RuntimeException("addEvent not supported!");
    }

    @Override
    public int getPPQ() {
        ModelElement parent = getParent();
        if (parent.getType().equals(Arrangement.TYPE)) {
            return ((Arrangement)parent).getPPQ();
        }
        return 0;
    }

    @Override
    public String getName() { return getAttributeValue(name); }

    @Override
    public boolean getMute() { return true; }

    @Override
    public int getChannel() { return 0; }

    @Override
    public int getInstrument() { return 0; }

    @Override
    public int getVolume() { return 0; }

    @Override
    public List<? extends CwnEvent> getEvents() {
        return (List<? extends CwnEvent>)(List<?>) getChildren();
    }

    @Override
    public Trias nextBar(Trias trias) {
        return trias.nextBar();
    }

    @Override
    public long nextBar(long position) throws TimeSignatureException {
        return PositionTools.getPosition(this, PositionTools.getTrias(this, position).nextBar());
    }

    @Override
    public <T extends CwnEvent> List<T> getList(Class<T> eventClass) {
        return StreamSupport.stream(getChildrenByClass(eventClass).spliterator(), false).map(element -> eventClass.cast(element)).collect(Collectors.toList());
    }

    @Override
    public CwnTimeSignatureEvent getTimeSignature(long from) {
        CwnTimeSignatureEvent timeSignatureEvent = StreamSupport.stream(getChildrenByClass(CwnTimeSignatureEvent.class).spliterator(), false).map(element -> CwnTimeSignatureEvent.class.cast(element))
                .filter(event -> event.getPosition() <= from).map(event -> CwnTimeSignatureEvent.class.cast(event)).reduce((a, b) -> b).orElse(null);
        if (timeSignatureEvent == null) {
            throw new RuntimeException("Track must contain a time signature!");
        }
        return timeSignatureEvent;
    }

    @Override
    public CwnTimeSignatureEvent getTimeSignature(String from) {
        return getTimeSignature(PositionTools.getPosition(this, new Trias(from)));
    }

    @Override
    public CwnKeyEvent getKey(long l) { return null; }

    @Override
    public CwnClefEvent getClef(long l) { return null; }

    @Override
    public CwnNoteEvent getHighestNote() { return null; }

    @Override
    public CwnNoteEvent getLowestNote() { return null; }

    @Override
    public boolean getPiano() { return false; }

    @Override
    public boolean isInfoTrack() {
        return true;
    }

    @Override
    public <T extends CwnEvent> Optional<T> findEventAtPosition(long position, Class<T> eventClass) {
        Optional<T> first = getChildren()
                .stream()
                .filter(ev -> eventClass.isAssignableFrom(ev.getClass()) &&  ((T)ev).getPosition() == position)
                .map(ev -> (T) ev)
                .findFirst();
        return first;
    }

    @Override
    public <T extends CwnEvent> Optional<T> findFirstEvent(Class<T> clasz) {
        List<T> children = getChildrenByClass(clasz);
        return (children.size() > 0 ? Optional.of(clasz.cast(children.get(0))) : Optional.empty());
    }

    @Override
    public Comparator<ModelElement> getComparator() {
        return eventComparator;
    }
}
