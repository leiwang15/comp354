package edu.concordia.comp354.model;

/**
 * Created by joao on 15.07.18.
 */
public class DirtyAware {

    protected enum DirtyLevels {
        NEW, MODIFIED, DELETED, UNTOUCHED
    }

    protected DirtyLevels dirty;

    public DirtyAware(DirtyLevels dirty) {
        this.dirty = dirty;
    }

    protected void changed() {

        switch (dirty) {
            case NEW:
                break;
            case MODIFIED:
                break;
            case DELETED:
                break;
            default:
                dirty = DirtyLevels.MODIFIED;
                break;
        }
    }

    protected void setDirty(DirtyLevels dirty) {
        this.dirty = dirty;
    }

    public boolean isDirty() {
        return dirty != DirtyLevels.UNTOUCHED;
    }

    protected boolean isNew() {
        return dirty == DirtyLevels.NEW;
    }

    protected boolean isModified() {
        return dirty == DirtyLevels.MODIFIED;
    }

    protected boolean isDeleted() {
        return dirty == DirtyLevels.DELETED;
    }

    public void written() {
        dirty = DirtyLevels.UNTOUCHED;
    }
}
