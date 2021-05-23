package org.wuerthner.ambitus.model;

import org.wuerthner.sport.api.ModelElement;
import org.wuerthner.sport.api.ModelElementFactory;

import java.util.Arrays;
import java.util.List;

public class AmbitusFactory implements ModelElementFactory {
	public final static List<ModelElement> elementList = Arrays.asList(new ModelElement[] {
			new Arrangement(), new BarEvent(), new ClefEvent(), new KeyEvent(), new MidiTrack(), new NoteEvent(), new SymbolEvent(), new TempoEvent(), new TimeSignatureEvent()
	});
	
	@Override
	public <T extends ModelElement> T createElement(String typeName) {
		T element;
		switch (typeName) {
			case Arrangement.TYPE: element = (T) new Arrangement();	break;
			case BarEvent.TYPE:	element = (T) new BarEvent(); break;
			case ClefEvent.TYPE: element = (T) new ClefEvent(); break;
			case KeyEvent.TYPE: element = (T) new KeyEvent(); break;
			case MidiTrack.TYPE: element = (T) new MidiTrack(); break;
			case NoteEvent.TYPE: element = (T) new NoteEvent(); break;
			case SymbolEvent.TYPE: element = (T) new SymbolEvent(); break;
			case TempoEvent.TYPE: element = (T) new TempoEvent(); break;
			case TimeSignatureEvent.TYPE: element = (T) new TimeSignatureEvent(); break;
			default: throw new RuntimeException("Invalid element type: " + typeName);
		}
		return element;
	}
	
	// @Override
	public List<ModelElement> createElementList() {
		return elementList;
	}
	
	// @Override
	public String getDocumentType() {
		return Arrangement.TYPE;
	}
	
}
