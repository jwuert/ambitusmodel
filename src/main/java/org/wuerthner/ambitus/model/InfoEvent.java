package org.wuerthner.ambitus.model;

import org.wuerthner.cwn.api.CwnEvent;
import org.wuerthner.cwn.api.CwnInfoEvent;
import org.wuerthner.cwn.api.CwnKeyEvent;
import org.wuerthner.sport.attribute.StringAttribute;
import org.wuerthner.sport.core.AbstractModelElement;

import java.util.Arrays;
import java.util.List;

public class InfoEvent extends AbstractModelElement implements CwnInfoEvent, Event {
    public final static String TYPE = "InfoEvent";

    public final static StringAttribute info = new StringAttribute("info")
            .defaultValue("");

    public final static StringAttribute type = new StringAttribute("type")
            .defaultValue("");

    public InfoEvent() {
        super(TYPE, Arrays.asList(), Arrays.asList(position, info, type));
    }

    public String getId() {
        return getAttributeValue(position) + " info: " + getAttributeValue(info) + ":" + getAttributeValue(type);
    }

    @Override
    public long getPosition() {
        return getAttributeValue(position);
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public String getName() {
        return getAttributeValue(info);
    }

    @Override
    public String getInfo(int i) {
        return i==0 ? getAttributeValue(info) : i==1 ? getAttributeValue(type) : "";
    }
}
