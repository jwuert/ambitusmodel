package org.wuerthner.ambitus.attribute;

import org.wuerthner.sport.api.Check;
import org.wuerthner.sport.attribute.AbstractAttribute;

import java.util.List;

public class PitchAttribute  extends AbstractAttribute<Integer> {
    public PitchAttribute(String name, String label, Integer defaultValue, boolean readonly, boolean required, boolean hidden,
                             String description, List<Check> dependencies, List<Check> validators) {
        super(name, label, Integer.class, defaultValue, readonly, required, hidden, description, dependencies, validators);
    }

    @Override
    public Integer getValue(String stringValue) {
        if (stringValue == null) {
            return null;
        } else {
            Integer value;
            try {
                value = Integer.valueOf(stringValue);
            } catch (NumberFormatException nfe) {
                value = getPitchValue(stringValue);
            }
            return value;
        }
    }

    @Override
    public String getStringPresentation(Integer value) {
        if (value != null) {
            return getCPitch(value, false);
        } else {
            return "";
        }
    }

    public static final int getPitchValue(String note) {
        byte pitch = 0;
        if (note == null) {
            throw new RuntimeException("NoteMessage: cannot construct a pitch value on null-string");
        } else {
            note = note.trim();
            char n = note.charAt(0);
            int p = 0;
            switch (n) {
                case 'c':
                    p = 0 + 24;
                    break;
                case 'd':
                    p = 2 + 24;
                    break;
                case 'e':
                    p = 4 + 24;
                    break;
                case 'f':
                    p = 5 + 24;
                    break;
                case 'g':
                    p = 7 + 24;
                    break;
                case 'a':
                    p = 9 + 24;
                    break;
                case 'h':
                    p = 11 + 24;
                    break;
                case 'b':
                    p = 11 + 24;
                    break;
            }
            int oct = 3;
            int sg = 0;
            if (note.length() > 1) {
                char c1 = note.charAt(1);
                if (c1 == '#' || c1 == 'b') {
                    if (c1 == '#')
                        sg = 1;
                    else
                        sg = -1;
                } else if (c1 >= '0' && c1 <= '9') {
                    try {
                        oct = Integer.parseInt("" + c1);
                    } catch (NumberFormatException nfe) {
                        throw new RuntimeException(nfe.getMessage());
                    }
                }
            }
            if (note.length() > 2) {
                char c2 = note.charAt(2);
                if (c2 >= '0' && c2 <= '9') {
                    try {
                        oct = Integer.parseInt("" + c2);
                    } catch (NumberFormatException nfe) {
                        throw new RuntimeException(nfe.getMessage());
                    }
                }
            }
            pitch = (byte) (p + sg + 12 * oct);
        }
        return pitch;
    }

    /**
     * Returns the pitch in a readable manner
     **/
    public final static String getCPitch(int p, boolean b) {
        int n = p % 12;
        char ff = 0;
        char sg = 0;
        switch (n) {
            case 0:
                ff = 'c';
                sg = ' ';
                break;
            case 1:
                ff = (b ? 'd' : 'c');
                sg = (b ? 'b' : '#');
                break;
            case 2:
                ff = 'd';
                sg = ' ';
                break;
            case 3:
                ff = (b ? 'e' : 'd');
                sg = (b ? 'b' : '#');
                break;
            case 4:
                ff = 'e';
                sg = ' ';
                break;
            case 5:
                ff = 'f';
                sg = ' ';
                break;
            case 6:
                ff = (b ? 'g' : 'f');
                sg = (b ? 'b' : '#');
                break;
            case 7:
                ff = 'g';
                sg = ' ';
                break;
            case 8:
                ff = (b ? 'a' : 'g');
                sg = (b ? 'b' : '#');
                break;
            case 9:
                ff = 'a';
                sg = ' ';
                break;
            case 10:
                ff = (b ? 'h' : 'a');
                sg = (b ? 'b' : '#');
                break;
            case 11:
                ff = 'h';
                sg = ' ';
                break;
        }
        if (sg == ' ')
            return " " + ff + (p / 12 - 2);
        else
            return "" + ff + sg + (p / 12 - 2);
    }
}
