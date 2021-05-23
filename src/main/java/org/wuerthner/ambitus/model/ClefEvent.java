package org.wuerthner.ambitus.model;

import java.util.ArrayList;
import java.util.Arrays;

import org.wuerthner.cwn.api.CwnClefEvent;
import org.wuerthner.sport.attribute.AttributeBuilder;
import org.wuerthner.sport.attribute.IntegerAttribute;
import org.wuerthner.sport.core.AbstractModelElement;

public class ClefEvent extends AbstractModelElement implements CwnClefEvent, Event {
	public final static String TYPE = "ClefEvent";
	public final static IntegerAttribute clef = new AttributeBuilder("clef")
			.required()
			.defaultValue(0)
			.buildSelectableIntegerAttribute();
	
	public ClefEvent() {
		super(TYPE, Arrays.asList(), Arrays.asList(position, clef));
	}

	public String getId() {
		return getAttributeValue(position) + " clef:" + getAttributeValue(clef);
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
	public int getClef() {
		return getAttributeValue(clef);
	}
	
	@Override
	public String toString() {
		return "SimpleTimeSignatureEvent={position: " + position + ", clef: " + clef + "}";
	}
}
