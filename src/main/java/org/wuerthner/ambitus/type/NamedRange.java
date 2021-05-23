package org.wuerthner.ambitus.type;

public class NamedRange {
    public final String name;
    public final long start;
    public final long end;

    public NamedRange(String name, long start, long end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public NamedRange(String range) {
        int indexColon = range.indexOf(":");
        int indexDash = range.indexOf("-", indexColon + 1);
        if (indexColon < 1) {
            throw new RuntimeException("wrong range format: " + range + ". Expected format: 'name: n-m'");
        } else if (indexDash < 3) {
            throw new RuntimeException("wrong range format: " + range + ". Expected format: 'name: n-m'");
        }
        name = range.substring(0, indexColon).trim();
        start = Long.valueOf(range.substring(indexColon + 1, indexDash).trim());
        end = Long.valueOf(range.substring(indexDash + 1).trim());
    }

    @Override
    public String toString() {
        return name + ": " + start + "-" + end;
    }
}
