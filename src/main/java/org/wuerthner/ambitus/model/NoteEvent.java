package org.wuerthner.ambitus.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import org.wuerthner.ambitus.attribute.AmbitusAttributeBuilder;
import org.wuerthner.ambitus.attribute.PitchAttribute;
import org.wuerthner.cwn.api.CwnAccent;
import org.wuerthner.cwn.api.CwnEvent;
import org.wuerthner.cwn.api.CwnNoteEvent;
import org.wuerthner.cwn.api.CwnTrack;
import org.wuerthner.cwn.position.PositionTools;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.attribute.*;
import org.wuerthner.sport.core.AbstractModelElement;

/**
 * The NoteEvent instances represent notes. Events are created on demand, all necessary information are contained within the underlying Message instances. When constructing NoteEvent instances, it is very important to use the setter-methods
 * ONLY BEFORE attaching the NoteEvent to a track! Otherwise the changes are lost to the undo-mechanism!
 **/
public class NoteEvent extends AbstractModelElement implements CwnNoteEvent, Event {
	public static final String TYPE = "NoteEvent";
	public static final String[] SHIFT = new String[] { "bb", "b", "No", "#", "x" };
	
	public final static LongAttribute duration = new LongAttribute("duration")
			.label("Duration")
			.defaultValue(0L);
	public final static PitchAttribute pitch = new PitchAttribute("pitch")
			.required()
			.label("Pitch")
			.defaultValue(0);
	public final static IntegerAttribute velocity = new IntegerAttribute("velocity")
			.required()
			.label("Velocity")
			.defaultValue(87);
	public final static SelectableIntegerAttribute shift = new SelectableIntegerAttribute("shift")
			.values(SHIFT)
			.defaultValue(0);
//	public final static IntegerAttribute tuplet = new IntegerAttribute("tuplet")
//			.defaultValue(0);
	public final static StringAttribute lyrics = new StringAttribute("lyrics")
			.defaultValue("");
	public final static IntegerAttribute stemDirection = new IntegerAttribute("stemDirection")
			.defaultValue(0);
	public final static BooleanAttribute isUngrouped = new BooleanAttribute("isUngrouped")
			.defaultValue(false);
	public final static IntegerAttribute voice = new IntegerAttribute("voice")
			.defaultValue(0)
			.label("Voice");
	public final static StaticListAttribute<Accent> accentList = new StaticListAttribute<Accent>("accents",Accent.class)
			.defaultValue(new ArrayList<Accent>());
	
	private final List<String> markList = new ArrayList<>();
	
	// private final Propagator<?, ?>[] propagators = new Propagator[] { new LyricsPropagator(this, lyrics) };
	
	public NoteEvent() {
		super(TYPE, Arrays.asList(), Arrays.asList(position, duration, pitch, velocity, shift, lyrics, stemDirection, isUngrouped, voice, accentList));
	}

	public String getId() {
		return getAttributeValue(position) + " pitch: " + getAttributeValue(pitch);
	}

	/**
	 * Returns the note's start position as a formatted string
	 **/
	public final String getStartFormatted() {
		CwnTrack track = (CwnTrack) getParent();
		return PositionTools.getTrias(track, getAttributeValue(position)).toString();
	}
	
	/**
	 * Returns the note's end position as a long integer
	 **/
	public final long getEnd() {
		return getAttributeValue(position) + getAttributeValue(duration);
	}
	
	/**
	 * Returns the duration of the note in ticks
	 **/
	public final long getDuration() {
		Long value = getAttributeValue(duration);
		return value==null ? 0 : value;
	}
	
	/**
	 * Returns the note's lyrics
	 **/
	public final String getLyrics() {
		return getAttributeValue(lyrics);
	}
	
	public boolean hasLyrics() {
		return getAttributeValue(lyrics) != null && !getAttributeValue(lyrics).equals("");
	}
	
	@Override
	public final boolean hasAccents() {
		List<Accent> aList = getAttributeValue(accentList);
		if (aList == null) {
			aList = new ArrayList<Accent>();
		}
		return !aList.isEmpty();
	}
	
	public final List<Accent> getAccents() {
		List<Accent> aList = getAttributeValue(accentList);
		if (aList == null) {
			aList = new ArrayList<Accent>();
		}
		return aList;
	}
	
	public final int getVelocity() {
		return getAttributeValue(velocity);
	}
	
	public final int getShift() {
		return getAttributeValue(shift);
	}
	
//	public final int getTuplet() {
//		return getAttributeValue(tuplet);
//	}
	
	public final int getEnharmonicShift() {
		return getAttributeValue(shift);
	}
	
	public final int getPitch() {
		return getAttributeValue(pitch);
	}
	
	public final String getCPitch() {
		return pitch.getStringPresentation(getAttributeValue(pitch));
	}
	
	
	/**
	 * Returns the note's start position as a long integer, (use <tt>new Position(note.getStart())</tt> to obtain the Position instance).
	 **/
	@Override
	public long getPosition() {
		return getAttributeValue(position);
	}
	
	@Override
	public int getVoice() {
		return getAttributeValue(voice);
	}
	
	@Override
	public int getStemDirection() {
		return getAttributeValue(stemDirection);
	}
	
	@Override
	public boolean isUngrouped() {
		return getAttributeValue(isUngrouped) == null ? false : getAttributeValue(isUngrouped);
	}
	
	@Override
	public List<? extends CwnAccent> getAccentList() {
		List<Accent> aList = getAttributeValue(accentList);
		if (aList == null) {
			aList = new ArrayList<>();
		}
		return aList;
	}

	@Override
	public void addAccent(CwnAccent cwnAccent) {
		throw new RuntimeException("addAccent not supported!");
	}

	@Override
	public void clearAccents() {
		throw new RuntimeException("Operation not supported!");
	}
	
	public final String toString() {
		// Accent[] accents = this.getAccents();
		String ornaments = "";
		String sep = " ";
		// for (Accent accent : accents) {
		// ornaments = sep + accent.getId();
		// }
		return getPosition() + " [" + getPitch() + ":" + getDuration() + "/" + getVelocity() + "] " + getCPitch() + ornaments;
	}
	
	public int compareTo(ModelElement element) {
		long thisPosition = getPosition();
		int thisPitch = getPitch();
		// if (thisPosition == 17 * 3840) {
		// System.out.println("compare NE: " + this + " to " + element);
		// }
		if (element instanceof CwnEvent) {
			CwnEvent event = (CwnEvent) element;
			long thatPosition = event.getPosition();
			if (thisPosition != thatPosition) {
				return (int) (thisPosition - thatPosition);
			} else {
				// if (thisPosition == 17 * 3840) {
				// System.out.println(" compare NE: " + this + " to " + element);
				// System.out.println(" " + this.getHandler().getType() + ", " + element.getHandler().getType());
				// }
				//
				if (element instanceof NoteEvent) {
					NoteEvent noteEvent = (NoteEvent) element;
					int thatPitch = noteEvent.getPitch();
					return thisPitch - thatPitch;
				}
			}
		}
		return 1;
	}
	
	@Override
	public void addMark(String mark) {
		markList.add(mark);
	}
	
	@Override
	public void clearMark() {
		markList.clear();
	}
	
	@Override
	public String getMarks() {
		return markList.stream().collect(Collectors.joining(", "));
	}

	@Override
	public List<String> getMarkList() { return markList; }
}
