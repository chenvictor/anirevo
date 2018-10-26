package cvic.anirevo.model;

public abstract class Starrable {

    private boolean starred = false;
    private StarListener mListener;
    private int listenerIndex;

    public void setListener(StarListener listener, int index) {
        mListener = listener;
        listenerIndex = index;
    }

    /**
     * Toggles the starred state
     * @return  the new starred state
     */
    public boolean toggleStarred() {
        setStarred(!starred);
        return starred;
    }

    public boolean isStarred() {
        return starred;
    }

    protected void setStarred(boolean starred) {
        this.starred = starred;
        if (mListener != null) {
            mListener.onValueChange(this);
        }
    }

    public int getListenerIndex() {
        return listenerIndex;
    }

    public interface StarListener {

        void onValueChange(Starrable object);

    }

}
