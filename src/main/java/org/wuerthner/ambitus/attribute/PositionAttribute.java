package org.wuerthner.ambitus.attribute;

import org.wuerthner.ambitus.model.MidiTrack;
import org.wuerthner.cwn.api.CwnTrack;
import org.wuerthner.cwn.api.TimeSignature;
import org.wuerthner.cwn.position.PositionTools;
import org.wuerthner.sport.api.Check;
import org.wuerthner.sport.attribute.AbstractAttribute;

import java.util.List;

public class PositionAttribute extends AbstractAttribute<Long> {
    public PositionAttribute(String name, String label, Long defaultValue, boolean readonly, boolean required, boolean hidden,
                                  String description, List<Check> dependencies, List<Check> validators) {
        super(name, label, Long.class, defaultValue, readonly, required, hidden, description, dependencies, validators);
    }

    @Override
    public Long getValue(String stringValue) {
        if (stringValue == null) {
            return null;
        } else {
            return Long.valueOf(stringValue);
        }
    }

    public String getPositionPresentation(MidiTrack track, Long value) {
        if (value != null) {
            return PositionTools.getTrias((CwnTrack) track, value).toString();
        } else {
            return "";
        }
    }

    public Long getLongPresentation(MidiTrack track, String value) {
        if (value != null) {
            return PositionTools.getPosition((CwnTrack) track, value);
        } else {
            return null;
        }
    }
}
