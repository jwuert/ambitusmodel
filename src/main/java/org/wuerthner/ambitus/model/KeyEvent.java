package org.wuerthner.ambitus.model;

import java.util.ArrayList;
import java.util.Arrays;

import org.wuerthner.cwn.api.CwnKeyEvent;
import org.wuerthner.sport.attribute.AttributeBuilder;
import org.wuerthner.sport.attribute.IntegerAttribute;
import org.wuerthner.sport.core.AbstractModelElement;

public class KeyEvent extends AbstractModelElement implements CwnKeyEvent, Event {
	public final static String TYPE = "KeyEvent";
	public final static IntegerAttribute key = new AttributeBuilder("key")
			.required()
			.defaultValue(0)
			.buildSelectableIntegerAttribute();
	
	
	public KeyEvent() {
		super(TYPE, Arrays.asList(), Arrays.asList(position, key));
	}

	public String getId() {
		return getAttributeValue(position) + " key: " + getAttributeValue(key);
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
	public int getKey() {
		return getAttributeValue(key);
	}
	
	@Override
	public String toString() {
		return "KeyEvent={position: " + position + ", key: " + key + "}";
	}
}
