package org.wuerthner.ambitus.model;

import java.util.ArrayList;
import java.util.Arrays;

import org.wuerthner.cwn.api.CwnBarEvent;
import org.wuerthner.sport.attribute.AttributeBuilder;
import org.wuerthner.sport.attribute.StringAttribute;
import org.wuerthner.sport.core.AbstractModelElement;

public class BarEvent extends AbstractModelElement implements CwnBarEvent, Event {
	public final static String TYPE = "BarEvent";
	public final static StringAttribute type = new AttributeBuilder("type")
			.defaultValue("")
			.buildStringAttribute();
	public BarEvent() {
		super(TYPE, Arrays.asList(), Arrays.asList(position, type));
	}

	public String getId() {
		return getAttributeValue(position) + " bar: " + getAttributeValue(type);
	}

	public String getName() {
		return getAttributeValue(position) + ": " + getAttributeValue(type);
	}
	
	@Override
	public long getPosition() {
		return getAttributeValue(position);
	}
	
	@Override
	public long getDuration() {
		return 0L;
	}
	
	@Override
	public String getTypeString() {
		return getAttributeValue(type);
	}

	public int getTypeIndex() {
		int index = -1;
		String myType = getAttributeValue(type);
		for (int i = 0; i < TYPES.length; i++) {
			if (myType.equals(TYPES[i])) {
				index = i;
				break;
			}
		}
		return index;
	}
}
