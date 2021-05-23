package org.wuerthner.ambitus.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import org.wuerthner.ambitus.attribute.AmbitusAttributeBuilder;
import org.wuerthner.ambitus.attribute.TimeSignatureAttribute;
import org.wuerthner.cwn.api.CwnTimeSignatureEvent;
import org.wuerthner.cwn.api.TimeSignature;
import org.wuerthner.cwn.timesignature.SimpleTimeSignature;
import org.wuerthner.sport.core.AbstractModelElement;

public class TimeSignatureEvent extends AbstractModelElement implements CwnTimeSignatureEvent, Event {
	public final static String TYPE = "TimeSignatureEvent";
	public final static TimeSignatureAttribute timeSignature = new AmbitusAttributeBuilder("timeSignature")
			.required()
			.defaultValue(new SimpleTimeSignature("4/4"))
			.buildTimeSignatureAttribute();

	public TimeSignatureEvent() {
		super(TYPE, Arrays.asList(), Arrays.asList(position, timeSignature));
	}

	public String getId() {
		return getAttributeValue(position) + " ts: " + getAttributeValue(timeSignature);
	}
	
	@Override
	public long getDuration() {
		return 0L;
	}
	
	@Override
	public TimeSignature getTimeSignature() {
		return getAttributeValue(timeSignature);
	}
	
	@Override
	public String toString() {
		return "TimeSignatureEvent={position: " + getPosition() + ", timeSignature: " + getTimeSignature() + "}";
	}
}
