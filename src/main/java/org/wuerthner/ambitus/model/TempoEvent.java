package org.wuerthner.ambitus.model;

import java.util.ArrayList;
import java.util.Arrays;

import org.wuerthner.cwn.api.CwnTempoEvent;
import org.wuerthner.sport.attribute.IntegerAttribute;
import org.wuerthner.sport.attribute.StringAttribute;
import org.wuerthner.sport.core.AbstractModelElement;

public class TempoEvent extends AbstractModelElement implements CwnTempoEvent, Event {
	public final static String TYPE = "TempoEvent";
	public final static int DEFAULT_TEMPO = 120;
	public final static String DEFAULT_LABEL = "Allegro";

	public final static IntegerAttribute tempo = new IntegerAttribute("tempo")
			.defaultValue(DEFAULT_TEMPO);

	public final static StringAttribute label = new StringAttribute("label")
			.defaultValue(DEFAULT_LABEL);
	
	public TempoEvent() {
		super(TYPE, Arrays.asList(), Arrays.asList(position, tempo, label));
	}

	public String getId() {
		return getAttributeValue(position) + " tempo: " + getAttributeValue(tempo);
	}

	@Override
	public String getLabel() {
		return getAttributeValue(label);
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
	public int getTempo() {
		return getAttributeValue(tempo);
	}
	
	@Override
	public String toString() {
		return "TempoEvent={position: " + position + ", tempo: " + tempo + ", label: " + label + "}";
	}
}
