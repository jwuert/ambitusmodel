package org.wuerthner.ambitus.tool;


import org.wuerthner.cwn.api.CwnPointer;
import static org.wuerthner.cwn.api.CwnPointer.Region.NONE;

public class AmbitusPointer implements CwnPointer {
    private long position = -1;
    private int pitch = 0;
    private Region region;
    private int staffIndex;
    private int deltaY;
    private int relativeY;

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public long getPosition() {
        return position;
    }

    @Override
    public void setPosition(long position) {
        this.position = position;
    }

    @Override
    public int getPitch() {
        return pitch;
    }

    @Override
    public void setPitch(int pitch) {
        this.pitch = pitch;
    }

    @Override
    public void setRegion(CwnPointer.Region region) {
        this.region = region;
    }

    @Override
    public int getStaffIndex() {
        return staffIndex;
    }

    @Override
    public void setRelativeY(int y) {
        this.relativeY = y;
    }

    @Override
    public int getRelativeY() {
        return relativeY;
    }

    @Override
    public void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
    }

    @Override
    public int getDeltaY() {
        return deltaY;
    }

    @Override
    public void setStaffIndex(int index) {
        this.staffIndex = index;
    }

    @Override
    public void clear() {
        position = -1;
        region = NONE;
    }
}
