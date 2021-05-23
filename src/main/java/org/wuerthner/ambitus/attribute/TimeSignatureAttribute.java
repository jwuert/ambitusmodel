package org.wuerthner.ambitus.attribute;

import org.wuerthner.cwn.api.TimeSignature;
import org.wuerthner.cwn.timesignature.SimpleTimeSignature;
import org.wuerthner.sport.api.Check;
import org.wuerthner.sport.attribute.AbstractAttribute;

import java.util.List;

public class TimeSignatureAttribute extends AbstractAttribute<TimeSignature> {
    public TimeSignatureAttribute(String name, String label, TimeSignature defaultValue, boolean readonly, boolean required, boolean hidden,
                                  String description, List<Check> dependencies, List<Check> validators) {
        super(name, label, TimeSignature.class, defaultValue, readonly, required, hidden, description, dependencies, validators);
    }

    @Override
    public TimeSignature getValue(String stringValue) {
        if (stringValue == null || stringValue.trim().equals("")) {
            return null;
        } else {
            return new SimpleTimeSignature(stringValue);
        }
    }
}