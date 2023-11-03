package org.wuerthner.ambitus.model;

import org.wuerthner.ambitus.attribute.AmbitusAttributeBuilder;
import org.wuerthner.ambitus.attribute.PositionAttribute;
import org.wuerthner.cwn.api.CwnEvent;
import org.wuerthner.sport.api.ModelElement;

public interface Event extends CwnEvent, ModelElement {
	public final static PositionAttribute position = new PositionAttribute("position")
			.label("Position")
			.required()
			.defaultValue(0L);

	default long getPosition() {
		return getAttributeValue(position);
	}
}
