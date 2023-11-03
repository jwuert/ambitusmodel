package org.wuerthner.ambitus.attribute;

import org.wuerthner.cwn.score.Score;
import org.wuerthner.sport.api.attributetype.Text;
import org.wuerthner.sport.attribute.AbstractAttribute;

public class PitchAttribute  extends AbstractAttribute<Integer,PitchAttribute, Text> implements Text {
    public PitchAttribute(String name) {
        super(name, Integer.class, Text.class);
    }

    @Override
    public Integer getValue(String stringValue) {
        if (stringValue == null) {
            return null;
        } else {
            Integer value;
            try {
                value = Integer.valueOf(stringValue);
            } catch (NumberFormatException nfe) {
                if (stringValue.trim().matches("^[a-z].*$")) {
                    value = Score.getPitchValueOldVersion(stringValue);
                } else {
                    value = Score.getPitch(stringValue);
                }
            }
            return value;
        }
    }

    @Override
    public String getStringPresentation(Integer value) {
        if (value != null) {
            return Score.getCPitch(value, 0);
        } else {
            return "";
        }
    }
}
