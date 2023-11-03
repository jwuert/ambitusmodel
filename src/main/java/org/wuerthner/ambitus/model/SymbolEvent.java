package org.wuerthner.ambitus.model;

import java.util.ArrayList;
import java.util.Arrays;

import org.wuerthner.cwn.api.CwnSymbolEvent;
import org.wuerthner.sport.attribute.IntegerAttribute;
import org.wuerthner.sport.attribute.LongAttribute;
import org.wuerthner.sport.attribute.StringAttribute;
import org.wuerthner.sport.core.AbstractModelElement;

public class SymbolEvent extends AbstractModelElement implements CwnSymbolEvent, Event {
	public final static String TYPE = "SymbolEvent";

	public final static StringAttribute name = new StringAttribute("name")
			.defaultValue("");
	public final static LongAttribute duration = new LongAttribute("duration")
			.label("Duration")
			.defaultValue(0L);
	public final static IntegerAttribute verticalOffset = new IntegerAttribute("verticalOffset")
			.label("Offset")
			.defaultValue(0);
	public final static IntegerAttribute parameter = new IntegerAttribute("parameter")
			.label("Parameter")
			.defaultValue(0);

	public SymbolEvent() {
		super(TYPE, Arrays.asList(), Arrays.asList(position, duration, name, verticalOffset, parameter));
	}

	public String getId() {
		return getAttributeValue(position) + " symbol: " + getAttributeValue(name);
	}
	
	@Override
	public String getSymbolName() {
		return getAttributeValue(SymbolEvent.name);
	}
	
	@Override
	public long getPosition() {
		return getAttributeValue(SymbolEvent.position);
	}
	
	@Override
	public long getDuration() {
		return getAttributeValue(duration);
	}
	
	@Override
	public int getVerticalOffset() {
		return getAttributeValue(verticalOffset);
	}
	
	@Override
	public int getParameter() {
		return getAttributeValue(parameter);
	}
	
	private int indexOf(String symbol) {
		int index = -1;
		for (int i = 0; i < SYMBOLS.length; i++) {
			if (symbol.equals(SYMBOLS[i])) {
				index = i;
				break;
			}
		}
		return index;
	}
	
	@Override
	public boolean isCrescendo() {
		return (getSymbolName().equals(SYMBOL_CRESCENDO));
	}
	
	@Override
	public boolean isDecrescendo() {
		return (getSymbolName().equals(SYMBOL_DECRESCENDO));
	}
	
	@Override
	public boolean isBowUp() {
		return (getSymbolName().equals(SYMBOL_BOWUP));
	}
	
	@Override
	public boolean isBowDown() {
		return (getSymbolName().equals(SYMBOL_BOWDOWN));
	}
	
	@Override
	public boolean isOctave() {
		return (getSymbolName().equals(SYMBOL_8VA) || getSymbolName().equals(SYMBOL_15VA));
	}
	
	@Override
	public boolean isCase() {
		return (getSymbolName().equals(SYMBOL_CASE1) || getSymbolName().equals(SYMBOL_CASE2));
	}
	
	public String toString() {
		return "Symbol{name=" + getSymbolName() + ", position=" + getPosition() + ", duration=" + getDuration() + ", verticalOffset=" + getVerticalOffset() + ", parameter=" + getParameter() + "}";
	}
}
