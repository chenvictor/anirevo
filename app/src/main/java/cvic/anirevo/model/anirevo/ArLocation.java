package cvic.anirevo.model.anirevo;

import java.util.Objects;
import java.util.Set;

public class ArLocation {

    /**
     * Represents a Location for an ArEvent
     */

    private String title;
    private String subtitle;

    private Set<ArEvent> events;

    public ArLocation(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArLocation that = (ArLocation) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(subtitle, that.subtitle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, subtitle);
    }
}
