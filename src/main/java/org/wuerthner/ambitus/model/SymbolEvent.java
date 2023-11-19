package org.wuerthner.ambitus.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	public final static IntegerAttribute voice = new IntegerAttribute("voice")
			.defaultValue(0)
			.label("Voice");

	public SymbolEvent() {
		super(TYPE, Arrays.asList(), Arrays.asList(position, duration, name, verticalOffset, parameter, voice));
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

	@Override
	public int getVoice() {
		return getAttributeValue(voice);
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
		return (getSymbolName().equals("o"+SYMBOL_8VA) || getSymbolName().equals("o"+SYMBOL_15VA));
	}
	
	@Override
	public boolean isCase() {
		return (getSymbolName().equals(SYMBOL_CASE1) || getSymbolName().equals(SYMBOL_CASE2));
	}
	
	public String toString() {
		return "Symbol{name=" + getSymbolName() + ", position=" + getPosition() + ", duration=" + getDuration() + ", verticalOffset=" + getVerticalOffset() + ", parameter=" + getParameter() + "}";
	}

	public static boolean aboveStaff(String name) {
		return name.equals("o"+SYMBOL_8VA) || name.equals("o"+SYMBOL_15VA) || name.equals(SYMBOL_CASE1) || name.equals(SYMBOL_CASE2)
				|| name.equals(SYMBOL_LABEL1)|| name.equals(SYMBOL_LABEL2)|| name.equals(SYMBOL_LABEL3);
	}
	public static boolean belowStaffRange(String name) {
		return dynamic.contains(name);
	}

	public static boolean belowStaffPoint(String name) {
		return pointLike.contains(name);
	}

	public static boolean withinStaff(String name) {
		return name.equals(SYMBOL_BOWDOWN) || name.equals(SYMBOL_BOWUP);
	}

	public static List<String> dynamic = Arrays.asList(SYMBOL_CRESCENDO, SYMBOL_DECRESCENDO);

	public static List<String> pointLike = Arrays.asList(SYMBOL_F, SYMBOL_FF, SYMBOL_FFF,
			SYMBOL_FP, SYMBOL_MF, SYMBOL_P, SYMBOL_MP, SYMBOL_PP, SYMBOL_PPP, SYMBOL_SF, SYMBOL_SFF, SYMBOL_SFZ, SYMBOL_PEDAL1, SYMBOL_PEDAL2);
}
