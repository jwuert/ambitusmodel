package org.wuerthner.ambitus.model;

import org.wuerthner.cwn.api.CwnAccent;

public class Accent implements CwnAccent {
	
	private final String name;
	private final int vertical_offset;
	private final int parameter;
	
	public static Accent createAccent(int i) {
		return new Accent(ACCENTS[i]);
	}
	
	/**
	 * Constructor parameter needs the format "name[:verticalOffset[:parameter]]"
	 * 
	 * @param init
	 */
	public Accent(String init) {
		String[] array = init.split(":");
		name = array[0];
		if (array.length > 1) {
			vertical_offset = Integer.parseInt(array[1]);
		} else {
			vertical_offset = -16;
		}
		if (array.length > 2) {
			parameter = Integer.parseInt(array[2]);
		} else {
			parameter = 0;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public int getOffset() {
		return vertical_offset;
	}
	
	public int getParameter() {
		return parameter;
	}
	
	public String toString() {
		return name + ":" + vertical_offset + ":" + parameter;
	}
}
