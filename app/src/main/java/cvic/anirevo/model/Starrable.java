package cvic.anirevo.model;

public abstract class Starrable {

    private boolean starred = false;

    /**
     * Toggles the starred state
     * @return  the new starred state
     */
    public boolean toggleStarred() {
        starred = !starred;
        return starred;
    }

    public boolean isStarred() {
        return starred;
    }

    protected void setStarred(boolean starred) {
        this.starred = starred;
    }
}
