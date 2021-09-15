package org.wuerthner.ambitus.model;

import java.util.*;
import java.util.stream.Collectors;

import org.wuerthner.ambitus.attribute.AmbitusAttributeBuilder;
import org.wuerthner.ambitus.attribute.TimeSignatureAttribute;
import org.wuerthner.ambitus.tool.AbstractSelection;
import org.wuerthner.ambitus.type.NamedRange;
import org.wuerthner.cwn.api.*;
import org.wuerthner.cwn.position.PositionTools;
import org.wuerthner.cwn.timesignature.SimpleTimeSignature;
import org.wuerthner.sport.api.*;
import org.wuerthner.sport.attribute.*;
import org.wuerthner.sport.core.AbstractModelElement;
import org.wuerthner.sport.core.ModelClipboard;
import org.wuerthner.sport.core.ModelHistory;
import org.wuerthner.sport.operation.*;


public class Arrangement extends AbstractModelElement {
	public final static String TYPE = "Arrangement";

	public static final String DEFAULT_NAME = "Untitled";
	public static final int DEFAULT_PPQ = 960;
	public static final String[] KEYS = MidiTrack.KEYS;
	public static final String[] CLEFS = MidiTrack.CLEFS;
	public static final String[] GRIDS = new String[] { "1", "1/2", "1/4", "1/8", "1/16", "1/32", "1/64" };
	public static final String[] LEVELS = new String[] { "-", "1st level", "2nd level", "3rd level", "4th level", "5th level" };
	public static final String[] TUPLET_PRESENTATION = new String[] { "Short", "Full" };
	public static final String[] STRETCH_FACTORS = new String[] { "Narrow", "Normal", "Wide" };
	// public static final String[] DEVICES_IN = BaseRegistry.getInstance().getAttachment(Deviceclass).getInputDevices();
	// public static final String[] DEVICES_OUT = BaseRegistry.getInstance().getAttachment(Deviceclass).getOutputDevices();
	public static final Long DEFAULT_POSITION = 0L;
	public static final Integer DEFAULT_TEMPO = 100;
	public static final Integer DEFAULT_EXPOSE_VALUE = 2;
	public static final Integer DEFAULT_KEY = 7;
	public static final Integer DEFAULT_TRANSPOSE = 0;
	public static final TimeSignature DEFAULT_SIGNATURE = new SimpleTimeSignature("4/4");
	public static final Integer DEFAULT_RESOLUTION_INDEX = 4;
	public static final Integer DEFAULT_GRID_INDEX = 3;
	public static final Integer DEFAULT_SCORE_OBJECT_INDEX = 3;
	public static final Integer DEFAULT_SHIFT = 0;
	public static final Integer DEFAULT_VOICE_INDEX = 0;
	public static final Integer DEFAULT_BAR_OFFSET = 0;
	public static final Integer DEFAULT_TUPLET_INDEX = 0;
	public static final Integer DEFAULT_DOTS_INDEX = 0;
	public static final Integer DEFAULT_GROUP_LEVEL = 1;
	public static final Integer DEFAULT_TUPLET_PRESENTATION = 0;
	public static final Integer DEFAULT_STRETCH_FACTOR_INDEX = 1;
	public static final List<NamedRange> DEFAULT_RANGE_LIST = new ArrayList<>();
	public static AbstractSelection selection = null;
	
	public final static StringAttribute name = new AttributeBuilder("name")
			.defaultValue(DEFAULT_NAME)
			.required()
			.buildStringAttribute();
	public final static StringAttribute composer = new AttributeBuilder("composer")
			.defaultValue("")
			.buildStringAttribute();
	public final static StringAttribute subtitle = new AttributeBuilder("subtitle")
			.defaultValue("")
			.buildStringAttribute();
	public final static BooleanAttribute autoBeamPrint = new AttributeBuilder("autoBeamPrint")
			.defaultValue(true)
			.buildBooleanAttribute();
	public final static IntegerAttribute pulsePerQuarter = new AttributeBuilder("ppqn")
			.defaultValue(DEFAULT_PPQ)
			.required()
			.buildIntegerAttribute();
	public final static SelectableIntegerAttribute key = new AttributeBuilder("key")
			.values(KEYS)
			.defaultValue(DEFAULT_KEY)
			.buildSelectableIntegerAttribute();
	public final static TimeSignatureAttribute timeSignature = new AmbitusAttributeBuilder("timeSignature")
			.defaultValue(DEFAULT_SIGNATURE)
			.buildTimeSignatureAttribute();
	public final static BooleanAttribute flagAllowDottedRests = new AttributeBuilder("flagAllowDottedRests")
			.defaultValue(true)
			.buildBooleanAttribute();
	// public final static BooleanAttribute flagSplitRests = new BooleanAttribute(new AttributeInit<>("flagSplitRests", true, false));
	public final static BooleanAttribute durationBiDotted = new AttributeBuilder("durationBiDotted")
			.defaultValue(false)
			.buildBooleanAttribute();
	public final static BooleanAttribute durationTuplet2 = new AttributeBuilder("durationTuplet2")
			.defaultValue(false)
			.buildBooleanAttribute();
	public final static BooleanAttribute durationTuplet3 = new AttributeBuilder("durationTuplet3")
			.defaultValue(false)
			.buildBooleanAttribute();
	public final static BooleanAttribute durationTuplet4 = new AttributeBuilder("durationTuplet4")
			.defaultValue(false)
			.buildBooleanAttribute();
	public final static BooleanAttribute durationTuplet5 = new AttributeBuilder("durationTuplet5")
			.defaultValue(false)
			.buildBooleanAttribute();
	public final static BooleanAttribute durationTuplet6 = new AttributeBuilder("durationTuplet6")
			.defaultValue(false)
			.buildBooleanAttribute();
	public final static SelectableIntegerAttribute groupLevel = new AttributeBuilder("groupLevel")
			.defaultValue(DEFAULT_GROUP_LEVEL)
			.values(LEVELS)
			.buildSelectableIntegerAttribute();
	public final static SelectableIntegerAttribute tupletPresentation = new AttributeBuilder("tupletPresentation")
			.defaultValue(DEFAULT_TUPLET_PRESENTATION)
			.values(TUPLET_PRESENTATION)
			.buildSelectableIntegerAttribute();
	public final static SelectableIntegerAttribute stretchFactor = new AttributeBuilder("stretchFactor")
			.defaultValue(DEFAULT_STRETCH_FACTOR_INDEX)
			.values(STRETCH_FACTORS)
			.buildSelectableIntegerAttribute();
	public final static SelectableIntegerAttribute grid = new AttributeBuilder("grid")
			.defaultValue(DEFAULT_GRID_INDEX)
			.values(GRIDS)
			.buildSelectableIntegerAttribute();
	public final static SelectableIntegerAttribute resolution = new AttributeBuilder("resolution")
			.defaultValue(DEFAULT_RESOLUTION_INDEX)
			.values(GRIDS)
			.buildSelectableIntegerAttribute();
	public final static ListAttribute<NamedRange> rangeList = new AttributeBuilder("ranges")
			.defaultValue(DEFAULT_RANGE_LIST)
			.buildListAttribute(NamedRange.class);
	public final static StringAttribute path = new AttributeBuilder("path")
			.defaultValue(null)
			.buildStringAttribute();

	public final static IntegerAttribute offset = new AttributeBuilder("offset")
			.defaultValue(DEFAULT_BAR_OFFSET)
			.buildIntegerAttribute();

	private final ModelHistory history = new ModelHistory();
	private final Clipboard clipboard = new ModelClipboard<Event>();

	public Arrangement() {
		super(TYPE, Arrays.asList(MidiTrack.TYPE), Arrays.asList(name, subtitle, composer, autoBeamPrint, pulsePerQuarter,
				key, timeSignature, stretchFactor, groupLevel, tupletPresentation, grid, resolution, rangeList, path,
				flagAllowDottedRests, durationBiDotted, durationTuplet2, durationTuplet3, durationTuplet4, durationTuplet5, durationTuplet6,
				offset));
	}

	public String getId() {
		return this.getAttributeValue(name);
	}

	public int getNumberOfActiveMidiTracks() {
		return getChildrenByClass(MidiTrack.class).size();
	}

	public List<MidiTrack> getActiveMidiTrackList() {
		return getChildrenByClass(MidiTrack.class).stream().map(e -> MidiTrack.class.cast(e)).filter(tr -> tr.isActive()).collect(Collectors.toList());
	}
	//
	// Selection
	//
	public AbstractSelection getSelection() {
		return selection;
	}

	public void setSelection(AbstractSelection selection) {
		this.selection = selection;
	}

	public Clipboard getClipboard() { return clipboard; }

	public boolean hasUndo() {
		return history.hasUndo();
	}

	public boolean hasRedo() {
		return history.hasRedo();
	}

	public void clearHistory() {
		history.clear();
	}

	public History getHistory() {
		return history;
	}

	//
	// MIDI TRACK
	//
	public MidiTrack getSelectedMidiTrack() {
		int selectedStaff = selection.getSelectedStaff();
		int no = getNumberOfActiveMidiTracks();
		if (selectedStaff < 0 || selectedStaff >= no) {
			return null;
		}
		return getActiveMidiTrackList().get(selectedStaff);
	}

	public Optional<MidiTrack> getMidiTrack(String id) {
		return getChildrenByClass(MidiTrack.class).stream().peek(System.out::println).filter(t -> t.getId().equals(id)).findAny();
	}

	public void setTrackMute(String id, boolean mute) {
		Optional<MidiTrack> track  = getMidiTrack(id);
		if (track.isPresent()) {
			track.get().performSetAttributeValueOperation(MidiTrack.mute, mute, history);
		}
	}

	public void setTrackMute(MidiTrack track, boolean mute) {
		track.performSetAttributeValueOperation(MidiTrack.mute, mute, history);
	}
	public void setTrackName(String id, String name) {
		Optional<MidiTrack> track  = getMidiTrack(id);
		if (track.isPresent()) {
			track.get().performSetAttributeValueOperation(MidiTrack.name, name, history);
		}
	}

	public void setTrackName(MidiTrack track, String name) {
		track.performSetAttributeValueOperation(MidiTrack.name, name, history);
	}

	public void setTrackMetric(String id, String metric) {
		Optional<MidiTrack> trackOptional  = getMidiTrack(id);
		if (trackOptional.isPresent()) {
			MidiTrack track = trackOptional.get();
			setTrackMetric(track, metric);
		}
	}

	public void setTrackMetric(MidiTrack track, String metric) {
		TimeSignatureEvent tsEvent = track.findFirstEvent(TimeSignatureEvent.class).get();
		TimeSignature ts = new SimpleTimeSignature(metric);
		Operation o1 = new SetAttributeValueOperation<>(track, MidiTrack.timeSignature, ts);
		Operation o2 = new SetAttributeValueOperation<>(tsEvent, TimeSignatureEvent.timeSignature, ts);
		Transaction transaction = new Transaction("TimeSignature="+metric, o1, o2);
		track.performTransaction(transaction, history);
	}

	public void setTrackKey(String id, int key) {
		Optional<MidiTrack> trackOptional  = getMidiTrack(id);
		if (trackOptional.isPresent()) {
			MidiTrack track = trackOptional.get();
			setTrackKey(track, key);
		}
	}

	public void setTrackKey(MidiTrack track, int key) {
		KeyEvent keyEvent = track.findFirstEvent(KeyEvent.class).get();
		Operation o1 = new SetAttributeValueOperation<>(track, MidiTrack.key, key);
		Operation o2 = new SetAttributeValueOperation<>(keyEvent, KeyEvent.key, key-7);
		Transaction transaction = new Transaction("Key="+key, o1, o2);
		track.performTransaction(transaction, history);
	}

	public void setTrackClef(String id, int clef) {
		Optional<MidiTrack> trackOptional  = getMidiTrack(id);
		if (trackOptional.isPresent()) {
			MidiTrack track = trackOptional.get();
			setTrackClef(track, clef);
		}
	}

	public void setTrackClef(MidiTrack track, int clef) {
		ClefEvent clefEvent = track.findFirstEvent(ClefEvent.class).get();
		Operation o1 = new SetAttributeValueOperation<>(track, MidiTrack.clef, clef);
		Operation o2 = new SetAttributeValueOperation<>(clefEvent, ClefEvent.clef, clef);
		Transaction transaction = new Transaction("Clef="+clef, o1, o2);
		track.performTransaction(transaction, history);
	}

	public void setTrackTempo(String id, int tempo) {
		Optional<MidiTrack> trackOptional  = getMidiTrack(id);
		if (trackOptional.isPresent()) {
			MidiTrack track = trackOptional.get();
			TempoEvent tempoEvent = track.findFirstEvent(TempoEvent.class).get();
			Operation o1 = new SetAttributeValueOperation<>(track, MidiTrack.tempo, tempo);
			Operation o2 = new SetAttributeValueOperation<>(tempoEvent, TempoEvent.tempo, tempo);
			Operation o3 = new SetAttributeValueOperation<>(tempoEvent, TempoEvent.label, MidiTrack.getTempoLabel(tempo));
			Transaction transaction = new Transaction("Tempo="+tempo, o1, o2, o3);
			track.performTransaction(transaction, history);
		}
	}

	public void setTrackInstrument(String id, int instrument) {
		Optional<MidiTrack> track  = getMidiTrack(id);
		if (track.isPresent()) {
			track.get().performSetAttributeValueOperation(MidiTrack.instrument, instrument, history);
		}
	}

	public void setTrackInstrument(MidiTrack track, int instrument) {
		track.performSetAttributeValueOperation(MidiTrack.instrument, instrument, history);
	}

	public void setTrackChannel(String id, int channel) {
		Optional<MidiTrack> track  = getMidiTrack(id);
		if (track.isPresent()) {
			track.get().performSetAttributeValueOperation(MidiTrack.channel, channel, history);
		}
	}

	public void setTrackChannel(MidiTrack track, int channel) {
		track.performSetAttributeValueOperation(MidiTrack.channel, channel, history);
	}

	public Integer getFirstFreeChannel() {
		Integer channel = null;
		List<Integer> channels = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15));
		for (MidiTrack track : getActiveMidiTrackList()) {
			channels.remove(Integer.valueOf(track.getChannel()));
		}
		if (channels.size() > 0) {
			Collections.sort(channels);
			channel = channels.get(0);
		}
		return channel;
	}

	public void setTrackProperties(String id, boolean mute, String name, String metric, int key, int clef, int tempo, int instrument, int channel) {
		Optional<MidiTrack> trackOptional  = getMidiTrack(id);
		if (trackOptional.isPresent()) {
			MidiTrack track = trackOptional.get();
			TimeSignatureEvent tsEvent = track.findFirstEvent(TimeSignatureEvent.class).get();
			KeyEvent keyEvent = track.findFirstEvent(KeyEvent.class).get();
			ClefEvent clefEvent = track.findFirstEvent(ClefEvent.class).get();
			Optional<TempoEvent> tempoEventOptional = track.findFirstEvent(TempoEvent.class);
			TimeSignature ts = new SimpleTimeSignature(metric);
			List<Operation> opList = new ArrayList<>();
			opList.add(new SetAttributeValueOperation<>(track, MidiTrack.mute, mute));
			opList.add(new SetAttributeValueOperation<>(track, MidiTrack.name, name));
			opList.add(new SetAttributeValueOperation<>(track, MidiTrack.timeSignature, ts));
			Operation opTimeSignature = new SetAttributeValueOperation<>(tsEvent, TimeSignatureEvent.timeSignature, ts);
			opList.add(opTimeSignature);
			opList.add(new SetAttributeValueOperation<>(track, MidiTrack.key, key));
			opList.add(new SetAttributeValueOperation<>(keyEvent, KeyEvent.key, key-7));
			opList.add(new SetAttributeValueOperation<>(track, MidiTrack.clef, clef));
			opList.add(new SetAttributeValueOperation<>(clefEvent, ClefEvent.clef, clef));
			opList.add(new SetAttributeValueOperation<>(track, MidiTrack.tempo, tempo));
			if (tempoEventOptional.isPresent()) {
				opList.add(new SetAttributeValueOperation<>(tempoEventOptional.get(), TempoEvent.tempo, tempo));
				opList.add(new SetAttributeValueOperation<>(tempoEventOptional.get(), TempoEvent.label, MidiTrack.getTempoLabel(tempo)));
			}
			opList.add(new SetAttributeValueOperation<>(track, MidiTrack.instrument, instrument));
			opList.add(new SetAttributeValueOperation<>(track, MidiTrack.channel, channel));
			Transaction transaction = new Transaction("Change track properties", opList);
			track.performTransaction(transaction, history);
			TimeSignature newValue = ((SetAttributeValueOperation<TimeSignature>) opTimeSignature).getNewValue();
			TimeSignature oldValue = ((SetAttributeValueOperation<TimeSignature>) opTimeSignature).getOldValue();
			if (!newValue.toString().equals(oldValue.toString())) {
				// when time signature changes, all key/clef/ts changes are removed
				opList = new ArrayList<>();
				opList.addAll(getAllEventsButFirst(track, TimeSignatureEvent.class));
				opList.addAll(getAllEventsButFirst(track, KeyEvent.class));
				opList.addAll(getAllEventsButFirst(track, ClefEvent.class));
				Transaction removeTransaction = new Transaction("Remove (" + opList.size() + ") events", opList);
				track.performTransaction(removeTransaction, history);
			}
		}
	}

	private List<? extends Operation> getAllEventsButFirst(MidiTrack track, Class<? extends Event> clasz) {
		List<Operation> opList = new ArrayList<>();
		List<? extends Event> eventList = track.getChildrenByClass(clasz);
		Iterator<? extends Event> iterator = eventList.iterator();
		for (iterator.next(); iterator.hasNext(); ) { opList.add(new RemoveChildOperation(iterator.next())); }
		return opList;
	}

	public void setBarProperties(String trackId, long barPosition, String metric, int key, int clef, String bar, int tempo, AmbitusFactory factory) {
		System.out.println("SET BAR PROP");
		TimeSignature timeSignature = new SimpleTimeSignature(metric);
		Optional<MidiTrack> trackOptional  = getMidiTrack(trackId);
		if (trackOptional.isPresent()) {
			MidiTrack track = trackOptional.get();
			long position = PositionTools.firstBeat(track, barPosition);
			handleEventSettingInBar(track, position, TimeSignatureEvent.class, TimeSignatureEvent.TYPE, TimeSignature.class, TimeSignatureEvent.timeSignature, timeSignature, factory);
			handleEventSettingInBar(track, position, KeyEvent.class, KeyEvent.TYPE, Integer.class, KeyEvent.key, key-7, factory);
			handleEventSettingInBar(track, position, ClefEvent.class, ClefEvent.TYPE, Integer.class, ClefEvent.clef, clef, factory);
			if (bar.equals(CwnBarEvent.STANDARD)) {
			} else {
				handleEventSettingInBar(track, position, BarEvent.class, BarEvent.TYPE, String.class, BarEvent.type, bar, factory);
			}
			handleEventSettingInBar(track, position, TempoEvent.class, TempoEvent.TYPE, Integer.class, TempoEvent.tempo, tempo, factory);
			if (position==0) {
				track.performTransientSetAttributeValueOperation(MidiTrack.timeSignature, timeSignature);
				track.performTransientSetAttributeValueOperation(MidiTrack.key, key);
				track.performTransientSetAttributeValueOperation(MidiTrack.clef, clef);
			}
		} else {
			System.err.println("Track " + trackId + " not found!");
		}
	}

	private <EVENT extends Event, TYPE> void handleEventSettingInBar(MidiTrack track, long position, Class<EVENT> eventClass, String eventType,
																	 Class<TYPE> typeClass, Attribute<TYPE> attribute, TYPE value, AmbitusFactory factory) {
		Optional<EVENT> recentEventOptional = track.findEventBefore(position, eventClass);
		boolean changeRequired = false;
		if (!recentEventOptional.isPresent()) {
			// throw new RuntimeException("Previous Event unexpectedly does not exist!");
			changeRequired = true;
		} else {
			EVENT recentEvent = recentEventOptional.get();
			changeRequired = (!recentEvent.getAttributeValue(attribute).toString().equals(value.toString()));
		}
		// lookup event in bar:
		Optional<EVENT> eventOptional = track.findFirstEventAtPosition(position, eventClass);
		if (eventOptional.isPresent()) {
			if (changeRequired) {
				Event event = eventOptional.get();
				List<Operation> opList = new ArrayList<>();
				opList.add(new SetAttributeValueOperation<>(event, attribute, value));
				opList.add(new SetAttributeValueOperation<>(event, Event.position, position));
				if (eventType.equals(TempoEvent.TYPE)) {
					opList.add(new SetAttributeValueOperation<>(event, TempoEvent.label, MidiTrack.getTempoLabel((int) value)));
				}
				Transaction transaction = new Transaction("Change " + attribute.getName(), opList);
				track.performTransaction(transaction, history);
			} else {
				track.performRemoveChildOperation(eventOptional.get(), history);
			}
		} else {
			if (changeRequired) {
				EVENT event = factory.createElement(eventType);
				List<Operation> opList = new ArrayList<>();
				opList.add(new SetAttributeValueOperation<>(event, attribute, value));
				opList.add(new SetAttributeValueOperation<>(event, Event.position, position));
				if (eventType.equals(TempoEvent.TYPE)) {
					opList.add(new SetAttributeValueOperation<>(event, TempoEvent.label, MidiTrack.getTempoLabel((int) value)));
				}
				opList.add(new AddChildOperation(track, event));
				Transaction transaction = new Transaction("Add " + attribute.getName(), opList);
				track.performTransaction(transaction, history);
			} else {
				// nothing to do here!
			}
		}
	}

	public void setConfiguration(String title, String subtitle, String composer, int ppq, int level, int resolution, int tupletPres, int stretchFac,
								 boolean dottedRests, boolean biDotted, boolean tuplet2, boolean tuplet3, boolean tuplet4, boolean tuplet5, boolean tuplet6) {
		List<Operation> opList = new ArrayList<>();
		if (!getAttributeValue(Arrangement.name, "").equals(title)) {
			opList.add(new SetAttributeValueOperation<>(this, Arrangement.name, title));
		}
		if (!getAttributeValue(Arrangement.subtitle, "").equals(subtitle)) {
			opList.add(new SetAttributeValueOperation<>(this, Arrangement.subtitle, subtitle));
		}
		if (!getAttributeValue(Arrangement.composer, "").equals(composer)) {
			opList.add(new SetAttributeValueOperation<>(this, Arrangement.composer, composer));
		}
		if (!getAttributeValue(Arrangement.pulsePerQuarter, DEFAULT_PPQ).equals(ppq)) {
			opList.add(new SetAttributeValueOperation<>(this, Arrangement.pulsePerQuarter, ppq));
		}
		if (!getAttributeValue(Arrangement.groupLevel, DEFAULT_GROUP_LEVEL).equals(level)) {
			opList.add(new SetAttributeValueOperation<>(this, Arrangement.groupLevel, level));
		}
		if (!getAttributeValue(Arrangement.resolution, DEFAULT_RESOLUTION_INDEX).equals(resolution)) {
			opList.add(new SetAttributeValueOperation<>(this, Arrangement.resolution, resolution));
		}
		if (!getAttributeValue(Arrangement.tupletPresentation, DEFAULT_TUPLET_PRESENTATION).equals(tupletPres)) {
			opList.add(new SetAttributeValueOperation<>(this, Arrangement.tupletPresentation, tupletPres));
		}
		if (!getAttributeValue(Arrangement.stretchFactor, DEFAULT_STRETCH_FACTOR_INDEX).equals(stretchFac)) {
			opList.add(new SetAttributeValueOperation<>(this, Arrangement.stretchFactor, stretchFac));
		}
		if (!getAttributeValue(Arrangement.flagAllowDottedRests, true).equals(dottedRests)) {
			opList.add(new SetAttributeValueOperation<>(this, Arrangement.flagAllowDottedRests, dottedRests));
		}
		if (!getAttributeValue(Arrangement.durationBiDotted, false).equals(biDotted)) {
			opList.add(new SetAttributeValueOperation<>(this, Arrangement.durationBiDotted, biDotted));
		}
		if (!getAttributeValue(Arrangement.durationTuplet2, false).equals(tuplet2)) {
			opList.add(new SetAttributeValueOperation<>(this, Arrangement.durationTuplet2, tuplet2));
		}
		if (!getAttributeValue(Arrangement.durationTuplet3, false).equals(tuplet3)) {
			opList.add(new SetAttributeValueOperation<>(this, Arrangement.durationTuplet3, tuplet3));
		}
		if (!getAttributeValue(Arrangement.durationTuplet4, false).equals(tuplet4)) {
			opList.add(new SetAttributeValueOperation<>(this, Arrangement.durationTuplet4, tuplet4));
		}
		if (!getAttributeValue(Arrangement.durationTuplet5, false).equals(tuplet5)) {
			opList.add(new SetAttributeValueOperation<>(this, Arrangement.durationTuplet5, tuplet5));
		}
		if (!getAttributeValue(Arrangement.durationTuplet6, false).equals(tuplet6)) {
			opList.add(new SetAttributeValueOperation<>(this, Arrangement.durationTuplet6, tuplet6));
		}
		// for (Operation op : opList) System.out.println(" = " + op.info());
		Transaction transaction = new Transaction("Change Configuration", opList);
		this.performTransaction(transaction, history);
	}

	public void addEvent(MidiTrack track, Event event) {
		track.performAddChildOperation(event, history);
	}

	public void addTrack(ModelElementFactory factory) {
		Integer channel = getFirstFreeChannel();
		if (channel!=null) {
			MidiTrack track = factory.createElement(MidiTrack.TYPE);
			track.performTransientSetAttributeValueOperation(MidiTrack.name, MidiTrack.DEFAULT_NAME);
			TimeSignatureEvent ts = factory.createElement(TimeSignatureEvent.TYPE);
			ClefEvent clef = factory.createElement(ClefEvent.TYPE);
			KeyEvent key = factory.createElement(KeyEvent.TYPE);
			track.performTransientAddChildOperation(ts);
			track.performTransientAddChildOperation(key);
			track.performTransientAddChildOperation(clef);
			if (channel==0) {
				TempoEvent tempo = factory.createElement(TempoEvent.TYPE);
				track.performTransientAddChildOperation(tempo);
			}
			track.performTransientSetAttributeValueOperation(MidiTrack.channel, channel);
			this.performAddChildOperation(track, history);
		} else {
			System.err.println("No free channel available!");
		}
	}

	public void init(String title, String subtitle, String composer) {
		this.performTransientSetAttributeValueOperation(Arrangement.name, title);
		this.performTransientSetAttributeValueOperation(Arrangement.subtitle, subtitle);
		this.performTransientSetAttributeValueOperation(Arrangement.composer, composer);
	}
//	public void setNotePitch(NoteEvent noteEvent, int pitch) {
//		noteEvent.performSetAttributeValueOperation(NoteEvent.pitch, pitch+1, history);
//	}

	public void changeSelectionPitch(List<CwnEvent> eventList, int pitchChange) {
		List<Operation> opList = new ArrayList<>();
		for (CwnEvent event: eventList) {
			if (event instanceof NoteEvent) {
				NoteEvent noteEvent = (NoteEvent) event;
				if (pitchChange != 0) {
					// PITCH
					Integer pitch = noteEvent.getAttributeValue(NoteEvent.pitch);
					Operation op = new SetAttributeValueOperation<>(noteEvent, NoteEvent.pitch, pitch + pitchChange);
					opList.add(op);
				}
			}
		}
		if (opList.size()>0) {
			Transaction transaction = new Transaction("Change selection: pitch", opList);
			this.performTransaction(transaction, history);
		}
	}

	public void changeSelectionPosition(List<CwnEvent> eventList, int positionChange) {
		List<Operation> opList = new ArrayList<>();
		for (CwnEvent event: eventList) {
			if (event instanceof Event) {
				Event theEvent = (Event) event;
				if (positionChange != 0) {
					// POSITION
					int unit = (int) (getAttributeValue(pulsePerQuarter) * 4.0 / Math.pow(2, getAttributeValue(Arrangement.resolution)));
					long change = unit * positionChange;
					Long position = theEvent.getAttributeValue(Event.position);
					Operation op = new SetAttributeValueOperation<>(theEvent, NoteEvent.position, position + change);
					opList.add(op);
				}
			}
		}
		if (opList.size()>0) {
			Transaction transaction = new Transaction("Change selection: position", opList);
			this.performTransaction(transaction, history);
		}
	}

	public void changeSelectionDuration(List<CwnEvent> eventList, int durationChange, int dots) {
		List<Operation> opList = new ArrayList<>();
		for (CwnEvent event: eventList) {
			if (event instanceof NoteEvent) {
				NoteEvent noteEvent = (NoteEvent) event;
				// DURATION
				Long duration = noteEvent.getAttributeValue(NoteEvent.duration);
				int durationDiff = 0;
				if (durationChange!=0) {
					int unit = (int) (getAttributeValue(pulsePerQuarter) * 4.0 / Math.pow(2, getAttributeValue(Arrangement.resolution)));
					durationDiff = unit * durationChange;
				}
				float dotFactor = 1.0f;
				if (dots > 0) {
					// dotChange = 1: (1 + ((2^1 -1) / 2^1 )) = 1.5
					// dotChange = 2: (1 + ((2^2 -1) / 2^2 )) = 1.75
					// dotChange = 3: (1 + ((3^1 -1) / 3^1 )) = 1.875
					dotFactor = (float) (1 + ((Math.pow(2, dots) - 1) / Math.pow(2, dots)));
				}
				Operation op = new SetAttributeValueOperation<>(noteEvent, NoteEvent.duration, (long) ((duration + durationDiff) * dotFactor));
				opList.add(op);
			}
		}
		if (opList.size()>0) {
			Transaction transaction = new Transaction("Change selection: duration", opList);
			this.performTransaction(transaction, history);
		}
	}

	public void changeSelectionEnharmonicShift(List<CwnEvent> eventList, int value) {
		List<Operation> opList = new ArrayList<>();
		for (CwnEvent event: eventList) {
			if (event instanceof NoteEvent) {
				NoteEvent noteEvent = (NoteEvent) event;
				if (value>=-2 && value<=2) {
					// ENHARMONIC SHIFT
					Operation op = new SetAttributeValueOperation<>(noteEvent, NoteEvent.shift, value);
					opList.add(op);
				}
			}
		}
		if (opList.size()>0) {
			Transaction transaction = new Transaction("Change selection: enharmonic shift", opList);
			this.performTransaction(transaction, history);
		}
	}

	public void changeSelectionVoice(List<CwnEvent> eventList, int value) {
		List<Operation> opList = new ArrayList<>();
		for (CwnEvent event: eventList) {
			if (event instanceof NoteEvent) {
				NoteEvent noteEvent = (NoteEvent) event;
				if (value>=0 && value<=4) {
					// VOICE
					Operation op = new SetAttributeValueOperation<>(noteEvent, NoteEvent.voice, value);
					opList.add(op);
				}
			}
		}
		if (opList.size()>0) {
			Transaction transaction = new Transaction("Change selection: voice", opList);
			this.performTransaction(transaction, history);
		}
	}

	public void deleteElements(List<Event> eventList) {
		List<Operation> opList = new ArrayList<>();
		for (CwnEvent cwnEvent: eventList) {
			Event event = (Event) cwnEvent;
			Operation operation = new RemoveChildOperation(event);
			opList.add(operation);
		}
		if (opList.size()>0) {
			Transaction transaction = new Transaction("Remove selection", opList);
			this.performTransaction(transaction, history);
		}
	}

//	public void setSelectionProperties(List<CwnEvent> eventList, int pitchChange, int positionChange, int durationChange, int dotChange,
//									   int enhChange, int voiceChange) {
//		List<Operation> opList = new ArrayList<>();
//		for (CwnEvent event: eventList) {
//			if (event instanceof NoteEvent) {
//				NoteEvent noteEvent = (NoteEvent) event;
//				if (pitchChange!=0) {
//					// PITCH
//					Integer pitch = noteEvent.getAttributeValue(NoteEvent.pitch);
//					Operation op = new SetAttributeValueOperation<>(noteEvent, NoteEvent.pitch, pitch + pitchChange);
//					opList.add(op);
//				}
//				if (durationChange!=0 || dotChange>0) {
//					// DURATION
//					Long duration = noteEvent.getAttributeValue(NoteEvent.duration);
//					int durationDiff = 0;
//					if (durationChange!=0) {
//						int unit = (int) (getAttributeValue(pulsePerQuarter) * 4.0 / Math.pow(2, getAttributeValue(Arrangement.resolution)));
//						durationDiff = unit * durationChange;
//					}
//					float dotFactor = 1.0f;
//					if (dotChange > 0) {
//						// dotChange = 1: (1 + ((2^1 -1) / 2^1 )) = 1.5
//						// dotChange = 2: (1 + ((2^2 -1) / 2^2 )) = 1.75
//						// dotChange = 3: (1 + ((3^1 -1) / 3^1 )) = 1.875
//						dotFactor = (float) (1 + ((Math.pow(2, dotChange) - 1) / Math.pow(2, dotChange)));
//					}
//					Operation op = new SetAttributeValueOperation<>(noteEvent, NoteEvent.duration, (long) ((duration + durationDiff) * dotFactor));
//					opList.add(op);
//				}
//				if (enhChange > -3) {
//					// ENHARMONIC SHIFT
//					Integer enh = noteEvent.getAttributeValue(NoteEvent.shift);
//					enh = Math.min(2, Math.max(-2, enh + enhChange));
//					Operation op = new SetAttributeValueOperation<>(noteEvent, NoteEvent.shift, enh);
//					opList.add(op);
//				}
//				if (voiceChange>=0) {
//					// VOICE
//					Integer voice = voiceChange;
//					Operation op = new SetAttributeValueOperation<>(noteEvent, NoteEvent.voice, voice);
//					opList.add(op);
//				}
//			}
//			if (event instanceof Event) {
//				Event theEvent = (Event) event;
//				if (positionChange!=0) {
//					// POSITION
//					Long position = theEvent.getAttributeValue(Event.position);
//					Operation op = new SetAttributeValueOperation<>(theEvent, NoteEvent.position, position + positionChange);
//					opList.add(op);
//				}
//			}
//		}
//		Transaction transaction = new Transaction("Change Selection", opList);
//		this.performTransaction(transaction, history);
//	}

	public String getTrackBarMetric(String trackId, long barPosition) {
		String result = "";
		Optional<MidiTrack> trackOptional  = getMidiTrack(trackId);
		if (trackOptional.isPresent()) {
			MidiTrack track = trackOptional.get();
			long position = PositionTools.firstBeat(track, barPosition);
			result = track.getBarTimeSignature(position).toString();
		}
		return result;
	}

	public String getTrackBarMetric(int trackIndex, long barPosition) {
		String result = "";
		List<MidiTrack> trackList = getActiveMidiTrackList();
		if (trackIndex < trackList.size()) {
			MidiTrack track = trackList.get(trackIndex);
			long position = PositionTools.firstBeat(track, barPosition);
			result = track.getBarTimeSignature(position).toString();
		}
		return result;
	}

	public int getTrackBarKey(String trackId, long barPosition) {
		int result = 0;
		Optional<MidiTrack> trackOptional  = getMidiTrack(trackId);
		if (trackOptional.isPresent()) {
			MidiTrack track = trackOptional.get();
			long position = PositionTools.firstBeat(track, barPosition);
			result = track.getBarKey(position);
		}
		return result;
	}

	public int getTrackBarKey(int trackIndex, long barPosition) {
		int result = 0;
		List<MidiTrack> trackList = getActiveMidiTrackList();
		if (trackIndex < trackList.size()) {
			MidiTrack track = trackList.get(trackIndex);
			long position = PositionTools.firstBeat(track, barPosition);
			result = track.getBarKey(position);
		}
		return result;
	}

	public int getTrackBarBar(int trackIndex, long barPosition) {
		int result = 0;
		List<MidiTrack> trackList = getActiveMidiTrackList();
		if (trackIndex < trackList.size()) {
			MidiTrack track = trackList.get(trackIndex);
			long position = PositionTools.firstBeat(track, barPosition);
			result = track.getBarBar(position);
		}
		return result;
	}

	public int getTrackBarClef(String trackId, long barPosition) {
		int result = 0;
		Optional<MidiTrack> trackOptional  = getMidiTrack(trackId);
		if (trackOptional.isPresent()) {
			MidiTrack track = trackOptional.get();
			long position = PositionTools.firstBeat(track, barPosition);
			result = track.getBarClef(position);
		}
		return result;
	}

	public int getTrackBarClef(int trackIndex, long barPosition) {
		int result = 0;
		List<MidiTrack> trackList = getActiveMidiTrackList();
		if (trackIndex < trackList.size()) {
			MidiTrack track = trackList.get(trackIndex);
			long position = PositionTools.firstBeat(track, barPosition);
			result = track.getBarClef(position);
		}
		return result;
	}

	public int getTrackBarTempo(String trackId, long barPosition) {
		int result = 0;
		Optional<MidiTrack> trackOptional  = getMidiTrack(trackId);
		if (trackOptional.isPresent()) {
			MidiTrack track = trackOptional.get();
			long position = PositionTools.firstBeat(track, barPosition);
			result = track.getBarTempo(position);
		}
		return result;
	}

	public int getTrackBarTempo(int trackIndex, long barPosition) {
		int result = 0;
		List<MidiTrack> trackList = getActiveMidiTrackList();
		if (trackIndex < trackList.size()) {
			MidiTrack track = trackList.get(trackIndex);
			long position = PositionTools.firstBeat(track, barPosition);
			result = track.getBarTempo(position);
		}
		return result;
	}

	public int getTempo(long barPosition) {
		int result = 0;
		MidiTrack track = this.getActiveMidiTrackList().get(0);
		long position = PositionTools.firstBeat(track, barPosition);
		result = track.getBarTempo(position);
		return result;
	}

	//
//	public List<MidiTrack> getMidiTrackList() {
//		return getChildren(MidiTrack.class).stream().map(e -> MidiTrack.class.cast(e)).collect(Collectors.toList());
//	}
//
	public Optional<MidiTrack> getFirstActiveMidiTrack() {
		return getChildren().stream().map(e -> MidiTrack.class.cast(e)).filter(tr -> tr.isActive()).findFirst();
	}

	public long findLastPosition() {
		long lastPosition = 0;
		for (MidiTrack track : getActiveMidiTrackList()) {
			Optional<NoteEvent> lastNote = track.findLastNote();
			if (lastNote.isPresent()) {
				lastPosition = Math.max(lastPosition, lastNote.get().getEnd());
			}
		}
		return lastPosition;
	}

	public int getPPQ() {
		return getAttributeValue(pulsePerQuarter);
	}

	public TimeSignature getTimeSignature() {
		return getAttributeValue(timeSignature);
	}

	public int getKey() {
		return getAttributeValue(key);
	}

	public void undo() {
		history.undo();
	}

	public void redo() {
		history.redo();
	}

	public double getExposeValue() {
		return 0; // TODO!
	}

	public void performTransaction(Transaction transacton) {
		performTransaction(transacton, history);
	}

	public int getStretchFactor() {
		Integer stretchFactorIndex = getAttributeValue(stretchFactor);
		int factor = 1;
		if (stretchFactorIndex == null) {
			factor += 2 * DEFAULT_STRETCH_FACTOR_INDEX;
		} else {
			factor += 2 * stretchFactorIndex;
		}
		return factor;
	}

	public int getResolutionInTicks() {
		Integer resolutionIndex = getAttributeValue(resolution);
		if (resolutionIndex == null) {
			resolutionIndex = DEFAULT_RESOLUTION_INDEX;
		}
		return (int) (getPPQ() * 4 / (Math.pow(2, resolutionIndex)));
	}

	public int getGridInTicks() {
		int gridIndex = getAttributeValue(grid);
		return (int) (getPPQ() * 4 / (getTuplet() * Math.pow(2, gridIndex)));
	}
//
//	public int getGroupLevel() {
//		Integer attributeValue = getAttributeValue(groupLevel);
//		if (attributeValue == null) {
//			attributeValue = DEFAULT_GROUP_LEVEL;
//		}
//		return attributeValue;
//	}
//
//	public boolean getFullTupletPresentation() {
//		Integer attributeValue = getAttributeValue(tupletPresentation);
//		if (attributeValue == null) {
//			attributeValue = DEFAULT_TUPLET_PRESENTATION;
//		}
//		return attributeValue == 1;
//	}
//
	public void increaseBarOffset(int value) {
		int offset = getAttributeValue(Arrangement.offset);
		offset += value;
		performTransientSetAttributeValueOperation(Arrangement.offset, offset);
	}

	public void decreaseBarOffset(int value) {
		int offset = getAttributeValue(Arrangement.offset);
		offset = (offset - value < 0 ? 0 : offset - value);
		performTransientSetAttributeValueOperation(Arrangement.offset, offset);
	}

	public void setTransientBarOffset(int offset) {
		performTransientSetAttributeValueOperation(Arrangement.offset, offset);
	}

	public void setOffsetToFirstBar() {
		performTransientSetAttributeValueOperation(Arrangement.offset, 0);
	}
//
//	public void setOffsetToBeginningOfSelectionOrFirstBar(GenericContext context) {
//		int bar = 0;
//		if (context.getSelection().hasSelection()) {
//			List<MidiTrack> activeMidiTrackList = getActiveMidiTrackList();
//			GenericModelElement element = context.getSelection().getSelection().get(0);
//			if (activeMidiTrackList.size() > 0) {
//				if (element instanceof Event) {
//					Event event = (Event) element;
//					long position = event.getPosition();
//					bar = PositionTools.getTrias(activeMidiTrackList.get(0), position).bar;
//				}
//			}
//		}
//		setTransientBarOffset(context, bar);
//	}
//
	public void setOffsetToLastBar() {
		List<MidiTrack> activeMidiTrackList = getActiveMidiTrackList();
		if (activeMidiTrackList.size() > 0) {
			long maxPosition = activeMidiTrackList.stream().filter(track -> track.getChildrenByClass(Event.class).size() > 0).map(track -> track.findLastNote().get().getPosition()).mapToLong(l -> l).max()
					.getAsLong();
			int lastBar = PositionTools.getTrias(activeMidiTrackList.get(0), maxPosition).bar;
			performTransientSetAttributeValueOperation(Arrangement.offset, lastBar);
		}
	}

	public long getLastPosition() {
		long lastPosition = 0;
		for (MidiTrack track : getActiveMidiTrackList()) {
			List<Event> eventList = track.getChildrenByClass(Event.class);
			if (!eventList.isEmpty()) {
				Event lastEvent = eventList.get(eventList.size() - 1);
				lastPosition = Math.max(lastPosition, lastEvent.getPosition());
			}
		}
		return lastPosition;
	}

	public int getBarOffset() {
		return getAttributeValue(Arrangement.offset);
	}

	public long getBarOffsetPosition() {
		long position = 0;
		Optional<MidiTrack> optionalTrack = getFirstActiveMidiTrack();
		if (optionalTrack.isPresent()) {
			MidiTrack track = optionalTrack.get();
			int barOffset = getBarOffset();
			position = PositionTools.getPosition(track, new Trias(barOffset, 0, 0));
		}
		return position;
	}

	public void addRange(NamedRange newRange) {
		List<NamedRange> range = getAttributeValue(Arrangement.rangeList);
		range.add(newRange);
		performSetAttributeValueOperation(Arrangement.rangeList, range, history);
		System.out.println("===> " + getAttributeValue(Arrangement.rangeList));
	}

//	public List<NamedRange> getRangeList() {
//		List<NamedRange> list = getAttributeValue(rangeList);
//		if (list == null) {
//			list = DEFAULT_RANGE_LIST;
//			setAttributeValueWithoutNotification(rangeList, list);
//		}
//		return list;
//	}
//
//	private int getIntegerValueFromContext(GenericContext context, String name, int defaultValue) {
//		int value = defaultValue;
//		ValueElement<Integer> element = (ValueElement<Integer>) context.getSelection().getElement(name);
//		if (element != null) {
//			value = element.getValue();
//		}
//		return value;
//	}
//
//	private void setIntegerValueToContext(GenericContext context, String name, int value) {
//		context.getSelection().putElement(name, new ValueElement<Integer>(value));
//	}
//
//	public int getTempo(GenericContext context) {
//		return getIntegerValueFromContext(context, "tempo", DEFAULT_TEMPO);
//	}
//
//	public int getExposeValue(GenericContext context) {
//		return getIntegerValueFromContext(context, "exposeValue", DEFAULT_EXPOSE_VALUE);
//	}
//
	public double getTuplet() {
		// Integer tupletIndex = getAttributeValue(defaultTuplet);
		// if (tupletIndex == null) {
		// tupletIndex = DEFAULT_TUPLET_INDEX;
		// }
		int tupletIndex = 0; // TODO: getIntegerValueFromContext("tuplet", DEFAULT_TUPLET_INDEX);
		return DurationType.TUPLETS[tupletIndex].getFactor();
	}

//
//	public int getDots(GenericContext context) {
//		// return getAttributeValue(defaultDots);
//		return getIntegerValueFromContext(context, "dots", DEFAULT_DOTS_INDEX);
//	}
//
//	public int getEnhShift(GenericContext context) {
//		// return getAttributeValue(defaultEnhShift);
//		return getIntegerValueFromContext(context, "enharmonicShift", DEFAULT_SHIFT);
//	}
//
//	public int getVoice(GenericContext context) {
//		// return getAttributeValue(defaultVoice);
//		return getIntegerValueFromContext(context, "voice", DEFAULT_VOICE_INDEX);
//	}
//
//	public int getBarOffset(GenericContext context) {
//		// if (getAttributeValue(barOffset) == null) {
//		// return 0;
//		// } else {
//		// return getAttributeValue(barOffset);
//		// }
//		return getIntegerValueFromContext(context, "barOffset", 0);
//	}
//
//	public void setTransientBarOffset(GenericContext context, int barOffset) {
//		// setAttributeValue(Arrangement.barOffset, barOffset, AttributeOperation.DUMMY_INTEGER_OPERATION);
//		setIntegerValueToContext(context, "barOffset", barOffset);
//	}
//
//	public boolean showGrid(GenericContext context) {
//		return getIntegerValueFromContext(context, "showGrid", 0) == 1;
//	}
//
//	public boolean showVelocity(GenericContext context) {
//		return getIntegerValueFromContext(context, "showVelocity", 0) == 1;
//	}
//
//	public void setShowGrid(GenericContext context, boolean showGrid) {
//		setIntegerValueToContext(context, "showGrid", (showGrid ? 1 : 0));
//	}
//
//	public void setShowVelocity(GenericContext context, boolean showVelocity) {
//		setIntegerValueToContext(context, "showVelocity", (showVelocity ? 1 : 0));
//	}
//
//	public int getDefaultFunctionDuration(GenericContext context) {
//		int duration = 0;
//		// Integer value = getAttributeValue(defaultFunctionSelector);
//		int value = getIntegerValueFromContext(context, "defaultFunction", DEFAULT_SCORE_OBJECT_INDEX);
//		if (value >= FunctionSelector.NOTE.lowerBound && value <= FunctionSelector.NOTE.upperBound) {
//			// value=3 => duration=1/4; value=4 => duration=1/8
//			int dots = getDots(context);
//			duration = (int) (getPPQ() * 4.0 / (getTuplet(context) * Math.pow(2, (value - 1))));
//			duration = (int) (duration * (1 + ((Math.pow(2, dots) - 1) / Math.pow(2, dots))));
//		}
//		return duration;
//	}
//
//	public int getDefaultAccent(GenericContext context) {
//		// Integer value = getAttributeValue(defaultFunctionSelector);
//		int value = getIntegerValueFromContext(context, "defaultFunction", DEFAULT_SCORE_OBJECT_INDEX);
//		int accentCode = 0;
//		if (value >= FunctionSelector.ACCENT.lowerBound && value <= FunctionSelector.ACCENT.upperBound) {
//			accentCode = value - FunctionSelector.ACCENT.lowerBound;
//		}
//		return accentCode;
//	}
//
//	public int getDefaultSymbol(GenericContext context) {
//		// Integer value = getAttributeValue(defaultFunctionSelector);
//		int value = getIntegerValueFromContext(context, "defaultFunction", DEFAULT_SCORE_OBJECT_INDEX);
//		int symbolCode = 0;
//		if (value >= FunctionSelector.SYMBOL.lowerBound && value <= FunctionSelector.SYMBOL.upperBound) {
//			symbolCode = value - FunctionSelector.SYMBOL.lowerBound;
//		}
//		return symbolCode;
//	}
//
//	public int getSelectorCode(GenericContext context) {
//		return getIntegerValueFromContext(context, "defaultFunction", DEFAULT_SCORE_OBJECT_INDEX);
//	}
//
//	public FunctionSelector getFunctionSelector(GenericContext context) {
//		// Integer currentSelectorValue = getAttributeValue(defaultFunctionSelector);
//		int currentSelectorValue = getIntegerValueFromContext(context, "defaultFunction", DEFAULT_SCORE_OBJECT_INDEX);
//		FunctionSelector currentSelector = FunctionSelector.DISPLAY;
//		// if (currentSelectorValue != null) {
//		for (int i = 0; i < FunctionSelector.functions.length; i++) {
//			FunctionSelector functionSelector = FunctionSelector.functions[i];
//			if (currentSelectorValue >= functionSelector.lowerBound && currentSelectorValue <= functionSelector.upperBound) {
//				currentSelector = functionSelector;
//				break;
//			}
//		}
//		// }
//		return currentSelector;
//	}
//
//	@Override
//	public int compareTo(GenericModelElement element) {
//		return getName().compareTo(element.getName());
//	}
//
//	@Override
//	public String toString() {
//		return "Arrangement " + getName();
//	}
//
//	public boolean hasLyrics() {
//		// Only check first track! (for performance reasons)
//		boolean hasLyrics = false;
//		Optional<MidiTrack> firstActiveMidiTrack = getFirstActiveMidiTrack();
//		if (firstActiveMidiTrack.isPresent()) {
//			hasLyrics = firstActiveMidiTrack.get().hasLyrics();
//		}
//		return hasLyrics;
//	}
//
//	@Override
//	public Optional<File> getFile() {
//		String absolutePath = getAttributeValue(path);
//		if (absolutePath == null || absolutePath.equals("")) {
//			return Optional.empty();
//		} else {
//			return Optional.of(new File(absolutePath));
//		}
//	}
//
//	@Override
//	public void setFile(GenericContext context, File file) {
//		String absolutePath = file.getAbsolutePath();
//		if (absolutePath != null) {
//			performSetValueOperation(context, path, absolutePath);
//		}
//	}
//
//	public boolean getAutoBeamPrint() {
//		Boolean value = getAttributeValue(autoBeamPrint);
//		return value == null ? false : value;
//	}
//
//	public boolean getFlagAllowDottedRests() {
//		Boolean value = getAttributeValue(flagAllowDottedRests);
//		return value == null ? false : value;
//	}
//
//	public boolean getFlagSplitRests() {
//		// Boolean value = getAttributeValue(flagSplitRests);
//		// return value == null ? false : value;
//		return true;
//	}
//
//	public boolean getDurationBiDotted() {
//		Boolean value = getAttributeValue(durationBiDotted);
//		return value == null ? false : value;
//	}
//
//	public boolean getDurationTuplet2() {
//		Boolean value = getAttributeValue(durationTuplet2);
//		return value == null ? false : value;
//	}
//
//	public boolean getDurationTuplet3() {
//		Boolean value = getAttributeValue(durationTuplet3);
//		return value == null ? false : value;
//	}
//
//	public boolean getDurationTuplet4() {
//		Boolean value = getAttributeValue(durationTuplet4);
//		return value == null ? false : value;
//	}
//
//	public boolean getDurationTuplet5() {
//		Boolean value = getAttributeValue(durationTuplet5);
//		return value == null ? false : value;
//	}
//
//	public boolean getDurationTuplet6() {
//		Boolean value = getAttributeValue(durationTuplet6);
//		return value == null ? false : value;
//	}
//
//	public int getFlags() {
//		return (getFlagAllowDottedRests() ? Score.ALLOW_DOTTED_RESTS : 0) + (getFlagSplitRests() ? Score.SPLIT_RESTS : 0);
//	}
//
//	public List<DurationType> getDurations() {
//		List<DurationType> list = new ArrayList<>();
//		list.add(DurationType.REGULAR);
//		if (getDurationBiDotted()) {
//			list.add(DurationType.BIDOTTED);
//		}
//		if (true) {
//			list.add(DurationType.DOTTED);
//		}
//		if (getDurationTuplet2()) {
//			list.add(DurationType.DUPLET);
//		}
//		if (getDurationTuplet3()) {
//			list.add(DurationType.TRIPLET);
//		}
//		if (getDurationTuplet4()) {
//			list.add(DurationType.QUADRUPLET);
//		}
//		if (getDurationTuplet5()) {
//			list.add(DurationType.QUINTUPLET);
//		}
//		if (getDurationTuplet6()) {
//			list.add(DurationType.SEXTUPLET);
//		}
//		return list;
//		// return Arrays.asList(new DurationType[] { DurationType.REGULAR, DurationType.DOTTED, DurationType.BIDOTTED, DurationType.TRIPLET, DurationType.QUINTUPLET });
//	}
	public void cut() {
		performCutToClipboardOperation(this.clipboard, this.getSelection().getSelection(), history);
	}

	public void copy(ModelElementFactory factory) {
		performCopyToClipboardOperation(this.clipboard, this.getSelection().getSelection(), factory, history);
	}

	public void paste(ModelElementFactory factory) {
		MidiTrack selectedMidiTrack = getSelectedMidiTrack();
		if (selectedMidiTrack!=null) {
			selectedMidiTrack.performPasteClipboardOperation(this.clipboard, factory, history);
		}
	}

	public void paste(ModelElementFactory factory, Modifier<Event> modifier) {
		MidiTrack selectedMidiTrack = getSelectedMidiTrack();
		if (selectedMidiTrack!=null) {
			selectedMidiTrack.performModifyPasteClipboardOperation(this.clipboard, factory, history, modifier);
		}
	}
}
