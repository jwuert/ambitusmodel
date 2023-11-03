package org.wuerthner.ambitus.attribute;

import org.wuerthner.cwn.api.TimeSignature;
import org.wuerthner.cwn.timesignature.SimpleTimeSignature;
import org.wuerthner.sport.api.Check;
import org.wuerthner.sport.api.attributetype.Text;
import org.wuerthner.sport.attribute.AbstractAttribute;

import java.util.List;

public class TimeSignatureAttribute extends AbstractAttribute<TimeSignature,TimeSignatureAttribute,Text> {
    public TimeSignatureAttribute(String name) {
        super(name, TimeSignature.class, Text.class);
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