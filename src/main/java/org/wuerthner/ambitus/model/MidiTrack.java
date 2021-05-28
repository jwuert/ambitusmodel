package org.wuerthner.ambitus.model;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.wuerthner.ambitus.attribute.AmbitusAttributeBuilder;
import org.wuerthner.ambitus.attribute.TimeSignatureAttribute;
import org.wuerthner.ambitus.model.*;
import org.wuerthner.cwn.api.CwnClefEvent;
import org.wuerthner.cwn.api.CwnEvent;
import org.wuerthner.cwn.api.CwnKeyEvent;
import org.wuerthner.cwn.api.CwnNoteEvent;
import org.wuerthner.cwn.api.CwnTimeSignatureEvent;
import org.wuerthner.cwn.api.CwnTrack;
import org.wuerthner.cwn.api.TimeSignature;
import org.wuerthner.cwn.api.Trias;
import org.wuerthner.cwn.api.exception.TimeSignatureException;
import org.wuerthner.cwn.position.PositionTools;
import org.wuerthner.cwn.score.Location;
import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.attribute.*;
import org.wuerthner.sport.core.AbstractModelElement;

public class MidiTrack extends AbstractModelElement implements CwnTrack {
	public final static String TYPE = "MidiTrack";
	public final static String DEFAULT_NAME = "Untitled";
	public final static Integer DEFAULT_VOLUME = 80;
	public final static Integer DEFAULT_CHANNEL = 0;
	public final static Integer DEFAULT_CLEF = 0;
	public final static Integer DEFAULT_INSTRUMENT = 0;
	public final static String[] MIDI_INSTRUMENTS = new String[] { "Acoustic Grand Piano", "Bright Acoustic Piano", "Electric grand Piano", "Honky Tonk Piano", "Eiectric Piano 1", "Electric Piano 2", "Harpsichord",
			"Clavinet", "Celesra", "Glockenspiel", "Music Box", "Vibraphone", "Marimba", "Xylophone", "Tubular bells", "Dulcimer", "Drawbar Organ", "Percussive Organ", "Rock Organ", "Church Organ", "Reed Organ",
			"Accordion", "Harmonica", "Tango Accordion", "Nylon Accustic Guitar", "Steel Acoustic Guitar", "Jazz Electric Guitar", "Clean Electric Guitar", "Muted Electric Guitar", "Overdrive Guitar", "Distorted Guitar",
			"Guitar Harmonics", "Acoustic Bass", "Electric Fingered Bass", "Electric Picked Bass", "Fretless Bass", "Slap Bass 1", "Slap Bass 2", "Syn Bass 1", "Syn Bass 2", "Violin", "Viola", "Cello", "Contrabass",
			"Tremolo Strings", "Pizzicato Strings", "Orchestral Harp", "Timpani", "String Ensemble 1", "String Ensemble 2 (Slow)", "Syn Strings 1", "Syn Strings 2", "Choir Aahs", "Voice Oohs", "Syn Choir",
			"Orchestral Hit", "Trumpet", "Trombone", "Tuba", "Muted Trumpet", "French Horn", "Brass Section", "Syn Brass 1", "Syn Brass 2", "Soprano Sax", "Alto Sax", "Tenor Sax", "Baritone Sax", "Oboe", "English Horn",
			"Bassoon", "Clarinet", "Piccolo", "Flute", "Recorder", "Pan Flute", "Bottle Blow", "Shakuhachi", "Whistle", "Ocarina", "Syn Square Wave", "Syn Sawtooth Wave", "Syn Calliope", "Syn Chiff", "Syn Charang",
			"Syn Voice", "Syn Fifths Sawtooth Wave", "Syn Brass & Lead", "New Age Syn Pad", "Warm Syn Pad", "Polysynth Syn Pad", "Choir Syn Pad", "Bowed Syn Pad", "Metal Syn Pad", "Halo Syn Pad", "Sweep Syn Pad",
			"SFX Rain", "SFX Soundtrack", "SFX Crystal", "SFX Atmosphere", "SFX Brightness", "SFX Goblins", "SFX Echoes", "SFX Sci-fi", "Sitar", "Banjo", "Shamisen", "Koto", "Kalimba", "Bag Pipe", "Fiddle", "Shanai",
			"Tinkle Bell", "Agogo", "Steel Drums", "Woodblock", "Taiko Drum", "Melodic Tom", "Syn Drum", "Reverse Cymbal", "Guitar Fret Noise", "Breath Noise", "Seashore", "Bird Tweet", "Telephone Ring", "Helicopter",
			"Applause", "Gun Shot" };
	public final static String[] CLEFS = new String[] { "Violin", "Bass", "Violin 8+", "Violin 15+", "Violin 8-", "Bass 8-", "Bass 15-", "Varbaritone", "Subbass", "Soprano", "Mezzosoprano", "Alto", "Tenor", "-" };
	public final static String[] TEMPI = new String[] { "Largo", "Larghetto", "Adagio", "Andante", "Moderao", "Allegro", "Presto", "Prestissimo"};
	public final static Map<String,List<Integer>> TEMPO_MAP = new HashMap<String, List<Integer>>() {{
		put("<Largo", Arrays.asList(0, 39));
		put("Largo", Arrays.asList(40, 59));
		put("Larghetto", Arrays.asList(60, 65));
		put("Adagio", Arrays.asList(66, 75));
		put("Andante", Arrays.asList(76, 107));
		put("Moderao", Arrays.asList(108, 119));
		put("Allegro", Arrays.asList(120, 167));
		put("Presto", Arrays.asList(168, 199));
		put("Prestissimo", Arrays.asList(200, 208));
		put(">Prestissimo", Arrays.asList(209, 255));
	}};

	public static final String[] KEYS = new String[] { "Ces", "Ges", "Des", "As", "Es", "B", "F", "C", "G", "D", "A", "E", "H", "Fis", "Cis" };

	public final static StringAttribute name = new AttributeBuilder("name")
			.defaultValue(DEFAULT_NAME)
			.required()
			.buildStringAttribute();
	public final static IntegerAttribute channel = new AttributeBuilder("channel")
			.defaultValue(DEFAULT_CHANNEL)
			.buildIntegerAttribute();
	public final static IntegerAttribute volume = new AttributeBuilder("volume")
			.defaultValue(DEFAULT_VOLUME)
			.buildIntegerAttribute();
	public final static SelectableIntegerAttribute instrument = new AttributeBuilder("instrument")
			.values(MIDI_INSTRUMENTS)
			.defaultValue(DEFAULT_INSTRUMENT)
			.buildSelectableIntegerAttribute();
	public final static SelectableIntegerAttribute clef = new AttributeBuilder("clef")
			.values(CLEFS)
			.defaultValue(DEFAULT_CLEF)
			.buildSelectableIntegerAttribute();
	public final static IntegerAttribute tempo = new AttributeBuilder("tempo")
			.defaultValue(TempoEvent.DEFAULT_TEMPO)
			.buildIntegerAttribute();
	public final static SelectableIntegerAttribute key = new AttributeBuilder("key")
			.values(Arrangement.KEYS)
			.defaultValue(Arrangement.DEFAULT_KEY)
			.buildSelectableIntegerAttribute();
	public final static TimeSignatureAttribute timeSignature = new AmbitusAttributeBuilder("timeSignature")
			.defaultValue(Arrangement.DEFAULT_SIGNATURE)
			.buildTimeSignatureAttribute();
	public final static BooleanAttribute mute = new AttributeBuilder("mute")
			.defaultValue(false)
			.buildBooleanAttribute();
	
	// private final Propagator<?, ?>[] propagators = new Propagator[] { new TrackKeyPropagator(this, key), new TrackTimeSignaturePropagator(this, timeSignature), new TrackClefPropagator(this, clef) };

	public final static Comparator eventComparator = new Comparator<ModelElement>() {
		@Override
		public int compare(ModelElement element1, ModelElement element2) {
			int comp = 0;
			if (element1 instanceof Event && element2 instanceof Event) {
				comp = Long.compare(((Event) element1).getPosition(), ((Event) element2).getPosition());
				if (comp==0) {
					if (element1 instanceof NoteEvent && element2 instanceof NoteEvent) {
						comp = Integer.compare(((NoteEvent) element1).getPitch(), ((NoteEvent) element2).getPitch());
					} else if (element1 instanceof NoteEvent) {
						comp = 1; // notes come after other event types
					}
				}
			}
			return comp;
		}
	};

	public static String getTempoLabel(int tempo) {
		String result = "?";
		for (Map.Entry<String,List<Integer>> entry : TEMPO_MAP.entrySet()) {
			String key = entry.getKey();
			List<Integer> pair = entry.getValue();
			if (pair.get(0) <= tempo && tempo <= pair.get(1)) {
				System.out.println("????? " + pair.get(0) + " <= " + tempo + " <= " + pair.get(1));
				result = key;
				break;
			}
		}
		return result;
	}

	public MidiTrack() {
		super(TYPE, Arrays.asList(BarEvent.TYPE, ClefEvent.TYPE, KeyEvent.TYPE, NoteEvent.TYPE, SymbolEvent.TYPE,TempoEvent.TYPE,TimeSignatureEvent.TYPE),
				Arrays.asList(name, channel, volume, instrument, clef, tempo, key, timeSignature, mute));
	}

	public String getId() {
		return getAttributeValue(channel) + " : " + getAttributeValue(name);
	}

	@Override
	public void addEvent(CwnEvent cwnEvent) {
		throw new RuntimeException("addEvent not supported!");
	}

	@Override
	public int getPPQ() {
		ModelElement parent = getParent();
		if (parent.getType().equals(Arrangement.TYPE)) {
			return ((Arrangement)parent).getPPQ();
		}
		return 0;
	}

	@Override
	public String getName() {
		return getAttributeValue(name);
	}
	
	public int getInstrument() {
		return getAttributeValue(instrument);
	}
	
	public TimeSignature getTimeSignature() {
		return getAttributeValue(timeSignature);
	}
	
	public int getChannel() {
		return getAttributeValue(channel);
	}
	
	public boolean getMute() {
		return getAttributeValue(mute);
	}
	
	public int getKey() {
		return getAttributeValue(key);
	}
	
	public int getClef() {
		return getAttributeValue(clef);
	}

	public int getTempo() {
		return getAttributeValue(tempo);
	}
	
	@Override
	public Trias nextBar(Trias trias) {
		return trias.nextBar();
	}
	
	@Override
	public long nextBar(long position) throws TimeSignatureException {
		return PositionTools.getPosition(this, PositionTools.getTrias(this, position).nextBar());
	}
	
	@Override
	public <T extends CwnEvent> List<T> getList(Class<T> eventClass) {
		return StreamSupport.stream(getChildrenByClass(eventClass).spliterator(), false).map(element -> eventClass.cast(element)).collect(Collectors.toList());
	}
	
	@Override
	public CwnTimeSignatureEvent getTimeSignature(long from) {
		CwnTimeSignatureEvent timeSignatureEvent = StreamSupport.stream(getChildrenByClass(CwnTimeSignatureEvent.class).spliterator(), false).map(element -> CwnTimeSignatureEvent.class.cast(element))
				.filter(event -> event.getPosition() <= from).map(event -> CwnTimeSignatureEvent.class.cast(event)).reduce((a, b) -> b).orElse(null);
		if (timeSignatureEvent == null) {
			throw new RuntimeException("Track must contain a time signature!");
		}
		return timeSignatureEvent;
	}
	
	@Override
	public CwnTimeSignatureEvent getTimeSignature(String from) {
		return getTimeSignature(PositionTools.getPosition(this, new Trias(from)));
	}
	
	@Override
	public CwnKeyEvent getKey(long from) {
		CwnKeyEvent keyEvent = StreamSupport
				.stream(getChildrenByClass(KeyEvent.class).spliterator(), false)
				.map(element -> CwnKeyEvent.class.cast(element)).filter(event -> event.getPosition() <= from)
				.map(event -> CwnKeyEvent.class.cast(event))
				.reduce((a, b) -> b).orElse(null);
		if (keyEvent == null) {
			throw new RuntimeException("Track must contain a key!");
		}
		return keyEvent;
	}
	
	@Override
	public CwnClefEvent getClef(long from) {
		CwnClefEvent clefEvent = StreamSupport.stream(getChildrenByType(ClefEvent.TYPE).spliterator(), false).map(element -> CwnClefEvent.class.cast(element)).filter(event -> event.getPosition() <= from)
				.map(event -> CwnClefEvent.class.cast(event)).reduce((a, b) -> b).orElse(null);
		if (clefEvent == null) {
			throw new RuntimeException("Track must contain a clef!");
		}
		return clefEvent;
	}

	public CwnNoteEvent findNoteAtLocation(Location location) {
		CwnNoteEvent noteEvent = StreamSupport.stream(getChildrenByType(NoteEvent.TYPE).spliterator(), false).map(element -> CwnNoteEvent.class.cast(element))
				.filter(event -> event.getPosition() == location.position && Math.abs(event.getPitch() - location.pitch) <= 1).map(event -> CwnNoteEvent.class.cast(event)).reduce((a, b) -> b).orElse(null);
		return noteEvent;
	}
	
//	public List<Long> findIdRangeBetween(Location location1, Location location2) {
//		return StreamSupport.stream(getChildrenByType(NoteEvent.TYPE).spliterator(), false).map(element -> CwnNoteEvent.class.cast(element))
//				.filter(event -> event.getPosition() >= location1.position && event.getPosition() <= location2.position).map(event -> ((NoteEvent) event).getId()).collect(Collectors.toList());
//	}
	
	public List<ModelElement> findRangeBetween(Location location1, Location location2) {
		return StreamSupport.stream(getChildrenByType(NoteEvent.TYPE).spliterator(), false).map(element -> CwnNoteEvent.class.cast(element))
				.filter(event -> event.getPosition() >= location1.position && event.getPosition() <= location2.position).map(event -> (NoteEvent) event).collect(Collectors.toList());
	}
	
	public NoteEvent findNoteBefore(ModelElement element) {
		/*
		 * ModelElement left = null; List<ModelElement> children = getChildren(NoteEvent.class); if (!children.isEmpty()) { for (ModelElement cursor : children) { if (cursor.getId() == element.getId()) { break;
		 * } left = cursor; } if (left == null) { left = children.get(children.size() - 1); } } return (NoteEvent) left;
		 */
		return findEventBefore(element, NoteEvent.TYPE);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Event> T findEventBefore(ModelElement element, String type) {
		ModelElement left = null;
		List<ModelElement> children = getChildrenByType(type);
		if (!children.isEmpty()) {
			for (ModelElement cursor : children) {
				if (cursor.getId() == element.getId()) {
					break;
				}
				left = cursor;
			}
			if (left == null) {
				left = children.get(children.size() - 1);
			}
		}
		return (T) left;
	}

	public NoteEvent findNoteAfter(ModelElement element) {
		/*
		 * ModelElement right = null; ModelElement left = null; List<ModelElement> children = getChildren(NoteEvent.class); if (!children.isEmpty()) { for (ModelElement cursor : children) { right =
		 * cursor; if (left != null && left.getId() == element.getId()) { break; } left = cursor; } if (right.getId() == left.getId()) { right = children.get(0); } } return (NoteEvent) right;
		 */
		return findEventAfter(element, NoteEvent.TYPE);
	}
	
	public <T extends Event> T findEventAfter(ModelElement element, String type) {
		ModelElement right = null;
		ModelElement left = null;
		List<ModelElement> children = getChildrenByType(type);
		if (!children.isEmpty()) {
			for (ModelElement cursor : children) {
				right = cursor;
				if (left != null && left.getId() == element.getId()) {
					break;
				}
				left = cursor;
			}
			if (right.getId() == left.getId()) {
				right = children.get(0);
			}
		}
		return (T) right;
	}
	
	public NoteEvent findNoteFromPosition(long position) {
		// NoteEvent note = null;
		// List<ModelElement> children = getChildren(NoteEvent.class);
		// if (!children.isEmpty()) {
		// for (ModelElement cursor : children) {
		// if (((NoteEvent) cursor).getPosition() >= position) {
		// note = (NoteEvent) cursor;
		// break;
		// }
		// }
		// }
		// return note;
		return findEventFromPosition(position, NoteEvent.class);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Event> T findEventFromPosition(long position, Class<T> clasz) {
		T note = null;
		List<T> children = getChildrenByClass(clasz);
		if (!children.isEmpty()) {
			for (ModelElement cursor : children) {
				if (((T) cursor).getPosition() >= position) {
					note = (T) cursor;
					break;
				}
			}
		}
		return note;
	}

	public <T extends Event> Optional<T> findFirstEventAtPosition(long position, Class<T> clasz) {
		Optional<T> first = getChildren()
				.stream()
				.filter(ev -> clasz.isAssignableFrom(ev.getClass()) &&  ((T)ev).getPosition() == position)
				.map(ev -> (T)ev)
				.findFirst();
		return first;
	}

	public <T extends Event> Optional<T> findEventBefore(long position, Class<T> clasz) {
		T result = null;
		for (T event : getChildrenByClass(clasz)) {
			if (event.getPosition()>=position) {
				break;
			}
			result = event;
		}
		return Optional.ofNullable(result);
	}

	public Optional<NoteEvent> findLastNote() {
		List<NoteEvent> children = getChildrenByClass(NoteEvent.class);
		return (children.size() > 0 ? Optional.of(NoteEvent.class.cast(children.get(children.size() - 1))) : Optional.empty());
	}
	
	public Optional<NoteEvent> findFirstNote() {
		List<NoteEvent> children = getChildrenByClass(NoteEvent.class);
		return (children.size() > 0 ? Optional.of(NoteEvent.class.cast(children.get(0))) : Optional.empty());
	}
	
	public <T extends Event> Optional<T> findLastEvent(Class<T> clasz) {
		List<T> children = getChildrenByClass(clasz);
		return (children.size() > 0 ? Optional.of(clasz.cast(children.get(children.size() - 1))) : Optional.empty());
	}
	
	public <T extends Event> Optional<T> findFirstEvent(Class<T> clasz) {
		List<T> children = getChildrenByClass(clasz);
		return (children.size() > 0 ? Optional.of(clasz.cast(children.get(0))) : Optional.empty());
	}

	public TimeSignature getBarTimeSignature(long position) {
		TimeSignature result;
		Optional<TimeSignatureEvent> firstEventAtPosition = findFirstEventAtPosition(position, TimeSignatureEvent.class);
		if (firstEventAtPosition.isPresent()) {
			result = firstEventAtPosition.get().getAttributeValue(TimeSignatureEvent.timeSignature);
		} else {
			result = findEventBefore(position, TimeSignatureEvent.class).get().getTimeSignature();
		}
		return result;
	}

	public int getBarKey(long position) {
		int result = 0;
		Optional<KeyEvent> firstEventAtPosition = findFirstEventAtPosition(position, KeyEvent.class);
		if (firstEventAtPosition.isPresent()) {
			result = firstEventAtPosition.get().getKey();
		} else {
			result = findEventBefore(position, KeyEvent.class).get().getKey();
		}
		return result;
	}

	public int getBarClef(long position) {
		int result = 0;
		Optional<ClefEvent> firstEventAtPosition = findFirstEventAtPosition(position, ClefEvent.class);
		if (firstEventAtPosition.isPresent()) {
			result = firstEventAtPosition.get().getClef();
		} else {
			result = findEventBefore(position, ClefEvent.class).get().getClef();
		}
		return result;
	}

	public int getBarTempo(long position) {
		int result = 0;
		Optional<TempoEvent> firstEventAtPosition = findFirstEventAtPosition(position, TempoEvent.class);
		if (firstEventAtPosition.isPresent()) {
			result = firstEventAtPosition.get().getTempo();
		} else {
			Optional<TempoEvent> tempoEvent = findEventBefore(position, TempoEvent.class);
			result = tempoEvent.isPresent() ? tempoEvent.get().getTempo() : 0;
		}
		return result;
	}

	public boolean hasLyrics() {
		boolean hasLyrics = false;
		List<NoteEvent> children = getChildrenByClass(NoteEvent.class);
		int size = children.size();
		for (int i = 0; i < Math.min(size, 20); i++) {
			NoteEvent note = (NoteEvent) children.get(i);
			if (note.hasLyrics()) {
				hasLyrics = true;
				break;
			}
		}
		return hasLyrics;
	}

	public boolean isActive() {
		return true;
	}
	
	@Override
	public String toString() {
		return "MidiTrack " + getName();
	}

	@Override
	public Comparator<ModelElement> getComparator() {
		return eventComparator;
	}
}
