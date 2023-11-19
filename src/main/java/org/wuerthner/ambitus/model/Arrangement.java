package org.wuerthner.ambitus.model;

import java.util.*;
import java.util.stream.Collectors;

import org.wuerthner.ambitus.attribute.TimeSignatureAttribute;
import org.wuerthner.ambitus.tool.AbstractSelection;
import org.wuerthner.ambitus.type.NamedRange;
import org.wuerthner.cwn.api.*;
import org.wuerthner.cwn.position.PositionTools;
import org.wuerthner.cwn.score.Score;
import org.wuerthner.cwn.timesignature.SimpleTimeSignature;
import org.wuerthner.cwn.util.Harmony;
import org.wuerthner.sport.api.*;
import org.wuerthner.sport.attribute.*;
import org.wuerthner.sport.core.AbstractModelElement;
import org.wuerthner.sport.core.ModelClipboard;
import org.wuerthner.sport.core.ModelHistory;
import org.wuerthner.sport.operation.*;


public class Arrangement extends AbstractModelElement implements CwnContainer {
	public final static String TYPE = "Arrangement";

	public static final String DEFAULT_NAME = "Untitled";
	public static final int DEFAULT_PPQ = 960;
	public static final String[] KEYS = MidiTrack.KEYS;
	public static final String [] GENUS = new String[]{"Undefined", "Minor", "Major", "Diminished"};
	public static final String[] CLEFS = MidiTrack.CLEFS;
	public static final String[] GRIDS = new String[] { "1", "1/2", "1/4", "1/8", "1/16", "1/32", "1/64" };
	public static final String[] LEVELS = new String[] { "-", "1st level", "2nd level", "3rd level", "4th level", "5th level" };
	public static final String[] TUPLET_PRESENTATION = new String[] { "Short", "Full" };
	public static final String[] STRETCH_FACTORS = new String[] { "Narrow", "Normal", "Wide" };
	public static final Long DEFAULT_POSITION = 0L;
	public static final Integer DEFAULT_TEMPO = 100;
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
	
	public final static StringAttribute name = new StringAttribute("name")
			.defaultValue(DEFAULT_NAME)
			.required();
	public final static StringAttribute composer = new StringAttribute("composer")
			.defaultValue("");
	public final static StringAttribute subtitle = new StringAttribute("subtitle")
			.defaultValue("");
	public final static BooleanAttribute autoBeamPrint = new BooleanAttribute("autoBeamPrint")
			.defaultValue(true);
	public final static IntegerAttribute pulsePerQuarter = new IntegerAttribute("ppqn")
			.defaultValue(DEFAULT_PPQ)
			.required();
	public final static SelectableIntegerAttribute key = new SelectableIntegerAttribute("key")
			.values(KEYS)
			.defaultValue(DEFAULT_KEY);
	public final static TimeSignatureAttribute timeSignature = new TimeSignatureAttribute("timeSignature")
			.defaultValue(DEFAULT_SIGNATURE);
	public final static BooleanAttribute flagAllowDottedRests = new BooleanAttribute("flagAllowDottedRests")
			.defaultValue(true);
	// public final static BooleanAttribute flagSplitRests = new BooleanAttribute(new AttributeInit<>("flagSplitRests", true, false));
	public final static BooleanAttribute durationBiDotted = new BooleanAttribute("durationBiDotted")
			.defaultValue(false);
	public final static BooleanAttribute durationTuplet2 = new BooleanAttribute("durationTuplet2")
			.defaultValue(false);
	public final static BooleanAttribute durationTuplet3 = new BooleanAttribute("durationTuplet3")
			.defaultValue(true);
	public final static BooleanAttribute durationTuplet4 = new BooleanAttribute("durationTuplet4")
			.defaultValue(false);
	public final static BooleanAttribute durationTuplet5 = new BooleanAttribute("durationTuplet5")
			.defaultValue(false);
	public final static BooleanAttribute durationTuplet6 = new BooleanAttribute("durationTuplet6")
			.defaultValue(false);
	public final static BooleanAttribute durationTuplet7 = new BooleanAttribute("durationTuplet7")
			.defaultValue(false);
	public final static SelectableIntegerAttribute groupLevel = new SelectableIntegerAttribute("groupLevel")
			.defaultValue(DEFAULT_GROUP_LEVEL)
			.values(LEVELS);
	public final static SelectableIntegerAttribute tupletPresentation = new SelectableIntegerAttribute("tupletPresentation")
			.defaultValue(DEFAULT_TUPLET_PRESENTATION)
			.values(TUPLET_PRESENTATION);
	public final static SelectableIntegerAttribute stretchFactor = new SelectableIntegerAttribute("stretchFactor")
			.defaultValue(DEFAULT_STRETCH_FACTOR_INDEX)
			.values(STRETCH_FACTORS);
	public final static SelectableIntegerAttribute grid = new SelectableIntegerAttribute("grid")
			.defaultValue(DEFAULT_GRID_INDEX)
			.values(GRIDS);
	public final static SelectableIntegerAttribute resolution = new SelectableIntegerAttribute("resolution")
			.defaultValue(DEFAULT_RESOLUTION_INDEX)
			.values(GRIDS);
	public final static StaticListAttribute<NamedRange> rangeList = new StaticListAttribute<>("ranges", NamedRange.class)
			.defaultValue(DEFAULT_RANGE_LIST);
	public final static StringAttribute path = new StringAttribute("path")
			.defaultValue(null);

	public final static IntegerAttribute offset = new IntegerAttribute("offset")
			.defaultValue(DEFAULT_BAR_OFFSET);

	private final ModelHistory history = new ModelHistory();
	private final Clipboard<Event> clipboard = new ModelClipboard<>();

	public Arrangement() {
		super(TYPE, Arrays.asList(MidiTrack.TYPE, InfoTrack.TYPE), Arrays.asList(name, subtitle, composer, autoBeamPrint, pulsePerQuarter,
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
		// return getChildrenByClass(MidiTrack.class).stream().map(e -> MidiTrack.class.cast(e)).filter(tr -> tr.isActive()).collect(Collectors.toList());
		return getChildrenByClass(MidiTrack.class).stream().map(MidiTrack.class::cast).filter(MidiTrack::isActive).collect(Collectors.toList());
	}

	@Override
	public List<CwnTrack> getTrackList() {
		return getChildrenByClass(CwnTrack.class);
	}

	@Override
	public boolean isEmpty() {
		return getTrackList().isEmpty();
	}

	//
	// Selection
	//
	public AbstractSelection getSelection() {
		return selection;
	}

	public void setSelection(AbstractSelection selection) {
		Arrangement.selection = selection;
	}

	public Clipboard<Event> getClipboard() { return clipboard; }

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
		return getChildrenByClass(MidiTrack.class).stream().filter(t -> t.getId().equals(id)).findAny(); // .peek(System.out::println)
	}

	public Optional<MidiTrack> getNextMidiTrack(String id) {
		Optional<MidiTrack> result = Optional.empty();
		List<MidiTrack> mTrackList = getChildrenByClass(MidiTrack.class);
		String previousId = "";
		for (MidiTrack mTrack : mTrackList) {
			if (previousId.equals(id)) {
				result = Optional.of(mTrack);
				break;
			}
			previousId = mTrack.getId();
		}
		return result;
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
		//Operation o1 = new SetAttributeValueOperation<>(track, MidiTrack.timeSignature, ts);
		Operation o2 = new SetAttributeValueOperation<>(tsEvent, TimeSignatureEvent.timeSignature, ts);
		Transaction transaction = new Transaction("TimeSignature="+metric, o2);
		track.performTransaction(transaction, history);
	}

	public void setMetric(String metric) {
		List<Operation> opList = new ArrayList<>();
		for (MidiTrack track : this.getActiveMidiTrackList()) {
			TimeSignatureEvent tsEvent = track.findFirstEvent(TimeSignatureEvent.class).get();
			TimeSignature ts = new SimpleTimeSignature(metric);
			// Operation o1 = new SetAttributeValueOperation<>(track, MidiTrack.timeSignature, ts);
			Operation o2 = new SetAttributeValueOperation<>(tsEvent, TimeSignatureEvent.timeSignature, ts);
			// opList.add(o1);
			opList.add(o2);
		}
		if (!opList.isEmpty()) {
			Transaction transaction = new Transaction("TimeSignature=" + metric, opList);
			this.performTransaction(transaction, history);
		}
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
		// Operation o1 = new SetAttributeValueOperation<>(track, MidiTrack.key, key);
		Operation o2 = new SetAttributeValueOperation<>(keyEvent, KeyEvent.key, key-7);
		Transaction transaction = new Transaction("Key="+key, o2);
		track.performTransaction(transaction, history);
	}

	public void setKey(int key) {
		List<Operation> opList = new ArrayList<>();
		for (MidiTrack track : this.getActiveMidiTrackList()) {
			KeyEvent keyEvent = track.findFirstEvent(KeyEvent.class).get();
			// Operation o1 = new SetAttributeValueOperation<>(track, MidiTrack.key, key);
			Operation o2 = new SetAttributeValueOperation<>(keyEvent, KeyEvent.key, key-7);
			// opList.add(o1);
			opList.add(o2);
		}
		if (!opList.isEmpty()) {
			Transaction transaction = new Transaction("Key=" + key, opList);
			this.performTransaction(transaction, history);
		}
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
		// Operation o1 = new SetAttributeValueOperation<>(track, MidiTrack.clef, clef);
		Operation o2 = new SetAttributeValueOperation<>(clefEvent, ClefEvent.clef, clef);
		Transaction transaction = new Transaction("Clef="+clef, o2);
		track.performTransaction(transaction, history);
	}

	public void setTrackTempo(int tempo) {
		Optional<MidiTrack> trackOptional  = getFirstActiveMidiTrack();
		if (trackOptional.isPresent()) {
			MidiTrack track = trackOptional.get();
			TempoEvent tempoEvent = track.findFirstEvent(TempoEvent.class).get();
			// Operation o1 = new SetAttributeValueOperation<>(track, MidiTrack.tempo, tempo);
			Operation o2 = new SetAttributeValueOperation<>(tempoEvent, TempoEvent.tempo, tempo);
			Operation o3 = new SetAttributeValueOperation<>(tempoEvent, TempoEvent.label, MidiTrack.getTempoLabel(tempo));
			Transaction transaction = new Transaction("Tempo="+tempo, o2, o3);
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

	public void setTrackProperties(String id, boolean mute, int volume, String name, String metric, int key, int genus, int clef, String bar, int tempo, int instrument, int channel, boolean piano, AmbitusFactory factory) {
		Optional<MidiTrack> trackOptional  = getMidiTrack(id);
		if (trackOptional.isPresent()) {
			MidiTrack track = trackOptional.get();
			MidiTrack firstTrack = (MidiTrack) getTrackList().get(0);
			ClefEvent clefEvent = track.findFirstEvent(ClefEvent.class).get();
			Optional<TempoEvent> tempoEventOptional = firstTrack.findFirstEvent(TempoEvent.class);
			TimeSignature ts = new SimpleTimeSignature(metric);
			List<Operation> opList = new ArrayList<>();
			if (track.getMute() != mute) { opList.add(new SetAttributeValueOperation<>(track, MidiTrack.mute, mute)); }
			if (track.getVolume() != volume) { opList.add(new SetAttributeValueOperation<>(track, MidiTrack.volume, volume)); }
			if (!track.getName().equals(name)) { opList.add(new SetAttributeValueOperation<>(track, MidiTrack.name, name)); }
			boolean removeLaterEvents = false;
			for (CwnTrack theTrack : getTrackList()) {
				TimeSignatureEvent tsEvent = theTrack.findFirstEvent(TimeSignatureEvent.class).get();
				if (!tsEvent.getTimeSignature().toString().equals(ts.toString())) {
					removeLaterEvents = true;
					opList.add(new SetAttributeValueOperation<>(tsEvent, TimeSignatureEvent.timeSignature, ts));
				}
				if (theTrack instanceof MidiTrack) {
					KeyEvent keyEvent = theTrack.findFirstEvent(KeyEvent.class).get();
					if (keyEvent.getKey() != key - 7) {
						opList.add(new SetAttributeValueOperation<>(keyEvent, KeyEvent.key, key - 7));
					}
					if (keyEvent.getGenus() != genus) {
						opList.add(new SetAttributeValueOperation<>(keyEvent, KeyEvent.genus, genus));
					}
				}
			}
			if (track.getClef() != clef) { opList.add(new SetAttributeValueOperation<>(clefEvent, ClefEvent.clef, clef)); }

			Optional<BarEvent> barEvent = track.getBarEvent(0);
			if (bar.equals(CwnBarEvent.STANDARD)) {
				if (barEvent.isPresent()) {	opList.add(new RemoveChildOperation(barEvent.get())); }
			} else {
				if (barEvent.isPresent()) {
					if (!barEvent.get().getTypeString().equals(bar)) {
						opList.add(new SetAttributeValueOperation<>(barEvent.get(), BarEvent.type, bar));
					}
				} else {
					BarEvent event = factory.createElement(BarEvent.TYPE);
					opList.add(new SetAttributeValueOperation<>(event, BarEvent.type, bar));
					opList.add(new SetAttributeValueOperation<>(event, Event.position, 0l));
					opList.add(new AddChildOperation(track, event));
				}
			}


			if (tempoEventOptional.isPresent()) {
				if (tempoEventOptional.get().getTempo() != tempo) { opList.add(new SetAttributeValueOperation<>(tempoEventOptional.get(), TempoEvent.tempo, tempo)); }
				if (!tempoEventOptional.get().getLabel().equals(MidiTrack.getTempoLabel(tempo))) { opList.add(new SetAttributeValueOperation<>(tempoEventOptional.get(), TempoEvent.label, MidiTrack.getTempoLabel(tempo))); }
			}
			if (track.getInstrument() != instrument) { opList.add(new SetAttributeValueOperation<>(track, MidiTrack.instrument, instrument)); }
			if (track.getChannel() != channel) { opList.add(new SetAttributeValueOperation<>(track, MidiTrack.channel, channel)); }
			if (track.getPiano() != piano) {
				opList.add(new SetAttributeValueOperation<>(track, MidiTrack.piano, piano));
				Optional<MidiTrack> nextTrackOpt = this.getNextMidiTrack(id);
				if (nextTrackOpt.isPresent()) {
					MidiTrack nextTrack = nextTrackOpt.get();
					if (nextTrack.getPiano() != piano) {
						opList.add(new SetAttributeValueOperation<>(nextTrack, MidiTrack.piano, piano));
					}
				}
			}

			if (removeLaterEvents) {
				// when time signature changes, all key/clef/ts changes are removed
				opList.addAll(getAllEventsButFirst(track, TimeSignatureEvent.class));
				opList.addAll(getAllEventsButFirst(track, KeyEvent.class));
				opList.addAll(getAllEventsButFirst(track, ClefEvent.class));
			}
			Transaction transaction = new Transaction("Change track properties", opList);
			track.performTransaction(transaction, history);
		}
	}

	private List<? extends Operation> getAllEventsButFirst(MidiTrack track, Class<? extends Event> clasz) {
		List<Operation> opList = new ArrayList<>();
		List<? extends Event> eventList = track.getChildrenByClass(clasz);
		Iterator<? extends Event> iterator = eventList.iterator();
		for (iterator.next(); iterator.hasNext(); ) { opList.add(new RemoveChildOperation(iterator.next())); }
		return opList;
	}

	public void setBarProperties(String trackId, long barPosition, String metric, int key, int genus, int clef, String bar, int tempo, AmbitusFactory factory) {
		TimeSignature timeSignature = new SimpleTimeSignature(metric);
		Optional<MidiTrack> trackOptional  = getMidiTrack(trackId);
		if (trackOptional.isPresent()) {
			MidiTrack firstTrack = (MidiTrack) getTrackList().get(0);
			List<Operation> opList = new ArrayList<>();
			MidiTrack track = trackOptional.get();
			long position = PositionTools.firstBeat(track, barPosition);
			opList.addAll(handleEventSettingInBar(Optional.empty(), position, TimeSignatureEvent.class, TimeSignatureEvent.TYPE, TimeSignature.class, TimeSignatureEvent.timeSignature, timeSignature, factory));
			opList.addAll(handleEventSettingInBar(Optional.empty(), position, KeyEvent.class, KeyEvent.TYPE, Integer.class, KeyEvent.key, key-7, factory));
			opList.addAll(handleEventSettingInBar(Optional.empty(), position, KeyEvent.class, KeyEvent.TYPE, Integer.class, KeyEvent.genus, genus, factory));
			opList.addAll(handleEventSettingInBar(Optional.of(track), position, ClefEvent.class, ClefEvent.TYPE, Integer.class, ClefEvent.clef, clef, factory));
			Optional<BarEvent> barEvent = track.getBarEvent(position);
			if (bar.equals(CwnBarEvent.STANDARD)) {
				if (barEvent.isPresent()) {	opList.add(new RemoveChildOperation(barEvent.get())); }
			} else {
				// opList.addAll(handleEventSettingInBar(Optional.empty(), position, BarEvent.class, BarEvent.TYPE, String.class, BarEvent.type, bar, factory));
				if (barEvent.isPresent()) {
					if (!barEvent.get().getTypeString().equals(bar)) {
						opList.add(new SetAttributeValueOperation<>(barEvent.get(), BarEvent.type, bar));
					}
				} else {
					BarEvent event = factory.createElement(BarEvent.TYPE);
					opList.add(new SetAttributeValueOperation<>(event, BarEvent.type, bar));
					opList.add(new SetAttributeValueOperation<>(event, Event.position, position));
					opList.add(new AddChildOperation(track, event));
				}
			}
			opList.addAll(handleEventSettingInBar(Optional.of(firstTrack), position, TempoEvent.class, TempoEvent.TYPE, Integer.class, TempoEvent.tempo, tempo, factory));
			Transaction transaction = new Transaction("Change bar properties", opList);
			track.performTransaction(transaction, history);
		} else {
			System.err.println("Track " + trackId + " not found!");
		}
	}

	private <EVENT extends Event, TYPE> List<Operation> handleEventSettingInBar(Optional<MidiTrack> trackOptional, long position, Class<EVENT> eventClass, String eventType,
																	 Class<TYPE> typeClass, Attribute<TYPE> attribute, TYPE value, AmbitusFactory factory) {
		List<Operation> opList = new ArrayList<>();
		for (CwnTrack cwnTrack : getTrackList()) {
			MidiTrack track = (MidiTrack) cwnTrack;
			if (trackOptional.isEmpty() || trackOptional.get().equals(track)) {
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
					// CHANGE EXISTING EVENT
					if (changeRequired) {
						Event event = eventOptional.get();
						opList.add(new SetAttributeValueOperation<>(event, attribute, value));
						opList.add(new SetAttributeValueOperation<>(event, Event.position, position));
						if (eventType.equals(TempoEvent.TYPE)) {
							opList.add(new SetAttributeValueOperation<>(event, TempoEvent.label, MidiTrack.getTempoLabel((int) value)));
						}
					} else {
						opList.add(new RemoveChildOperation(eventOptional.get()));
					}
				} else {
					// INSERT NEW EVENT
					if (changeRequired) {
						EVENT event = factory.createElement(eventType);
						opList.add(new SetAttributeValueOperation<>(event, attribute, value));
						opList.add(new SetAttributeValueOperation<>(event, Event.position, position));
						if (eventType.equals(TempoEvent.TYPE)) {
							opList.add(new SetAttributeValueOperation<>(event, TempoEvent.label, MidiTrack.getTempoLabel((int) value)));
						}
						opList.add(new AddChildOperation(track, event));
					} else {
						// nothing to do here!
					}
				}
			}
		}
		return opList;
	}

	public void setConfiguration(String title, String subtitle, String composer, int ppq, int level, int resolution, int stretchFac,
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

	public void addEventWithCaution(MidiTrack track, NoteEvent event) {
		long position = track.findNextFreeSpace(event.getPosition(), event.getDuration(), event.getVoice());
		event.setAttributeValue(NoteEvent.position, position);
		track.performAddChildOperation(event, history);
	}

	public void setLyrics(NoteEvent noteEvent, String value) {
		noteEvent.performSetAttributeValueOperation(NoteEvent.lyrics, value, history);
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

	public void removeTrack(MidiTrack track) {
		this.performRemoveChildOperation(track, history);
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

	public List<CwnTrack> deleteElements(List<Event> eventList) {
		Set<CwnTrack> trackSet = new HashSet<>();
		List<Operation> opList = new ArrayList<>();
		for (CwnEvent cwnEvent: eventList) {
			Event event = (Event) cwnEvent;
			Operation operation = new RemoveChildOperation(event);
			opList.add(operation);
			trackSet.add((CwnTrack)event.getParent());
		}
		if (opList.size()>0) {
			Transaction transaction = new Transaction("Remove selection", opList);
			this.performTransaction(transaction, history);
		}
		return new ArrayList<>(trackSet);
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

	public int getNumberOfBeats() {
		if (getFirstActiveMidiTrack().isPresent()) {
			return getFirstActiveMidiTrack().get().getTimeSignature().getDenominatorInt();
		}
		return 4;
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

	public int getTrackBarKeyGenus(int trackIndex, long barPosition) {
		int result = 0;
		List<MidiTrack> trackList = getActiveMidiTrackList();
		if (trackIndex < trackList.size()) {
			MidiTrack track = trackList.get(trackIndex);
			long position = PositionTools.firstBeat(track, barPosition);
			result = track.getBarKeyGenus(position);
		}
		return result;
	}

	public Optional<Integer> getTrackBarEvent(int trackIndex, long barPosition) {
		Optional<Integer> result = Optional.empty();
		List<MidiTrack> trackList = getActiveMidiTrackList();
		if (trackIndex < trackList.size()) {
			MidiTrack track = trackList.get(trackIndex);
			long position = PositionTools.firstBeat(track, barPosition);
			Optional<BarEvent> barEventOpt = track.getBarEvent(position);
			result = barEventOpt.isPresent() ? Optional.of(barEventOpt.get().getTypeIndex()) : Optional.empty();
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

	public Optional<InfoTrack> getInfoTrack() {
		return getChildren().stream().filter(t -> InfoTrack.class.isAssignableFrom(t.getClass())).map(e -> InfoTrack.class.cast(e)).findFirst();
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

	public void performTransaction(Transaction transacton) {
		performTransaction(transacton, history);
	}

	public int getStretchFactor() {
		Integer stretchFactorIndex = getAttributeValue(stretchFactor);
		int factor = 2;
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

	public void setGrid(int gridValue) {
		this.performTransientSetAttributeValueOperation(grid, gridValue);
	}

	public void increaseBarOffset(int value) {
		int offset = getAttributeValue(Arrangement.offset);
		offset += value;
		performTransientSetAttributeValueOperation(Arrangement.offset, offset);
	}

	public void decreaseBarOffset(int value) {
		int offset = getAttributeValue(Arrangement.offset);
		int newOffset = (offset - value < 0 ? 0 : offset - value);
		performTransientSetAttributeValueOperation(Arrangement.offset, newOffset);
	}

	public void setTransientBarOffset(int offset) {
		performTransientSetAttributeValueOperation(Arrangement.offset, offset);
	}

	public void setOffsetToFirstBar() {
		performTransientSetAttributeValueOperation(Arrangement.offset, 0);
	}

	public void setOffsetToLastBar() {
		List<MidiTrack> activeMidiTrackList = getActiveMidiTrackList();
		if (activeMidiTrackList.size() > 0) {
			long maxPosition = activeMidiTrackList.stream().filter(track -> track.getChildrenByClass(Event.class).size() > 0).map(track -> track.findLastNote().get().getPosition()).mapToLong(l -> l).max()
					.getAsLong();
			int lastBar = PositionTools.getTrias(activeMidiTrackList.get(0), maxPosition).bar;
			performTransientSetAttributeValueOperation(Arrangement.offset, lastBar);
		}
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

	public long findLastPositionBarStart() {
		long lastPosition = 0;
		for (MidiTrack track : getActiveMidiTrackList()) {
			Optional<NoteEvent> lastNote = track.findLastNote();
			if (lastNote.isPresent()) {
				lastPosition = Math.max(lastPosition, lastNote.get().getEnd());
			}
		}
		Trias trias = PositionTools.getTrias(getFirstActiveMidiTrack().get(), lastPosition);
		if (trias.beat==0 && trias.tick==0)
			return lastPosition;
		else
			return PositionTools.getPosition(getFirstActiveMidiTrack().get(), trias.nextBar());
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

//	public List<NoteEvent> getNoteEventsAt(long position) {
//		List<NoteEvent> list = new ArrayList<>();
//		for (MidiTrack track : getActiveMidiTrackList()) {
//			track.get
//		}
//		return list;
//	}

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

	public void setTransientBarOffsetPosition(long position) {
		Optional<MidiTrack> optionalTrack = getFirstActiveMidiTrack();
		if (optionalTrack.isPresent()) {
			MidiTrack track = optionalTrack.get();
			Trias trias = PositionTools.getTrias(track, position);
			performTransientSetAttributeValueOperation(Arrangement.offset, trias.bar);
		}
	}

	public void addRange(NamedRange newRange) {
		List<NamedRange> range = getAttributeValue(Arrangement.rangeList);
		range.add(newRange);
		performSetAttributeValueOperation(Arrangement.rangeList, range, history);
		//System.out.println("===> " + getAttributeValue(Arrangement.rangeList));
	}

	public double getTuplet() {
		// Integer tupletIndex = getAttributeValue(defaultTuplet);
		// if (tupletIndex == null) {
		// tupletIndex = DEFAULT_TUPLET_INDEX;
		// }
		int tupletIndex = 0; // TODO: getIntegerValueFromContext("tuplet", DEFAULT_TUPLET_INDEX);
		return DurationType.TUPLETS[tupletIndex].getFactor();
	}

	public List<CwnTrack> cut() {
		Set<CwnTrack> trackSet = new HashSet<>();
		for (CwnEvent cwnEvent: this.getSelection().getSelection()) {
			Event event = (Event) cwnEvent;
			trackSet.add((CwnTrack)event.getParent());
		}
		performCutToClipboardOperation(this.clipboard, this.getSelection().getSelection(), history);
		return new ArrayList<>(trackSet);
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
		} else {
			this.performModifyPasteClipboardToReferenceOperation(this.clipboard, factory, history, modifier);
		}
	}

	public <T> void setEventAttribute(Event event, Attribute<T> attribute, T value) {
		event.performSetAttributeValueOperation(attribute, value, history);
	}

	public void setNoteEventAttributes(NoteEvent event, long position, long duration, int pitch, int enharmonicShift, int velocity, int voice, String lyrics) {
		List<Operation> opList = new ArrayList<>();
		if (position != event.getPosition()) {
			opList.add(new SetAttributeValueOperation<>(event, NoteEvent.position, position));
		}
		if (duration != event.getDuration()) {
			opList.add(new SetAttributeValueOperation<>(event, NoteEvent.duration, duration));
		}
		if (pitch != event.getPitch()) {
			opList.add(new SetAttributeValueOperation<>(event, NoteEvent.pitch, pitch));
		}
		if (enharmonicShift != event.getEnharmonicShift()) {
			opList.add(new SetAttributeValueOperation<>(event, NoteEvent.shift, enharmonicShift));
		}
		if (velocity != event.getVelocity()) {
			opList.add(new SetAttributeValueOperation<>(event, NoteEvent.velocity, velocity));
		}
		if (voice != event.getVoice()) {
			opList.add(new SetAttributeValueOperation<>(event, NoteEvent.voice, voice));
		}
		if (!lyrics.equals(event.getLyrics())) {
			opList.add(new SetAttributeValueOperation<>(event, NoteEvent.lyrics, lyrics));
		}
		if (!opList.isEmpty()) {
			Transaction transaction = new Transaction("Set Attribute List", opList);
			this.performTransaction(transaction, history);
		}
	}

	public boolean getFlagAllowDottedRests() {
		Boolean value = getAttributeValue(flagAllowDottedRests);
		return value == null ? false : value;
	}

	public boolean getFlagSplitRests() {
		return true;
	}

	public int getFlags() {
		return (getFlagAllowDottedRests() ? Score.ALLOW_DOTTED_RESTS : 0) + (getFlagSplitRests() ? Score.SPLIT_RESTS : 0);
	}

	public List<DurationType> getDurations() {
		List<DurationType> list = new ArrayList<>();
		list.add(DurationType.REGULAR);
		if (getAttributeValue(Arrangement.durationBiDotted)) {
			list.add(DurationType.BIDOTTED);
		}
		if (true) {
			list.add(DurationType.DOTTED);
		}
		if (getAttributeValue(Arrangement.durationTuplet2)) {
			list.add(DurationType.DUPLET);
		}
		if (getAttributeValue(Arrangement.durationTuplet3)) {
			list.add(DurationType.TRIPLET);
		}
		if (getAttributeValue(Arrangement.durationTuplet4)) {
			list.add(DurationType.QUADRUPLET);
		}
		if (getAttributeValue(Arrangement.durationTuplet5)) {
			list.add(DurationType.QUINTUPLET);
		}
		if (getAttributeValue(Arrangement.durationTuplet6)) {
			list.add(DurationType.SEXTUPLET);
		}
		return list;
	}

	public List<NamedRange> getRangeList() {
		List<NamedRange> list = getAttributeValue(rangeList);
		if (list == null) {
			list = DEFAULT_RANGE_LIST;
			performTransientSetAttributeValueOperation(rangeList, list);
		}
		return list;
	}

	public void addAccent(NoteEvent noteEvent, int accentIndex) {
		List<Accent> accentList = noteEvent.getAttributeValue(NoteEvent.accentList);
		Accent accent = Accent.createAccent(accentIndex);
		accentList.add(accent);
		noteEvent.performSetAttributeValueOperation(NoteEvent.accentList, accentList, history);
	}

	public void addOrRemoveInfoEvent(long position, String info, String mode, AmbitusFactory factory) {
		Harmony.Fix type = mode==null ?	Harmony.Fix.NONE :
				mode.equals(Harmony.Fix.CHORD.name()) ? Harmony.Fix.CHORD :
						mode.equals(Harmony.Fix.HARMONY.name()) ? Harmony.Fix.HARMONY :
								Harmony.Fix.ALL;
		if (getFirstActiveMidiTrack().isPresent()) {
			List<Operation> opList = new ArrayList<>();
			InfoTrack infoTrack = null;
			if (getInfoTrack().isPresent()) {
				infoTrack = getInfoTrack().get();
			} else {
				infoTrack = factory.createElement(InfoTrack.TYPE);
				TimeSignature timeSignature = getFirstActiveMidiTrack().get().getTimeSignature();
				ModelElement tsEvent = factory.createElement(TimeSignatureEvent.TYPE);
				tsEvent.performTransientSetAttributeValueOperation(TimeSignatureEvent.timeSignature, timeSignature);
				opList.add(new AddChildOperation(infoTrack, tsEvent));
				opList.add(new AddChildOperation(this, infoTrack));
			}
			Optional<InfoEvent> infoEventOptional = infoTrack.findEventAtPosition(position, InfoEvent.class);
			if (infoEventOptional.isPresent()) {
				if (info != null && !info.trim().equals("")) {
					opList.add(new SetAttributeValueOperation<>(infoEventOptional.get(), InfoEvent.info, info));
					opList.add(new SetAttributeValueOperation<>(infoEventOptional.get(), InfoEvent.type, type.name()));
				} else {
					opList.add(new RemoveChildOperation(infoEventOptional.get()));
				}
			} else {
				if (info != null && !info.trim().equals("")) {
					InfoEvent infoEvent = factory.createElement(InfoEvent.TYPE);
					infoEvent.setAttributeValue(InfoEvent.position, position);
					infoEvent.setAttributeValue(InfoEvent.info, info);
					infoEvent.setAttributeValue(InfoEvent.type, type.name());
					opList.add(new AddChildOperation(infoTrack, infoEvent));
				}
			}
			if (!opList.isEmpty()) {
				Transaction transaction = new Transaction("add or remove chord", opList);
				this.performTransaction(transaction, history);
			}
		}
	}
}
