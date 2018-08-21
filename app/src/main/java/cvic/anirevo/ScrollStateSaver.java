package cvic.anirevo;

import android.os.Parcelable;

public class ScrollStateSaver {

    private Parcelable scrollState;

    public void setScrollState(Parcelable scrollState) {
        this.scrollState = scrollState;
    }

    public Parcelable getScrollState() {
        return scrollState;
    }

    public boolean hasScrollState() {
        return scrollState != null;
    }

}
